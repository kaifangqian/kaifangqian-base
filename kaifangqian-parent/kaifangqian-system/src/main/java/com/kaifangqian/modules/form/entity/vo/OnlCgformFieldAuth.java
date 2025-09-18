package com.kaifangqian.modules.form.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class OnlCgformFieldAuth {

    private Integer edit;
    private Integer view;

    private String id;
    private String cgformHeadId;
    private String dbFieldName;
    private String dbFieldTxt;
    private String dbFieldNameOld;
    private Integer dbIsKey;
    private Integer dbIsNull;
    private String dbType;
    private Integer dbLength;
    private Integer dbPointLength;
    private String dbDefaultVal;
    private String dictField;
    private String dictTable;
    private String dictText;
    private String fieldShowType;
    private String fieldHref;
    private Integer fieldLength;
    private String fieldValidType;
    private String fieldMustInput;
    private String fieldExtendJson;
    private String fieldDefaultValue;
    private Integer isQuery;
    private Integer isShowForm;
    private Integer isShowList;
    private Integer isReadOnly;
    private String queryMode;
    private String mainTable;
    private String mainField;
    private Integer orderNum;
    private String updateBy;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    private String createBy;
    private String converter;
    private String queryConfigFlag;
    private String queryDefVal;
    private String queryDictText;
    private String queryDictField;
    private String queryDictTable;
    private String queryShowType;
    private String queryValidType;
    private String queryMustInput;
    private String sortFlag;
    private transient String alias;


    public OnlCgformFieldAuth() {
        super();
    }

    public OnlCgformFieldAuth(Integer edit, Integer view) {
        this.edit = edit;
        this.view = view;
    }
}
