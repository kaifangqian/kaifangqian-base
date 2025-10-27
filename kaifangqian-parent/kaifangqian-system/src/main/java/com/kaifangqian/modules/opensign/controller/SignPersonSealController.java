/**
 * @description 电子印章-个人签名管理
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.vo.request.*;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.enums.TenantStatus;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.entity.SignPersonSeal;
import com.kaifangqian.modules.opensign.enums.SealAddContextEnum;
import com.kaifangqian.modules.opensign.enums.SealShapeStyleEnum;
import com.kaifangqian.modules.opensign.enums.SignFileEnum;
import com.kaifangqian.modules.opensign.service.seal.SignPersonSealService;
import com.kaifangqian.modules.opensign.service.tool.PsersonSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.modules.opensign.vo.request.*;
import com.kaifangqian.modules.opensign.vo.response.PersonSealListResponse;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.system.model.SysUserSearchModel;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: SignPrivateSealController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignPrivateSealController
 * @author: FengLai_Gong
 * @Date: 2023/12/28 10:09
 */
@Slf4j
@RestController
@RequestMapping("/sign/person/seal")
@ResrunLogModule(name = "个人签名")
// @Api(tags = "电子印章-个人签名")
public class SignPersonSealController {


    @Autowired
    private PsersonSealGenerateService sealGenerateService ;
    @Autowired
    private ISysTenantInfoService sysTenantInfoService;
    @Autowired
    private ISysTenantUserService tenantUserService ;
    @Autowired
    private SignFileService signFileService ;
    @Autowired
    private SignPersonSealService personSealService ;


    // @ApiOperation("签名列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result<IPage<PersonSealListResponse>> list(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        List<PersonSealListResponse> responseList = new ArrayList<>();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();

        IPage<PersonSealListResponse> result = new Page<>(pageNo, pageSize);

