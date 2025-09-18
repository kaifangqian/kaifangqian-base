package com.kaifangqian.modules.opensign.controller;

import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.service.business.RuBusinessService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: SignConfirmController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignConfirmController
 * @author: FengLai_Gong
 */
@Slf4j
@RestController
@RequestMapping("/sign/confirm")
@ResrunLogModule(name = "业务线-签署意愿校验")
// @Api(tags = "业务线-签署意愿校验")
public class SignConfirmController {




    @Autowired
    private RuBusinessService ruBusinessService ;


    // @ApiOperation("获取系统级别签署意愿校验开关（目前只有人脸校验）")
    @RequestMapping(value = "/type", method = RequestMethod.GET)
    public Result<Boolean> getConfirmType(){


        return Result.OK(ruBusinessService.getSystemConfirmType()) ;
    }



}