package loli.helper;

import loli.enumeration.HColorScheme;
import loli.exception.HColorException;

import java.awt.*;

public class AssStyle {
    private String name;
    private AssFont assFont;
    private HColor textColor;
    private HColor karaokeColor;
    private HColor outlineColor;
    private HColor shadowColor;
    private float scaleX;
    private float scaleY;
    private float spacing;
    private float angleZ;
    private int borderStyle;
    private float outline;
    private float shadow;
    private AssAlignment alignment;
    private int marginL;
    private int marginR;
    private int marginV;
    private int encoding;

    public AssStyle(String name, AssFont assFont, HColor textColor, HColor karaokeColor, HColor outlineColor, HColor shadowColor, float scaleX, float scaleY, float spacing, float angleZ, int borderStyle, float outline, float shadow, AssAlignment alignment, int marginL, int marginR, int marginV, int encoding) {
        this.name = name;
        this.assFont = assFont;
        this.textColor = textColor;
        this.karaokeColor = karaokeColor;
        this.outlineColor = outlineColor;
        this.shadowColor = shadowColor;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.spacing = spacing;
        this.angleZ = angleZ;
        this.borderStyle = borderStyle;
        this.outline = outline;
        this.shadow = shadow;
        this.alignment = alignment;
        this.marginL = marginL;
        this.marginR = marginR;
        this.marginV = marginV;
        this.encoding = encoding;
    }

    public AssStyle() {
        this(
                "Default", // Name
                new AssFont("Arial"), // Font
                new HColor(Color.white), // Text color
                new HColor(Color.red), // Karaoke color
                new HColor(Color.black), // Outline color
                new HColor(Color.black), // Shadow color
                1f, // Scale X
                1f, // Scale Y
                0f, // Spacing
                0f, // Angle Z
                1, // BorderStyle (1 normal, 3 rectangle)
                2f, // Outline
                2f, // Shadow
                new AssAlignment(), // ASS Alignment
                0, // Margin Left
                0, // Margin Right
                0, // Margin Vertical
                1 // Encoding
        );
    }

    public AssStyle(String rawline) throws HColorException {
        String[] t = rawline.split(",");

        AssFont font = new AssFont(
                t[1], // Name
                Float.parseFloat(t[2]), // Size
                t[7].equalsIgnoreCase("-1"), // Bold
                t[8].equalsIgnoreCase("-1"), // Italic
                t[9].equalsIgnoreCase("-1"), // Underline
                t[10].equalsIgnoreCase("-1") // Strikeout
        );

        name            = t[0].substring(t[0].indexOf(" ") + 1); // Name
        assFont         = font; // Font
        textColor       = HColor.fromScheme(t[3], HColorScheme.ABGR); // Text color
        karaokeColor    = HColor.fromScheme(t[4], HColorScheme.ABGR); // Karaoke color
        outlineColor    = HColor.fromScheme(t[5], HColorScheme.ABGR); // Outline color
        shadowColor     = HColor.fromScheme(t[6], HColorScheme.ABGR); // Shadow color
        scaleX          = Float.parseFloat(t[11]); // Scale X
        scaleY          = Float.parseFloat(t[12]); // Scale Y
        spacing         = Float.parseFloat(t[13]); // Spacing
        angleZ          = Float.parseFloat(t[14]); // Angle Z
        borderStyle     = t[15].equalsIgnoreCase("1") ? 1 : 3; // BorderStyle (1 normal, 3 rectangle)
        outline         = Float.parseFloat(t[16]); // Outline
        shadow          = Float.parseFloat(t[17]); // Shadow
        alignment       = new AssAlignment(Integer.parseInt(t[18])); // ASS Alignment
        marginL         = Integer.parseInt(t[19]); // Margin Left
        marginR         = Integer.parseInt(t[20]); // Margin Right
        marginV         = Integer.parseInt(t[21]); // Margin Vertical
        encoding        = Integer.parseInt(t[22]); // Encoding
    }

    public String toRawLine() throws HColorException {
        return String.format("Style: %s,%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s,%s,%d,%d,%d,%d,%d",
                getName(), // Style name
                getAssFont().getName(), // Font name
                Math.round(getAssFont().getSize()), // Font size
                HColor.withScheme(getTextColor().getColor(), HColorScheme.ABGR),
                HColor.withScheme(getKaraokeColor().getColor(), HColorScheme.ABGR),
                HColor.withScheme(getOutlineColor().getColor(), HColorScheme.ABGR),
                HColor.withScheme(getShadowColor().getColor(), HColorScheme.ABGR),
                getAssFont().isBold() ? "-1" : "0",
                getAssFont().isItalic() ? "-1" : "0",
                getAssFont().isUnderline() ? "-1" : "0",
                getAssFont().isStrikeout() ? "-1" : "0",
                Float.toString(getScaleX()).replace(",", "."),
                Float.toString(getScaleY()).replace(",", "."),
                Float.toString(getSpacing()).replace(",", "."),
                Float.toString(getAngleZ()).replace(",", "."),
                getBorderStyle(),
                Float.toString(getOutline()).replace(",", "."),
                Float.toString(getShadow()).replace(",", "."),
                getAlignment().getNumber(),
                getMarginL(),
                getMarginR(),
                getMarginV(),
                getEncoding()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssFont getAssFont() {
        return assFont;
    }

    public void setAssFont(AssFont assFont) {
        this.assFont = assFont;
    }

    public HColor getTextColor() {
        return textColor;
    }

    public void setTextColor(HColor textColor) {
        this.textColor = textColor;
    }

    public HColor getKaraokeColor() {
        return karaokeColor;
    }

    public void setKaraokeColor(HColor karaokeColor) {
        this.karaokeColor = karaokeColor;
    }

    public HColor getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(HColor outlineColor) {
        this.outlineColor = outlineColor;
    }

    public HColor getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(HColor shadowColor) {
        this.shadowColor = shadowColor;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public float getAngleZ() {
        return angleZ;
    }

    public void setAngleZ(float angleZ) {
        this.angleZ = angleZ;
    }

    public int getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(int borderStyle) {
        this.borderStyle = borderStyle;
    }

    public float getOutline() {
        return outline;
    }

    public void setOutline(float outline) {
        this.outline = outline;
    }

    public float getShadow() {
        return shadow;
    }

    public void setShadow(float shadow) {
        this.shadow = shadow;
    }

    public AssAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(AssAlignment alignment) {
        this.alignment = alignment;
    }

    public int getMarginL() {
        return marginL;
    }

    public void setMarginL(int marginL) {
        this.marginL = marginL;
    }

    public int getMarginR() {
        return marginR;
    }

    public void setMarginR(int marginR) {
        this.marginR = marginR;
    }

    public int getMarginV() {
        return marginV;
    }

    public void setMarginV(int marginV) {
        this.marginV = marginV;
    }

    public int getEncoding() {
        return encoding;
    }

    public void setEncoding(int encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return getName();
    }
}
