package org.resrun.sdk.pdfbox.vo;

public class AssinaturaPosition {

    /**
     * 签名的页码
     */
    private Integer page;

    /**
     * 距离左侧的距离
     */
    private String offsetX;
    /**
     * 距离顶部的距离
     */
    private String offsetY;

    /**
     * 前端页面显示的宽度
     */
    private String pageWidth;
    /**
     * 前端页面显示的高度
     */
    private String pageHeight;
    /**
     * 签章图片宽度
     */
    private String signWidth;
    /**
     * 签章图片高度
     */
    private String signHeight;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getOffsetX() {
        return offsetX;
    }
    public Float getParseX() {
        return Float.parseFloat(offsetX);
    }
    public Float getParseY() {
        return Float.parseFloat(offsetY);
    }

    public void setOffsetX(String offsetX) {
        this.offsetX = offsetX;
    }

    public String getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(String offsetY) {
        this.offsetY = offsetY;
    }

    public String getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(String pageWidth) {
        this.pageWidth = pageWidth;
    }

    public String getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(String pageHeight) {
        this.pageHeight = pageHeight;
    }

    public String getSignWidth() {
        return signWidth;
    }

    public void setSignWidth(String signWidth) {
        this.signWidth = signWidth;
    }

    public String getSignHeight() {
        return signHeight;
    }
    public Float getSignHeightFloat() {
        return Float.parseFloat(signHeight);
    }
    public Float getSignWidthFloat() {
        return Float.parseFloat(signWidth);
    }

    public void setSignHeight(String signHeight) {
        this.signHeight = signHeight;
    }
}
