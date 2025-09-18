/**
 * @description 字体工具类
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
package com.kaifangqian.modules.opensign.util;

/**
 * @Description: 字体工具类
 * @Package: com.kaifangqian.modules.opensign.util
 * @ClassName: FontUtils
 * @author: Fusion
 * CreateTime:  2023/8/22  17:53
 * @copyright 北京资源律动科技有限公司
 */
public class FontUtils {

    /*宋体全部：
    Name:STSong
    Name:STSongti-SC-Black
    Name:STSongti-SC-Bold
    Name:STSongti-SC-Light
    Name:STSongti-SC-Regular
    Name:STSongti-TC-Bold
    Name:STSongti-TC-Light
    Name:STSongti-TC-Regular
    */

    /*Arial字体：
    Name:Arial-Black
    Name:Arial-BoldItalicMT
    Name:Arial-BoldMT
    Name:Arial-ItalicMT*/

    /*隶书：
    Name:STBaoliSC-Regular
    Name:STBaoliTC-Regular*/

    public static final String base64Prefix="data:image/png;base64,";
    public static final String SEAL_SAVE_PATH = System.getProperty("user.dir") + "/src/main/resources/static/upload/";
    public static final String Font_LiShu = "STBaoliTC-Regular";//隶书
    public static final String Font_SongTi = "STSongti-SC-Bold";//宋体
    public static final String Font_FangSong = "STFangsong";//仿宋
    public static final String Font_KaiTi = "STKaiti";//楷体
    public static final String Font_Arial = "Arial-Black";//Arial
    public final static int INIT_BEGIN = 10;
}
