package com.kaifangqian.modules.opensign.pdfbox.vo;

/**
 * @Description: FormFieldType
 * @Package: com.kaifangqian.modules.opensign.pdfbox.vo
 * @ClassName: FormFieldType
 * @author: FengLai_Gong
 */
public enum FormFieldType {
    Radio("radio","单选"),

    CheckBox("check-box","复选"),

    LineText("line-text","单行文本"),

    MultilineText("multiline-text","单行文本"),

    Date("date","日期"),

    Number("number","数字"),

    DropdownList("dropdown-list","下拉列表"),

    Image("image","图片"),

    Seal("seal","",true),

    Signature("signature","个人签名",true),

    SignDate("sign-date","签署日期",true),
    ;





    private String type;

    private String name;

    private boolean sign;


    FormFieldType (String type,String name){
        this.type = type;
        this.name = name;
        this.sign = false;
    }
    FormFieldType (String type,String name,boolean isSign){
        this.type = type;
        this.name = name;
        this.sign = isSign;
    }

    public String getType() {
        return type;
    }

    public boolean isSign() {
        return sign;
    }
}