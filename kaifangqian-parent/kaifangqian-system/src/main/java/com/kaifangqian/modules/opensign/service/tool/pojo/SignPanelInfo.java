/**
 * @description 文件签署属性类
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
package com.kaifangqian.modules.opensign.service.tool.pojo;

import java.io.Serializable;

/**
 * @Description: SignPanelInfo
 * @Package: com.kaifangqian.modules.sign.pojo
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