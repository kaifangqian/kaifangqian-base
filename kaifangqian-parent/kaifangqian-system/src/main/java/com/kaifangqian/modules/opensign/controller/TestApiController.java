package com.kaifangqian.modules.opensign.controller;

import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportDocFileVo;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignNodeVo;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignSubVO;
import com.kaifangqian.modules.opensign.service.confirm.IUserConfirmService;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.pdfbox.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;

@Controller
@RequestMapping("/testApi")
public class TestApiController {

    @Autowired
    private IFlowService flowService;
    @Autowired
    private IUserConfirmService userConfirmService;

    @GetMapping()
    @ResponseBody
    public String testApi() {
        System.out.println("testApi");

        return "api,110";
    }

    @GetMapping("/client")
    @ResponseBody
    public String apiClient() {

        System.out.println("/testApi/client");

        return "apiClient,100";
    }

    @GetMapping("/testComplete")
    @ResponseBody
    public Result testComplete(String instanceId, String operate) {
        flowService.complete(instanceId, operate);
        return Result.OK();
    }


    @GetMapping("/testReport")
    @ResponseBody
    public Result testReport(String ruId) {
        flowService.signReportAndSave(ruId);
       // SignReportSignSubVO signSubVO = userConfirmService.getConfirmInfoByTaskId("b391f08e-cbf2-4042-a84d-03c8b10d1b1e");
        return Result.OK();
    }


    public static void main1(String[] args) {
        String mainjasperPath = "/Users/zhenghuihan/JaspersoftWorkspace/MyReports/opensign/sign_2.jasper";
        Map<String, Object> parameters = new HashMap<String, Object>(2);
        String SUBREPORT_DIR = "/Users/zhenghuihan/JaspersoftWorkspace/MyReports/opensign/";
        parameters.put("SUBREPORT_DIR", SUBREPORT_DIR);
        parameters.put("operatorTenantName", "测试租户名称");

        List<SignReportSignNodeVo> nodeVoList = new ArrayList<>();
        addData(nodeVoList, 1);
        addData(nodeVoList, 2);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(nodeVoList);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainjasperPath, parameters, dataSource);


