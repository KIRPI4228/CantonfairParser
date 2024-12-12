package ru.KIRPI4.chinaparser.http.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RsaPublicKeyModel {
    @JsonProperty("data")
    public String key;
    @JsonProperty("errMsg")
    public String errorMessage;
    @JsonProperty("errCode")
    public String errorCode;
    @JsonProperty("traceId")
    public String traceId;
    @JsonProperty("isSuccess")
    public boolean isSuccess;
}
