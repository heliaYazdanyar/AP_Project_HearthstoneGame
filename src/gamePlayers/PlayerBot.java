package gamePlayers;

import Models.*;
import logic.Administer;

import java.util.List;

public class PlayerBot implements InGamePlayer{
    Deck deck;
    String user;

    @Override
    public String getUsername() {
        return user;
    }

    @Override
    public void setAdminister(Administer administer) {

    }

    @Override
    public Administer getAdminister() {
        return null;
    }

    public Deck getCurrentDeck() {
        return null;
    }
    public String getHeroName() {
        return null;
    }
    public Hero getHero() {
        return null;
    }
    public boolean isMyTurn() {
        return false;
    }
    public void setTurn(boolean trun, boolean manaBurn) {

    }
    public List<Card> getDeckCards() {
        return null;
    }
    public List<Card> getHandCards() {
        return null;
    }
    public List<Card> getGroundCards() {
        return null;
    }
    public Card getRandomCard() {
        return null;
    }
    public void addToHand(Card card) {

    }

    @Override
    public void addToDeck(Card card) {

    }

    @Override
    public void removeFromDeck(Card card) {

    }

    public void removeFromHand(Card card) {

    }
    public void addToGroundCards(Card card) {

    }
    public void addWeapon(Weapon weapon) {

    }

    @Override
    public Weapon getWeapon() {
        return null;
    }

    @Override
    public void removeFromGround(Card card) {

    }

    @Override
    public void setPassive(Passive passive) {

    }
    public Passive getPassive() {
        return null;
    }

    @Override
    public int getMana() {
        return 0;
    }
    public void setMana(int t) {

    }
}
