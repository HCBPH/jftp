package ycj.jftp.pojo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 53059
 * @date 2021/12/10 13:19
 */
public class Request implements Serializable {
    private int type;
    private Map<String, String> param;

    public int getType() {
        return type;
    }

    public void setType(int requestType) {
        this.type = type;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void addParam(String requestParam, String value) {
        param.put(requestParam, value);
    }
}
