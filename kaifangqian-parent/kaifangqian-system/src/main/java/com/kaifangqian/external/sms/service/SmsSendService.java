package com.kaifangqian.external.sms.service;

import com.kaifangqian.external.base.CommonResult;
import com.kaifangqian.external.sms.request.MsgRequest;

public interface SmsSendService {
    CommonResult<?> sendMsg(MsgRequest msgRequest);
}
