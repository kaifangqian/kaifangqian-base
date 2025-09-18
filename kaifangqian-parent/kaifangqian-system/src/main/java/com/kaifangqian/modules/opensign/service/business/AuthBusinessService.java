/**
 * @description 签署业务权限服务
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
package com.kaifangqian.modules.opensign.service.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.opensign.entity.SignBusinessAuth;
import com.kaifangqian.modules.opensign.entity.SignBusinessAuthPermission;
import com.kaifangqian.modules.opensign.enums.AuthTypeEnum;
import com.kaifangqian.modules.opensign.enums.BusinessAuthType;
import com.kaifangqian.modules.opensign.enums.BusinessAuthTypeRole;
import com.kaifangqian.modules.opensign.service.auth.SignBusinessAuthPermissionService;
import com.kaifangqian.modules.opensign.service.auth.SignBusinessAuthService;
import com.kaifangqian.modules.opensign.service.auth.vo.BusinessAuthQueryVo;
import com.kaifangqian.modules.opensign.vo.base.BusinessAuthVo;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: AuthBusinessService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: AuthBusinessService
 * @author: FengLai_Gong
 */
@Service
public class AuthBusinessService {


    @Autowired
    private SignBusinessAuthService businessAuthService;
    @Autowired
    private SignBusinessAuthPermissionService businessAuthPermissionService ;
    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysUserDepartService userDepartService ;
    @Autowired
    private ISysTenantUserService tenantUserService ;
    @Autowired
    private ISysDepartService departService ;
    @Autowired
    private ISysRoleService roleService ;



