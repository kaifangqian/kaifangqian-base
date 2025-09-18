package com.kaifangqian.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kaifangqian.modules.system.entity.TenantAuthLog;
import com.kaifangqian.modules.system.entity.TenantInfoExtend;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.modules.system.vo.request.TenantAuthAuditRequest;
import com.kaifangqian.modules.system.vo.request.TenantAuthRequest;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.entity.SysConfig;
import com.kaifangqian.modules.system.service.ITenantAuthLogService;
import com.kaifangqian.modules.system.service.ITenantInfoExtendService;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.pagination.PagePaas;
import com.kaifangqian.service.ISysConfigService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


// @Api(tags = "账号管理")
@RestController
@RequestMapping("/system/tenantInfoExtend")
@Slf4j
public class TenantInfoExtendController {
    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;

    @Autowired
    private ITenantAuthLogService tenantAuthLogService;
    @Autowired
    private ISysConfigService iSysConfigService;


    /**
     * 分页列表查询
     *
     * @param tenantQueryDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    // @ApiOperation(value = "租户信息扩展表-分页列表查询", notes = "租户信息扩展表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TenantQueryDTO tenantQueryDTO,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TenantInfoDTO> page = new Page<TenantInfoDTO>(pageNo, pageSize);
        IPage<TenantInfoDTO> pageList = tenantInfoExtendService.pageExt(page, tenantQueryDTO);
        return Result.OK(pageList);
    }

    /**
     * 工作站列表全部数据查询
     */
    // @ApiOperation(value = "工作站列表全部数据查询", notes = "工作站列表全部数据查询")
    @GetMapping(value = "/listAll")
    public Result<?> listAll(TenantQueryDTO tenantQueryDTO) {
        TenantInfoExtend query = new TenantInfoExtend();
        query.setTenantType(tenantQueryDTO.getTenantType());
        query.setName(tenantQueryDTO.getTenantName());
        List<TenantInfoExtend> list = tenantInfoExtendService.getByEntity(query);
        return Result.OK(list);
    }


    /**
     * 通过id查询详情
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "租户信息扩展表-通过扩展id查询", notes = "租户信息扩展表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(tenantInfoExtendService.getByIdExt(id));
    }

    /**
     * 编辑
     *
     * @param tenantInfoDTO
     * @return
     */
    // @ApiOperation(value = "租户信息扩展表-编辑", notes = "租户信息扩展表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TenantInfoDTO tenantInfoDTO) {
        tenantInfoExtendService.editExt(tenantInfoDTO);
        return Result.OK("编辑成功!");
    }

    /**
     * 注册--维护扩展表
     *
     * @param userJionSystemVO
     * @return
     */
    // @ApiOperation(value = "注册", notes = "注册")
    @PutMapping(value = "/tenantRegister")
    public Result<?> tenantRegister(@RequestBody UserJionSystemVO userJionSystemVO) {
        tenantInfoExtendService.tenantRegister(userJionSystemVO);
        return Result.OK("注册成功！");
    }

    /**
     * 获取租户信息
     *
     * @return
     */
    // @ApiOperation(value = "获取租户扩展信息", notes = "获取租户信息")
    @GetMapping(value = "/getTenantInfoExt")
    public Result<TenantInfoDTO> getTenantInfoExt() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        return Result.OK(tenantInfoExtendService.getTenantInfoExt(loginUser.getTenantId()));
    }

    /**
     * 已加入的企业 列表
     *
     * @return
     */
    // @ApiOperation(value = "已加入的企业)", notes = "已加入的企业")
    @GetMapping(value = "/joined/enterprise")
    public Result<?> joinedEnterprise() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        return Result.OK(tenantInfoExtendService.getJoinedEnterprise(loginUser.getTenantUserId()));
    }

    /**
     * 企业认证记录 列表
     *
     * @return
     */
    // @ApiOperation(value = "企业认证记录)", notes = "企业认证记录")
    @GetMapping(value = "/enterprise/auth/log")
    public Result<Page<TenantAuthLogVO>> enterpriseAuthLog(PagePaas pagePaas, String tenantId) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        Page<TenantAuthLog> page = new Page(pagePaas.getPageNo(), pagePaas.getPageSize());
        TenantAuthRequest authRequest = new TenantAuthRequest();
        authRequest.setTenantId(tenantId);
        return Result.OK(tenantAuthLogService.getTenantAuthLog(page,authRequest));
    }

    // @ApiOperation(value = "查询认证记录（根据id）")
    @GetMapping(value = "/enterprise/auth/log/{id}")
    public Result<?> enterpriseAuthLog(@PathVariable("id") String logId) {
        return Result.OK(tenantAuthLogService.getAuthLogById(logId));
    }

    /**
     * 运营端 企业认证记录审核 列表
     *
     * @return
     */
    // @ApiOperation(value = "运营端 企业认证记录审核列表)")
    @GetMapping(value = "/enterprise/auth/audit/list")
    @RequiresPermissions("system:manage")
    public Result<Page<TenantAuthLogVO>> enterpriseAuthLogList(PagePaas pagePaas, TenantAuthRequest authRequest) {
        authRequest.setTenantId(null);
        authRequest.setTenantType(TenantType.GROUP.getType());
        Page<TenantAuthLog> page = new Page(pagePaas.getPageNo(), pagePaas.getPageSize());
        return Result.OK(tenantAuthLogService.getTenantAuthLog(page,authRequest));
    }

    /**
     * 运营端 企业认证记录审核 列表
     *
     * @return
     */
    // @ApiOperation(value = "运营端 企业认证审核)")
    @PostMapping(value = "/enterprise/auth/audit")
    @RequiresPermissions("system:manage")
    public Result<?> enterpriseAuthLog(@RequestBody TenantAuthAuditRequest authRequest) throws Exception {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        return Result.OK(tenantAuthLogService.authAudit(authRequest,loginUser.getTenantUserId()));
    }

    // @ApiOperation(value = "个人实名认证")
    @GetMapping("/person/auth/type")
    public Result<?> personAuthType() throws Exception {
        SysConfig config = iSysConfigService.getByType("person_face");
        if(config == null || config.getValue().equals("false")){
            return Result.OK(0);
        }else{
            return Result.OK(1);
        }
    }

    // @ApiOperation(value = "通过企业名称查询企业")
    @GetMapping("/enterprise/query")
    public Result<?> queryEnterpriseInfo(String companyName) throws Exception {
        return Result.OK(tenantInfoExtendService.getTenantInfoCompanyInfo(companyName));
    }

    // @ApiOperation(value = "创建企业租户")
    @PostMapping("/enterprise/add")
    public Result<?> addEnterpriseInfo(@RequestBody TenantCompanyInfoAddVO companyName) throws Exception {
        return tenantInfoExtendService.createCompanyTenant(companyName);
    }

}
