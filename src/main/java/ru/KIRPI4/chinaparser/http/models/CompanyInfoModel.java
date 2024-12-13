package ru.KIRPI4.chinaparser.http.models;

import lombok.Builder;

@Builder
public class CompanyInfoModel {
    public String name;
    public String description;
    public String website;
    public String contactEmail;
    public String contactTelephone;
    public String contactName;
}
