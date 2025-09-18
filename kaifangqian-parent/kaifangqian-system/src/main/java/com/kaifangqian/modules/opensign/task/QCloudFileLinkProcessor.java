package com.kaifangqian.modules.opensign.task;

import com.kaifangqian.modules.opensign.service.confirm.IUserConfirmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.log.OmsLogger;


/**
 * @author : zhenghuihan
 * create at:  2022/8/24  17:02
 * @description: 刷脸数据记录关联
 */
@Component
@Slf4j
public class QCloudFileLinkProcessor implements tech.powerjob.worker.core.processor.sdk.BasicProcessor {

    @Autowired
    private IUserConfirmService userConfirmService;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        OmsLogger omsLogger = context.getOmsLogger();
        omsLogger.info("刷脸数据记录关联定时任务开始");
        userConfirmService.bindFaceFile();
        return new ProcessResult(true, "刷脸数据记录关联定时任务结束");
    }
}