package cn.com.authDemo.common;

import lombok.Data;
import lombok.ToString;

/**
 * @author: nj
 * @date: 2018/12/19:下午4:41
 */
@Data
@ToString
public class BaseResponseExt<T> {
    private BaseResponse baseResponse;
    private T data;
}
