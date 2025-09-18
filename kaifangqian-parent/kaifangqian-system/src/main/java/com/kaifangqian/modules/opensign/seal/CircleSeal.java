/**
 * @description 印章生成圆章类
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
package com.kaifangqian.modules.opensign.seal;

import com.kaifangqian.exception.PaasException;
import com.kaifangqian.utils.MyStringUtils;

public class CircleSeal extends BaseSeal {

    @Override
    public String createSeal(SealDTO sealDTO) throws Exception {
        if (MyStringUtils.isNotBlank(sealDTO.getCompanyName())) {
            if (sealDTO.getCompanyName().length() < 6 || sealDTO.getCompanyName().length() > 30) {
                throw new PaasException("上行文字字数不符合要求");
            }
        }
        if (MyStringUtils.isNotBlank(sealDTO.getTitle())) {
            if (sealDTO.getTitle().length() < 2 || sealDTO.getTitle().length() > 10) {
                throw new PaasException("横排文字字数不符合要求");
            }
        }
        if (MyStringUtils.isNotBlank(sealDTO.getSerNo())) {
            if (sealDTO.getSerNo().length() < 13 || sealDTO.getSerNo().length() > 14) {
                throw new PaasException("下行数字字数不符合要求");
            }
        }
        SealConfiguration configuration = new SealConfiguration();
        /**
         * 主文字
         */
        SealFont mainFont = new SealFont();
        mainFont.setBold(true);
        mainFont.setFontFamily("宋体");
        mainFont.setMarginSize(10);
        mainFont.setFontText(sealDTO.getCompanyName());
        if (sealDTO.getCompanyName().length() <= 13) {
            mainFont.setFontSize(50);
            mainFont.setFontSpace((double) (45 + (13 - sealDTO.getCompanyName().length()) * 4));
        }
        if (sealDTO.getCompanyName().length() > 13 && sealDTO.getCompanyName().length() <= 15) {
            mainFont.setFontSize(45);
            mainFont.setFontSpace(40.0);
        } else if (sealDTO.getCompanyName().length() > 15 && sealDTO.getCompanyName().length() <= 18) {
            mainFont.setFontSize(40);
            mainFont.setFontSpace(35.0);
        } else if (sealDTO.getCompanyName().length() > 18 && sealDTO.getCompanyName().length() <= 20) {
            mainFont.setFontSize(40);
            mainFont.setFontSpace(32.0);
        } else if (sealDTO.getCompanyName().length() > 20 && sealDTO.getCompanyName().length() <= 22) {
            mainFont.setFontSize(40);
            mainFont.setFontSpace(32.0);
        } else if (sealDTO.getCompanyName().length() > 22 && sealDTO.getCompanyName().length() <= 24) {
            mainFont.setFontSize(40);
            mainFont.setFontSpace(28.0);
        } else if (sealDTO.getCompanyName().length() > 24 && sealDTO.getCompanyName().length() <= 26) {
            mainFont.setFontSize(35);
            mainFont.setFontSpace(25.0);
        } else if (sealDTO.getCompanyName().length() > 26 && sealDTO.getCompanyName().length() <= 28) {
            mainFont.setFontSize(35);
            mainFont.setFontSpace(22.0);
        } else if (sealDTO.getCompanyName().length() > 28 && sealDTO.getCompanyName().length() <= 30) {
            mainFont.setFontSize(35);
            mainFont.setFontSpace(22.0);
        }

        configuration.setMainFont(mainFont);

        /**
         * 副文字
         */
        if (sealDTO.getSerNo() != null && !"".equals(sealDTO.getSerNo())) {
            SealFont viceFont = new SealFont();
            viceFont.setFontFamily("Arial");
            viceFont.setMarginSize(-10);
            /**************************************************/
            viceFont.setFontText(sealDTO.getSerNo());
            viceFont.setBold(true);
            viceFont.setFontSize(25);
            viceFont.setFontSpace(20.0);
            /**************************************************/
            configuration.setViceFont(viceFont);
        }

        /**
         * 中心文字
         */
        if (sealDTO.isStarFlag()) {
            SealFont centerFont = new SealFont();
            centerFont.setBold(true);
            centerFont.setFontFamily("宋体");
            centerFont.setFontText("★");
            centerFont.setFontSize(150);

            configuration.setCenterFont(centerFont);
        }

        /**
         * 抬头文字
         */
        if (sealDTO.getTitle() != null && !"".equals(sealDTO.getTitle())) {
            SealFont titleFont = new SealFont();
            titleFont.setBold(true);
            titleFont.setFontFamily("宋体");
            titleFont.setFontText(sealDTO.getTitle());
            titleFont.setFontSpace(5.0);
            if (sealDTO.getTitle().length() <= 5) {
                titleFont.setLeftSpace((5 - sealDTO.getTitle().length()) * 10);
                titleFont.setFontSize(50);
                titleFont.setMarginSize(90);
            } else if (5 < sealDTO.getTitle().length() && sealDTO.getTitle().length() <= 8) {
                titleFont.setLeftSpace(-(sealDTO.getTitle().length() - 7) * 5);
                titleFont.setFontSize(33);
                titleFont.setMarginSize(75);
            } else if (8 < sealDTO.getTitle().length()) {
                titleFont.setLeftSpace(-(sealDTO.getTitle().length() - 10) * 5);
                titleFont.setFontSize(26);
                titleFont.setMarginSize(70);
            }

            configuration.setTitleFont(titleFont);
        }

        /**
         * 图片大小
         */
        //configuration.setHeight(400);
        /**
         * 背景颜色
         */
        configuration.setBackgroudColor(sealDTO.getColor());
        /**
         * 边线粗细、半径
         */
        configuration.setBorderCircle(new SealCircle(12, 190, 190));

        return SealUtils.buildAndStoreSeal(configuration);
    }
}
