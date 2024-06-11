package org.resrun.api.vo.base;


import java.io.Serializable;

/**
 * @Description: 签署位置数据
 * @Package: org.resrun.vo.base
 * @ClassName: SignLocation
 * @author: FengLai_Gong
 */
public class SignLocation implements Serializable {

    private static final long serialVersionUID = -3441618832286287804L;


    //文件宽度
    private Number pageWidth ;

    //文件高度
    private Number pageHeight ;

    //页码
    private Integer page;

    //偏移量-X坐标
    private Number offsetX ;

    //偏移量-Y坐标
    private Number offsetY ;

    //签章宽度
    private Number signWidth ;

    //签章高度
    private Number signHeight ;

    public Number getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Number pageWidth) {
        this.pageWidth = pageWidth;
    }

    public Number getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Number pageHeight) {
        this.pageHeight = pageHeight;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Number getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(Number offsetX) {
        this.offsetX = offsetX;
    }

    public Number getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Number offsetY) {
        this.offsetY = offsetY;
    }

    public Number getSignWidth() {
        return signWidth;
    }

    public void setSignWidth(Number signWidth) {
        this.signWidth = signWidth;
    }

    public Number getSignHeight() {
        return signHeight;
    }

    public void setSignHeight(Number signHeight) {
        this.signHeight = signHeight;
    }
}