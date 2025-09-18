package com.kaifangqian.modules.opensign.pdfbox.vo;

import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * @Description: FontEntity
 * @Package: com.kaifangqian.modules.opensign.pdfbox.vo
 * @ClassName: FontEntity
 * @author: FengLai_Gong
 */
public class FontEntity {

    private PDFont pdFont;

    private String fontName;

    private String appearanceString;

    public PDFont getPdFont() {
        return pdFont;
    }

    public void setPdFont(PDFont pdFont) {
        this.pdFont = pdFont;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getAppearanceString() {
        return appearanceString;
    }

    public void setAppearanceString(String appearanceString) {
        this.appearanceString = appearanceString;
    }

    public FontEntity(PDFont pdFont, String fontName, String appearanceString) {
        this.pdFont = pdFont;
        this.fontName = fontName;
        this.appearanceString = appearanceString;
    }
}