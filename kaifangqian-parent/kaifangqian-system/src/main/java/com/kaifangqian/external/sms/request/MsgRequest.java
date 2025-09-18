package com.kaifangqian.external.sms.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
@Data
public class MsgRequest implements Serializable {
    private static final long serialVersionUID = 3L;

    private String phoneNumbers;

    private String templateName;

    private Map<String, String> params;

    private String msgType;

}
