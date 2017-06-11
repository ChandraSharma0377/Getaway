package com.gatewayclub.app.pojos;

/**
 * Created by C0678642 on 6/7/2017.
 */

public class Medical {

    String PMedDoctor;
    String PMedMedicalShop;
    String PMedHospital;
    String PMedLargeHospital;

    public String getPMedDoctor() {
        return PMedDoctor;
    }

    public void setPMedDoctor(String PMedDoctor) {
        this.PMedDoctor = PMedDoctor;
    }

    public String getPMedMedicalShop() {
        return PMedMedicalShop;
    }

    public void setPMedMedicalShop(String PMedMedicalShop) {
        this.PMedMedicalShop = PMedMedicalShop;
    }

    public String getPMedHospital() {
        return PMedHospital;
    }

    public void setPMedHospital(String PMedHospital) {
        this.PMedHospital = PMedHospital;
    }

    public String getPMedLargeHospital() {
        return PMedLargeHospital;
    }

    public void setPMedLargeHospital(String PMedLargeHospital) {
        this.PMedLargeHospital = PMedLargeHospital;
    }

    @Override
    public String toString() {
        return "Medical{" +
                "PMedDoctor='" + PMedDoctor + '\'' +
                ", PMedMedicalShop='" + PMedMedicalShop + '\'' +
                ", PMedHospital='" + PMedHospital + '\'' +
                ", PMedLargeHospital='" + PMedLargeHospital + '\'' +
                '}';
    }
    //            "Medical":
//
//    {
//        "PMedDoctor":"YES",
//            "PMedMedicalShop":"NO",
//            "PMedHospital":"YES",
//            "PMedLargeHospital":"YES"
//    }
}