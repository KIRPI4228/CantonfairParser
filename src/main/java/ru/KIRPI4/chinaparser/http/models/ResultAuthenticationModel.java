package ru.KIRPI4.chinaparser.http.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultAuthenticationModel {
    @JsonProperty("data")
    public ResultAuthenticationModel.Data data;
    @JsonProperty("errMsg")
    public String errorMessage;
    @JsonProperty("errCode")
    public String errorCode;
    @JsonProperty("traceId")
    public String traceId;
    @JsonProperty("isSuccess")
    public boolean isSuccess;

    public class Data  {
        @JsonProperty("userId")
        public String id;
        @JsonProperty("authorizationCode")
        public String code;
        @JsonProperty("clientType")
        public String clientType;
        @JsonProperty("userIdExpiration")
        public int expiration;
    }
}
