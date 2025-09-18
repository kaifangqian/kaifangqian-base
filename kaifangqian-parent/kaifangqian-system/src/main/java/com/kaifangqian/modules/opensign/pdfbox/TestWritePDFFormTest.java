package com.kaifangqian.modules.opensign.pdfbox;

import com.kaifangqian.modules.opensign.pdfbox.vo.FormField;
import com.kaifangqian.modules.opensign.pdfbox.vo.FormFieldType;
import com.kaifangqian.modules.opensign.pdfbox.vo.Widget;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TestWritePDFFormTest
 * @Package: com.kaifangqian.modules.opensign.pdfbox
 * @ClassName: TestWritePDFFormTest
 * @author: FengLai_Gong
 */
public class TestWritePDFFormTest {
    static String base = "/Users/kaifangqian/file";
    private static final File OUT_DIR = new File(base+"/pdfbox/out");
    private static final File IN_DIR = new File(base + "/pdfbox/in");


    public static void main(String[] args) throws IOException {
//        File file = new File(OUT_DIR, "SimpleForm-out.pdf");
        WritePDFForm pdfForm = new WritePDFForm(new File(IN_DIR + "/testContract01.pdf"));

        byte [] out = pdfForm.addForms(buildFields(),"/Users/kaifangqian/file/");
        FileUtils.writeByteArrayToFile(new File("/Users/kaifangqian/file/pdfbox/out/write3.pdf"), out);
        System.out.println("success");
    }

    public static List<FormField> buildFields() throws IOException {
        List<FormField> fields = new ArrayList<>();
        FormField formField = new FormField();
        formField.setInterfaceParamName("text1");
        formField.setHeight("22.33");
        formField.setWidth("148.825");
        formField.setOffsetX((59.53 * 5) + "");
        formField.setOffsetY("700.16");
        formField.setPage(0);
        formField.setValue("中国人民共和国中国人民共和国中国人民共和国");
        formField.setFontFamily("SimSun");
        formField.setFontSize("8.9");
//
        FormField formField2 = new FormField();
        formField2.setInterfaceParamName("text2");
        formField2.setHeight("30");
        formField2.setWidth("200");
        formField2.setOffsetX("100");
        formField2.setOffsetY("100");
        formField2.setPage(0);
        formField2.setValue("1黑体字体");
        formField2.setFontSize("12");
        formField2.setFontFamily("SimSun");

        formField2.setControlType(FormFieldType.DropdownList.getType());
        Widget widget7 = new Widget("选项1",false);
        Widget widget8 = new Widget("选项2",true);
        Widget widget9 = new Widget("选项3",false);

        List<Widget> widgets2= new ArrayList<>();
        widgets2.add(widget7);
        widgets2.add(widget8);
        widgets2.add(widget9);
        formField2.setWidgets(widgets2);




        FormField formField3 = new FormField();
        formField3.setInterfaceParamName("text3");
        formField3.setHeight("30");
        formField3.setWidth("200");
        formField3.setOffsetX("200");
        formField3.setOffsetY("200");
        formField3.setPage(0);
        formField3.setValue("2楷体字体");
        formField3.setFontSize("12");
        formField3.setControlType(FormFieldType.Radio.getType());
        List<Widget> widgetsRadio = new ArrayList<>();
        for (int i =0;i<10;i++){
            Widget widget2 = new Widget((59.53 * i) + "","809.16","选项1",i%2!=0);
            widget2.setWidth(23);
            widget2.setHeight(23);
            widgetsRadio.add(widget2);
        }

//        Widget widget1 = new Widget("300","300","选项1",false);
//        Widget widget2 = new Widget("0","823.9","选项1",true);
//        widget2.setWidth(59);
//        widget2.setHeight(18);
//        Widget widget3 = new Widget("100","300","选项1",false);

//        widgetsRadio.add(widget1);
//        widgetsRadio.add(widget2);
//        widgetsRadio.add(widget3);
        formField3.setWidgets(widgetsRadio);
        formField3.setFontFamily("SimSun");


        FormField formField4 = new FormField();
        formField4.setInterfaceParamName("text4");
        formField4.setHeight("32");
        formField4.setWidth("32");
        formField4.setOffsetX("300");
        formField4.setOffsetY("300");
        formField4.setPage(0);
        formField4.setValue("Yes");
        formField4.setFontSize("12");
//        formField4.setFontFamily();
        formField4.setControlType(FormFieldType.CheckBox.getType());
        formField4.setFontFamily("SimSun");

        Widget widget4 = new Widget("300","400","选项1",true);
        Widget widget5 = new Widget("200","400","选项1",true);
        Widget widget6 = new Widget("100","400","选项1",true);
        List<Widget> widgets = new ArrayList<>();
        widgets.add(widget4);
        widgets.add(widget5);
        widgets.add(widget6);
        formField4.setWidgets(widgets);


        FormField formField5 = new FormField();
        formField5.setInterfaceParamName("text5");
        formField5.setHeight("300");
        formField5.setWidth("180");
        formField5.setOffsetX("100");
        formField5.setOffsetY("600");
        formField5.setPage(0);

        byte [] file = FileUtils.readFileToByteArray(new File("/Users/kaifangqian/file/zyld.png"));
        formField5.setValue(file);
        formField5.setFontSize("12");
        formField5.setControlType(FormFieldType.Image.getType());
        fields.add(formField);
        fields.add(formField2);
        fields.add(formField3);
        fields.add(formField4);
        fields.add(formField5);

        return fields;
    }
}