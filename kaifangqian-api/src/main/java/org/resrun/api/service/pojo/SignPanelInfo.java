package org.resrun.api.service.pojo;

import java.io.Serializable;

/**
 * @Description: SignPanelInfo
 * @Package: org.resrun.modules.sign.pojo
 * @ClassName: SignPanelInfo
 * @author: FengLai_Gong
 */
public class SignPanelInfo implements Serializable {

        /** 签署方指定签名位置距离页面合同图片左边的距离 */
        private String leftzb;
        /** 签署方指定签名位置距离页面合同图片顶部的距离 */
        private String topzb;
        /** 页码  */
        private String pageNum;
        /** 签名弹框的宽度  */
        private String signW;
        /** 签名弹框的高度  */
        private String signH;
        /** 获取手机屏幕宽、高 */
        private String phoneWidth;
        /** 获取手机屏幕宽、高 */
        private String phoneHeight;


        public String getLeftzb() {
            return leftzb;
        }
        public void setLeftzb(String leftzb) {
            this.leftzb = leftzb;
        }
        public String getTopzb() {
            return topzb;
        }
        public void setTopzb(String topzb) {
            this.topzb = topzb;
        }
        public String getPageNum() {
            return pageNum;
        }
        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }
        public String getSignW() {
            return signW;
        }
        public void setSignW(String signW) {
            this.signW = signW;
        }
        public String getSignH() {
            return signH;
        }
        public void setSignH(String signH) {
            this.signH = signH;
        }
        public String getPhoneWidth() {
            return phoneWidth;
        }
        public void setPhoneWidth(String phoneWidth) {
            this.phoneWidth = phoneWidth;
        }
        public String getPhoneHeight() {
            return phoneHeight;
        }
        public void setPhoneHeight(String phoneHeight) {
            this.phoneHeight = phoneHeight;
        }



}