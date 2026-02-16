package loli.enumeration;

import loli.Sub;

/**
 * See ASS Encoding
 */
public enum HEncodingLanguage {
    ANSI(0, Sub.L.getString("encoding.ansi")),
    Default(1, Sub.L.getString("encoding.default")),
    Symbol(2, Sub.L.getString("encoding.symbol")),
    ShiftJIS(128, Sub.L.getString("encoding.shiftJIS")),
    Hangeul(129, Sub.L.getString("encoding.hangeul")),
    Johab(130, Sub.L.getString("encoding.johab")),
    SChinese(134, Sub.L.getString("encoding.s_chinese")),
    TChinese(136, Sub.L.getString("encoding.t_chinese")),
    Turkish(162, Sub.L.getString("encoding.turkish")),
    Vietnamese(163, Sub.L.getString("encoding.vietnamese")),
    Hebrew(177, Sub.L.getString("encoding.hebrew")),
    Arabic(178, Sub.L.getString("encoding.arabic"));

    final int codepage;
    final String name;

    HEncodingLanguage(int codepage, String name){
        this.codepage = codepage;
        this.name = name;
    }

    public int getCodepage() {
        return codepage;
    }

    public String getName() {
        return name;
    }

    public static HEncodingLanguage search(String s) {
        HEncodingLanguage e = HEncodingLanguage.Default;
        for(HEncodingLanguage l : HEncodingLanguage.values()){
            if(l.getName().equalsIgnoreCase(s)){
                e = l;
                break;
            }
        }
        return e;
    }

    public static HEncodingLanguage search(int v) {
        HEncodingLanguage e = HEncodingLanguage.Default;
        for(HEncodingLanguage l : HEncodingLanguage.values()){
            if(l.getCodepage() == v){
                e = l;
                break;
            }
        }
        return e;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", getCodepage(), getName());
    }
}
