package ru.KIRPI4.chinaparser.http.models;

import lombok.Builder;

@Builder
public class ProductInfoModel {
    public String name;
    public String companyName;
    public String website;
    public String targetMarkets;
    public String details;
    public String price;
    public String minimumOrderQuantity;
    public String pictureUrl;
    public Contacts contacts;
    public Specifications specifications;


    @Builder
    public static class Specifications {
        public String productCode;
        public String color;
        public String size;
        public String model;
        public String material;
        public String placeOfShipment;
        public String placeOfOrigin;
        public String characteristics;
    }
    @Builder
    public static class Contacts {
        public String phone;
        public String name;
        public String email;
    }
}
