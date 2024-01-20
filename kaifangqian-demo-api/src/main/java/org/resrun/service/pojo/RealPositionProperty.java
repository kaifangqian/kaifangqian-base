package org.resrun.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 经过计算后的文件签署位置属性类
 * @Package: org.resrun.service.pojo
 * @ClassName: PositionProperty
 * @copyright 北京资源律动科技有限公司
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RealPositionProperty implements Serializable {


        private static final long serialVersionUID = 8586984409612483553L;

        /** 签章左下角x坐标 */
        private  float startx;

        /** 签章左下角y坐标*/
        private  float starty;

        /** 签章右上角x坐标*/
        private  float endx;

        /** 签章右上角x坐标*/
        private  float endy;

        private  int pageNum;

        // 填写值，填写专用
        private String value ;
        //对齐方式
        private String align ;
        //字体
        private String fontFamily ;
        //文字大小
        private Integer fontSize ;
}