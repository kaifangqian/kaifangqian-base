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
package com.kaifangqian.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.external.sms.request.MsgRequest;
import com.kaifangqian.external.sms.service.SmsSendService;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.mapper.SysDepartMapper;
import com.kaifangqian.modules.system.model.SysDepartSearchModel;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.DepartStasticsVO;
import com.kaifangqian.modules.system.vo.InviteUserVO;
import com.kaifangqian.modules.system.vo.NameAndIdForView;
import com.kaifangqian.modules.system.vo.SysDepartVO;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.constant.FillRuleConstant;
import com.kaifangqian.common.constant.StatusConstant;
import com.kaifangqian.common.constant.enums.AuthType;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.FillRuleUtil;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.dto.EmailDto;
import com.kaifangqian.dto.MailDto;
import com.kaifangqian.dto.MessageDto;
import com.kaifangqian.entity.SysConfig;
import com.kaifangqian.enums.SysConfigType;
import com.kaifangqian.eunms.MesAuthType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.inteface.CommonToolsAPI;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.mapper.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.SysMessageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 */
@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {

    @Autowired
    private ISysUserDepartService iSysUserDepartService;
    @Resource
    private SysDepartMapper mapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    @Lazy
    private ISysAuthGroupMemberService iSysAuthGroupMemberService;
    @Autowired
    @Lazy
    private ISysTenantInfoService iSysTenantInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    @Lazy
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private SysMessageUtil sysMessageUtil;
    @Autowired
    private ISysAppInfoService sysAppInfoService;
    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private CommonToolsAPI commonToolsAPI;


    /**
     * 新增组织
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveOrganization(SysDepartVO sysDepartVO, String tenantId, String userId) {
        if (MyStringUtils.isNotBlank(sysDepartVO.getParentId())) {
            throw new PaasException("上级部门必须为空！");
        }
        sysDepartVO.setParentId("");
        if (MyStringUtils.isNotBlank(sysDepartVO.getId())) {
            throw new PaasException("新增组织接口，不可修改！");
        }
        String parentId = sysDepartVO.getParentId();
        JSONObject formData = new JSONObject();
        formData.put("parentId", parentId);
        String[] codeArray = (String[]) FillRuleUtil.executeRule(FillRuleConstant.DEPART, formData);
        sysDepartVO.setOrgCode(codeArray[0]);

        SysDepart sysDepart = new SysDepart();
        BeanUtils.copyProperties(sysDepartVO, sysDepart);
        sysDepart.setTenantId(tenantId);

        this.save(sysDepart);

        //更新 用户-部门 对用表数据
        if (MyStringUtils.isNotBlank(userId)) {
            List<String> userList = new ArrayList<>();
            userList.add(userId);

            iSysUserDepartService.addUserByDepartId(tenantId, sysDepart.getId(), userList);
        }

        return sysDepart.getId();
    }

    /**
     * 新增部门
     */
    @Override
    @Transactional
    public void save(SysDepartVO sysDepartVO) {
        //校验
        if (MyStringUtils.isBlank(sysDepartVO.getParentId())) {
            throw new PaasException("上级部门不能为空！");
        }
        if (MyStringUtils.isNotBlank(sysDepartVO.getId())) {
            throw new PaasException("新增部门接口，不可修改！");
        }

        //生成code
        String parentId = sysDepartVO.getParentId();
        JSONObject formData = new JSONObject();
        formData.put("parentId", parentId);
        String[] codeArray = (String[]) FillRuleUtil.executeRule(FillRuleConstant.DEPART, formData);
        sysDepartVO.setOrgCode(codeArray[0]);

        //新增主数据
        SysDepart sysDepart = new SysDepart();
        BeanUtils.copyProperties(sysDepartVO, sysDepart);
        LoginUser user = MySecurityUtils.getCurrentUser();
        sysDepart.setTenantId(user.getTenantId());

        this.save(sysDepart);
    }

    /**
     * 更新部门、组织
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDepartVO sysDepartVO) {
        //校验
        if (MyStringUtils.isBlank(sysDepartVO.getId())) {
            throw new PaasException("编辑部门接口，数据主ID能为空！");
        }

        //更新主数据
        SysDepart sysDepart = new SysDepart();
        BeanUtils.copyProperties(sysDepartVO, sysDepart);

        //上级部门不可修改
        sysDepart.setParentId(null);
        sysDepart.setTenantId(null);

        this.updateById(sysDepart);
        //更新 用户-部门 对应表数据
        iSysUserDepartService.updateManageByDepartId(sysDepart.getId(), sysDepartVO.getUserIds());
    }

    @Override
    public SysDepartVO getByIdExt(String id) {
        SysDepart sysDepart = this.getById(id);
        if (sysDepart != null) {
            SysDepartVO vo = new SysDepartVO();
            BeanUtils.copyProperties(sysDepart, vo);

            //主管
            SysUserDepart query = new SysUserDepart();
            query.setDepartId(id);
            query.setManageFlag(true);
            List<SysUserDepart> sysUserDeparts = iSysUserDepartService.getByEntity(query);
            if (CollUtil.isNotEmpty(sysUserDeparts)) {
                List<String> userIds = sysUserDeparts.stream().map(SysUserDepart::getUserId).collect(Collectors.toList());
                vo.setUserIds(userIds);
            }
            //上级部门名称（数据权限导致回显问题）
            if (MyStringUtils.isNotBlank(sysDepart.getParentId())) {
                SysDepart parent = this.getById(sysDepart.getParentId());
                if (parent != null) {
                    vo.setParentName(parent.getDepartName());
                }
            }
            return vo;
        }
        throw new PaasException("数据不存在！");
    }

    @Override
    public List<NameAndIdForView> getManagersByIdExt(String id) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        SysUserDepart query = new SysUserDepart();
        query.setDepartId(id);
        query.setTenantId(user.getTenantId());
        query.setManageFlag(true);
        List<SysUserDepart> sysUserDeparts = iSysUserDepartService.getByEntity(query);
        if (CollUtil.isNotEmpty(sysUserDeparts)) {
            List<String> userIds = sysUserDeparts.stream().map(SysUserDepart::getUserId).collect(Collectors.toList());

            List<SysUser> users = userMapper.selectList(Wrappers.lambdaQuery(SysUser.class).in(SysUser::getId, userIds));
//            List<SysUser> users = iSysUserService.findByUserIds(userIds);
            Map<String, SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getId, Function.identity(), (i, j) -> i));
            List<NameAndIdForView> result = new ArrayList<>();
            userIds.forEach(i -> {
                NameAndIdForView view = new NameAndIdForView();
                view.setId(i);
                view.setName(userMap.get(i) == null ? "" : userMap.get(i).getRealname());

                result.add(view);
            });
            return result;
        }
        return new ArrayList<>();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchExt(List<String> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            //校验部门下用户
            ids.forEach(i -> {
                if (!checkDelete(i)) {
                    throw new PaasException("选中删除的部门中存在用户，不可以删除！");
                }
            });
            //删除部门数据
            LoginUser user = MySecurityUtils.getCurrentUser();
            List<SysDepart> departs = mapper.queryDepartAllList(ids);
            if (CollUtil.isNotEmpty(departs)) {
                departs.forEach(d -> {
                    d.setDeleteFlag(true);
                    d.setDeleteBy(user.getUsername());
                    d.setDeleteTime(new Date());
                });
                //删除部门权限数据
                List<String> departIds = departs.stream().map(SysDepart::getId).collect(Collectors.toList());
                iSysAuthGroupMemberService.deleteByTenantIdAndTypeAndAuthIds(user.getTenantId(), AuthType.DEPT, departIds);
            }
            this.updateBatchById(departs);
        }
    }

    boolean checkDelete(String id) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<String> departIds = new ArrayList<>();
        departIds.add(id);
        List<SysDepartVO> departUsers = mapper.countDepartUsers(departIds, user.getTenantId());
        if (CollUtil.isNotEmpty(departUsers) && departUsers.get(0).getUserCount() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<SysDepartVO> queryMyList(String departId) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        if (MyStringUtils.isBlank(departId)) {
            List<String> departIds = queryDepartIdsByUsername(user.getUsername());
            String[] codeArr = this.getMyDeptParentOrgCode(departIds);
            if (codeArr.length > 0) {
                List<SysDepartVO> result = mapper.queryMyList(Arrays.asList(codeArr), user.getTenantId());

                //设置部门的主管名称/子部门数量
                setDepartManagesAndUserCount(result);
                return result;
            } else {
                return new ArrayList<>();
            }
        } else {
            List<SysDepartVO> result = mapper.queryList(departId, user.getTenantId());
            //设置部门的主管名称/子部门数量
            setDepartManagesAndUserCount(result);
            return result;
        }
    }

    @Override
    public List<SysDepartVO> queryList(String departId) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<SysDepartVO> result = mapper.queryList(departId, user.getTenantId());
        //设置部门的主管名称/子部门数量
        setDepartManagesAndUserCount(result);
        return result;
    }

    //设置部门的主管名称/子部门数量
    void setDepartManagesAndUserCount(List<SysDepartVO> result) {
        if (CollUtil.isNotEmpty(result)) {
            //查询部门的主管名称
            List<String> departIds = result.stream().map(SysDepartVO::getId).collect(Collectors.toList());
            List<SysDepartVO> departManages = mapper.queryManagesListByDepartIds(departIds);
            Map<String, SysDepartVO> tem = departManages.stream().collect(Collectors.toMap(SysDepartVO::getId, Function.identity(), (i, j) -> i));

            //查询部门子部门数量
            LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.in(SysDepart::getParentId, departIds).eq(SysDepart::getDeleteFlag, StatusConstant.DEL_FLAG_0);
            List<SysDepart> list = this.list(queryWrapper);
            Map<String, List<SysDepart>> childMap = new HashMap<>();
            if (CollUtil.isNotEmpty(list)) {
                childMap = list.stream().collect(Collectors.groupingBy((SysDepart::getParentId)));
            }

            for (SysDepartVO d : result) {
                //设置部门的主管名称
                d.setManageNames(tem.get(d.getId()) == null ? null : tem.get(d.getId()).getManageNames());
                //设置部门的子部门数量
                d.setChildCount(childMap.get(d.getId()) == null ? 0 : childMap.get(d.getId()).size());
            }
        }
    }

    @Override
    public List<Tree<String>> queryMyDeptTreeList(List<String> departIds) {
        //根据部门id获取所负责部门
        LoginUser user = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        String[] codeArr = this.getMyDeptParentOrgCode(departIds);
        if (codeArr.length > 0) {
            List<String> codes = Arrays.asList(codeArr);
            codes.forEach(c -> {
                query.or().likeRight(SysDepart::getOrgCode, c);
            });
            query.eq(SysDepart::getDeleteFlag, StatusConstant.DEL_FLAG_0).eq(SysDepart::getTenantId, user.getTenantId());
            query.orderByAsc(SysDepart::getDepartOrder);
            List<SysDepart> listDepts = this.list(query);

            //将父节点ParentId设为""
            for (SysDepart dept : listDepts) {
                if (codes.contains(dept.getOrgCode())) {
                    dept.setParentId("");
                }
            }
            //返回树结构
            return getDepartTree(listDepts);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Tree<String>> queryMyDeptTreeConcise(List<String> departIds) {
        //根据部门id获取所负责部门
        LoginUser user = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        String[] codeArr = this.getMyDeptParentOrgCode(departIds);
        if (codeArr.length > 0) {
            List<String> codes = Arrays.asList(codeArr);
            codes.forEach(c -> {
                query.or().likeRight(SysDepart::getOrgCode, c);
            });
            query.eq(SysDepart::getDeleteFlag, StatusConstant.DEL_FLAG_0).eq(SysDepart::getTenantId, user.getTenantId());
            query.orderByAsc(SysDepart::getDepartOrder);
            List<SysDepart> listDepts = this.list(query);

            //将父节点ParentId设为""
            for (SysDepart dept : listDepts) {
                if (codes.contains(dept.getOrgCode())) {
                    dept.setParentId("");
                }
            }
            //返回树结构
            return getDepartTreeConcise(listDepts);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Tree<String>> allDeptTreeConciseForSelect(List<String> departIds) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        //查询所有部门
        LambdaQueryWrapper<SysDepart> queryAll = new LambdaQueryWrapper<SysDepart>();
        queryAll.eq(SysDepart::getDeleteFlag, StatusConstant.DEL_FLAG_0).eq(SysDepart::getTenantId, user.getTenantId());
        queryAll.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> listAll = this.list(queryAll);
        //查询我的部门
        LambdaQueryWrapper<SysDepart> queryMy = new LambdaQueryWrapper<SysDepart>();
        String[] codeArr = this.getMyDeptParentOrgCode(departIds);
        if (codeArr.length > 0) {
            List<String> codes = Arrays.asList(codeArr);
            codes.forEach(c -> {
                queryMy.or().likeRight(SysDepart::getOrgCode, c);
            });
            queryMy.eq(SysDepart::getDeleteFlag, StatusConstant.DEL_FLAG_0).eq(SysDepart::getTenantId, user.getTenantId());
            queryMy.orderByAsc(SysDepart::getDepartOrder);
            List<SysDepart> listMy = this.list(queryMy);
            List<String> listMyIds = listMy.stream().map(SysDepart::getId).collect(Collectors.toList());
            listAll.forEach(d -> {
                if (listMyIds.contains(d.getId())) {
                    d.setOprateFlag(true);
                }
            });

        }
        //返回树结构
        return getDepartTreeConciseForSelect(listAll);
    }

    /**
     * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
     */
    @Override
    public List<Tree<String>> queryTreeList() {
        LoginUser user = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDeleteFlag, StatusConstant.DEL_FLAG_0).eq(SysDepart::getTenantId, user.getTenantId());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);

        //返回树结构
        return getDepartTree(list);
    }

    /**
     * @create by zhenghuihan
     * @createTime 2022/8/31 10:14
     * @description 构建组织树（包含用户量）
     */
    public List<Tree<String>> getDepartTree(List<SysDepart> list) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        //设置部门人数（性能有问题可以时删掉）
        List<String> departIds = list.stream().map(SysDepart::getId).collect(Collectors.toList());
        List<SysDepartVO> departUsers = mapper.countDepartUsers(departIds, user.getTenantId());
        Map<String, SysDepartVO> departUsersMap = departUsers.stream().collect(Collectors.toMap(SysDepartVO::getId, Function.identity(), (i, j) -> i));

        if (CollUtil.isNotEmpty(list)) {
            List<Tree<String>> treeList = TreeUtil.build(list, "", (object, tree) -> {
                tree.setId(object.getId());
                tree.setParentId(object.getParentId());

                tree.putExtra("key", object.getId());
                tree.putExtra("departName", object.getDepartName());
                tree.putExtra("title", object.getDepartName());
                tree.putExtra("orgCode", object.getOrgCode());
                tree.putExtra("departOrder", object.getDepartOrder());
                tree.putExtra("userCount", departUsersMap.get(object.getId()) == null ? 0 : departUsersMap.get(object.getId()).getUserCount());
                tree.putExtra("oprateFlag", true);
                tree.putExtra("type", "depart");
            });

            return treeList;
        }
        return new ArrayList<>();
    }

    /**
     * @create by zhenghuihan
     * @createTime 2022/8/31 10:14
     * @description 构建组织树（不包含用户量）
     */
    public List<Tree<String>> getDepartTreeConcise(List<SysDepart> list) {
        if (CollUtil.isNotEmpty(list)) {
            List<Tree<String>> treeList = TreeUtil.build(list, "", (object, tree) -> {
                tree.setId(object.getId());
                tree.setParentId(object.getParentId());

                tree.putExtra("key", object.getId());
                tree.putExtra("departName", object.getDepartName());
                tree.putExtra("title", object.getDepartName());
                tree.putExtra("orgCode", object.getOrgCode());
                tree.putExtra("departOrder", object.getDepartOrder());
                tree.putExtra("type", "dept");
                tree.putExtra("oprateFlag", true);
            });

            return treeList;
        }
        return new ArrayList<>();
    }

    /**
     * @create by zhenghuihan
     * @createTime 2022/8/31 10:14
     * @description 构建组织树（不包含用户量）
     */
    public List<Tree<String>> getDepartTreeConciseForSelect(List<SysDepart> list) {
        if (CollUtil.isNotEmpty(list)) {
            List<Tree<String>> treeList = TreeUtil.build(list, "", (object, tree) -> {
                tree.setId(object.getId());
                tree.setParentId(object.getParentId());

                tree.putExtra("key", object.getId());
                tree.putExtra("departName", object.getDepartName());
                tree.putExtra("title", object.getDepartName());
                tree.putExtra("orgCode", object.getOrgCode());
                tree.putExtra("departOrder", object.getDepartOrder());
                tree.putExtra("type", "dept");
                tree.putExtra("oprateFlag", object.isOprateFlag());
            });

            return treeList;
        }
        return new ArrayList<>();
    }


    /**
     * <p>
     * 根据关键字搜索相关的部门数据
     * </p>
     */
    @Override
    public IPage<SysDepartSearchModel> searchMyBy(String keyWord, List<String> departIds, Page<SysDepartSearchModel> page) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        return mapper.queryDepartAllPage(page, departIds, keyWord, user.getTenantId());
    }

    @Override
    public List<SysDepart> queryUserDeparts(String userId, String tenantId) {
        return baseMapper.queryUserDeparts(userId, tenantId);
    }

    @Override
    public List<SysDepart> queryDepartsByUsername(String username) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        return baseMapper.queryDepartsByUsername(username, user.getTenantId());
    }

    @Override
    public List<String> queryDepartIdsByUsername(String username) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<SysDepart> list = baseMapper.queryDepartsByUsername(username, user.getTenantId());
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(SysDepart::getId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 根据用户所负责部门ids获取父级部门编码
     *
     * @param departIds
     * @return
     */
    private String[] getMyDeptParentOrgCode(List<String> departIds) {
        //根据部门id查询所负责部门
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDeleteFlag, StatusConstant.DEL_FLAG_0);
        query.in(SysDepart::getId, departIds);
        query.orderByAsc(SysDepart::getOrgCode);
        List<SysDepart> list = this.list(query);
        //查找根部门
        if (!CollUtil.isNotEmpty(list)) {
            return new String[]{};
        }
        String orgCode = this.getMyDeptParentNode(list);
        String[] codeArr = orgCode.split(",");
        return codeArr;
    }

    /**
     * 获取负责部门父节点
     *
     * @param list
     * @return
     */
    private String getMyDeptParentNode(List<SysDepart> list) {
        Map<String, String> map = new HashMap<>();
        //1.先将同一公司归类
        for (SysDepart dept : list) {
            String code = dept.getOrgCode().substring(0, 3);
            if (map.containsKey(code)) {
                String mapCode = map.get(code) + "," + dept.getOrgCode();
                map.put(code, mapCode);
            } else {
                map.put(code, dept.getOrgCode());
            }
        }
        StringBuffer parentOrgCode = new StringBuffer();
        //2.获取同一公司的根节点
        for (String str : map.values()) {
            String[] arrStr = str.split(",");
            parentOrgCode.append(",").append(this.getMinLengthNode(arrStr));
        }
        return parentOrgCode.substring(1);
    }

    /**
     * 获取同一公司中部门编码长度最小的部门
     *
     * @param str
     * @return
     */
    private String getMinLengthNode(String[] str) {
        int min = str[0].length();
        String orgCode = str[0];
        for (int i = 1; i < str.length; i++) {
            if (str[i].length() <= min) {
                min = str[i].length();
                orgCode = orgCode + "," + str[i];
            }
        }
        return orgCode;
    }

    /**
     * 查询某个部门的所有父ID信息
     *
     * @param fieldName 字段名
     * @param value     值
     */
    private JSONObject queryAllParentId(String fieldName, String value) {
        JSONObject data = new JSONObject();
        // 父ID集合，有序
        data.put("parentIds", new JSONArray());
        // 父ID的部门数据，key是id，value是数据
        data.put("parentMap", new JSONObject());
        this.queryAllParentIdRecursion(fieldName, value, data);
        return data;
    }

    /**
     * 递归调用查询父部门接口
     */
    private void queryAllParentIdRecursion(String fieldName, String value, JSONObject data) {
        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(fieldName, value);
        SysDepart depart = super.getOne(queryWrapper);
        if (depart != null) {
            data.getJSONArray("parentIds").add(0, depart.getId());
            data.getJSONObject("parentMap").put(depart.getId(), depart);
            if (oConvertUtils.isNotEmpty(depart.getParentId())) {
                this.queryAllParentIdRecursion("id", depart.getParentId(), data);
            }
        }
    }

    @Override
    public List<SysDepart> getByOrgCodes(List<String> orgCodes) {
        LambdaQueryWrapper<SysDepart> sysDepartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDepartLambdaQueryWrapper.in(SysDepart::getOrgCode, orgCodes);
        List<SysDepart> sysDeparts = this.baseMapper.selectList(sysDepartLambdaQueryWrapper);
        return sysDeparts;
    }

    @Override
    public void sendInviteMes(String tenantUserId) {
        SysTenantUser sysTenantUser = iSysTenantUserService.getById(tenantUserId);
        if (sysTenantUser != null) {
            SysUser sysUser = userMapper.selectById(sysTenantUser.getUserId());
            if (sysUser != null) {
                InviteUserVO redis = new InviteUserVO();
                redis.setPhone(sysUser.getPhone());
                redis.setEmail(sysUser.getEmail());
                redis.setTenantId(sysTenantUser.getTenantId());

                //String pre = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 3).toUpperCase();
                String key = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8).toUpperCase();
                //String key = pre + "-" + last;
                //存入redis
                redisUtil.set(CommonConstant.PREFIX_INVITE_USER_CODE + key, redis);
                //一天有效
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + key, (long) (24 * 60 * 60));

                String tenantName = "";
                SysTenantInfo sysTenantInfo = iSysTenantInfoService.getById(sysTenantUser.getTenantId());
                if (sysTenantInfo != null) {
                    tenantName = sysTenantInfo.getTenantName();
                }
                if (MyStringUtils.isNotBlank(sysUser.getPhone())) {

                    SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
                    SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
                    if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
                        MsgRequest msgRequest = new MsgRequest();
                        msgRequest.setPhoneNumbers(sysUser.getPhone());
                        msgRequest.setTemplateName("inviteUser");
                        Map<String, String> para = new HashMap<>();
                        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                        if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                            para.put("domain", sysAppInfo.getAppAddress());
                        }
                        para.put("companyName", tenantName);
                        para.put("key", key);
                        msgRequest.setParams(para);
                        smsSendService.sendMsg(msgRequest);
                    }else{
                        //发送短信
                        Map<String, String> para = new HashMap<>();
                        para.put("companyName", tenantName);
                        para.put("key", key);
                        MessageDto messageDto = new MessageDto();
                        messageDto.setSendType(SendType.IMMEDIATELY);
                        List<String> receivers = new ArrayList<>();
                        receivers.add(sysUser.getPhone());
                        messageDto.setReceivers(receivers);
                        messageDto.setTemplateCode("inviteUser");
                        messageDto.setContentParaMap(para);
                        sysMessageUtil.asyncSendMessage(messageDto);
                    }


                }
                if (MyStringUtils.isNotBlank(sysUser.getEmail())) {
                    //发送邮件
                    EmailDto emailDto = new EmailDto();
                    Map<String, String> para = new HashMap<>();
                    para.put("companyName", tenantName);
                    SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                    if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                        para.put("domain", sysAppInfo.getAppAddress());
                    }
                    para.put("key", key);
                    emailDto.setSendType(SendType.IMMEDIATELY);
                    List<String> receivers = new ArrayList<>();
                    receivers.add(sysUser.getEmail());
                    emailDto.setReceivers(receivers);
                    emailDto.setTemplateCode("email_inviteUser");
                    emailDto.setContentParaMap(para);
                    sysMessageUtil.asyncSendEmail(emailDto);
                }


