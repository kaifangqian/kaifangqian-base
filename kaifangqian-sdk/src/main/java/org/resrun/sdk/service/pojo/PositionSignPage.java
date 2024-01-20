package org.resrun.sdk.service.pojo;

import java.io.Serializable;

/**
 * @Description: PositionSignPage
 * @Package: org.resrun.modules.sign.pojo
 * @ClassName: PositionSignPage
 * @author: FengLai_Gong
 */
public class PositionSignPage implements Serializable {

    //签署点坐在页码
    private int pageNum;
    //签署点相对于该页的x坐标
    private String xScale;
    //签署点相对于该页的y坐标
    private String yScale;



    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public String getxScale() {
        return xScale;
    }
    public void setxScale(String xScale) {
        this.xScale = xScale;
    }
    public String getyScale() {
        return yScale;
    }
    public void setyScale(String yScale) {
        this.yScale = yScale;
    }
}