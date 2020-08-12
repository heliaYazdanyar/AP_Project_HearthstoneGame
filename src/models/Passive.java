package models;

import gamePlayers.PracticePlayer;

public enum Passive {
    TWICEDRAW,
    FREEPOWER,
    WARRIORS,
    MANAJUMP,
    NURSE;

    public void apply(PracticePlayer player){
        player.setPassive(this);
    }
}