//                //发送站内信
                MailDto mailDto = new MailDto();
                mailDto.setSendType(SendType.IMMEDIATELY);
                Map<MesAuthType, List<String>> userMap = new HashMap<>();
                List<String> userIds = new ArrayList<>();
                userIds.add(tenantUserId);
                userMap.put(MesAuthType.USER, userIds);
                mailDto.setReceivers(userMap);

                mailDto.setTemplateCode("invite_user");

                Map<String, String> titleParaMap = new HashMap<>();
                titleParaMap.put("companyName", tenantName);
                mailDto.setTitleParaMap(titleParaMap);

                Map<String, String> contentParaMap = new HashMap<>();
                contentParaMap.put("companyName", tenantName);
                mailDto.setContentParaMap(contentParaMap);

                Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
                Map<String, String> para1 = new HashMap<>();
                para1.put("type", "1");
                para1.put("key", key);
                buttonParaMap.put("join", para1);
                mailDto.setButtonParaMap(buttonParaMap);

                //发送通知
                sysMessageUtil.asyncSendMail(mailDto);
            }
        }
    }

    @Override
    public InviteUserVO inviteUser(String departId) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        String tenantId = loginUser.getTenantId();
        checkDepart(tenantId, departId);
        InviteUserVO redis = new InviteUserVO();
        redis.setDepartId(departId);
        redis.setTenantId(loginUser.getTenantId());

        String pre = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 4).toUpperCase();
        String last = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 9).toUpperCase();
        String key = pre + "-" + last;
        //存入redis
        redisUtil.set(CommonConstant.PREFIX_INVITE_USER_CODE + key, redis);
        //一天有效
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + key, (long) (24 * 60 * 60));

        InviteUserVO result = new InviteUserVO();
        result.setDepartCode(key);
        SysTenantInfo sysTenantInfo = iSysTenantInfoService.getById(tenantId);
        result.setInvitationCode(sysTenantInfo.getInvitationCode());
        result.setTenantName(sysTenantInfo.getTenantName());
        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
        String url = "";
        if (sysAppInfo != null) {
            url = sysAppInfo.getAppAddress() + "/#/join?key=" + key + "&type=1";
        }
        result.setUrl(url);

        return result;
    }

    @Override
    public List<SysDepart> getByEntity(SysDepart sysDepart) {
        LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDepart::getDeleteFlag, 0).eq(MyStringUtils.isNotBlank(sysDepart.getTenantId()), SysDepart::getTenantId, sysDepart.getTenantId()).eq(MyStringUtils.isNotBlank(sysDepart.getParentId()), SysDepart::getParentId, sysDepart.getParentId()).eq(MyStringUtils.isBlank(sysDepart.getParentId()), SysDepart::getParentId, "");

        return this.list(queryWrapper);
    }

    @Override
    public List<SysDepart> getTenantDeparts() {
        LoginUser user = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDeleteFlag, StatusConstant.DEL_FLAG_0).eq(SysDepart::getTenantId, user.getTenantId());
        query.orderByAsc(SysDepart::getDepartOrder);

        return this.list(query);
    }

    @Override
    public SysDepart getByOrgCode(String orgCode) {
        LambdaQueryWrapper<SysDepart> sysDepartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDepartLambdaQueryWrapper.eq(SysDepart::getDeleteFlag, 0).eq(SysDepart::getOrgCode, orgCode);
        return this.baseMapper.selectOne(sysDepartLambdaQueryWrapper);
    }

    @Override
    public SysDepart getByTenantId(String tenantId) {
        LambdaQueryWrapper<SysDepart> sysDepartLambdaQueryWrapper = new LambdaQueryWrapper<>();

        sysDepartLambdaQueryWrapper.eq(SysDepart::getDeleteFlag, 0).eq(SysDepart::getTenantId, tenantId).eq(SysDepart::getParentId, "");

        return this.baseMapper.selectOne(sysDepartLambdaQueryWrapper);
    }

    @Override
    public List<SysDepart> queryByDepartIds(List<String> departIds) {
        if (CollUtil.isNotEmpty(departIds)) {
            LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysDepart::getId, departIds);
            List<SysDepart> sysDeparts = this.baseMapper.selectList(queryWrapper);
            return sysDeparts;
        }
        return new ArrayList<>();
    }

    @Override
    public DepartStasticsVO departStastics() {
        DepartStasticsVO result = new DepartStasticsVO();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysDepart> departQueryWrapper = new LambdaQueryWrapper<>();
        departQueryWrapper.eq(SysDepart::getTenantId, loginUser.getTenantId()).eq(SysDepart::getDeleteFlag, 0);
        Long departAllCount = this.count(departQueryWrapper);
        result.setDepartAllCount(departAllCount.intValue());

        LambdaQueryWrapper<SysTenantUser> tenantUserQueryWrapper1 = new LambdaQueryWrapper<>();
        tenantUserQueryWrapper1.eq(SysTenantUser::getTenantId, loginUser.getTenantId()).eq(SysTenantUser::getDeleteFlag, 0).gt(SysTenantUser::getStatus, -2);
        Long userAllCount = iSysTenantUserService.count(tenantUserQueryWrapper1);
        result.setUserAllCount(userAllCount.intValue());

        LambdaQueryWrapper<SysTenantUser> tenantUserQueryWrapper2 = new LambdaQueryWrapper<>();
        tenantUserQueryWrapper2.eq(SysTenantUser::getTenantId, loginUser.getTenantId()).eq(SysTenantUser::getDeleteFlag, 0).eq(SysTenantUser::getStatus, 0);
        Long user0Count = iSysTenantUserService.count(tenantUserQueryWrapper2);
        result.setUser0Count(user0Count.intValue());

        return result;
    }

    @Override
    public void batckSendInviteMes() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUser::getTenantId, loginUser.getTenantId()).eq(SysTenantUser::getDeleteFlag, 0).eq(SysTenantUser::getStatus, 0);

        List<SysTenantUser> list = iSysTenantUserService.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(l -> {
                sendInviteMes(l.getId());
            });
        }
    }

    void checkDepart(String tenantId, String departId) {
        LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDepart::getDeleteFlag, 0).eq(SysDepart::getTenantId, tenantId).eq(SysDepart::getId, departId);

        List<SysDepart> sysDeparts = this.list(queryWrapper);
        if (CollUtil.isEmpty(sysDeparts)) {
            throw new PaasException("传值有误");
        }
    }
}
