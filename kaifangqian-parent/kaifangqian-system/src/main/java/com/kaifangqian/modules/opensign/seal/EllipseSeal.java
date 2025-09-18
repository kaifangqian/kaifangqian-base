/**
 * @description 印章生成椭圆类
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

public class EllipseSeal extends BaseSeal {

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
        if (MyStringUtils.isNotBlank(sealDTO.getCenter())) {
            if (sealDTO.getCenter().length() != 18) {
                throw new PaasException("横排上序列码字数不符合要求");
            }
        }

        SealConfiguration configuration = new SealConfiguration();
        /**
         * 主文字
         */
        SealFont mainFont = new SealFont();
        mainFont.setBold(true);
        mainFont.setFontFamily("宋体");
        mainFont.setMarginSize(5);
        mainFont.setFontText(sealDTO.getCompanyName());
        mainFont.setFontSize(40);
        mainFont.setFontSpace(45.0);
        if (14 < sealDTO.getCompanyName().length() && sealDTO.getCompanyName().length() <= 20) {
            mainFont.setFontSize(30);
            mainFont.setFontSpace(35.0);
        }else if (20 < sealDTO.getCompanyName().length() && sealDTO.getCompanyName().length() <= 25) {
            mainFont.setFontSize(25);
            mainFont.setFontSpace(35.0);
        }else if (25 < sealDTO.getCompanyName().length() && sealDTO.getCompanyName().length() <= 30) {
            mainFont.setFontSize(20);
            mainFont.setFontSpace(35.0);
        }

        configuration.setMainFont(mainFont);

        /**
         * 副文字
         */
        if (sealDTO.getSerNo() != null && !"".equals(sealDTO.getSerNo())) {
            SealFont viceFont = new SealFont();
            viceFont.setBold(true);
            viceFont.setFontFamily("Arial");
            viceFont.setMarginSize(30);
            viceFont.setFontText(sealDTO.getSerNo());
            viceFont.setFontSize(25);
            viceFont.setFontSpace(0.0);

            configuration.setViceFont(viceFont);
        }

        /**
         * 中心文字
         */
        if (MyStringUtils.isNotBlank(sealDTO.getCenter())) {
            SealFont centerFont = new SealFont();
            centerFont.setBold(true);
            centerFont.setFontFamily("Arial");
            centerFont.setFontSize(35);
            centerFont.setLeftSpace(-2);
            if (sealDTO.getCenter().length() > 14) {
                centerFont.setFontSize(25);
            }
            centerFont.setFontText(sealDTO.getCenter());
            centerFont.setMarginSize(0);
            centerFont.setFontSpace(5.0);

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

            if (sealDTO.getTitle().length() <= 6) {
                titleFont.setLeftSpace((5 - sealDTO.getTitle().length()) * 10);
                titleFont.setFontSize(40);
                titleFont.setMarginSize(60);
            } else if (6 < sealDTO.getTitle().length()) {
                titleFont.setLeftSpace(-(sealDTO.getTitle().length() - 7) * 5);
                titleFont.setFontSize(30);
                titleFont.setMarginSize(45);
            }

            configuration.setTitleFont(titleFont);
        }

        /**
         * 图片大小
         */
        configuration.setHeight(300);
        configuration.setWidth(400);
        /**
         * 背景颜色
         */
        configuration.setBackgroudColor(sealDTO.getColor());
        /**
         * 边线粗细、半径
         */
        configuration.setBorderCircle(new SealCircle(10, 190, 140));

        return SealUtils.buildAndStoreSeal(configuration);
    }
}
