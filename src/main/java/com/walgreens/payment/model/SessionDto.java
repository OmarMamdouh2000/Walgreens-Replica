package com.walgreens.payment.model;

import lombok.Data;

import java.util.Map;


@Data
public class SessionDto  {

    private String userId;
    private String sessionUrl;
    private String sessionId;
    private String message;
    private Map<String, String> data;
}
