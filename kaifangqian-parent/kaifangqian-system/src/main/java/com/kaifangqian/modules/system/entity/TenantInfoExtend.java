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
package com.kaifangqian.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.kaifangqian.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户扩展信息表
 * </p>
 *
 * @author Administrator
 * @since 2023-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tenant_info_extend")
public class TenantInfoExtend extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("tenant_id")
    private String tenantId;

    /**
     * 租户类型（个人、企业）
     */
    @TableField("tenant_type")
    private Integer tenantType;

    /**
     * 姓名或企业名称
     */
    @TableField("name")
    private String name;

    /**
     * 身份证号码或单位证件号码
     */
    @TableField("organization_no")
    private String organizationNo;

    /**
     * 法定代表人
     */
    @TableField("corporation")
    private String corporation;

    /**
     * 代表人证件号
     */
    @TableField("corporation_no")
    private String corporationNo;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 认证状态：未认证、认证审核中、认证审核失败、认证成功
     */
    @TableField("auth_status")
    private Integer authStatus;

    /**
     * 认证类型：首次认证、变更认证
     */
    @TableField("auth_type")
    private Integer authType;

    /**
     * 组织码
     */
    @TableField("org_code")
    private String orgCode;


    /**
     * 所属账户
     */
    @TableField("sys_account_id")
    private String sysAccountId;

    /**
     * 所属租户
     */
    @TableField("sys_tenant_id")
    private String sysTenantId;

    /**
     * 所属用户
     */
    @TableField("sys_user_id")
    private String sysUserId;

    /**
     * 所属部门
     */
    @TableField("sys_org_code")
    private String sysOrgCode;

    //基础 base  核心 core
    private String sysType;

    /**
     * 云盾实名认证订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 个人认证方式：mobile: 实名手机号三要素认证；bank: 个人银行卡四要素认证；offline: 人工审核认证
     */
    @TableField("person_verify_method")
    private String personVerifyMethod;

    /**
     * 企业认证方式：legalRep：法定代表人本人认证；invite：法定代表人授权认证，代理人邀请法定代表人在线授权认证；letter：上传授权书认证；
     */
    @TableField("company_verify_method")
    private String companyVerifyMethod;

    /**
     * 个人/法人证件类型，1.中国居民身份证2.港澳居民居住证3.台湾居民居住证4.港澳居民来往内地通行证（即：回乡证）5.台湾居民来往大陆通行证（即：台胞证）6.外国人永久居留居住证（即：中国绿卡、永居证）7.国际护照
     */
    @TableField("id_card_type")
    private Integer idCardType;

    /**
     * 租户银行卡号
     */
    @TableField("bank_card")
    private String bankCard;

    /**
     * 认证通过时间
     */
    @TableField("verify_time")
    private Date verifyTime;

    /**
     * 最新认证记录id
     */
    @TableField("auth_id")
    private String authId;

    @TableField("apply_tenant_user")
    private String applyTenantUser;

}
