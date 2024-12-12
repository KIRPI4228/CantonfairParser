package ru.KIRPI4.chinaparser.http.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultLoginModel {
    @JsonProperty("userId")
    public String id;
    @JsonProperty("userName")
    public String name;
    @JsonProperty("authIdentifier")
    public String identifier;
    @JsonProperty("bestLoginTime")
    public String loginTime;
    @JsonProperty("userBiz")
    public String biz;
    @JsonProperty("accessToken")
    public String accessToken;
    @JsonProperty("refreshToken")
    public String refreshToken;
    @JsonProperty("expiresRefresh")
    public String expiresRefresh;
    @JsonProperty("expiresIn")
    public String expiresIn;
    @JsonProperty("lhCookie")
    public String lhCookie;
    @JsonProperty("lhExpiresIn")
    public String lhExpiresIn;
}
