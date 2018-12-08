package cn.com.authDemo.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author: nj
 * @date: 2018/12/8:下午4:09
 */
@ToString
@Data
public class BaseResponse implements Serializable {
    private String responseMsg;
    private Integer responseCode;
    private Boolean isSuccess;
}