        String tenantId = null ;
        //找到该租户的个人租户
        List<SysTenantInfo> tenantInfoList = tenantUserService.getTenantsByUserId(currentUser.getId());
        if(tenantInfoList == null || tenantInfoList.size() == 0){
            return Result.error("租户不存在",null) ;
        }
        for(SysTenantInfo tenant : tenantInfoList){
            if(tenant.getTenantType().equals(TenantType.PERSONAL.getType()) && tenant.getTenantStatus().equals(TenantStatus.ENABLE.getType())){
                tenantId = tenant.getId();
            }
        }
        if(tenantId == null || tenantId.length() == 0){
//            return Result.error("个人租户不存在",null) ;
            return Result.OK(result) ;
        }
        //查询条件
        QueryWrapper<SignPersonSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignPersonSeal::getSysTenantId,tenantId);
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);

        Page<SignPersonSeal> page = personSealService.page(new Page<>(pageNo, pageSize), wrapper);

        //List<SignPersonSeal> list = personSealService.list(page,wrapper);
        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){
            for(SignPersonSeal seal : page.getRecords()){
                PersonSealListResponse response = new PersonSealListResponse();
                response.setSealId(seal.getId());
                response.setSealName(seal.getSealName());
                response.setCreateTime(seal.getCreateTime());
                response.setIsDefault(seal.getIsDefault());
                AnnexStorage annex = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_PERSON, seal.getId());
                if(annex != null){
                    response.setAnnexId(annex.getId());
                }
                responseList.add(response);
            }
        }
        result.setSize(page.getSize());
        result.setPages(page.getPages());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setRecords(responseList);

        return Result.OK(result) ;
    }

    // @ApiOperation("签名新增")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result<?> save(@RequestBody PersonSealSaveRequest request){

        if(request == null || request.getAnnexId() == null || request.getSealName() == null){
            return Result.error("参数缺失");
        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignPersonSeal personSeal = new SignPersonSeal();
        personSeal.setSealName(request.getSealName());
        personSeal.setSysDeptId(currentUser.getDepartId());
        personSeal.setDeleteFlag(false);

        boolean save = personSealService.save(personSeal);
        if(!save){
            return Result.error("操作失败");
        }
        String annexId = signFileService.updateAnnexStorage(SignFileEnum.SEAL_FILE_PERSON, personSeal.getId(), request.getAnnexId());
        if(annexId == null){
            return Result.error("操作失败");
        }

        return Result.OK("",personSeal.getId()) ;
    }

    // @ApiOperation("设置默认签名")
    @RequestMapping(value = "/isDefault",method = RequestMethod.PUT)
    public Result<?> isDefault(@RequestBody PersonSealIsDefaultRequest request){

        if(request == null || request.getSealId() == null){
            return Result.error("参数缺失");
        }
        //取消掉之前的默认签章
        personSealService.cancelDefault();
        //设置现在的签章为默认签章
        personSealService.setDefault(request.getSealId());

        return Result.OK() ;
    }

    // @ApiOperation("签名删除")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public Result<?> delete(PersonSealDeleteRequest request){
        if(request == null || request.getSealId() == null){
            return Result.error("参数缺失");
        }

        personSealService.delete(request.getSealId());

        return Result.OK() ;
    }


    // @ApiOperation("签名生成-上传生成")
    @RequestMapping(value = "/generate/upload",method = RequestMethod.POST)
    public Result<?> generateUpload(@RequestBody PersonSealGenerateUploadRequest request){

        if(request.getImage() == null || request.getImage().length() == 0){
            return Result.error("参数缺失");
        }
        byte[] bytes = Base64.decode(request.getImage());
        if(bytes == null || bytes.length == 0){
            return Result.error("参数缺失");
        }

        String annexId = signFileService.saveAnnexStorage(bytes, SignFileEnum.SEAL_FILE_PERSON, null);
        if(annexId == null){
            return Result.error("操作失败");
        }
        return Result.OK(null,annexId) ;
    }

    // @ApiOperation("签名生成-参数生成")
    @RequestMapping(value = "/generate/param",method = RequestMethod.POST)
    public Result<?> generate(@RequestBody PersonSealGenerateParamRequest request){
        byte[] sealByte = null ;
        LoginUser sysUser = MySecurityUtils.getCurrentUser();
        SysTenantUser tenantUser = tenantUserService.getById(sysUser.getTenantUserId());


        if(tenantUser == null){
            return Result.error("用户不存在");
        }
        if(tenantUser.getNickName() == null){
            return Result.error("用户名称未设置");
        }
        if(tenantUser.getNickName().length() < 2){
            return Result.error("用户名称至少2个字");
        }
        String nickName = sysUser.getRealname();


        String name = nickName;
        Integer addContext = request.getAddContext();
        if(addContext == null){
            addContext = SealAddContextEnum.NOTHING.getCode();
        }
        Integer shapeStyle = request.getShapeStyle() ;
        if(shapeStyle == null){
            //没参数
            shapeStyle = SealShapeStyleEnum.SQUARE_FRAME.getCode();
        }
        if(shapeStyle.equals(SealShapeStyleEnum.RECTANGLE_NO_FRAME.getCode()) || shapeStyle.equals(SealShapeStyleEnum.RECTANGLE_FRAME.getCode())){
            if(SealAddContextEnum.YIN.getCode().equals(addContext)){
                name = name + SealAddContextEnum.YIN.getName();
            }else if(SealAddContextEnum.ZHI_YIN.getCode().equals(addContext)){
                name = name + SealAddContextEnum.ZHI_YIN.getName();
            }
        }else {
            if(SealAddContextEnum.NOTHING.getCode().equals(request.getAddContext())){
                if(name.length() == 2){
                    name = name + SealAddContextEnum.ZHI_YIN.getName();
                }else if(name.length() > 4){
                    name = name.substring(0,4);
                }
            }else if(SealAddContextEnum.YIN.getCode().equals(request.getAddContext())){
                if(name.length() > 3){
                    name = name.substring(0,3) + SealAddContextEnum.YIN.getName() ;
                }else {
                    name = name + SealAddContextEnum.YIN.getName() ;
                }
            }else if(SealAddContextEnum.ZHI_YIN.getCode().equals(request.getAddContext())){
                if(name.length() > 2){
                    name = name.substring(0,2) + SealAddContextEnum.ZHI_YIN.getName() ;
                }else {
                    name = name + SealAddContextEnum.ZHI_YIN.getName() ;
                }
            }
        }

        BufferedImage bufferedImage = null ;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            if(SealShapeStyleEnum.RECTANGLE_NO_FRAME.getCode().equals(shapeStyle)){
                bufferedImage = sealGenerateService.drawRectangleNoFrame(name, request.getColor());
            }else if(SealShapeStyleEnum.RECTANGLE_FRAME.getCode().equals(shapeStyle)){

                bufferedImage = sealGenerateService.drawRectangleFrame(name, request.getColor());
            }else if(SealShapeStyleEnum.SQUARE_NO_FRAME.getCode().equals(shapeStyle)){

                bufferedImage = sealGenerateService.drawSquareNoFrame(name, request.getColor());
            }else if(SealShapeStyleEnum.SQUARE_FRAME.getCode().equals(shapeStyle)){
                bufferedImage = sealGenerateService.drawSquareFrame(name, request.getColor());
            }
            ImageIO.write(bufferedImage, "png", output);
            sealByte = output.toByteArray();
            output.close();
        } catch (Exception e) {
            return Result.error("操作失败");
        }

        if(sealByte == null){
            return Result.error("操作失败");
        }
        String annexId = signFileService.saveAnnexStorage(sealByte, SignFileEnum.SEAL_FILE_PERSON, null);
        if(annexId == null){
            return Result.error("操作失败");
        }

        return Result.OK("",annexId) ;
    }



}