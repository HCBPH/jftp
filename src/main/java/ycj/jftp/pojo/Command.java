package ycj.jftp.pojo;

import java.io.Serializable;

/**
 * @author 53059
 * @date 2021/12/10 13:21
 */
public class Command implements Serializable {
    private int type;
    private String params;
    private byte[] content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
