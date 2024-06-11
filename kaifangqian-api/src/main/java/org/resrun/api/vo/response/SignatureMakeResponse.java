package org.resrun.api.vo.response;

public class SignatureMakeResponse {

    private String entSeal;

    public String getEntSeal() {
        return entSeal;
    }

    public void setEntSeal(String entSeal) {
        this.entSeal = entSeal;
    }

    public SignatureMakeResponse(String entSeal) {
        this.entSeal = entSeal;
    }

    public SignatureMakeResponse() {
    }
}
