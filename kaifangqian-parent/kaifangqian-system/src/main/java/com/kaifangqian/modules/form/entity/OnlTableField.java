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
 * @description 表字段
 * @createTime 2022/9/2 18:04
 */
@Data
@TableName("onl_table_field")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "onl_table_field对象", description = "表字段")
public class OnlTableField implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 父id
     */
    // @ApiModelProperty(value = "父id")
    private java.lang.String formTableId;
    /**
     * 字段名
     */
    // @ApiModelProperty(value = "字段名")
    private java.lang.String dbFieldName;
    /**
     * 字段描述
     */
    // @ApiModelProperty(value = "字段描述")
    private java.lang.String dbFieldTxt;
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

    private Boolean authVisible;
}
