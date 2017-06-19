package com.gatewayclub.app.pojos;

/**
 * Created by C0678642 on 6/7/2017.
 */

public class FacilityDto {

    private IndoorGames indoorGames;
    private OutdoorGames outdoorGames;
    private Medical medical;
    private Transport transport;
    private Tourism tourism;
    private Bedroom bedroom;
    private Kitchen kitchen;
    private Food food;
    private Hall hall;

    public IndoorGames getIndoorGames() {
        return indoorGames;
    }

    public void setIndoorGames(IndoorGames indoorGames) {
        this.indoorGames = indoorGames;
    }

    public OutdoorGames getOutdoorGames() {
        return outdoorGames;
    }

    public void setOutdoorGames(OutdoorGames outdoorGames) {
        this.outdoorGames = outdoorGames;
    }

    public Medical getMedical() {
        return medical;
    }

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Tourism getTourism() {
        return tourism;
    }

    public void setTourism(Tourism tourism) {
        this.tourism = tourism;
    }

    public Bedroom getBedroom() {
        return bedroom;
    }

    public void setBedroom(Bedroom bedroom) {
        this.bedroom = bedroom;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    @Override
    public String toString() {
        return "FacilityDto{" +
                "indoorGames=" + indoorGames +
                ", outdoorGames=" + outdoorGames +
                ", medical=" + medical +
                ", transport=" + transport +
                ", tourism=" + tourism +
                ", bedroom=" + bedroom +
                ", kitchen=" + kitchen +
                ", food=" + food +
                ", hall=" + hall +
                '}';
    }
}
