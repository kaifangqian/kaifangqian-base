/**
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
package com.kaifangqian.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.model.SysDepartSearchModel;
import com.kaifangqian.modules.system.vo.SysDepartVO;
import com.kaifangqian.modules.system.entity.SysDepart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 */

public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据部门Ids查询部门及以下部门
     */
    List<SysDepart> queryDepartAllList(@org.apache.ibatis.annotations.Param("departIds") List<String> departIds);

    /**
     * 根据部门Ids查询部门及以下部门
     */
    IPage<SysDepartSearchModel> queryDepartAllPage(Page page, @org.apache.ibatis.annotations.Param("departIds") List<String> departIds, @Param("keyWord") String keyWord, @Param("tenantId") String tenantId);

    /**
     * 根据部门Ids查询部门人数
     */
    List<SysDepartVO> countDepartUsers(@org.apache.ibatis.annotations.Param("departIds") List<String> departIds, @Param("tenantId") String tenantId);

    /**
     * 根据部门Ids查询部门主管
     */
    List<SysDepartVO> queryManagesListByDepartIds(@org.apache.ibatis.annotations.Param("departIds") List<String> departIds);

    /**
     * 根据用户ID查询部门集合
     */
    List<SysDepart> queryUserDeparts(@Param("userId") String userId, @Param("tenantId") String tenantId);

    /**
     * 根据父ID分成查询部门集合
     */
    List<SysDepartVO> queryList(@Param("departId") String departId, @Param("tenantId") String tenantId);

    /**
     * 查询我的顶级部门集合
     */
    List<SysDepartVO> queryMyList(@org.apache.ibatis.annotations.Param("orgCodes") List<String> orgCodes, @Param("tenantId") String tenantId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    List<SysDepart> queryDepartsByUsername(@Param("username") String username, @Param("tenantId") String tenantId);
}
