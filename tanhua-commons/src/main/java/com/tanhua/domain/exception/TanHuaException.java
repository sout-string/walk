package com.tanhua.domain.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 涂根
 * @date: 2021/05/03 下午 10:02
 */
@Data
@NoArgsConstructor
public class TanHuaException extends RuntimeException {
    private Object errData;

    public TanHuaException(String errMessage) {
        super(errMessage);
    }

    public TanHuaException(Object Data) {
        super();
        this.errData = Data;
    }
}
