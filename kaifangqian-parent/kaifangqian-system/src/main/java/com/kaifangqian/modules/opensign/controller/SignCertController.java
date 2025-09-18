//package com.kaifangqian.modules.opensign.controller;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.kaifangqian.annotation.ResrunLogModule;
//import com.kaifangqian.common.base.entity.BaseEntity;
//import com.kaifangqian.common.vo.Result;
//import com.kaifangqian.modules.opensign.entity.SignCertInfo;
//import com.kaifangqian.modules.opensign.service.cert.SignCertInfoService;
//// import io.swagger.annotations.Api;
//// import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * @Description: SignCertController
// * @Package: com.kaifangqian.modules.opensign.controller
// * @ClassName: SignCertController
// * @author: FengLai_Gong
// */
//@Slf4j
//@RestController
//@RequestMapping("/sign/cert")
//@ResrunLogModule(name = "证书管理")
//// @Api(tags = "电子印章-证书管理")
//public class SignCertController {
//
//
//    @Autowired
//    private SignCertInfoService certInfoService ;
//
//
//    // @ApiOperation("证书列表")
//    @RequestMapping(value = "/list",method = RequestMethod.GET)
//    public Result<List<SignCertInfo>> list(){
//
//        QueryWrapper<SignCertInfo> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
//        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
//
//        List<SignCertInfo> list = certInfoService.list(wrapper);
//
//        return Result.OK(list) ;
//    }
//
//
//    // @ApiOperation("证书详情")
//    @RequestMapping(value = "/info",method = RequestMethod.GET)
//    public Result<SignCertInfo> info(@RequestParam(value = "id") String id){
//
//        SignCertInfo certInfo = certInfoService.getById(id);
//
//        return Result.OK(certInfo) ;
//    }
//
//
//
//}