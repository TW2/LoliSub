package org.wingate.lolisub.ass.render;

import org.wingate.lolisub.ass.ASS;
import org.wingate.lolisub.ass.AssEvent;
import org.wingate.lolisub.ass.AssStyle;
import org.wingate.lolisub.ass.AssTime;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

    private List<AGraphicElement> elements;

    private String character;
    private double contourX;
    private double contourY;
    private double contourWidth;
    private double contourHeight;
    private int videoWidth;
    private int videoHeight;
    private Shape visibility;
    private AssStyle assStyle;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikeOut;
    private String fontName;
    private float fontSize;
    private Color textColor;
    private Color karaokeColor;
    private Color outlineColor;
    private Color shadowColor;
    private double alpha;
    private double textAlpha;
    private double karaokeAlpha;
    private double outlineAlpha;
    private double shadowAlpha;
    private double scaleX;
    private double scaleY;
    private double spacing;
    private double angleX;
    private double angleY;
    private double angleZ;
    private int borderStyle;
    private double outlineThicknessX;
    private double outlineThicknessY;
    private double shadowShiftX;
    private double shadowShiftY;
    private double blurEdge;
    private double blur;
    private int alignment;
    private double marginL;
    private double marginR;
    private double marginV;
    private double marginT;
    private double marginB;
    private int encoding;
    private Point2D origin;
    private double shearX;
    private double shearY;
    private AssTime karaoke;
    private AssTime karaokeFill;
    private AssTime karaokeOutline;
    private int wrapStyle;
    private Point2D position;

    // TODO: Animation \t
    // TODO: Visibility \clip
    // TODO: Drawing
    // TODO: Baseline offset \pbo

    private Converter(AssEvent event){
        elements = new ArrayList<>();

        bold = event.getStyle().getAssFont().isBold(); // STYLE: true is 1, false is 0
        italic = event.getStyle().getAssFont().isItalic(); // STYLE: true is 1, false is 0
        underline = event.getStyle().getAssFont().isUnderline(); // STYLE: true is 1, false is 0
        strikeOut = event.getStyle().getAssFont().isStrikeout(); // STYLE: true is 1, false is 0
        fontName = event.getStyle().getAssFont().getFont().getFontName();
        fontSize = event.getStyle().getAssFont().getFont().getSize2D(); // SIZE: must be the size of height in pixels (TODO)
        textColor = event.getStyle().getTextColor().getColor();
        karaokeColor = event.getStyle().getKaraokeColor().getColor();
        outlineColor = event.getStyle().getOutlineColor().getColor();
        shadowColor = event.getStyle().getShadowColor().getColor();
        alpha = 1d; // ALPHA: 255 is transparent, 0 is opaque (must be the inverse (1d is opaque))
        textAlpha = 1d; // ALPHA: 255 is transparent, 0 is opaque (must be the inverse (1d is opaque))
        karaokeAlpha = 1d; // ALPHA: 255 is transparent, 0 is opaque (must be the inverse (1d is opaque))
        outlineAlpha = 1d; // ALPHA: 255 is transparent, 0 is opaque (must be the inverse (1d is opaque))
        shadowAlpha = 1d; // ALPHA: 255 is transparent, 0 is opaque (must be the inverse (1d is opaque))
        scaleX = 1d; // SCALE: 100 is 1d (100%)
        scaleY = 1d; // SCALE: 100 is 1d (100%)
        spacing = 0d;
        angleX = 0d;
        angleY = 0d;
        angleZ = 0d;
        borderStyle = 1; // TODO: Something to define it, and use it
        outlineThicknessX = 2d; // Bord: in pixel
        outlineThicknessY = 2d; // Bord: in pixel
        shadowShiftX = 2d; // Shad: in pixel
        shadowShiftY = 2d; // Shad: in pixel
        blurEdge = 0d;
        blur = 0d;
        alignment = 2;
        marginL = 0d;
        marginR = 0d;
        marginV = 0d;
        marginT = 0d;
        marginB = 0d;
        encoding = 1; // TODO: Something to define it, and use it
        origin = new Point2D.Double(Double.MIN_VALUE, Double.MAX_VALUE); // Set to min (undesirable value)
        shearX = 0d;
        shearY = 0d;
        karaoke = new AssTime(-1L);;
        karaokeFill = new AssTime(-1L);;
        karaokeOutline = new AssTime(-1L);;
        wrapStyle = 2; // TODO: Something to define it, and use it
        position = new Point2D.Double(Double.MIN_VALUE, Double.MAX_VALUE); // Set to min (undesirable value)
    }

    public static Converter creteShapes(AssEvent event, ASS ass){
        Converter converter = new Converter(event);
        GraphicType type = converter.strContains("\\{\\\\p\\d+\\}", event.getText()) ?
                GraphicType.Drawing : GraphicType.Letter;

        BufferedImage image = new BufferedImage(10, 10,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();

        switch(type){
            case Letter -> {
                converter.elements.addAll(converter.getChars(g, event, ass));
                // To do directly while drawing :
                // outline, shadow, rotation, blur, shear, color + alpha,
                // fading, position, karaoke, origin, clip,
                // transform (others than already treated in boxing)
            }
            case Drawing -> {
                converter.elements.add(converter.getDrawing(event, ass));
            }
        }

        g.dispose();

        return converter;
    }

    private List<Char> getChars(Graphics2D g, AssEvent event, ASS ass){
        // Group all characters with their tags
        List<Char> chars = doDiv(event);

        // Original font and for all Chars -- treat b i u s fn fs r
        return doFontForEach(g, chars, event, ass);
    }

    private Drawing getDrawing(AssEvent event, ASS ass){
        GeneralPath gp = new GeneralPath();
        Drawing dr = null;
        Pattern p = Pattern.compile("\\{p(?<scale>\\d+)\\}(?<cm>[^\\{]+)");
        Matcher m = p.matcher(event.getText());

        if(m.find()){
            dr = new Drawing();

            float scale = 1f / Math.max(1, Integer.parseInt(m.group("scale")));

            String commands = m.group("cm");

            Pattern pp = Pattern.compile("(?<tag>[mnlbspc]*)\\s(?<x>\\d+.*\\d*)\\s(?<y>\\d+.*\\d*)");
            Matcher mm = pp.matcher(commands);

            String rem = "";
            final List<Point2D> points = new ArrayList<>();
            while(mm.find()){
                String tag = mm.group("tag");
                String com = tag.isEmpty() ? rem : tag;
                rem = com;
                switch(com){
                    case "m", "n" -> {
                        double x = Double.parseDouble(mm.group("x"));
                        double y = Double.parseDouble(mm.group("y"));
                        gp.moveTo(x, y);
                    }
                    case "l" -> {
                        double x = Double.parseDouble(mm.group("x"));
                        double y = Double.parseDouble(mm.group("y"));
                        gp.lineTo(x, y);
                    }
                    case "b" -> {
                        double x = Double.parseDouble(mm.group("x"));
                        double y = Double.parseDouble(mm.group("y"));
                        points.add(new Point2D.Double(x, y));
                        if(points.size() == 3){
                            gp.curveTo(
                                    points.getFirst().getX(), points.getFirst().getY(),
                                    points.get(1).getX(), points.get(1).getY(),
                                    points.getLast().getX(), points.getLast().getY()
                            );
                            points.clear();
                        }
                    }
                    case "s", "p" -> {
                        double x = Double.parseDouble(mm.group("x"));
                        double y = Double.parseDouble(mm.group("y"));
                        points.add(new Point2D.Double(x, y));
                    }
                    case "c" -> {
                        if(!points.isEmpty()){
                            BSplineCurve spline = new BSplineCurve(points.getFirst());
                            for(int i=1; i<points.size(); i++){
                                spline.addControlPoint(points.get(i));
                            }
                            for(BezierCurve bc : spline.extractAllBezierCurves()){
                                List<Point2D> pts = bc.getControlPoints();
                                // Point A
                                gp.moveTo(pts.getFirst().getX(), pts.getFirst().getY());
                                // ControlPoint CP1, CP2, Point B is basically bezier
                                gp.curveTo(
                                        pts.get(1).getX(), pts.get(1).getY(),
                                        pts.get(2).getX(), pts.get(2).getY(),
                                        pts.getLast().getX(), pts.getLast().getY()
                                );
                            }
                            points.clear();
                        }
                    }
                }
            }

            gp.closePath();

            AffineTransform transform = new AffineTransform();
            transform.scale(scale, scale);
            gp.transform(transform);

            dr.setShape(gp);
        }

        return dr;
    }

    // ----------------------------------------------------------------------------------

    private List<Char> doDiv(AssEvent event){
        final List<Char> chars = new ArrayList<>();

        Pattern p = Pattern.compile("\\{*(?<tags>[^\\}]*)\\}*(?<letter>[^\\{]{1})");
        Matcher m = p.matcher(event.getText());

        String remember = "";

        while(m.find()){
            Char c = new Char();
            if(!m.group("tags").isEmpty() || !remember.isEmpty()){
                remember += m.group("tags");
                if(remember.contains("\r")){
                    int resetIndex = remember.indexOf("\r");
                    remember = remember.substring(resetIndex);
                }

                for(Map<Tag, String> tags : Tag.getTagsFrom(remember)){
                    c.addTag(tags);
                }
            }
            if(!m.group("letter").isEmpty()) c.setCharacter(m.group("letter"));
        }

        return chars;
    }

    /**
     * Calculation of points in pixel and font size for ASS
     * @param chars a list of Char
     * @param event the event of the line
     * @return a list of char with true size for ASS pixel font size
     */
    private List<Char> doFontForEach(Graphics2D g, List<Char> chars, AssEvent event, ASS ass){
        // Main line -- getFont features : b i u s fn fs
        Font fontWithAttrs = getFont(g, event, ass);
        // Populate all chars with default font
        for(Char c : chars){
            c.setFont(fontWithAttrs);
        }

        // All chars treatment -- getFont features : b i u s fn fs r
        for (Char c : chars){
            Font fontWithAttrsTags = getFont(g, event, ass, c);
            c.setFont(fontWithAttrsTags);
        }

        // Rectangle boxing
        // Calculate real boxing -- treat fsc[_/x/y] fsp t(only fsc[_/x/y] fsp)
        // TODO t (animation)
        for (Char c : chars){
            TextLayout layout = new TextLayout(
                    c.getCharacter(), c.getFont(), g.getFontRenderContext()
            );
            AffineTransform transform = new AffineTransform();
            for(Map<Tag, String> map : c.getTags()){
                for(Map.Entry<Tag, String> entry : map.entrySet()){
                    switch(entry.getKey()){
                        case Tag.Scale -> {
                            double sc = Double.parseDouble(entry.getValue());
                            transform.scale(sc, sc);
                        }
                        case Tag.ScaleX -> {
                            double sc = Double.parseDouble(entry.getValue());
                            transform.scale(sc, 1d);
                        }
                        case Tag.ScaleY -> {
                            double sc = Double.parseDouble(entry.getValue());
                            transform.scale(1d, sc);
                        }
                        case Tag.Spacing -> c.setExtraSpacing(Double.parseDouble(entry.getValue()));
                    }
                }
            }
            c.setShape((GeneralPath) layout.getOutline(transform));
        }

        return chars;
    }

    //-------------------------------------------------------------------------------------

    /**
     * Get font with pixel ASS size, and parse b i u s fn fs r
     * @param g a graphics 2D object to calculate size in pixel
     * @param event the main event
     * @param ass the main ASS
     * @return a Font to apply
     */
    private Font getFont(Graphics2D g, AssEvent event, ASS ass){
        // =========
        // Size in pixel
        // =========
        Font notEval = event.getStyle().getAssFont().getFont();
        notEval = notEval.deriveFont(event.getStyle().getAssFont().getSize());

        Rectangle2D evaluation = new Rectangle2D.Double(
                0d,
                0d,
                ass.getInfos().getPlayResX() * 2.5,
                event.getStyle().getAssFont().getFont().getSize()
        );

        TextLayout layout = new TextLayout(
                event.getText(),
                notEval,
                g.getFontRenderContext()
        );

        float fontSizePoints = notEval.getSize2D();
        while(!evaluation.contains(layout.getBounds())){
            fontSizePoints--;
            layout = new TextLayout(
                    event.getText(),
                    notEval.deriveFont(fontSizePoints),
                    g.getFontRenderContext()
            );
        }

        // =========
        // Font
        // =========
        Map<TextAttribute, Object> attributes = new HashMap<>();
        // Font
        attributes.put(TextAttribute.FONT, event.getStyle().getAssFont().getFont());
        // Size Points to pixels
        attributes.put(TextAttribute.SIZE, fontSizePoints);
        // Bold / Plain
        attributes.put(TextAttribute.WEIGHT, event.getStyle().getAssFont().isBold() ?
                TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
        // Italic / Plain
        attributes.put(TextAttribute.POSTURE, event.getStyle().getAssFont().isItalic() ?
                TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        // Underline / Plain
        attributes.put(TextAttribute.UNDERLINE, event.getStyle().getAssFont().isUnderline() ?
                TextAttribute.UNDERLINE_ON : -1);
        // StrikeOut / Plain
        attributes.put(TextAttribute.STRIKETHROUGH, event.getStyle().getAssFont().isStrikeout() ?
                TextAttribute.STRIKETHROUGH_ON : false);

        // Kerning
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        // Ligatures
        attributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);

        return new Font(attributes);
    }

    /**
     * Get font with pixel ASS size, and parse b i u s fn fs r
     * @param g a graphics 2D object to calculate size in pixel
     * @param event the main event
     * @param ass the main ASS
     * @param c the char
     * @return a Font to apply
     */
    private Font getFont(Graphics2D g, AssEvent event, ASS ass, Char c){
        // =========
        // Char tags -- can change style : b i u s fn fs r
        // =========
        String strStyle = null;
        boolean bold = event.getStyle().getAssFont().isBold();
        boolean italic = event.getStyle().getAssFont().isItalic();
        boolean underline = event.getStyle().getAssFont().isUnderline();
        boolean strikeOut = event.getStyle().getAssFont().isStrikeout();
        String fontName = event.getStyle().getAssFont().getFont().getFontName();
        float fontPoints = event.getStyle().getAssFont().getFont().getSize2D();
        // Treat reset and others
        for(Map<Tag, String> map : c.getTags()){
            for(Map.Entry<Tag, String> entry : map.entrySet()){
                switch(entry.getKey()){
                    case Tag.Reset -> strStyle = entry.getValue();
                    case Tag.Bold -> bold = entry.getValue().equals("1");
                    case Tag.Italic -> italic = entry.getValue().equals("1");
                    case Tag.Underline -> underline = entry.getValue().equals("1");
                    case Tag.StrikeOut -> strikeOut = entry.getValue().equals("1");
                    case Tag.FontName -> fontName = entry.getValue();
                    case Tag.FontSize -> fontPoints = Float.parseFloat(entry.getValue());
                }
            }
        }

        // =========
        // Style
        // =========
        AssStyle style = event.getStyle();
        if(strStyle != null && !strStyle.isEmpty()){
            for(AssStyle s : ass.getStyles()){
                if(s.getName().equals(strStyle)){
                    style = s;
                    break;
                }
            }
        }

        // =========
        // Size in pixel
        // =========
        int strFontStyle = Font.PLAIN;
        if(bold) strFontStyle += Font.BOLD;
        if(italic) strFontStyle += Font.ITALIC;
        Font notEval = new Font(fontName, strFontStyle, style.getAssFont().getFont().getSize());
        notEval = notEval.deriveFont(fontPoints);

        Rectangle2D evaluation = new Rectangle2D.Double(
                0d,
                0d,
                ass.getInfos().getPlayResX() * 2.5,
                style.getAssFont().getFont().getSize() // pixels
        );

        TextLayout layout = new TextLayout(
                event.getText(),
                notEval,
                g.getFontRenderContext()
        );

        float fontSizePoints = notEval.getSize2D();
        while(!evaluation.contains(layout.getBounds())){
            fontSizePoints--;
            layout = new TextLayout(
                    event.getText(),
                    notEval.deriveFont(fontSizePoints),
                    g.getFontRenderContext()
            );
        }

        // =========
        // Font
        // =========
        Map<TextAttribute, Object> attributes = new HashMap<>();
        // Font
        attributes.put(TextAttribute.FONT, style.getAssFont().getFont());
        // Size Points to pixels
        attributes.put(TextAttribute.SIZE, fontSizePoints);
        // Bold / Plain
        attributes.put(TextAttribute.WEIGHT, bold ?
                TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
        // Italic / Plain
        attributes.put(TextAttribute.POSTURE, italic ?
                TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        // Underline / Plain
        attributes.put(TextAttribute.UNDERLINE, underline ?
                TextAttribute.UNDERLINE_ON : -1);
        // StrikeOut / Plain
        attributes.put(TextAttribute.STRIKETHROUGH, strikeOut ?
                TextAttribute.STRIKETHROUGH_ON : false);

        // Kerning
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        // Ligatures
        attributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);

        return new Font(attributes);
    }

    private boolean strContains(String regex, String sample){
        return Pattern.compile(regex).matcher(sample).find();
    }

}
