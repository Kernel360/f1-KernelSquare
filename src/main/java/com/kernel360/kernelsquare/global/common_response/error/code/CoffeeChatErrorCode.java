package com.kernel360.kernelsquare.global.common_response.error.code;

import com.kernel360.kernelsquare.global.common_response.service.code.CoffeeChatServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CoffeeChatErrorCode implements ErrorCode {
    COFFEE_CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, CoffeeChatServiceStatus.COFFEE_CHAT_ROOM_NOT_FOUND, "존재하지 않는 채팅방");

    private final HttpStatus httpStatus;
    private final ServiceStatus serviceStatus;
    private final String msg;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public Integer getCode() {
        return serviceStatus.getServiceStatus();
    }

    @Override
    public String getMsg() {
        return msg;
    }
}