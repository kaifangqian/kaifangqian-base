package com.kaifangqian.modules.opensign.pdfbox;

import com.kaifangqian.modules.opensign.pdfbox.vo.*;
import com.kaifangqian.modules.opensign.pdfbox.vo.*;
import org.apache.fontbox.ttf.TrueTypeCollection;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: WritePDFForm
 * @Package: com.kaifangqian.modules.opensign.pdfbox
 * @ClassName: WritePDFForm
 * @author: FengLai_Gong
 */
public class WritePDFForm {

    private PDDocument document;
    private PDAcroForm acroForm;

    private PDResources resources;

    private Map<String, FontEntity> fonts = new HashMap<>();

    public WritePDFForm(byte [] pdf){
        try {
            document = Loader.loadPDF(pdf);
            acroForm = new PDAcroForm(document);
        } catch (IOException e) {
            throw new RuntimeException("load pdf error:",e);
        }
    }
    public WritePDFForm(File pdf){
        try {
            document = Loader.loadPDF(pdf);
            acroForm = new PDAcroForm(document);
        } catch (IOException e) {
            throw new RuntimeException("load pdf error:",e);
        }
    }

    /**
     * 加载控件中的字体
     * @param fields
     * @throws IOException
     */
    public void buildFonts(List<FormField> fields, String baseFilePath) throws IOException {
        FontFactory factory = FontFactory.getInstance(baseFilePath);
        try {
            for (FormField field: fields) {
                if(field.getFontFamily() != null && field.getFontFamily().length() > 0 && fonts.get(field.getFontFamily()) == null){
                    FontEnum font = FontEnum.findFont(field.getFontFamily());
                    InputStream fontIns = factory.getFont(font);
                    PDFont pdFont = null;
                    if(font.getType().equals("ttf")){
                        pdFont = PDType0Font.load(document,  fontIns, true);
                    }else if(font.getType().equals("ttc")) {
                        TrueTypeCollection coll = new TrueTypeCollection(fontIns);
                        pdFont = PDType0Font.load(document,  coll.getFontByName(font.getFontKey()), true);
                    }
                    String fontName = resources.add(pdFont).getName();
                    String defaultAppearanceString = "/" + fontName + " "+field.getFontSize()+" Tf 0 g";
                    FontEntity fontEntity =new FontEntity(pdFont,fontName,defaultAppearanceString);
                    fonts.put(field.getFontFamily(),fontEntity);
                }
            }
        }finally {
            factory.close();
        }
    }

    public byte [] addForms(List<FormField> fields,String baseFilePath) throws IOException {

        resources = new PDResources();
        this.buildFonts(fields,baseFilePath);
        //设置字体
//        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
//        resources.put(COSName.HELV, font);

        // Add a new AcroForm and add that to the document
        acroForm = new PDAcroForm(document);
        document.getDocumentCatalog().setAcroForm(acroForm);

        acroForm.setDefaultResources(resources);

        for (FormField field: fields) {
            buildPDField(field);
        }

        //设置控件的value
//        this.setFormValue(fields);
        this.subset();
        //将表单内容固化到PDF上
        document.getDocumentCatalog().getAcroForm().flatten();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        document.save(bos);
        document.close();

        return bos.toByteArray();
    }

