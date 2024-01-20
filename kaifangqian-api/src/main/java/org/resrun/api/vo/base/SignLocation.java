package org.resrun.api.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 签署位置数据
 * @Package: org.resrun.vo.base
 * @ClassName: SignLocation
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignLocation implements Serializable {

    private static final long serialVersionUID = -3441618832286287804L;


    //文件宽度
    private Number pageWidth ;

    //文件高度
    private Number pageHeight ;

    //页码
    private Integer page;

    //偏移量-X坐标
    private Number offsetX ;

    //偏移量-Y坐标
    private Number offsetY ;

    //签章宽度
    private Number signWidth ;

    //签章高度
    private Number signHeight ;

}