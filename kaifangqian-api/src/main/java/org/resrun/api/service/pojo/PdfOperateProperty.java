package org.resrun.api.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: pdf文件操作属性对象
 * @Package: org.resrun.modules.sign.service.tool.pojo
 * @ClassName: PdfOperateProperty
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfOperateProperty implements Serializable {

    private static final long serialVersionUID = 136217989504135671L;

    //pdf文件数据
    private byte[] pdfFile ;
    //图片数据
    private byte[] imageFile ;
    //文本数据
    private String value ;
    //横坐标偏移量
    private Float offsetX ;
    //纵坐标偏移量
    private Float offsetY ;
    //页码
    private Integer pageNum ;




}