    private void buildPDField(FormField field) throws IOException {

        PDRectangle rect = new PDRectangle(Float.parseFloat(field.getOffsetX()), Float.parseFloat(field.getOffsetY()),
                Float.parseFloat(field.getWidth()), Float.parseFloat(field.getHeight()));


        FontEntity font = fonts.get(field.getFontFamily());

        //默认单行文本
        if(field.getControlType() == null || FormFieldType.LineText.getType().equals(field.getControlType())
                || FormFieldType.MultilineText.getType().equals(field.getControlType()) || FormFieldType.Date.getType().equals(field.getControlType())
                || FormFieldType.Number.getType().equals(field.getControlType()) || FormFieldType.DropdownList.getType().equals(field.getControlType())){
            if(font == null){
                //TODO 无法识别的字体
                return;
            }

            PDTextField textBox = new PDTextField(acroForm);
            textBox.setPartialName(field.getInterfaceParamName());

//            textBox.setDefaultValue(field.getValue());
            String defaultAppearanceString = "/" + font.getFontName() + " "+field.getFontSize()+" Tf 0 g";

            textBox.setDefaultAppearance(defaultAppearanceString);
//            textBox.setDefaultAppearance(font.getAppearanceString());

            if( field.getTextAlign() == null){
                textBox.setQ(PDVariableText.QUADDING_LEFT);
            } else if (field.getTextAlign().equals("left")) {
                textBox.setQ(PDVariableText.QUADDING_LEFT);
            }else if (field.getTextAlign().equals("center")) {
                textBox.setQ(PDVariableText.QUADDING_CENTERED);
            }else if (field.getTextAlign().equals("right")) {
                textBox.setQ(PDVariableText.QUADDING_RIGHT);
            }
            if( field.getControlType() != null && field.getControlType().equals("multiline-text")){
                textBox.setMultiline(true);
            }

            PDAnnotationWidget widget = textBox.getWidgets().get(0);
            PDPage page = document.getPage(field.getPage());
            widget.setRectangle(rect);
            widget.setPage(page);
            widget.setPrinted(true);
            page.getAnnotations().add(widget);
            acroForm.getFields().add(textBox);
            this.setFormValue(field,textBox);
        }else{
            this.setFormValue(field,null);
        }
    }


