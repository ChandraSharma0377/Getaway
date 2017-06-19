package com.gatewayclub.app.pojos;

/**
 * Created by Aditi on 6/11/2017.
 */

public class Food {

    private String FDFNonVeg;
    private String FDFJainFood;
    private String FDFVeg;
    private String FDFInhouseCooking;
    private String FDFCateringService;
    private String FDFSelfCooking;

    public String getFDFNonVeg() {
        return FDFNonVeg;
    }

    public void setFDFNonVeg(String FDFNonVeg) {
        this.FDFNonVeg = FDFNonVeg;
    }

    public String getFDFJainFood() {
        return FDFJainFood;
    }

    public void setFDFJainFood(String FDFJainFood) {
        this.FDFJainFood = FDFJainFood;
    }

    public String getFDFVeg() {
        return FDFVeg;
    }

    public void setFDFVeg(String FDFVeg) {
        this.FDFVeg = FDFVeg;
    }

    public String getFDFInhouseCooking() {
        return FDFInhouseCooking;
    }

    public void setFDFInhouseCooking(String FDFInhouseCooking) {
        this.FDFInhouseCooking = FDFInhouseCooking;
    }

    public String getFDFCateringService() {
        return FDFCateringService;
    }

    public void setFDFCateringService(String FDFCateringService) {
        this.FDFCateringService = FDFCateringService;
    }

    public String getFDFSelfCooking() {
        return FDFSelfCooking;
    }

    public void setFDFSelfCooking(String FDFSelfCooking) {
        this.FDFSelfCooking = FDFSelfCooking;
    }

    @Override
    public String toString() {
        return "Food{" +
                "FDFNonVeg='" + FDFNonVeg + '\'' +
                ", FDFJainFood='" + FDFJainFood + '\'' +
                ", FDFVeg='" + FDFVeg + '\'' +
                ", FDFInhouseCooking='" + FDFInhouseCooking + '\'' +
                ", FDFCateringService='" + FDFCateringService + '\'' +
                ", FDFSelfCooking='" + FDFSelfCooking + '\'' +
                '}';
    }

//    	"FDFNonVeg": "NO",
//                "FDFJainFood": "NO",
//                "FDFVeg": "NO",
//                "FDFInhouseCooking": "NO",
//                "FDFCateringService": "NO",
//                "FDFSelfCooking": "NO"
}
