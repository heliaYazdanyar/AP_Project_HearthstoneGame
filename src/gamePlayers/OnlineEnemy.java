package gamePlayers;

import models.*;
import logic.Administer;

import java.util.ArrayList;
import java.util.List;

public class OnlineEnemy implements InGamePlayer {
    public Administer administer;
    private String username;

    private String heroName;
    private Hero hero;

    private List<Card> groundCards;

    private Passive passive;

    private boolean isMyTurn;

    private int currentMana;
    private Weapon weapon;
    private int level;

    public OnlineEnemy(String username,String heroName){
        this.username=username;
        this.hero=Hero.getHero(heroName);
        this.heroName=heroName;

        this.groundCards=new ArrayList<>();

        level=0;
        isMyTurn=false;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setAdminister(Administer administer) {
        this.administer=administer;
    }
    public Administer getAdminister() {
        return administer;
    }

    public String getHeroName() {
        return heroName;
    }
    public Hero getHero() {
        return hero;
    }


    public void playCard(Card card){
        if(card.getType().equalsIgnoreCase("Minion")){
            if(administer.canSummonMinion(this,(Minion)card)) {
                administer.summonMinion(this, (Minion) card);
                administer.removeFromHand(this,card);
                setMana(getMana() - card.getManaCost());
            }
            else{
                administer.setCantHappen(true);
            }
        }
        else if(card.getType().equalsIgnoreCase("Spell")){
            administer.useSpell(this,(Spell)card);
            administer.removeFromHand(this,card);
            if(this.getHero().getName().equalsIgnoreCase("Mage")) setMana(getMana() - (card.getManaCost()-2));
            else setMana(getMana() - card.getManaCost());
        }
        else if(card.getType().equalsIgnoreCase("Weapon")){
            administer.addWeapon((Weapon) card,this);
            administer.removeFromHand(this,card);
            setMana(getMana() - card.getManaCost());
        }
        else {
            //quest and reward
        }
    }


    //not needed???
    public Decks getCurrentDeck() {
        return null;
    }
    public List<Card> getDeckCards() {
        return null;
    }
    public void addToDeck(Card card) {

    }
    public void removeFromDeck(Card card) {

    }
    public Card getRandomCard() {
        return null;
    }
    public void addToHand(Card card) {

    }
    public List<Card> getHandCards() {
        return null;
    }
    public void removeFromHand(Card card) {

    }

    @Override
    public List<Card> getGroundCards() {
        return groundCards;
    }
    public void addToGroundCards(Card card) {
        groundCards.add(card);
    }
    public void removeFromGround(Card card) {
        groundCards.remove(card);
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }
    public void setTurn(boolean turn, boolean manaBurn) {
        if (!isMyTurn && turn){
            level++;
            if(level<10 && manaBurn) {
                if(level>=2) setMana(level-2);
                else setMana(0);
            }
            else if(level<10 && !manaBurn) setMana(level);
            else{
                if(manaBurn) setMana(8);
                else setMana(10);
            }
//            needsUpdate=true;
        }
        this.isMyTurn=turn;

    }

    public int getMana() {
        return currentMana;
    }
    public void setMana(int mana) {
        if(mana<10) this.currentMana=mana;
        else this.currentMana=10;
//        changeNeedsUpdate(true);
    }
    public void addWeapon(Weapon weapon) {
        this.weapon=weapon;
    }
    public Weapon getWeapon() {
        return weapon;
    }

    public void setPassive(Passive passive) {
        this.passive=passive;
        if(passive==Passive.MANAJUMP) level++;
    }
    public Passive getPassive() {
        return passive;
    }




}