    private void setFormValue(List<FormField> fields) throws IOException {

        PDDocumentCatalog docCatalog = document.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        for (FormField item:fields) {
            FontEntity font = fonts.get(item.getFontFamily());
            PDField field = acroForm.getField(item.getInterfaceParamName());
            if(field != null){
                if(item.getValue() != null){
                    field.setValue(item.getValue().toString());
                    this.textAddSubset(item.getValue().toString(),font.getPdFont());
                }else {
                    field.setValue("");
                    this.textAddSubset("",font.getPdFont());
                }

            }
        }
    }
    private void setFormValue(FormField item,PDField field) throws IOException {
        if(item.getControlType() == null || FormFieldType.LineText.getType().equals(item.getControlType())
                || FormFieldType.MultilineText.getType().equals(item.getControlType()) || FormFieldType.Date.getType().equals(item.getControlType())
                || FormFieldType.Number.getType().equals(item.getControlType())){
            FontEntity font = fonts.get(item.getFontFamily());
            if(field != null){
                if(item.getValue() != null){
                    field.setValue(item.getValue().toString());
                    this.textAddSubset(item.getValue().toString(),font.getPdFont());
                }else {
                    field.setValue("");
                    this.textAddSubset("",font.getPdFont());
                }
            }
        }else if(FormFieldType.DropdownList.getType().equals(item.getControlType())){
            if(item.getWidgets() != null && item.getWidgets().size()>0){
                FontEntity font = fonts.get(item.getFontFamily());
                for (Widget widget:item.getWidgets()) {
                    if(widget.getWidgetValue()){
                        field.setValue(widget.getWidgetName());
                        this.textAddSubset(widget.getWidgetName(),font.getPdFont());
                    }
                }
            }
        }else if(FormFieldType.Image.getType().equals(item.getControlType())){
            byte [] image = (byte[])item.getValue();
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document,image,item.getInterfaceParamName());
            PDPage page = document.getPage(item.getPage());
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true))
            {
                float scale = 1f;
                contentStream.drawImage(pdImage, Float.parseFloat(item.getOffsetX()),  Float.parseFloat(item.getOffsetY()),
                        Float.parseFloat(item.getWidth())*scale, Float.parseFloat(item.getHeight())*scale);
            }

        }else if(FormFieldType.CheckBox.getType().equals(item.getControlType())||FormFieldType.Radio.getType().equals(item.getControlType())){
            if(item.getWidgets() != null && item.getWidgets().size()>0){
//                FontEntity font = fonts.get(item.getFontFamily());
                for (Widget widget:item.getWidgets()) {

                    PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, Base64.decode(getCheckBoxImg(widget.getWidgetValue())),item.getInterfaceParamName());

                    PDPage page = document.getPage(item.getPage());
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true))
                    {
//                        float scale = 1f;
//                        contentStream.drawImage(pdImage, Float.parseFloat(widget.getOffsetX()),  Float.parseFloat(widget.getOffsetY()), widget.getWidth()*scale, widget.getHeight()*scale);
//                        contentStream.drawImage(pdImage, Float.parseFloat(widget.getOffsetX())+ 5.9f,  Float.parseFloat(widget.getOffsetY()) + 5.9f, 12, 12);
                        contentStream.drawImage(pdImage, Float.parseFloat(widget.getOffsetX()) + 10f,  Float.parseFloat(widget.getOffsetY()) + 12f, 12, 12);
                    }
                }
            }
        }
    }
    public String getCheckBoxImg(boolean select){
        if(select){
            //选中 框打钩
            return "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABmJLR0QA/wD/AP+gvaeTAAAB1UlEQVRoge3ZP4vUQByH8Y8iiJVYWrgrdqeFhZU2FscVFoKI4FuwtbKysBfPSi3sxJcgi43/EE5BKysLsbhG7gpPENRC7oqsbDyT3GwyuYncPDBsNpPf8H12yWSSkMnsbfY19C3gJPbvUpYmNrCC76EFy9gcWFvFqZDwZwcQtq5Ntoc9UCFwprR9Bx9DrHvmBk7gdMjB182Mz/cYah5eK/Ksb+8YwgnaiSyQmiyQmiwQmWO4j6uhBUMSGOMVruERDoYUDUVgjBc4Pv2+gl8hhUMQGOGZWfj3uBRanFpghOeKdQ5F+CV8DR0gpUDn8KQTiBKe+QTOKU6um5rv5HZijJdm4d9iUYvwddQtpx+X9t/VTmKMz6Vx3uBwQF3tcrqKOoFFxdTWVqJteCIJwAX8KPU/ECYxwqdS3TscCQkzJZoA80t0DU9kAcIlYoSnBwF2logVnp4EqJeIGZ4eBeCiv2enh9rPNnX0KsC//0SsX/4PvT9WmeAyfpb2tV4ezEPMtdAEV/AFT0VeHtRR9WixC09wNPKYjaS+H+hMFkhNFkjNfy9QNY1ulLZvYW2XsjSxMP38FnLwSPE2MPX7sKp2O9R4CR/wewChNxVX9Hs4FCqQyewVtgCRDDapQgGaSgAAAABJRU5ErkJggg==";
        }else{
            //未选中  方框
            return "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABmJLR0QA/wD/AP+gvaeTAAAA3klEQVRoge3ZoQrCUBiG4VexeBMGmxoMJpPJCzKZ7aLNW/EGBC/AZLZpUMEqM0xwjDmOyPZN/B447MDZ4H/D0gGz/1bLOesAXaBe0ix5LsAWuIV+sASiiq0D0AsZfliBYd+tdXrYRkbAILFfAPuQ6oJNgTbQD3l5wqt4VOBQn9gQz3NKH1ThB/2KA9QcoOYANQeoOUDNAWoOUHOAmgPUHKDmADUHqDlAzQFqDlD7+YCsG5pLYj8DjiXNkqfzfF5DXm4R3waq78Oy1jy0eAzsgHsFho6AM7ACmqEBZv/iAYhbpbtFjTgeAAAAAElFTkSuQmCC";
        }
    }

    private void subset() throws IOException {
        for (Map.Entry<String,FontEntity> font :fonts.entrySet()){
            font.getValue().getPdFont().subset();
        }
    }
    private void textAddSubset(String text,PDFont font) throws IOException
    {
        int codePoint;
        if (font.willBeSubset()) {
            for(int offset = 0; offset < text.length(); offset += Character.charCount(codePoint)) {
                codePoint = text.codePointAt(offset);
                font.addToSubset(codePoint);
            }
        }
    }


}