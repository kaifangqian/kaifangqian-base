/**
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
package com.kaifangqian.modules.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.SysDepart;
import com.kaifangqian.modules.system.model.SysDepartSearchModel;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.utils.DateUtil;
import com.kaifangqian.utils.MyStringUtils;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.kaifangqian.modules.system.service.ISysDepartService;
import com.kaifangqian.modules.system.service.ISysUserDepartService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author zhenghuihan
 * @description 部门管理
 * @createTime 2022/8/29 14:43
 */
@RestController
@RequestMapping("/sys/sysDepart")
@Slf4j
@ResrunLogModule(name = "组织部门管理模块")
public class SysDepartController {

    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ISysUserDepartService sysUserDepartService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 新增组织(当前租户下的组织)
     *
     * @param sysDepartVO
     * @return
     */
    @ResrunLogMethod(name = "新增组织", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/addOrganization", method = RequestMethod.POST)
    @RequiresPermissions({"organize:add"})
    public Result<?> addOrganization(@RequestBody SysDepartVO sysDepartVO) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        sysDepartService.saveOrganization(sysDepartVO, user.getTenantId(), user.getId());
        return Result.OK();
    }

    /**
     * 新增部门（当前租户下的部门）
     *
     * @param sysDepartVO
     * @return
     */
    @ResrunLogMethod(name = "新增部门", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions(value = {"dept:add", "dept:addchild"}, logical = Logical.OR)
    public Result<?> add(@RequestBody SysDepartVO sysDepartVO) {
        sysDepartService.save(sysDepartVO);
        return Result.OK();
    }

    /**
     * 编辑组织、部门（不可编辑所属租户）
     *
     * @param sysDepartVO
     * @return
     */
    @ResrunLogMethod(name = "编辑组织部门", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @RequiresPermissions(value = {"dept:edit", "dept:setmanager"}, logical = Logical.OR)
    public Result<SysDepart> edit(@RequestBody SysDepartVO sysDepartVO) {
        sysDepartService.update(sysDepartVO);
        return Result.OK();
    }

    /**
     * 通过id查询详情
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "部门-通过id查询", notes = "部门-通过id查询")
    @GetMapping(value = "/queryById")
    @ResrunLogMethod(name = "查询组织部门详情", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<SysDepartVO> queryById(@RequestParam(name = "id", required = true) String id) {
        SysDepartVO sysDepartVO = sysDepartService.getByIdExt(id);
        return Result.OK(sysDepartVO);
    }

    /**
     * 通过id获取主管list-面板回显部门主管
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "通过id获取主管list", notes = "通过id获取主管list")
    @GetMapping(value = "/queryManagerById")
    @ResrunLogMethod(name = "查询部门主管列表", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> queryManagerById(@RequestParam(name = "id", required = true) String id) {
        List<NameAndIdForView> names = sysDepartService.getManagersByIdExt(id);
        return Result.OK(names);
    }

    /**
     * 根据部门id获取人员角色列表
     *
     * @param id
     * @return
     */
    @GetMapping("/getUserRolesByDepartId")
    @ResrunLogMethod(name = "查询部门人员角色列表", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<List<SysDepartRoleVO>> getUserRolesByDepartId(@RequestParam(name = "id") String id) {
        List<SysDepartRoleVO> sysUsers = sysUserDepartService.getUserRolesByDepartId(id);
        return Result.OK(sysUsers);
    }

    /**
     * 分层查出所有部门
     *
     * @return
     */
    @ResrunLogMethod(name = "分层查询所有部门", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/queryList", method = RequestMethod.GET)
    public Result<List<SysDepartVO>> queryList(String id) {
        if (MyStringUtils.isBlank(id)) {
            id = "";
        }
        List<SysDepartVO> departList = sysDepartService.queryList(id);
        return Result.OK(departList);
    }

    /**
     * 分层查询我的部门数据
     *
     * @return
     */
    @ResrunLogMethod(name = "分层查询我的部门", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/queryMyList", method = RequestMethod.GET)
    public Result<List<SysDepartVO>> queryMyList(String id) {
        List<SysDepartVO> list = sysDepartService.queryMyList(id);
        return Result.OK(list);
    }

    /**
     * 查出所有部门,并以树结构数据格式响应给前端（包含用户量）
     *
     * @return
     */
    @ResrunLogMethod(name = "查询所有部门树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<List<Tree<String>>> queryTreeList() {
        List<Tree<String>> departList = sysDepartService.queryTreeList();
        return Result.OK(departList);
    }

    /**
     * 查询数据 查出我的部门,并以树结构数据格式响应给前端（左侧树接口，包含用户量）
     *
     * @return
     */
    @ResrunLogMethod(name = "查询我的部门树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/queryMyDeptTreeList", method = RequestMethod.GET)
    public Result<List<Tree<String>>> queryMyDeptTreeList() {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<String> departIds = sysDepartService.queryDepartIdsByUsername(user.getUsername());
        List<Tree<String>> list = sysDepartService.queryMyDeptTreeList(departIds);
        return Result.OK(list);
    }

    /**
     * 查询数据 查出我的部门,并以树结构数据格式响应给前端（弹框接口，不包含用户量）
     *
     * @return
     */
    @ResrunLogMethod(name = "弹框查询我的部门树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/queryMyDeptTreeConcise", method = RequestMethod.GET)
    public Result<List<Tree<String>>> queryMyDeptTreeConcise() {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<String> departIds = sysDepartService.queryDepartIdsByUsername(user.getUsername());
        List<Tree<String>> list = sysDepartService.queryMyDeptTreeConcise(departIds);
        return Result.OK(list);
    }

    /**
     * 所有部门（我的部门可以操作）（反显使用，不包含用户量）
     *
     * @return
     */
    @ResrunLogMethod(name = "所有部门（我的部门可以操作）（反显使用，不包含用户量）", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/allDeptTreeConciseForSelect", method = RequestMethod.GET)
    public Result<List<Tree<String>>> allDeptTreeConciseForSelect() {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<String> departIds = sysDepartService.queryDepartIdsByUsername(user.getUsername());
        List<Tree<String>> list = sysDepartService.allDeptTreeConciseForSelect(departIds);
        return Result.OK(list);
    }

    /**
     * <p>
     * 部门搜索功能方法,根据关键字模糊搜索我的相关部门
     * </p>
     *
     * @param keyWord
     * @return
     */
    @ResrunLogMethod(name = "关键字搜索我的部门列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/searchMyBy", method = RequestMethod.GET)
    public Result<IPage<SysDepartSearchModel>> searchMyBy(@RequestParam(name = "keyWord", required = true) String keyWord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<String> departIds = sysDepartService.queryDepartIdsByUsername(user.getUsername());
        Page<SysDepartSearchModel> page = new Page<SysDepartSearchModel>(pageNo, pageSize);
        IPage<SysDepartSearchModel> result = sysDepartService.searchMyBy(keyWord, departIds, page);
        return Result.OK(result);
    }

    /**
     * 通过ids删除
     *
     * @param ids
     * @return
     */
    @RequiresPermissions({"dept:deletebatch"})
    @ResrunLogMethod(name = "批量删除部门", operateType = OperateLogType.OPERATE_TYPE_4)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name = "ids", required = true) String ids) {
        String[] arr = ids.split(",");
        sysDepartService.deleteBatchExt(Arrays.asList(arr));
        return Result.OK();
    }

    /**
     * 根据部门id获取人员列表(弹框)
     *
     * @param id
     * @return
     */
    @GetMapping("/getUsersByDepartId")
    @ResrunLogMethod(name = "查询部门人员列表(弹框)", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<SysDepartUserVO>> getUsersByDepartId(@RequestParam(name = "id") String id, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysDepartUserVO> page = new Page<SysDepartUserVO>(pageNo, pageSize);
        IPage<SysDepartUserVO> sysUsers = sysUserDepartService.queryUserByDepId(id, page);
        return Result.OK(sysUsers);
    }


    /**
     * 根据部门id获取人员列表（列表）
     *
     * @param id
     * @return
     */
    @GetMapping("/getUserListByDepartId")
    @ResrunLogMethod(name = "查询部门人员列表（列表）", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<SysDepartUserVO>> getUserListByDepartId(@RequestParam(name = "id") String id, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysDepartUserVO> page = new Page<SysDepartUserVO>(pageNo, pageSize);
        IPage<SysDepartUserVO> sysUsers = sysUserDepartService.getUserListByDepartId(id, page);
        return Result.OK(sysUsers);
    }

    /**
     * 发送提醒
     *
     * @param id
     * @return
     */
    @GetMapping("/sendInviteMes")
    @ResrunLogMethod(name = "发送提醒", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendInviteMes(@RequestParam(name = "id") String id) {


        //邀请法人授权认证-申请通知（法人接收）

        String oneOnlyOrderKey = id+ DateUtil.date2Str(DateUtil.getDate(), DateUtil.yyyyMMdd.get());

        Integer oneDayOrderLimit = (Integer) redisUtil.get(oneOnlyOrderKey);

        if(oneDayOrderLimit !=null && oneDayOrderLimit >= 1){
            return Result.error("您今天已经发送过邀请，请明天重试",null);
        }

        sysDepartService.sendInviteMes(id);

        redisUtil.incr(oneOnlyOrderKey,1);

        redisUtil.expire(oneOnlyOrderKey, 24 * 60 * 60);

        return Result.OK();
    }

    /**
     * 邀请成员加入
     *
     * @param departId
     * @return
     */
    @GetMapping("/inviteUser")
    @ResrunLogMethod(name = "邀请成员加入", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<InviteUserVO> inviteUser(@RequestParam(name = "departId") String departId) {
        InviteUserVO result = sysDepartService.inviteUser(departId);
        return Result.OK(result);
    }


    /**
     * 组织概览-统计
     *
     * @return
     */
    @GetMapping("/departStastics")
    @ResrunLogMethod(name = "组织概览-统计", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<DepartStasticsVO> departStastics() {
        DepartStasticsVO result = sysDepartService.departStastics();
        return Result.OK(result);
    }

    @GetMapping("/batckSendInviteMes")
    @ResrunLogMethod(name = "批量发送提醒", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> batckSendInviteMes() {
        sysDepartService.batckSendInviteMes();
        return Result.OK();
    }
}
