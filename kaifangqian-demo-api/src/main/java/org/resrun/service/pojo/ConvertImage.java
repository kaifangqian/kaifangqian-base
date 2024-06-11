package org.resrun.service.pojo;

import io.swagger.annotations.ApiModelProperty;


import java.io.Serializable;

/**
 * @Description: 图片转换对象
 * @Package: org.resrun.service.pojo
 * @ClassName: ConvertImage
 * @copyright 北京资源律动科技有限公司
 */
public class ConvertImage implements Serializable {


    private static final long serialVersionUID = -7285616018724242137L;

    @ApiModelProperty("页码")
    private Integer page ;

    @ApiModelProperty("图片数据")
    private byte[] imageByte ;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }
}