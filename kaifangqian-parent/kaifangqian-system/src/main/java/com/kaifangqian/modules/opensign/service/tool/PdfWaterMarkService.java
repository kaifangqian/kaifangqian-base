/**
 * @description PDF水印工具类
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
package com.kaifangqian.modules.opensign.service.tool;

import com.kaifangqian.config.FileProperties;
import com.kaifangqian.exception.PaasException;
import org.apache.commons.io.IOUtils;
import org.apache.fontbox.ttf.TrueTypeCollection;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.util.Matrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @Description: PdfWaterMarkService
 * @Package: com.kaifangqian.modules.opensign.service.tool
 * @ClassName: PdfWaterMarkService
 * @author: FengLai_Gong
 */
@Service
public class PdfWaterMarkService {

    @Autowired
    private FileProperties fileProperties;

    public static void main(String[] args) {
        try {
            addTextWatermark();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] addTextWatermark(byte[] pdfByte) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try  {
            PDDocument document = Loader.loadPDF(pdfByte);
            InputStream fontInputStream = new FileInputStream(new File(fileProperties.getPath().getPath() + "simsun.ttc"));
            TrueTypeCollection coll = new TrueTypeCollection(fontInputStream);
            PDType0Font font = PDType0Font.load(document,  coll.getFontByName("SimSun"), true);

//            PDType0Font font = PDType0Font.load(document, fontInputStream,true);

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            PDPageTree pages = document.getDocumentCatalog().getPages();

            for (PDPage page : pages) {

                PDRectangle mediaBox = page.getMediaBox();
                float width = mediaBox.getWidth();
                System.out.println("pdf文件宽：" + width);
                float height = mediaBox.getHeight();
                System.out.println("pdf文件高：" + height);
                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                        PDPageContentStream.AppendMode.APPEND, true, true);
                // 设置字体和字号
                contentStream.setFont(font, 130);

                // 设置透明度
                contentStream.setNonStrokingColor(0.7f, 0.7f, 0.7f);

                // 添加文本水印
                contentStream.beginText();
                // 设置水印位置,倾斜角度
                Matrix matrix = Matrix.getRotateInstance(45, 150, 100);
                contentStream.setTextMatrix(matrix);
                // 设置水印内容
                contentStream.showText("测 试 环 境");
                contentStream.endText();

                contentStream.close();
            }
            document.save(outputStream);
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return outputStream.toByteArray() ;

    }


    public static void addTextWatermark() throws IOException {

        byte[] pdfByte = IOUtils.toByteArray(new FileInputStream(new File("/Users/kaifangqian/test/testContract01.pdf")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try  {
            PDDocument document = Loader.loadPDF(pdfByte);
            InputStream fontInputStream = new FileInputStream(new File("/Users/kaifangqian/file/simsun.ttf"));
            PDType0Font font = PDType0Font.load(document, fontInputStream,true);

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            PDPageTree pages = document.getDocumentCatalog().getPages();

            for (PDPage page : pages) {

                PDRectangle mediaBox = page.getMediaBox();
                float width = mediaBox.getWidth();
                System.out.println("pdf文件宽：" + width);
                float height = mediaBox.getHeight();
                System.out.println("pdf文件高：" + height);
                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                        PDPageContentStream.AppendMode.APPEND, true, true);
                // 设置字体和字号
                contentStream.setFont(font, 130);

                // 设置透明度
                contentStream.setNonStrokingColor(0.7f, 0.7f, 0.7f);

                // 添加文本水印
                contentStream.beginText();
                // 设置水印位置,倾斜角度
                Matrix matrix = Matrix.getRotateInstance(45, 150, 100);
                contentStream.setTextMatrix(matrix);
                // 设置水印内容
                contentStream.showText("测 试 环 境");
                contentStream.endText();

                contentStream.close();
            }
            document.save(outputStream);
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        byte[] bytes = outputStream.toByteArray();
        IOUtils.write(bytes,new FileOutputStream(new File("/Users/kaifangqian/test/water_mark_11.pdf")));

        try {
            PDDocument document = Loader.loadPDF(bytes);
            if (document == null) {
                throw new PaasException("pdf文件解析失败");
            }
            int numberOfPages = document.getNumberOfPages();
            System.out.println(numberOfPages);
            document.close();
        } catch (Exception e) {
            throw new PaasException("pdf文件异常");
        }






    }


}