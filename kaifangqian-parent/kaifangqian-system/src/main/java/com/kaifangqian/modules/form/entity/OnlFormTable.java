package com.kaifangqian.modules.form.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhenghuihan
 * @description 表
 * @createTime 2022/9/2 18:04
 */
@Data
@TableName("onl_form_table")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "onl_form_table对象", description = "表")
public class OnlFormTable implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 表名
     */
    // @ApiModelProperty(value = "表名")
    private java.lang.String tableName;
    /**
     * 版本
     */
    // @ApiModelProperty(value = "版本")
    private java.lang.Integer tableVersion;
    /**
     * 表描述
     */
    // @ApiModelProperty(value = "表描述")
    private java.lang.String tableTxt;
    /**
     * 创建人
     */
    // @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    // @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;

    private String appInfoId;

    private transient String component;
}
