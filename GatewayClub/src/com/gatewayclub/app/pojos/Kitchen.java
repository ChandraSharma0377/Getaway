package com.gatewayclub.app.pojos;

/**
 * Created by Aditi on 6/11/2017.
 */

public class Kitchen {

    private String PkdMicrowave;
    private String PkdUtensils;
    private String PkdWaterFilter;
    private String PkdCookingGas;
    private String PkdCrockery;
    private String PkdDrinkingGlasses;
    private String PkdRefrigarator;

    public String getPkdMicrowave() {
        return PkdMicrowave;
    }

    public void setPkdMicrowave(String pkdMicrowave) {
        PkdMicrowave = pkdMicrowave;
    }

    public String getPkdUtensils() {
        return PkdUtensils;
    }

    public void setPkdUtensils(String pkdUtensils) {
        PkdUtensils = pkdUtensils;
    }

    public String getPkdWaterFilter() {
        return PkdWaterFilter;
    }

    public void setPkdWaterFilter(String pkdWaterFilter) {
        PkdWaterFilter = pkdWaterFilter;
    }

    public String getPkdCookingGas() {
        return PkdCookingGas;
    }

    public void setPkdCookingGas(String pkdCookingGas) {
        PkdCookingGas = pkdCookingGas;
    }

    public String getPkdCrockery() {
        return PkdCrockery;
    }

    public void setPkdCrockery(String pkdCrockery) {
        PkdCrockery = pkdCrockery;
    }

    public String getPkdDrinkingGlasses() {
        return PkdDrinkingGlasses;
    }

    public void setPkdDrinkingGlasses(String pkdDrinkingGlasses) {
        PkdDrinkingGlasses = pkdDrinkingGlasses;
    }

    public String getPkdRefrigarator() {
        return PkdRefrigarator;
    }

    public void setPkdRefrigarator(String pkdRefrigarator) {
        PkdRefrigarator = pkdRefrigarator;
    }

    @Override
    public String toString() {
        return "Kitchen{" +
                "PkdMicrowave='" + PkdMicrowave + '\'' +
                ", PkdUtensils='" + PkdUtensils + '\'' +
                ", PkdWaterFilter='" + PkdWaterFilter + '\'' +
                ", PkdCookingGas='" + PkdCookingGas + '\'' +
                ", PkdCrockery='" + PkdCrockery + '\'' +
                ", PkdDrinkingGlasses='" + PkdDrinkingGlasses + '\'' +
                ", PkdRefrigarator='" + PkdRefrigarator + '\'' +
                '}';
    }
//    	"PkdMicrowave": "NO",
//                "PkdUtensils": "NO",
//                "PkdWaterFilter": "NO",
//                "PkdCookingGas": "NO",
//                "PkdCrockery": "NO",
//                "PkdDrinkingGlasses": "NO",
//                "PkdRefrigarator": "NO"
}
