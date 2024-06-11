package org.resrun.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: SignRequest
 * @Package: org.resrun.controller.vo.request
 * @ClassName: SignRequest
 * @copyright 北京资源律动科技有限公司
 */

@ApiModel("签署-请求对象")
public class SignRequest implements Serializable {

    @ApiModelProperty("签署类型，1位置签署，2关键字签署")
    private Integer signType ;

    @ApiModelProperty("企业签章base64")
    private String entSeal ;

    @ApiModelProperty("企业名称")
    private String entName ;

    @ApiModelProperty("个人签章base64")
    private String personalSeal ;

    @ApiModelProperty("个人名称")
    private String personalName ;

    @ApiModelProperty("企业签署位置")
    private List<PositionRequest> entPositionList ;

    @ApiModelProperty("个人签章位置")
    private List<PositionRequest> personalPositionList ;

    @ApiModelProperty("企业签章关键字")
    private String entKeyword ;

    @ApiModelProperty("个人签章关键字")
    private String personalKeyword ;


    public Integer getSignType() {
        return signType;
    }

    public void setSignType(Integer signType) {
        this.signType = signType;
    }

    public String getEntSeal() {
        return entSeal;
    }

    public void setEntSeal(String entSeal) {
        this.entSeal = entSeal;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getPersonalSeal() {
        return personalSeal;
    }

    public void setPersonalSeal(String personalSeal) {
        this.personalSeal = personalSeal;
    }

    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    public List<PositionRequest> getEntPositionList() {
        return entPositionList;
    }

    public void setEntPositionList(List<PositionRequest> entPositionList) {
        this.entPositionList = entPositionList;
    }

    public List<PositionRequest> getPersonalPositionList() {
        return personalPositionList;
    }

    public void setPersonalPositionList(List<PositionRequest> personalPositionList) {
        this.personalPositionList = personalPositionList;
    }

    public String getEntKeyword() {
        return entKeyword;
    }

    public void setEntKeyword(String entKeyword) {
        this.entKeyword = entKeyword;
    }

    public String getPersonalKeyword() {
        return personalKeyword;
    }

    public void setPersonalKeyword(String personalKeyword) {
        this.personalKeyword = personalKeyword;
    }
}