package org.resrun.api.service.pojo;


import java.io.Serializable;

/**
 * @Description: 经过计算后的文件签署位置属性类
 * @Package: org.resrun.modules.sign.pojo
 * @ClassName: PositionProperty
 * @author: FengLai_Gong
 */
public class RealPositionProperty implements Serializable {


        private static final long serialVersionUID = 8586984409612483553L;

        /** 签章左下角x坐标 */
        private  float startx;

        /** 签章左下角y坐标*/
        private  float starty;

        /** 签章右上角x坐标*/
        private  float endx;

        /** 签章右上角y坐标*/
        private  float endy;

        private  int pageNum;

        //真实pdf文件宽
        private float realPdfWidth ;
        //真实pdf文件高
        private float realPdfHeight ;

        // 填写值，填写专用
        private String value ;
        //对齐方式
        private String align ;
        //字体
        private String fontFamily ;
        //文字大小
        private Integer fontSize ;


        public float getStartx() {
                return startx;
        }

        public void setStartx(float startx) {
                this.startx = startx;
        }

        public float getStarty() {
                return starty;
        }

        public void setStarty(float starty) {
                this.starty = starty;
        }

        public float getEndx() {
                return endx;
        }

        public void setEndx(float endx) {
                this.endx = endx;
        }

        public float getEndy() {
                return endy;
        }

        public void setEndy(float endy) {
                this.endy = endy;
        }

        public int getPageNum() {
                return pageNum;
        }

        public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
        }

        public float getRealPdfWidth() {
                return realPdfWidth;
        }

        public void setRealPdfWidth(float realPdfWidth) {
                this.realPdfWidth = realPdfWidth;
        }

        public float getRealPdfHeight() {
                return realPdfHeight;
        }

        public void setRealPdfHeight(float realPdfHeight) {
                this.realPdfHeight = realPdfHeight;
        }

        public String getValue() {
                return value;
        }

        public void setValue(String value) {
                this.value = value;
        }

        public String getAlign() {
                return align;
        }

        public void setAlign(String align) {
                this.align = align;
        }

        public String getFontFamily() {
                return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
                this.fontFamily = fontFamily;
        }

        public Integer getFontSize() {
                return fontSize;
        }

        public void setFontSize(Integer fontSize) {
                this.fontSize = fontSize;
        }
}