package com.gatewayclub.app.pojos;

/**
 * Created by C0678642 on 6/7/2017.
 */

public class Transport {

    String PMTransTaxi;
    String PMTransAutoStand;
    String PMTransBusStand;
    String PMTransRailwayStation;

    public String getPMTransTaxi() {
        return PMTransTaxi;
    }

    public void setPMTransTaxi(String PMTransTaxi) {
        this.PMTransTaxi = PMTransTaxi;
    }

    public String getPMTransAutoStand() {
        return PMTransAutoStand;
    }

    public void setPMTransAutoStand(String PMTransAutoStand) {
        this.PMTransAutoStand = PMTransAutoStand;
    }

    public String getPMTransBusStand() {
        return PMTransBusStand;
    }

    public void setPMTransBusStand(String PMTransBusStand) {
        this.PMTransBusStand = PMTransBusStand;
    }

    public String getPMTransRailwayStation() {
        return PMTransRailwayStation;
    }

    public void setPMTransRailwayStation(String PMTransRailwayStation) {
        this.PMTransRailwayStation = PMTransRailwayStation;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "PMTransTaxi='" + PMTransTaxi + '\'' +
                ", PMTransAutoStand='" + PMTransAutoStand + '\'' +
                ", PMTransBusStand='" + PMTransBusStand + '\'' +
                ", PMTransRailwayStation='" + PMTransRailwayStation + '\'' +
                '}';
    }
//            "Transport":
//
//    {
//        "PMTransTaxi":"YES",
//            "PMTransAutoStand":"NO",
//            "PMTransBusStand":"YES",
//            "PMTransRailwayStation":"YES"
//    },
}