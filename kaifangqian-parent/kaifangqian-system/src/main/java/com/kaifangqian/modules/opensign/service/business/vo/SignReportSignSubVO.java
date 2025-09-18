package com.kaifangqian.modules.opensign.service.business.vo;

import lombok.Data;

import java.util.List;

/**
 * @author : zhenghuihan
 * create at:  2024/5/30  16:00
 * @description:
 */
@Data
public class SignReportSignSubVO {
    private Integer fontSize = 11;

    private String title;

    private List<SignReportDocFileVo> subReport;
}