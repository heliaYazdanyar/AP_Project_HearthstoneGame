package gamePlayers;

import Models.Card;
import Models.Deck;
import Models.Hero;
import Models.Weapon;

import java.util.List;

public class PlayerBot implements InGamePlayer{
    Deck deck;
    String user;

    @Override
    public String getUsername() {
        return user;
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
    public void setTurn(boolean trun, boolean manaBurn, boolean manaJump) {

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

    public void removeFromHand(Card card) {

    }
    public void addToGroundCards(Card card) {

    }
    public void addWeapon(Weapon weapon) {

    }

    @Override
    public void removeFromGround(Card card) {

    }
}
