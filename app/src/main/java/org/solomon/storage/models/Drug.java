package org.solomon.storage.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Drug extends RealmObject {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

   
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @SerializedName("id")
    @PrimaryKey
    private long id;
    @SerializedName("drug_name")
    private String drugName;
    @SerializedName("code")
    private String code;
    @SerializedName("thumb_nail")
    private String thumbNail;
    @SerializedName("description")
    private String description;

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    @SerializedName("exp_date")
    private String exp_date;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @SerializedName("manufacturer")
    private String manufacturer;


    @Override
    public String toString(){
        return String.format("DRUG:%s MAN:%s DATE:%s", this.description, this.manufacturer, this.exp_date);
    }
}
