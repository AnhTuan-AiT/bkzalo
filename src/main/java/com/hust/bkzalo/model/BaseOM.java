package com.hust.bkzalo.model;

import com.hust.bkzalo.utils.BaseHTTP;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseOM {

    private String code;

    private String message;

    public void success() {
        setCode(String.valueOf(BaseHTTP.CODE_1000));
        setMessage(BaseHTTP.MESSAGE_1000);
    }

    public void invalidParameter() {
        setCode(String.valueOf(BaseHTTP.CODE_1004));
        setMessage(BaseHTTP.MESSAGE_1004);
    }
}
