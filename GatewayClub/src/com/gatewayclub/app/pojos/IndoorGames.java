package com.gatewayclub.app.pojos;

/**
 * Created by C0678642 on 6/7/2017.
 */

public class IndoorGames {

    String PGCarrom;
    String PGTableTennis;
    String PGChess;
    String PGLudo;
    String PGPlayingCards;
    String PGMonopoly;
    String PGVideoGames;

    public String getPGCarrom() {
        return PGCarrom;
    }

    public void setPGCarrom(String PGCarrom) {
        this.PGCarrom = PGCarrom;
    }

    public String getPGTableTennis() {
        return PGTableTennis;
    }

    public void setPGTableTennis(String PGTableTennis) {
        this.PGTableTennis = PGTableTennis;
    }

    public String getPGChess() {
        return PGChess;
    }

    public void setPGChess(String PGChess) {
        this.PGChess = PGChess;
    }

    public String getPGLudo() {
        return PGLudo;
    }

    public void setPGLudo(String PGLudo) {
        this.PGLudo = PGLudo;
    }

    public String getPGPlayingCards() {
        return PGPlayingCards;
    }

    public void setPGPlayingCards(String PGPlayingCards) {
        this.PGPlayingCards = PGPlayingCards;
    }

    public String getPGMonopoly() {
        return PGMonopoly;
    }

    public void setPGMonopoly(String PGMonopoly) {
        this.PGMonopoly = PGMonopoly;
    }

    public String getPGVideoGames() {
        return PGVideoGames;
    }

    public void setPGVideoGames(String PGVideoGames) {
        this.PGVideoGames = PGVideoGames;
    }

    @Override
    public String toString() {
        return "IndoorGames{" +
                "PGCarrom='" + PGCarrom + '\'' +
                ", PGTableTennis='" + PGTableTennis + '\'' +
                ", PGChess='" + PGChess + '\'' +
                ", PGLudo='" + PGLudo + '\'' +
                ", PGPlayingCards='" + PGPlayingCards + '\'' +
                ", PGMonopoly='" + PGMonopoly + '\'' +
                ", PGVideoGames='" + PGVideoGames + '\'' +
                '}';
    }
//      "IndoorGames": {
//            "PGCarrom": "NO",
//                    "PGTableTennis": "NO",
//                    "PGChess": "YES",
//                    "PGLudo": "NO",
//                    "PGPlayingCards": "YES",
//                    "PGMonopoly": "NO",
//                    "PGVideoGames": "NO"
//        }
}
