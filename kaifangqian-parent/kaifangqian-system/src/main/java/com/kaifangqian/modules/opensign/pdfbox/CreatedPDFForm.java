package com.kaifangqian.modules.opensign.pdfbox;

import com.kaifangqian.modules.opensign.pdfbox.vo.FormField;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;

import java.io.*;
import java.util.List;

public class CreatedPDFForm {


    private PDDocument document;
    private PDAcroForm acroForm;
    public CreatedPDFForm(byte [] pdf){
        try {
            document = Loader.loadPDF(pdf);
            acroForm = new PDAcroForm(document);
        } catch (IOException e) {
            throw new RuntimeException("load pdf error:",e);
        }
    }
    public CreatedPDFForm(File pdf){
        try {
            document = Loader.loadPDF(pdf);
            acroForm = new PDAcroForm(document);
        } catch (IOException e) {
            throw new RuntimeException("load pdf error:",e);
        }
    }

//    public byte [] addForms(List<FormField> fields) throws IOException {
//
//        PDResources resources = new PDResources();
//
//        //设置字体
//        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
//        resources.put(COSName.HELV, font);
//
//        // Add a new AcroForm and add that to the document
//        PDAcroForm acroForm = new PDAcroForm(document);
//        document.getDocumentCatalog().setAcroForm(acroForm);
//
//        acroForm.setDefaultResources(resources);
//
//        for (FormField field: fields) {
//
//            PDTextField textBox = new PDTextField(acroForm);
//            textBox.setPartialName(field.getInterfaceParamName());
//            acroForm.getFields().add(textBox);
//            textBox.setDefaultValue(field.getValue());
//
//
//            //设置控件边框
//            PDAppearanceCharacteristicsDictionary fieldAppearance
//                    = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
//            fieldAppearance.setBorderColour(new PDColor(new float[]{0,0,0}, PDDeviceRGB.INSTANCE));
//
//
//            //设置控件位置
//            PDAnnotationWidget widget = textBox.getWidgets().get(0);
//            widget.setAppearanceCharacteristics(fieldAppearance);
//            PDRectangle rect = new PDRectangle(Float.parseFloat(field.getOffsetX()), Float.parseFloat(field.getOffsetY()),
//                    Float.parseFloat(field.getWidth()), Float.parseFloat(field.getHeight()));
//            PDPage page = document.getPage(field.getPage());
//            widget.setRectangle(rect);
//
//            widget.setPage(page);
//            widget.setPrinted(true);
//
//
//            //内容居左
//            if( field.getTextAlign() == null){
//                textBox.setQ(PDVariableText.QUADDING_LEFT);
//            } else if (field.getTextAlign().equals("left")) {
//                textBox.setQ(PDVariableText.QUADDING_LEFT);
//            }else if (field.getTextAlign().equals("center")) {
//                textBox.setQ(PDVariableText.QUADDING_CENTERED);
//            }else if (field.getTextAlign().equals("right")) {
//                textBox.setQ(PDVariableText.QUADDING_RIGHT);
//            }
//            if( field.getControlType() != null && field.getControlType().equals("multiline-text")){
//                textBox.setMultiline(true);
//            }
////            textBox.setValue(field.getValue());
//            page.getAnnotations().add(widget);
//        }
//
//        //设置控件的value
//        setFormValue(fields);
//        //将表单内容固话到PDF上
//        document.getDocumentCatalog().getAcroForm().flatten();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        document.save(bos);
//        document.close();
//
//        return bos.toByteArray();
//    }

    private void setFormValue(List<FormField> fields) throws IOException {

        PDDocumentCatalog docCatalog = document.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        for (FormField item:fields) {
            PDField field = acroForm.getField(item.getInterfaceParamName());
            if(field != null){
//                field.setValue(item.getValue());
            }

        }
    }
    private void setFormValue(List<FormField> fields,PDType0Font font) throws IOException {

        PDDocumentCatalog docCatalog = document.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        for (FormField item:fields) {
            PDField field = acroForm.getField(item.getInterfaceParamName());
            if(field != null){
//                field.setValue(item.getValue());
//                showTextInternal(item.getValue(),font);
            }

        }
    }


    public byte [] addFormsTTF(List<FormField> fields,InputStream fontInputStream) throws IOException {

        PDResources resources = new PDResources();
        //embedSubset需要为true，否则会将字体文件也嵌入到文件中导致文件变大
        PDType0Font font = PDType0Font.load(document, fontInputStream,true);
//        PDType0Font font = PDType0Font.load(document, fontInputStream,false);
        COSName fontCOSName = resources.add(font);
//        resources.put(COSName.HELV, font);

        // Add a new AcroForm and add that to the document
        PDAcroForm acroForm = new PDAcroForm(document);
        document.getDocumentCatalog().setAcroForm(acroForm);

        acroForm.setDefaultResources(resources);

        for (FormField field: fields) {

            PDTextField textBox = new PDTextField(acroForm);
            textBox.setPartialName(field.getInterfaceParamName());
            acroForm.getFields().add(textBox);
//            textBox.setDefaultValue(field.getValue());
            // 设置文本字段的默认外观，这里设置字体为 Helvetica，大小为 12 点
            String defaultAppearance = "/" + fontCOSName.getName() + " " + field.getFontSize() + " Tf 0 g";
            textBox.setDefaultAppearance(defaultAppearance);

            //设置控件位置
            PDAnnotationWidget widget = textBox.getWidgets().get(0);

            PDRectangle rect = new PDRectangle(Float.parseFloat(field.getOffsetX()), Float.parseFloat(field.getOffsetY()),
                    Float.parseFloat(field.getWidth()), Float.parseFloat(field.getHeight()));
            PDPage page = document.getPage(field.getPage());
            widget.setRectangle(rect);

            widget.setPage(page);
            widget.setPrinted(true);


            //内容居左
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
//            textBox.setValue(field.getValue());
            page.getAnnotations().add(widget);
        }

        //设置控件的value
        setFormValue(fields,font);
        font.subset();
        //将表单内容固话到PDF上
        document.getDocumentCatalog().getAcroForm().flatten();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        document.save(bos);
        document.close();

        return bos.toByteArray();
    }

    protected void showTextInternal(String text,PDFont font) throws IOException{

        // Unicode code points to keep when subsetting
        if (font.willBeSubset())
        {
            for (int offset = 0; offset < text.length(); )
            {
                int codePoint = text.codePointAt(offset);
                font.addToSubset(codePoint);
                offset += Character.charCount(codePoint);
            }
        }
//        System.out.println(font.encode(text).toString());
//        COSWriter.writeString(font.encode(text), output);
    }
}