    public List<SysTenantUser> getTenantUserByBusinessRelationId(String businessRelationId, List<Integer> businessTypeRoleList, Integer businessType){

        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        Map<String,SysTenantUser> tenantUserMap = new HashMap<>();
        List<SysTenantUser> tenantUserList = new ArrayList<>();

        QueryWrapper<SignBusinessAuth> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        //关联业务id
        wrapper.lambda().eq(SignBusinessAuth::getBusinessRelationId,businessRelationId);
        //关联业务角色，管理者，审计者，使用者
        wrapper.lambda().in(SignBusinessAuth::getBusinessTypeRole,businessTypeRoleList);
        //业务类型，1为签章，2为模板，3为文档，4为业务线
        wrapper.lambda().eq(SignBusinessAuth::getBusinessType,businessType);
        List<SignBusinessAuth> list = businessAuthService.list(wrapper);

        if(list != null && list.size() > 0){
            for(SignBusinessAuth auth : list){
                if(auth.getAuthType() == AuthTypeEnum.TENANT_USER.getCode() && auth.getAuthRelationId() != null && auth.getAuthRelationId().length() > 0){
                    //1用户(租户下用户)
                    SysTenantUser tenantUser = tenantUserService.getById(auth.getAuthRelationId());
                    if(tenantUser != null){
                        if(!tenantUserMap.containsKey(tenantUser.getId())){
                            if(!tenantUser.getDeleteFlag() && tenantUser.getStatus() == 1){
                                tenantUserMap.put(tenantUser.getId(),tenantUser);
                            }
                        }
                    }
                }else if(auth.getAuthType() == AuthTypeEnum.DEPART.getCode() && auth.getAuthRelationId() != null && auth.getAuthRelationId().length() > 0){
                    //2部门
                    SysUserDepart sysUserDepart = new SysUserDepart();
                    sysUserDepart.setDepartId(auth.getAuthRelationId());
                    sysUserDepart.setTenantId(currentUser.getTenantId());
                    List<SysUserDepart> userDepartList = userDepartService.getByEntity(sysUserDepart);
                    if(userDepartList != null && userDepartList.size() > 0){
                        List<String> userIdList = userDepartList.stream().map(SysUserDepart::getUserId).collect(Collectors.toList());
                        if(userIdList != null && userIdList.size() > 0){
                            QueryWrapper<SysTenantUser> tenantUserQueryWrapper = new QueryWrapper<>();
                            tenantUserQueryWrapper.lambda().eq(SysTenantUser::getTenantId,currentUser.getTenantId());
                            tenantUserQueryWrapper.lambda().in(SysTenantUser::getUserId,userIdList);
                            tenantUserQueryWrapper.lambda().eq(SysTenantUser::getStatus,1);
                            tenantUserQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
                            List<SysTenantUser> tenantUsers = tenantUserService.list(tenantUserQueryWrapper);
                            if(tenantUsers != null && tenantUsers.size() > 0){
                                for(SysTenantUser tenantUser : tenantUsers){
                                    if(!tenantUserMap.containsKey(tenantUser.getId())){
                                        tenantUserMap.put(tenantUser.getId(),tenantUser);
                                    }
                                }
                            }
                        }
                    }
                }else if(auth.getAuthType() == AuthTypeEnum.ROLE.getCode() && auth.getAuthRelationId() != null && auth.getAuthRelationId().length() > 0){
                    //3角色
                    QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                    userRoleQueryWrapper.lambda().eq(SysUserRole::getTenantId,currentUser.getTenantId());
                    userRoleQueryWrapper.lambda().eq(SysUserRole::getRoleId,auth.getAuthRelationId());
                    List<SysUserRole> userRoleList = userRoleService.list(userRoleQueryWrapper);
                    if(userRoleList != null && userRoleList.size() > 0){
                        List<String> userIdList = userRoleList.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
                        if(userIdList != null && userIdList.size() > 0){
                            QueryWrapper<SysTenantUser> tenantUserQueryWrapper = new QueryWrapper<>();
                            tenantUserQueryWrapper.lambda().eq(SysTenantUser::getTenantId,currentUser.getTenantId());
                            tenantUserQueryWrapper.lambda().in(SysTenantUser::getUserId,userIdList);
                            tenantUserQueryWrapper.lambda().eq(SysTenantUser::getStatus,1);
                            tenantUserQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
                            List<SysTenantUser> tenantUsers = tenantUserService.list(tenantUserQueryWrapper);
                            if(tenantUsers != null && tenantUsers.size() > 0){
                                for(SysTenantUser tenantUser : tenantUsers){
                                    if(!tenantUserMap.containsKey(tenantUser.getId())){
                                        tenantUserMap.put(tenantUser.getId(),tenantUser);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        if(tenantUserMap.size() > 0){
            List<SysTenantUser> collect = tenantUserMap.values().stream().collect(Collectors.toList());
            tenantUserList.addAll(collect);
        }

        return tenantUserList ;
    }




    public Integer authIdentify(String businessRelationId){
        BusinessAuthQueryVo vo = build(businessRelationId,BusinessAuthType.SEAL,null);

        Integer authIdentify = businessAuthService.getAuthIdentify(vo);

        return authIdentify ;
    }


    public List<SignBusinessAuthPermission> getPermissionList(BusinessAuthType businessAuthType,String businessRelationId){
        BusinessAuthQueryVo query = build(businessRelationId,businessAuthType,null);

        Integer authIdentify = businessAuthService.getAuthIdentify(query);
        //获取签章业务权限
        List<SignBusinessAuthPermission> sealAuthList = businessAuthPermissionService.getList(businessAuthType.getCode(),authIdentify);

        return sealAuthList ;
    }

    public List<String> getBusinessIdList(BusinessAuthType businessAuthType,List<Integer> businessTypeRoleList){
        BusinessAuthQueryVo vo = build(null, businessAuthType,businessTypeRoleList);
        List<SignBusinessAuth> list = businessAuthService.queryAuthList(vo);

        if(list != null && list.size() > 0){
            List<String> collect = list.stream().map(SignBusinessAuth::getBusinessRelationId).collect(Collectors.toList());
            return collect ;
        }
        return null ;
    }


//    public void buildSealAuthList(EntSealInfoResponse response){
//        List<SignBusinessAuth> authList = getAuthListByBusinessId(response.getSealId(), BusinessAuthType.SEAL);
//
//        if(authList == null || authList.size() == 0){
//            return;
//        }
//        Map<Integer, List<SignBusinessAuth>> collect = authList.stream().collect(Collectors.groupingBy(SignBusinessAuth::getBusinessTypeRole));
//        if(collect == null || collect.size() == 0){
//            return;
//        }
//        response.setManagerList(convertVo(collect.get(BusinessAuthTypeRole.MANAGER.getCode())));
//        response.setUserList(convertVo(collect.get(BusinessAuthTypeRole.USER.getCode())));
//        response.setAuditorList(convertVo(collect.get(BusinessAuthTypeRole.AUDITOR.getCode())));
//    }

    public List<BusinessAuthVo> convertVo(List<SignBusinessAuth> authList){
        List<BusinessAuthVo> voList = new ArrayList<>();

        if(authList == null || authList.size() == 0){
            return voList ;
        }

        for(SignBusinessAuth auth : authList){
            BusinessAuthVo vo = new BusinessAuthVo();
            vo.setAuthType(auth.getAuthType());
//            vo.setAuthTypeName(businessAuthTypeRole.getName());
            vo.setAuthRelationId(auth.getAuthRelationId());
            if(auth.getAuthType() == AuthTypeEnum.TENANT_USER.getCode()){
                //1用户(租户下用户)
                SysTenantUser tenantUser = tenantUserService.getById(auth.getAuthRelationId());
                if(tenantUser != null){
                    if(!tenantUser.getDeleteFlag() && tenantUser.getStatus() == 1){
                        vo.setAuthRelationName(tenantUser.getNickName());
                        voList.add(vo);
                    }
                }
            }else if(auth.getAuthType() == AuthTypeEnum.DEPART.getCode()){
                //2部门
                SysDepart depart = departService.getById(auth.getAuthRelationId());
                if(depart != null){
                    vo.setAuthRelationName(depart.getDepartName());
                    voList.add(vo);
                }
            }else if(auth.getAuthType() == AuthTypeEnum.ROLE.getCode()){
                //3角色
                SysRole role = roleService.getById(auth.getAuthRelationId());
                if(role != null){
                    vo.setAuthRelationName(role.getRoleName());
                    voList.add(vo);
                }
            }

        }
        return voList ;
    }

    public List<SignBusinessAuth> getAuthListByBusinessId(String businessId,BusinessAuthType businessAuthType){
        QueryWrapper<SignBusinessAuth> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignBusinessAuth::getBusinessType,businessAuthType.getCode());
        wrapper.lambda().eq(SignBusinessAuth::getBusinessRelationId,businessId);
        List<SignBusinessAuth> list = businessAuthService.list(wrapper);

        return list ;
    }


    public void saveSealAuthManagerList(String businessRelationId, List<BusinessAuthVo> voList,String departId){
        saveList(BusinessAuthType.SEAL, BusinessAuthTypeRole.SEAL_MANAGER, businessRelationId, voList,departId);
    }

    public void saveSealAuthUserList(String businessRelationId, List<BusinessAuthVo> voList,String departId){
        saveList(BusinessAuthType.SEAL, BusinessAuthTypeRole.SEAL_USER, businessRelationId, voList, departId);
    }

    public void saveSealAuthAuditorList(String businessRelationId, List<BusinessAuthVo> voList,String departId){
        saveList(BusinessAuthType.SEAL, BusinessAuthTypeRole.SEAL_AUDITOR, businessRelationId, voList,departId);
    }

    public void saveTemplateManageList(String businessRelationId, List<BusinessAuthVo> voList,String departId){
        saveList(BusinessAuthType.TEMPLATE, BusinessAuthTypeRole.TEMPLATE_MANAGE, businessRelationId, voList,departId);
    }

    public void saveTemplateUserList(String businessRelationId, List<BusinessAuthVo> voList,String departId){
        saveList(BusinessAuthType.TEMPLATE, BusinessAuthTypeRole.TEMPLATE_USER, businessRelationId, voList,departId);
    }


    public void saveList(BusinessAuthType businessAuthType ,BusinessAuthTypeRole businessTypeRole , String businessRelationId, List<BusinessAuthVo> voList,String departId){

        List<SignBusinessAuth> list = new ArrayList<>();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        for(BusinessAuthVo vo : voList){
            SignBusinessAuth auth = new SignBusinessAuth();
            //授权类型，1用户(租户下用户)，2部门，3角色
            auth.setAuthType(vo.getAuthType());
            //权限关联id，租户下用户id或者部门ID或者角色ID
            auth.setAuthRelationId(vo.getAuthRelationId());
            //业务类型，1为签章，2为模板，3为文档，4为业务线
            auth.setBusinessType(businessAuthType.getCode());
            //业务类型角色，具体数值参照枚举类
            auth.setBusinessTypeRole(businessTypeRole.getCode());
            //业务关联数据id
            auth.setBusinessRelationId(businessRelationId);
            //系统数据
            if (currentUser == null) {
                auth.setSysDeptId(departId);
            }else{
                auth.setSysDeptId(currentUser.getDepartId());
            }

            auth.setDeleteFlag(false);
            //添加到列表中
            list.add(auth);
        }
        //更新已有的权限
        QueryWrapper<SignBusinessAuth> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignBusinessAuth::getBusinessType,businessAuthType.getCode());
        wrapper.lambda().eq(SignBusinessAuth::getBusinessTypeRole,businessTypeRole.getCode());
        wrapper.lambda().eq(SignBusinessAuth::getBusinessRelationId,businessRelationId);
        SignBusinessAuth update = new SignBusinessAuth();
        update.setDeleteFlag(true);
        businessAuthService.update(update, wrapper);
        //保存新的权限
        businessAuthService.saveBatch(list);
    }

    public BusinessAuthQueryVo build(String businessRelationId,BusinessAuthType businessAuthType,List<Integer> businessTypeRoleList){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();

        BusinessAuthQueryVo businessAuthQueryVo = new BusinessAuthQueryVo();
        businessAuthQueryVo.setTenantUserId(currentUser.getTenantUserId());
        businessAuthQueryVo.setDepartId(currentUser.getDepartId());
        List<SysUserRole> userRoles = userRoleService.queryByUserIdAndTenantId(currentUser.getId(), currentUser.getTenantId());
        if(userRoles != null && userRoles.size() > 0){
            List<String> roleIdList = new ArrayList<>();
            for(SysUserRole role : userRoles){
                roleIdList.add(role.getRoleId());
            }
            businessAuthQueryVo.setRoleIds(roleIdList);
        }
        businessAuthQueryVo.setBusinessType(businessAuthType.getCode());
        if(businessRelationId != null){
            businessAuthQueryVo.setBusinessRelationId(businessRelationId);
        }
        //业务类型角色，1印章管理员,2印章审计者,3印章使用者
        if(businessTypeRoleList != null && businessTypeRoleList.size() > 0){
            businessAuthQueryVo.setBusinessTypeRoleList(businessTypeRoleList);
        }
        return businessAuthQueryVo ;
    }
}