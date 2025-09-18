/**
 * @description 业务线数据清洗
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
package com.kaifangqian.modules.opensign.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.ControlPropertyTypeEnum;
import com.kaifangqian.modules.opensign.enums.ControlPropertyTypePageConfigEnum;
import com.kaifangqian.modules.opensign.enums.SignFileTypeEnum;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.re.SignReDocControlPropertyService;
import com.kaifangqian.modules.opensign.service.re.SignReDocControlService;
import com.kaifangqian.modules.opensign.service.re.SignReDocService;
import com.kaifangqian.modules.opensign.service.re.SignReService;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateService;
// import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: SignDataHandleController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignDataHandleController
 * @author: FengLai_Gong
 * @Date: 2021/11/23
 */
@Slf4j
@RestController
@RequestMapping("/sign/data")
@ResrunLogModule(name = "业务线数据清洗")
// @Api(tags = "业务线数据清洗")
public class SignDataHandleController {

    @Autowired
    private SignTemplateService templateService ;
    @Autowired
    private SignTemplateRecordService templateRecordService ;


    @Autowired
    private SignReService reService ;
    @Autowired
    private SignReDocService reDocService ;
    @Autowired
    private SignReDocControlService reDocControlService ;
    @Autowired
    private SignReDocControlPropertyService reDocControlPropertyService ;

    @Autowired
    private SignRuService ruService ;
    @Autowired
    private SignRuDocService ruDocService ;
    @Autowired
    private SignRuDocOperateService ruDocOperateService ;
    @Autowired
    private SignRuDocControlService ruDocControlService ;
    @Autowired
    private SignRuDocControlPropertyService ruDocControlPropertyService ;

    @Autowired
    private AnnexImageService annexImageService ;


    @Value("${service.token}")
    private String token ;
    @Autowired
    private RedisUtil redisUtil;

    public static final String REDIS_KEY = "kaifangqian_handle_data" ;


    @GetMapping("/handle")
    public Result handleData(String keyWord){
        if(keyWord != null && keyWord.equals(token)){
            //查询key是否存在
            if (redisUtil.hasKey(REDIS_KEY)) {
                return Result.error("请勿重复提交");
            }
            //如果不存在，则设置key，60秒钟失效
            redisUtil.set(REDIS_KEY, "", 300l);
            try {
                System.out.println("清理数据了==================开始了");
                String timeStr = "2024-03-21 00:00:00";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(timeStr);
                //模板
                templateDataHandle(date);
                //业务线配置
                reDataHandle(date);
                //业务线实例
                ruDataHandle(date);
                System.out.println("清理数据了==================结束了");
            }catch (Exception e){
                throw new PaasException("");
            }finally {
                redisUtil.del(REDIS_KEY);
            }

        }
        return Result.OK() ;
    }

