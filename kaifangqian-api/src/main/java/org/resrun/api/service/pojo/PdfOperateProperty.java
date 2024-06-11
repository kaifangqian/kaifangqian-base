package org.resrun.api.service.pojo;


import java.io.Serializable;

/**
 * @Description: pdf文件操作属性对象
 * @Package: org.resrun.modules.sign.service.tool.pojo
 * @ClassName: PdfOperateProperty
 * @author: FengLai_Gong
 */
public class PdfOperateProperty implements Serializable {

    private static final long serialVersionUID = 136217989504135671L;

    //pdf文件数据
    private byte[] pdfFile ;
    //图片数据
    private byte[] imageFile ;
    //文本数据
    private String value ;
    //横坐标偏移量
    private Float offsetX ;
    //纵坐标偏移量
    private Float offsetY ;
    //页码
    private Integer pageNum ;


    public byte[] getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(byte[] pdfFile) {
        this.pdfFile = pdfFile;
    }

    public byte[] getImageFile() {
        return imageFile;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(Float offsetX) {
        this.offsetX = offsetX;
    }

    public Float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Float offsetY) {
        this.offsetY = offsetY;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}