            // 4. 导出报表为 PDF 文件
            String pdfFile = "/Users/zhenghuihan/Desktop/report1.pdf";
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));

            exporter.exportReport();

            System.out.println("PDF 文件已导出到: " + pdfFile);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String mainjasperPath = "/Users/zhenghuihan/JaspersoftWorkspace/MyReports/opensign/sign_3.jasper";

        String backgroundImagePath = "/Users/zhenghuihan/Desktop/001.png";

        Map<String, Object> parameters = new HashMap<String, Object>(2);
        String SUBREPORT_DIR = "/Users/zhenghuihan/JaspersoftWorkspace/MyReports/opensign/";
        parameters.put("SUBREPORT_DIR", SUBREPORT_DIR);


        List<SignReportSignSubVO> docFileVoList = new ArrayList<>();

        SignReportSignSubVO docFileVo1 = new SignReportSignSubVO();
        //docFileVo1.setTitle("发起");
        docFileVo1.setFontSize(14);


        List<SignReportDocFileVo> subReport = new ArrayList<>();

        SignReportSignSubVO docFileVo2 = new SignReportSignSubVO();
        docFileVo2.setTitle("签署1");
        docFileVo2.setFontSize(14);
        SignReportSignSubVO docFileVo3 = new SignReportSignSubVO();
        docFileVo3.setTitle("数字证书信息");
        docFileVo3.setFontSize(11);

        SignReportDocFileVo vo1 = new SignReportDocFileVo();
        vo1.setValueFirst("标题18");
        vo1.setValueSecond("标题1111打好卡了发哈标题1111打好卡了发哈标题1111打好卡了发哈标题1111打好卡了发哈标题1111打好卡了发哈标题1111打好卡了发哈标题1111打好卡了发哈标题1111打好卡了发哈");

        SignReportDocFileVo vo2 = new SignReportDocFileVo();
        vo2.setValueFirst("签章图片：");
        vo2.setValueSecond("iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAOKklEQVR42u3dBYwk1RYG4MFdgrsFtwAhWLCggaAJFpzgJJDg7u7+cHd3dxZ398XdXRY5b7539+7U9nT3tE7P7uMklVlmqqtv/XXkP3KLrvhXmpKujn77kCER330Xcf/9EcceG7HllhErrxyx4IIRs84aMd106Zh55oj5549YZpmIjTaK2HffiKuvjhg8OOK33yL++ef/CMBPPok477yIzTePmG++iHHG6V5FV2PHKKNETDNNAv2ggyKeeCLizz9HQgA//zzisssill8+YrTRegCYYIKIOeaIWGONiF12iTj77IhBgyLeey/i5597Pv/77xFffRXx4osRt90WcfTRSVsXWSRi6qmHv+YMM0QccEA6tx/AbC+Ab72VNG2KKXpucKKJIrbbLuLmmxNQwGlUmO7XX0c880zECSck08/fM9ZYEUsuGXHllRF//z2CAfjGGxFbbx0x3njpZiaeOGKTTSJuvDHil1/qv96339YOKM1jzvPOm0zc9y+6aMS117YFyNYC+P33EfvvHzHuuGnhk08esfvuEV980dvR//FH5evQqief7Pnvo46KOPfc+tbi+tdcE7HQQsnERx01YumlE8AtDDqtA/DWW5M/A9z440ccfHDEZ5+VvzGacNNNEX/9Vfl6J50Ucc89KdpyA0ceGfHgg73PK/q5Sy/trWVcBM0XsKyNVXiozbiOlgL4448Re+4ZMcYYEaOPngICEy41rXfeibjllkRZrrgi4vbbI55+uro5ZnBOPLHyefzo44+nAFTtPHTnmGMiJpkkAbnwwkkbOwrg228n/8LXTDll8jOVIt+jj0bce2/EddelAILK0Ixy8sgjievRwNNPTxG3mv+yjksuifjgg4jHHkucspKZ4o4rrJDWPOGEaR1N+MbGAQQGCuFp4mE0rJq8+Wa6wXPOSTd4xx0Rxx9f2YzPOiv9pK133hnx4Yflz3vhhYjddkvazG++9loi6NX8HG084ojkq1nNzjs3bNKNAXj99RFjjpmcM0pSLSAUAbz88oj//CdxwvPPT5zOtSoB+M03CUDZCq3lLory0kvpQfCXAEZn/K5W4YcFOkqAJfz6az8A6MY5Yj7vtNOqB4Jy/kpEZeoXXBBx0UXp3+XkkEOSidOOjz9OIOF05YIXYAUsfrDa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");


        SignReportDocFileVo vo3 = new SignReportDocFileVo();
        vo3.setValueFirst("标题20");
        vo3.setValueSecond("标题3333");


        SignReportDocFileVo vo31 = new SignReportDocFileVo();
        vo31.setValueFirst("签章图片：");
        vo31.setValueSecond("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");

        SignReportDocFileVo vo4 = new SignReportDocFileVo();
        vo4.setValueFirst("标题4");
        vo4.setValueSecond("标题44444");

        SignReportDocFileVo vo5 = new SignReportDocFileVo();
        vo5.setValueFirst("标题5");
        vo5.setValueSecond("标题555555");

        subReport.add(vo1);
        //subReport.add(vo2);
        subReport.add(vo3);
        subReport.add(vo4);
        //subReport.add(vo31);
        subReport.add(vo5);
        subReport.add(vo1);
        //subReport.add(vo2);
        subReport.add(vo3);
        subReport.add(vo5);
        subReport.add(vo1);
        subReport.add(vo5);
        subReport.add(vo1);
        subReport.add(vo5);
        subReport.add(vo1);

        docFileVo1.setSubReport(subReport);
        docFileVo2.setSubReport(subReport);
        docFileVo3.setSubReport(subReport);

        docFileVoList.add(docFileVo1);
        docFileVoList.add(docFileVo2);
