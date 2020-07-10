package gamePlayers;

import Models.*;

import java.util.List;

public interface InGamePlayer {

    public String getUsername();

    //hero handling
    public String getHeroName();
    public Hero getHero();

    //deck
    public Decks getCurrentDeck();
    public List<Card> getDeckCards();
    public void addToDeck(Card card);
    public void removeFromDeck(Card card);


    //hand cards
    public Card getRandomCard();
    public void addToHand(Card card);
    public List<Card> getHandCards();
    void removeFromHand(Card card);

    //ground cards
    public List<Card> getGroundCards();
    public void addToGroundCards(Card card);
    public void removeFromGround(Card card);

    //turn
    public boolean isMyTurn();
    public void setTurn(boolean trun,boolean manaBurn);

    //mana
    public int getMana();
    public void setMana(int t);

    public void addWeapon(Weapon weapon);

    public void setPassive(Passive passive);
    public Passive getPassive();

}
