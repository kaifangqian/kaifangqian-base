package org.resrun.api.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 文件签署属性类
 * @Package: org.resrun.modules.sign.pojo
 * @ClassName: PositionProperty
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PositionProperty implements Serializable {


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
}