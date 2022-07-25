package ycj.jftp.pojo;

import java.io.Serializable;

/**
 * @author 53059
 * @date 2021/12/10 13:19
 */
public class Response implements Serializable {
    private int code;
    private String reason;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
