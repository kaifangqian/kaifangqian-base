package com.kaifangqian.modules.form.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zhenghuihan
 * @description 表
 * @createTime 2022/9/2 18:04
 */
@Data
@TableName("onl_table_form")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "onl_table_form", description = "表")
public class OnlTableForm implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 表名
     */
    // @ApiModelProperty(value = "表ID")
    private String tableId;
    /**
     * 版本
     */
    // @ApiModelProperty(value = "菜单ID")
    private String sysPermissionId;
}
