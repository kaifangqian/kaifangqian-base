/**
 * @description 统计分析服务接口实现类
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
package com.kaifangqian.modules.opensign.service.statistics.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.service.statistics.SignStatisticsService;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.modules.opensign.dto.SealAuditDto;
import com.kaifangqian.modules.opensign.dto.UseSealDetailDto;
import com.kaifangqian.modules.opensign.mapper.SignStasticsMapper;
import com.kaifangqian.modules.opensign.util.ReportUtils;
import com.kaifangqian.modules.opensign.vo.base.SealAuditVo;
import com.kaifangqian.modules.opensign.vo.base.SealStatisticsVo;
import com.kaifangqian.modules.opensign.vo.base.UseSealDetailVo;
import com.kaifangqian.modules.opensign.vo.base.UseSealStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description: 统计分析服务实现类
 * @Package: com.kaifangqian.modules.opensign.service.doc.impl
 * @ClassName: SignStatisticsServiceImpl
 * @author: Fusion
 * CreateTime:  2023/8/18  10:53
 * @copyright 北京资源律动科技有限公司
 */
@Slf4j
@Service
public class SignStatisticsServiceImpl implements SignStatisticsService {
    @Resource
    private SignStasticsMapper signStasticsMapper;

    @Autowired
    private FileProperties fileProperties;


    /**
     * 印章统计
     */
    public SealStatisticsVo sealStatistics() {
        SealStatisticsVo sealStatisticsVo = new SealStatisticsVo();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        String tenantId = loginUser.getTenantId();
        if (Optional.ofNullable(loginUser).isPresent()) {
            Integer useCount = signStasticsMapper.getUseCount(tenantId);
            sealStatisticsVo.setUseCount(useCount);
            Integer stopCount = signStasticsMapper.getStopCount(tenantId);
            sealStatisticsVo.setStopCount(stopCount);
            Integer collectCount = signStasticsMapper.getCollectCount(tenantId);
            sealStatisticsVo.setCollectCount(collectCount);
            Integer destructionCount = signStasticsMapper.getDestructionCount(tenantId);
            sealStatisticsVo.setDestructionCount(destructionCount);
            sealStatisticsVo.setAllSealsCount(useCount+stopCount+collectCount+destructionCount);
        }

        return sealStatisticsVo;
    }


    /**
     * 用印统计
     */
    public UseSealStatisticsVo useSealStatistics() {
        UseSealStatisticsVo useSealStatisticsVo = new UseSealStatisticsVo();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        String tenantId = loginUser.getTenantId();
        if (Optional.ofNullable(loginUser).isPresent()) {
            Integer electronicUseSealCount = signStasticsMapper.getElectronicUseSealCount(tenantId);
            useSealStatisticsVo.setElectronicUseSealCount(electronicUseSealCount);
            Integer physicsUseSealCount = signStasticsMapper.getPhysicsUseSealCount(tenantId);
            useSealStatisticsVo.setPhysicsUseSealCount(physicsUseSealCount);
            Integer interfaceUseSealCount = signStasticsMapper.getInterfaceUseSealCount(tenantId);
            useSealStatisticsVo.setInterfaceUseSealCount(interfaceUseSealCount);
            useSealStatisticsVo.setTotalUseSealsCount(electronicUseSealCount+physicsUseSealCount+interfaceUseSealCount);
        }
        return useSealStatisticsVo;
    }

    /**
     * 用印明细-分页
     */
    @Override
    public IPage<UseSealDetailVo> useSealDetailList(Page<UseSealDetailVo> page, UseSealDetailDto useSealDetailDto) {
//        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        return signStasticsMapper.getUseSealDetailList(page, useSealDetailDto);
    }

    /**
     * 印章审计-分页
     */
    @Override
    public IPage<SealAuditVo> sealAuditList(Page<SealAuditVo> page, SealAuditDto sealAuditDto) {
//        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        return signStasticsMapper.getSealAuditList(page, sealAuditDto);
    }

    /**
     * 用印明细-列表
     */
    @Override
    public List<UseSealDetailVo> useSealDetailList(UseSealDetailDto useSealDetailDto) {
//        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        return signStasticsMapper.getUseSealDetailList(useSealDetailDto);
    }

    /**
     * 导出用印统计报表
     */
    @Override
    public void exportUseSealStatistic(HttpServletResponse response,UseSealDetailDto useSealDetailDto) {
        String mainjasperPath = fileProperties.getPath().getPath() + "useSignReport.jasper";

        Map<String, Object> parameters = new HashMap<String, Object>(16);
        String SUBREPORT_DIR = fileProperties.getPath().getPath();
        parameters.put("SUBREPORT_DIR", SUBREPORT_DIR);
        parameters.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        parameters.put("rangeTime", StringUtils.isEmpty(useSealDetailDto.getStartTime())&&StringUtils.isEmpty(useSealDetailDto.getEndTime()) ? "":useSealDetailDto.getStartTime()+"至"+useSealDetailDto.getEndTime());


        List<UseSealDetailVo> list = signStasticsMapper.getUseSealDetailList(useSealDetailDto);

        ReportUtils.setResponse(response, UUID.randomUUID().toString() + ".xlsx");
        OutputStream outputStream = null;
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainjasperPath, parameters, new JRBeanCollectionDataSource(list));
            JRXlsxExporter exporter = new JRXlsxExporter();
            List<JasperPrint> listJasperPrint = new ArrayList<>();
            listJasperPrint.add(jasperPrint);

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, listJasperPrint);
            //设置为true，即可在一个excel中，每个单独的jasper对象放入到一个sheet页中
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
            outputStream = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
            String[] sheetNames = {"用印统计报表"};
            exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNames);
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导出用印审计报表
     */
    @Override
    public void exportSealAuditStatistic(HttpServletResponse response,SealAuditDto sealAuditDto) {
        String mainjasperPath = fileProperties.getPath().getPath() + "signAuditReport.jasper";

        Map<String, Object> parameters = new HashMap<String, Object>(16);
        String SUBREPORT_DIR = fileProperties.getPath().getPath();
        parameters.put("SUBREPORT_DIR", SUBREPORT_DIR);
        parameters.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        List<SealAuditVo> list = signStasticsMapper.getSealAuditList(sealAuditDto);

        ReportUtils.setResponse(response, UUID.randomUUID().toString() + ".xlsx");
        OutputStream outputStream = null;
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainjasperPath, parameters, new JRBeanCollectionDataSource(list));
            JRXlsxExporter exporter = new JRXlsxExporter();
            List<JasperPrint> listJasperPrint = new ArrayList<>();
            listJasperPrint.add(jasperPrint);

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, listJasperPrint);
            //设置为true，即可在一个excel中，每个单独的jasper对象放入到一个sheet页中
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
            outputStream = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
            String[] sheetNames = {"印章备案报表"};
            exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNames);
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