//        docFileVoList.add(docFileVo3);
//        docFileVoList.add(docFileVo3);


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(docFileVoList);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainjasperPath, parameters, dataSource);


            // 4. 导出报表为 PDF 文件
            String pdfFile = "/Users/zhenghuihan/Desktop/report001.pdf";
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));

            exporter.exportReport();

            System.out.println("PDF 文件已导出到: " + pdfFile);

            // Step 2: 使用 PDFBox 添加背景图片
            try (PDDocument document = Loader.loadPDF(new File(pdfFile))) {
                PDImageXObject backgroundImage = PDImageXObject.createFromFile(backgroundImagePath, document);

                for (PDPage page : document.getPages()) {
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {
                        contentStream.drawImage(backgroundImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                    }
                }


                String pdfFile2 = "/Users/zhenghuihan/Desktop/report002.pdf";

                document.save(pdfFile2);
                System.out.println("PDF 文件已成功保存到: " + pdfFile2);

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (JRException e) {
            e.printStackTrace();
        }
    }


    public static void main22(String[] args) {
        String mainjasperPath = "/Users/zhenghuihan/JaspersoftWorkspace/MyReports/opensign/test001.jasper";

        String backgroundImagePath = "/Users/zhenghuihan/Desktop/001.png";

        Map<String, Object> parameters = new HashMap<String, Object>(2);
        List<SignReportDocFileVo> docFileVoList = new ArrayList<>();
        SignReportDocFileVo vo1 = new SignReportDocFileVo();
        vo1.setValueFirst("标题18");
        vo1.setValueSecond("标题1111打好卡了发哈");

        SignReportDocFileVo vo2 = new SignReportDocFileVo();
        vo2.setValueFirst("图片");
        vo2.setValueSecond("iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAOKklEQVR42u3dBYwk1RYG4MFdgrsFtwAhWLCggaAJFpzgJJDg7u7+cHd3dxZ398XdXRY5b7539+7U9nT3tE7P7uMklVlmqqtv/XXkP3KLrvhXmpKujn77kCER330Xcf/9EcceG7HllhErrxyx4IIRs84aMd106Zh55oj5549YZpmIjTaK2HffiKuvjhg8OOK33yL++ef/CMBPPok477yIzTePmG++iHHG6V5FV2PHKKNETDNNAv2ggyKeeCLizz9HQgA//zzisssill8+YrTRegCYYIKIOeaIWGONiF12iTj77IhBgyLeey/i5597Pv/77xFffRXx4osRt90WcfTRSVsXWSRi6qmHv+YMM0QccEA6tx/AbC+Ab72VNG2KKXpucKKJIrbbLuLmmxNQwGlUmO7XX0c880zECSck08/fM9ZYEUsuGXHllRF//z2CAfjGGxFbbx0x3njpZiaeOGKTTSJuvDHil1/qv96339YOKM1jzvPOm0zc9y+6aMS117YFyNYC+P33EfvvHzHuuGnhk08esfvuEV980dvR//FH5evQqief7Pnvo46KOPfc+tbi+tdcE7HQQsnERx01YumlE8AtDDqtA/DWW5M/A9z440ccfHDEZ5+VvzGacNNNEX/9Vfl6J50Ucc89KdpyA0ceGfHgg73PK/q5Sy/trWVcBM0XsKyNVXiozbiOlgL4448Re+4ZMcYYEaOPngICEy41rXfeibjllkRZrrgi4vbbI55+uro5ZnBOPLHyefzo44+nAFTtPHTnmGMiJpkkAbnwwkkbOwrg228n/8LXTDll8jOVIt+jj0bce2/EddelAILK0Ixy8sgjievRwNNPTxG3mv+yjksuifjgg4jHHkucspKZ4o4rrJDWPOGEaR1N+MbGAQQGCuFp4mE0rJq8+Wa6wXPOSTd4xx0Rxx9f2YzPOiv9pK133hnx4Yflz3vhhYjddkvazG++9loi6NX8HG084ojkq1nNzjs3bNKNAXj99RFjjpmcM0pSLSAUAbz88oj//CdxwvPPT5zOtSoB+M03CUDZCq3lLory0kvpQfCXAEZn/K5W4YcFOkqAJfz6az8A6MY5Yj7vtNOqB4Jy/kpEZeoXXBBx0UXp3+XkkEOSidOOjz9OIOF05YIXYAUsfrDa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");


        SignReportDocFileVo vo3 = new SignReportDocFileVo();
        vo3.setValueFirst("标题20");
        vo3.setValueSecond("标题3333");


        SignReportDocFileVo vo31 = new SignReportDocFileVo();
        vo31.setValueFirst("图片");
        vo31.setValueSecond("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");

        SignReportDocFileVo vo4 = new SignReportDocFileVo();
        vo4.setValueFirst("标题4");
        vo4.setValueSecond("标题44444");

        SignReportDocFileVo vo5 = new SignReportDocFileVo();
        vo5.setValueFirst("标题5");
        vo5.setValueSecond("标题555555");

        docFileVoList.add(vo1);
        docFileVoList.add(vo2);
        docFileVoList.add(vo3);
        docFileVoList.add(vo4);
        docFileVoList.add(vo31);
        docFileVoList.add(vo5);
        docFileVoList.add(vo1);
        docFileVoList.add(vo2);
        docFileVoList.add(vo3);
        docFileVoList.add(vo31);
        docFileVoList.add(vo4);
        docFileVoList.add(vo5);
        docFileVoList.add(vo1);
        docFileVoList.add(vo2);
        docFileVoList.add(vo3);
        docFileVoList.add(vo4);
        docFileVoList.add(vo5);
        docFileVoList.add(vo1);
        docFileVoList.add(vo2);
        docFileVoList.add(vo3);
        docFileVoList.add(vo4);
        docFileVoList.add(vo5);
        docFileVoList.add(vo31);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(docFileVoList);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainjasperPath, parameters, dataSource);


            // 4. 导出报表为 PDF 文件
            String pdfFile = "/Users/zhenghuihan/Desktop/report001.pdf";
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));

            exporter.exportReport();

            System.out.println("PDF 文件已导出到: " + pdfFile);

            // Step 2: 使用 PDFBox 添加背景图片
            try (PDDocument document = Loader.loadPDF(new File(pdfFile))) {
                PDImageXObject backgroundImage = PDImageXObject.createFromFile(backgroundImagePath, document);

                for (PDPage page : document.getPages()) {
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {
                        contentStream.drawImage(backgroundImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                    }
                }


                String pdfFile2 = "/Users/zhenghuihan/Desktop/report002.pdf";

                document.save(pdfFile2);
                System.out.println("PDF 文件已成功保存到: " + pdfFile2);

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private static void addData(List<SignReportSignNodeVo> nodeVoList, Integer num) {
        SignReportSignNodeVo signReportSignNodeVo = new SignReportSignNodeVo();
        signReportSignNodeVo.setNum(num);
        signReportSignNodeVo.setOperatorTenantName("测试租户名称");

        List<SignReportSignSubVO> subReport = new ArrayList<>();
        SignReportSignSubVO signReportSignSubVO = new SignReportSignSubVO();
        signReportSignSubVO.setTitle("数字证书信息");

        List<SignReportDocFileVo> subReport2 = new ArrayList<>();
        SignReportDocFileVo fileVo0 = new SignReportDocFileVo();
        fileVo0.setValueFirst("数字证书信息");

        SignReportDocFileVo fileVo1 = new SignReportDocFileVo();
        fileVo1.setValueFirst("证书类型：");
        fileVo1.setValueSecond("事件证书");

        SignReportDocFileVo fileVo2 = new SignReportDocFileVo();
        fileVo2.setValueFirst("证书状态：");
        fileVo2.setValueSecond("有效");

        SignReportDocFileVo fileVo3 = new SignReportDocFileVo();
        fileVo3.setValueFirst("颁发机构：");
        fileVo3.setValueSecond("中认环宇CQCCA");

        SignReportDocFileVo fileVo4 = new SignReportDocFileVo();
        fileVo4.setValueFirst("颁发机构：");
        fileVo4.setValueSecond("中认环宇CQCCA");

        SignReportDocFileVo fileVo5 = new SignReportDocFileVo();
        fileVo5.setValueFirst("颁发机构：");
        fileVo5.setValueSecond("中认环宇CQCCA");

        SignReportDocFileVo fileVo6 = new SignReportDocFileVo();
        fileVo6.setValueFirst("颁发机构：");
        fileVo6.setValueSecond("中认环宇CQCCA");

        SignReportDocFileVo fileVo7 = new SignReportDocFileVo();
        fileVo7.setValueFirst("颁发机构：");
        fileVo7.setValueSecond("中认环宇CQCCA");

        subReport2.add(fileVo0);
        subReport2.add(fileVo1);
        subReport2.add(fileVo2);
        subReport2.add(fileVo3);
        subReport2.add(fileVo4);
        subReport2.add(fileVo5);
        subReport2.add(fileVo6);
        subReport2.add(fileVo7);

        signReportSignSubVO.setSubReport(subReport2);

        subReport.add(signReportSignSubVO);


        SignReportSignSubVO signReportSignSubVO2 = new SignReportSignSubVO();
        signReportSignSubVO2.setTitle("签署意愿校验");

        List<SignReportDocFileVo> subReport3 = new ArrayList<>();
        SignReportDocFileVo fileVo11 = new SignReportDocFileVo();
        fileVo11.setValueFirst("校验类型：");
        fileVo11.setValueSecond("人脸识别认证");

        SignReportDocFileVo fileVo22 = new SignReportDocFileVo();
        fileVo22.setValueFirst("人脸识别校验通道：");
        fileVo22.setValueSecond("腾讯云人脸识别校验");

        SignReportDocFileVo fileVo33 = new SignReportDocFileVo();
        fileVo33.setValueFirst("颁发机构：");
        fileVo33.setValueSecond("中认环宇CQCCA");

        SignReportDocFileVo fileVo44 = new SignReportDocFileVo();
        fileVo44.setValueFirst("签署校验时间：");
        fileVo44.setValueSecond("2024-05-11 10:20:22");

        SignReportDocFileVo fileVo55 = new SignReportDocFileVo();
        fileVo55.setValueFirst("活体检测分数：");
        fileVo55.setValueSecond("98%");

        subReport3.add(fileVo11);
        subReport3.add(fileVo22);
        subReport3.add(fileVo33);
        subReport3.add(fileVo44);
        subReport3.add(fileVo55);


        signReportSignSubVO2.setSubReport(subReport3);

        //subReport.add(signReportSignSubVO2);


        SignReportSignSubVO signReportSignSubVO3 = new SignReportSignSubVO();
        signReportSignSubVO3.setTitle("电子文件清单");

        List<SignReportDocFileVo> subReport4 = new ArrayList<>();
        SignReportDocFileVo fileVo000 = new SignReportDocFileVo();
        fileVo000.setValueFirst("电子文件清单");

        SignReportDocFileVo fileVo111 = new SignReportDocFileVo();
        fileVo111.setValueFirst("文件1：");
        fileVo111.setValueSecond("劳动合同");

        SignReportDocFileVo fileVo222 = new SignReportDocFileVo();
        fileVo222.setValueFirst("签署前文件hash：");
        fileVo222.setValueSecond("23417c908440ebb270d50c7ab107cbe61fb48a9e364590e2d04cee4e60fb1e43");

        SignReportDocFileVo fileVo333 = new SignReportDocFileVo();
        fileVo333.setValueFirst("签署后文件hash：");
        fileVo333.setValueSecond("23417c908440ebb270d50c7ab107cbe61fb48a9e364590e2d04cee4e60fb1e43");


        subReport4.add(fileVo000);
        subReport4.add(fileVo111);
        subReport4.add(fileVo222);
        subReport4.add(fileVo333);


        signReportSignSubVO3.setSubReport(subReport4);

        subReport.add(signReportSignSubVO3);


        signReportSignNodeVo.setSubReport1(subReport);

        nodeVoList.add(signReportSignNodeVo);
    }
}
