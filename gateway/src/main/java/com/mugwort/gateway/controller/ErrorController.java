package com.mugwort.gateway.controller;

import com.hand.hospital.common.api.ApiResponse;
import com.hand.hospital.common.api.ApiResponseBuilder;
import com.hand.hospital.common.constant.ErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @GetMapping(value = "/error")
    public ApiResponse error(HttpServletRequest request) {
        ApiResponse apiResponse = ApiResponseBuilder.buildFailure().message("系统开小差了，请稍后重试");
        try {
            String message = request.getAttribute("javax.servlet.error.message").toString();
            Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
            int errorStatus = (int) request.getAttribute("javax.servlet.error.status_code");
            if (errorStatus == ErrorCodeEnum.USER_LOGIN_OTHER_END.getCode()) {
                apiResponse = ApiResponseBuilder.buildResponse(ErrorCodeEnum.USER_LOGIN_OTHER_END);
            }
        } catch (Exception e) {
            return apiResponse;
        }

        return apiResponse;
    }

}
