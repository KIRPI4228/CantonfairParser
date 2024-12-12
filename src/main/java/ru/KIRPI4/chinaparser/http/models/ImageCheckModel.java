package ru.KIRPI4.chinaparser.http.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageCheckModel {
    @JsonProperty("data")
    public Data data;
    @JsonProperty("errMsg")
    public String errorMessage;
    @JsonProperty("errCode")
    public String errorCode;
    @JsonProperty("traceId")
    public String traceId;
    @JsonProperty("isSuccess")
    public String isSuccess;

    public class Data  {
        @JsonProperty("sliderKey")
        public String id;
        @JsonProperty("bgImg")
        public String backgroundImage;
        @JsonProperty("blockImg")
        public String blockImage;
        @JsonProperty("yBlock")
        public int blockY;
    }
}
