package org.resrun.controller.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: Result
 * @Package: org.resrun.controller.vo
 * @ClassName: Result
 * @copyright 北京资源律动科技有限公司
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("统一返回对象")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 4186430915088458662L;

    @ApiModelProperty("成功标志")
    private boolean success = true;
    @ApiModelProperty("返回处理消息")
    private String message = "";
    @ApiModelProperty("返回代码")
    private Integer code = 200;
    @ApiModelProperty("返回数据对象")
    private T result;
    @ApiModelProperty("时间戳")
    private long timestamp = System.currentTimeMillis();


    public static <T> Result<T> OK(T data) {
        Result<T> r = new Result();
        r.setSuccess(true);
        r.setCode(200);
        r.setResult(data);
        return r;
    }



    public static <T> Result<T> error(String msg, T data) {
        Result<T> r = new Result();
        r.setSuccess(false);
        r.setCode(500);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

}