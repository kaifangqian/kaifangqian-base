package com.kaifangqian.modules.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kaifangqian.common.base.entity.BaseEntity;
import lombok.Data;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("sys_tenant_app_version")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "sys_tenant_app_version对象", description = "租户应用表")
public class SysTenantAppVersion extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 租户ID
     */
    // @ApiModelProperty(value = "租户ID")
    private String tenantId;
    /**
     * 应用id
     */
    // @ApiModelProperty(value = "应用id")
    private String appId;
    /**
     * 应用版本id
     */
    // @ApiModelProperty(value = "应用版本id")
    private String appVersionId;
    /**
     * 状态
     */
    // @ApiModelProperty(value = "状态,1可用 2不可用,3租户停用")
    private Integer status;
    /**
     * 是否全部可用
     */
    // @ApiModelProperty(value = "是否全部可用")
    private Boolean useful;
}
