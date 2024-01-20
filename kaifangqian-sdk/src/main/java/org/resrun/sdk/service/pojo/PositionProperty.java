package org.resrun.sdk.service.pojo;


import java.io.Serializable;

/**
 * @Description: 文件签署属性类
 * @Package: org.resrun.modules.sign.pojo
 * @ClassName: PositionProperty
 * @author: FengLai_Gong
 */
public class PositionProperty implements Serializable {


        private static final long serialVersionUID = 8586984409612483553L;

        /** 签章左下角x坐标 */
        private  float startx;

        /** 签章左下角y坐标*/
        private  float starty;

        /** 签章右上角x坐标*/
        private  float endx;

        /** 签章右上角x坐标*/
        private  float endy;

        private  int pageNum;

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
}