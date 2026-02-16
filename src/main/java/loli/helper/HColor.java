package loli.helper;

import loli.enumeration.HColorScheme;
import loli.exception.HColorException;

import java.awt.*;

public class HColor {
    private Color color;

    public HColor(Color color, float alpha) {
        this.color = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                Math.round(alpha * 255)
        );
    }

    public HColor(Color color) {
        this(color, 1f);
    }

    public HColor() {
        this(Color.yellow);
    }

    public static HColor fromScheme(String code, HColorScheme scheme) throws HColorException {
        HColor c;
        if(code == null || code.isEmpty()) return new HColor();
        code = code.replace("H", "");
        code = code.replace("&", "");
        code = code.replace("#", "");

        switch(scheme){
            case ABGR -> {
                if(!code.toLowerCase().matches("[a-f0-9]{8}")){
                    throw new HColorException("Malformed code: Not ABGR");
                }

                c = new HColor(new Color(
                        Integer.parseInt(code.substring(6), 16),    // R
                        Integer.parseInt(code.substring(4, 6), 16), // G
                        Integer.parseInt(code.substring(2, 4), 16), // B
                        Integer.parseInt(code.substring(0, 2), 16)  // A
                ));
            }
            case BGRA -> {
                if(!code.toLowerCase().matches("[a-f0-9]{8}")){
                    throw new HColorException("Malformed code: Not BGRA");
                }

                c = new HColor(new Color(
                        Integer.parseInt(code.substring(4, 6), 16), // R
                        Integer.parseInt(code.substring(2, 4), 16), // G
                        Integer.parseInt(code.substring(0, 2), 16), // B
                        Integer.parseInt(code.substring(6), 16)     // A
                ));
            }
            case BGR -> {
                if(!code.toLowerCase().matches("[a-f0-9]{6}")){
                    throw new HColorException("Malformed code: Not BGR");
                }

                c = new HColor(new Color(
                        Integer.parseInt(code.substring(4), 16),    // R
                        Integer.parseInt(code.substring(2, 4), 16), // G
                        Integer.parseInt(code.substring(0, 2), 16), // B
                        255                                         // A
                ));
            }
            case ARGB -> {
                if(!code.toLowerCase().matches("[a-f0-9]{8}")){
                    throw new HColorException("Malformed code: Not ARGB");
                }

                c = new HColor(new Color(
                        Integer.parseInt(code.substring(2, 4), 16), // R
                        Integer.parseInt(code.substring(4, 6), 16), // G
                        Integer.parseInt(code.substring(6), 16),    // B
                        Integer.parseInt(code.substring(0, 2), 16)  // A
                ));
            }
            case RGBA -> {
                if(!code.toLowerCase().matches("[a-f0-9]{8}")){
                    throw new HColorException("Malformed code: Not RGBA");
                }

                c = new HColor(new Color(
                        Integer.parseInt(code.substring(0, 2), 16), // R
                        Integer.parseInt(code.substring(2, 4), 16), // G
                        Integer.parseInt(code.substring(4, 6), 16), // B
                        Integer.parseInt(code.substring(6), 16)     // A
                ));
            }
            case RGB, HTML -> {
                if(!code.toLowerCase().matches("[a-f0-9]{6}")){
                    throw new HColorException("Malformed code: Not RGB or HTML 6 digits");
                }

                c = new HColor(new Color(
                        Integer.parseInt(code.substring(0, 2), 16), // R
                        Integer.parseInt(code.substring(2, 4), 16), // G
                        Integer.parseInt(code.substring(4), 16),    // B
                        255                                         // A
                ));
            }
            default -> c = new HColor();
        }

        return c;
    }

    public static String withScheme(Color c, HColorScheme scheme) throws HColorException{
        String s;

        String a = Integer.toHexString(255 - c.getAlpha()); if(a.length() == 1) a = "0"+a;
        String r = Integer.toHexString(c.getRed()); if(r.length() == 1) r = "0"+r;
        String g = Integer.toHexString(c.getGreen()); if(g.length() == 1) g = "0"+g;
        String b = Integer.toHexString(c.getBlue()); if(b.length() == 1) b = "0"+b;

        switch (scheme) {
            case ABGR -> {
                s = "&H" + a + b + g + r; // Used in style
            }
            case BGR -> {
                s = "&H" + b + g + r + "&"; // Used in event for colors
            }
            case HTML -> {
                s = "#" + b + g + r; // Not used
            }
            case ALPHA -> {
                s = "&H" + a + "&"; // Used in event for alphas
            }
            default -> {
                throw new HColorException("Unexpected error!");
            }
        }
        return s;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
