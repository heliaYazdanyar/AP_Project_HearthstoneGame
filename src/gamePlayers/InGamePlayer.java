package gamePlayers;

import Models.Card;
import Models.Decks;
import Models.Hero;
import Models.Weapon;

import java.util.List;

public interface InGamePlayer {

    public String getUsername();
    public Decks getCurrentDeck();
    public String getHeroName();
    public Hero getHero();
    public boolean isMyTurn();
    public void setTurn(boolean trun,boolean manaBurn,boolean manaJump);
    public List<Card> getDeckCards();
    public List<Card> getHandCards();
    public List<Card> getGroundCards();
    public Card getRandomCard();
    public void addToHand(Card card);
    public void addToDeck(Card card);
    public void removeFromHand(Card card);
    public void addToGroundCards(Card card);
    public void addWeapon(Weapon weapon);
    public void removeFromGround(Card card);

}