    public void reDataHandle(Date beforeDate){



        QueryWrapper<SignRe> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().le(BaseEntity::getCreateTime,beforeDate);
        List<SignRe> reList = reService.list(wrapper);
        if(reList != null && reList.size() > 0){
            for(SignRe re : reList){
                //文档页面更新
                List<SignReDoc> reDocList = reDocService.listByReId(re.getId());
                if(reDocList != null && reDocList.size() > 0){
                    for(SignReDoc reDoc : reDocList){
                        Integer pageCount = 0 ;
                        if(SignFileTypeEnum.TEMPLATE.getCode().equals(reDoc.getDocType())){
                            //模板
                            SignTemplateRecord current = templateRecordService.getCurrent(reDoc.getDocOriginId());
                            if(current != null){
                                pageCount = annexImageService.countByAnnexId(current.getAnnexId());
                            }
                        }else if(SignFileTypeEnum.UPLOAD.getCode().equals(reDoc.getDocType())){
                            //上传
                            pageCount = annexImageService.countByAnnexId(reDoc.getAnnexId());
                        }
                        SignReDoc newReDoc = new SignReDoc();
                        newReDoc.setId(reDoc.getId());
                        newReDoc.setDocPage(pageCount);
                        reDocService.updateById(newReDoc);
                    }
                }
                //文档控件
                List<SignReDocControl> reDocControlList = reDocControlService.listByParam(re.getId());
                if(reDocControlList != null && reDocControlList.size() > 0){
                    List<SignReDocControlProperty> propertyList = new ArrayList<>();
                    for(SignReDocControl reDocControl : reDocControlList){
                        //应用页面
                        SignReDocControlProperty pageConfig = new SignReDocControlProperty();
                        pageConfig.setPropertyType(ControlPropertyTypeEnum.PAGE_CONFIG.getCode());
                        pageConfig.setPropertyValue(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
                        pageConfig.setControlId(reDocControl.getId());
                        pageConfig.setReId(re.getId());
                        propertyList.add(pageConfig);
                        //指定页
                        SignReDocControlProperty custom = new SignReDocControlProperty();
                        custom.setPropertyType(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
                        custom.setPropertyValue((reDocControl.getPage() + 1)+ "");
                        custom.setControlId(reDocControl.getId());
                        custom.setReId(re.getId());
                        propertyList.add(custom);

                        //应用文档
                        SignReDocControlProperty relationDoc = new SignReDocControlProperty();
                        relationDoc.setPropertyType(ControlPropertyTypeEnum.RELATION_DOC.getCode());
                        relationDoc.setPropertyValue(reDocControl.getSignReDocId());
                        relationDoc.setControlId(reDocControl.getId());
                        relationDoc.setReId(re.getId());
                        propertyList.add(relationDoc);
                    }
                    if(propertyList.size() > 0){
                        reDocControlPropertyService.saveBatch(propertyList);
                    }
                }
            }
        }


    }

    public void ruDataHandle(Date beforeDate){
        QueryWrapper<SignRu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().le(BaseEntity::getCreateTime,beforeDate);
        List<SignRu> ruList = ruService.list(wrapper);
        if(ruList != null && ruList.size() > 0){
            for(SignRu ru : ruList){
                //页码
                List<SignRuDoc> docList = ruDocService.listByRuId(ru.getId());
                if(docList != null && docList.size() > 0){
                    for(SignRuDoc ruDoc : docList){
                        SignRuDocOperate current = ruDocOperateService.getCurrentByDocId(ruDoc.getId());
                        if(current != null){
                            Integer pageCount =  annexImageService.countByAnnexId(current.getAnnexId());
                            SignRuDoc newRuDoc = new SignRuDoc();
                            newRuDoc.setId(ruDoc.getId());
                            newRuDoc.setDocPage(pageCount);
                            ruDocService.updateById(newRuDoc);
                        }
                    }
                }
                //控件
                List<SignRuDocControl> docControlList = ruDocControlService.listSignControlList(ru.getId());
                if(docControlList != null && docControlList.size() > 0){
                    List<SignRuDocControlProperty> propertyList = new ArrayList<>();
                    for(SignRuDocControl ruDocControl : docControlList){
                        //应用页面
                        SignRuDocControlProperty pageConfig = new SignRuDocControlProperty();
                        pageConfig.setPropertyType(ControlPropertyTypeEnum.PAGE_CONFIG.getCode());
                        pageConfig.setPropertyValue(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
                        pageConfig.setControlId(ruDocControl.getId());
                        pageConfig.setRuId(ru.getId());
                        propertyList.add(pageConfig);
                        //指定页
                        SignRuDocControlProperty custom = new SignRuDocControlProperty();
                        custom.setPropertyType(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
                        custom.setPropertyValue((ruDocControl.getPage() + 1)+ "");
                        custom.setControlId(ruDocControl.getId());
                        custom.setRuId(ru.getId());
                        propertyList.add(custom);

                        //应用文档
                        SignRuDocControlProperty relationDoc = new SignRuDocControlProperty();
                        relationDoc.setPropertyType(ControlPropertyTypeEnum.RELATION_DOC.getCode());
                        relationDoc.setPropertyValue(ruDocControl.getSignRuDocId());
                        relationDoc.setControlId(ruDocControl.getId());
                        relationDoc.setRuId(ru.getId());
                        propertyList.add(relationDoc);
                    }
                    if(propertyList.size() > 0){
                        ruDocControlPropertyService.saveBatch(propertyList);
                    }
                }
            }
        }
    }

    public void templateDataHandle(Date beforeDate){
        QueryWrapper<SignTemplateRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().le(BaseEntity::getCreateTime,beforeDate);
        List<SignTemplateRecord> list = templateRecordService.list(wrapper);

        if(list != null && list.size() > 0){
            for(SignTemplateRecord record : list){
                if(record.getDocPage() == null || record.getDocPage() == 0){
                    String annexId = record.getAnnexId();
                    Integer imageCount = annexImageService.countByAnnexId(annexId);
                    SignTemplateRecord r = new SignTemplateRecord();
                    r.setId(record.getId());
                    r.setDocPage(imageCount);
                    templateRecordService.updateById(r);
                }

            }
        }

    }

}