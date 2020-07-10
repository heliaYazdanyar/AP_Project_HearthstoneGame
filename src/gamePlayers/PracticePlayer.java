package gamePlayers;

import Models.*;
import Util.DeckReader;
import logic.Administer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PracticePlayer implements InGamePlayer {
    private String username;
    private String heroName;
    private Hero hero;
    private Decks deck;

    private List<Card> handCards;
    private List<Card> deckCards;
    private List<Card> groundCards;

    private boolean isMyTurn=false;
    private boolean carPicked=true;
    private boolean needsUpdate=false;
    private int currentMana;
    private Weapon weapon;

    private int level=0;


    private Thread pickCardThread=new Thread(){
        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(3000);
                    if(isMyTurn && !carPicked){
                        Administer.getInstance().drawCard(PracticePlayer.this,true);
                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    };

    private Passive passive;

    public PracticePlayer(Player player){
        this.username=player.getUsername();
        this.deck=player.getCurrentDeck();
        this.heroName=deck.getHeroName();
        this.hero=Hero.getHero(deck.getHeroName());

        deckCards=new ArrayList<>();
        deckCards.addAll(deck.getDeckCards());
        handCards=new ArrayList<>();
        groundCards=new ArrayList<>();

        level=0;
        currentMana=0;
        pickCardThread.start();


    }
    public PracticePlayer(String user,int random){
        this.username=user;
        this.deck=new RandomDeck();
        this.heroName=deck.getHeroName();
        this.hero=Hero.getHero(deck.getHeroName());

        deckCards=new ArrayList<>();
        deckCards.addAll(deck.getDeckCards());
        handCards=new ArrayList<>();
        groundCards=new ArrayList<>();

        currentMana=0;
        level=0;
        pickCardThread.start();

    }
    public PracticePlayer(String user, boolean enemy, DeckReader deckReader){
        this.username=user;
        this.hero=Hero.getHero("Mage");
        this.heroName="Mage";
        if(enemy){
            deckCards=deckReader.getEnemyDeck();

        }else {
            deckCards=deckReader.getFriendDeck();
        }

        handCards=new ArrayList<>();
        groundCards=new ArrayList<>();
        currentMana=0;
        level=0;
        pickCardThread.start();
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getHeroName() {
        return heroName;
    }
    public Hero getHero() {
        return hero;
    }

    public Decks getCurrentDeck() {
        return deck;
    }
    public void addToDeck(Card card){
        deckCards.add(card);
    }
    public List<Card> getDeckCards() {
        return deckCards;
    }
    public void removeFromDeck(Card card){
        deckCards.remove(card);
    }

    public Card getRandomCard(){
        Random r=new Random();
        int i=r.nextInt(deckCards.size());
        return deckCards.get(i);
    }
    public void addToHand(Card card){
        handCards.add(card);
//        deckCards.remove(card);
        carPicked=true;
        needsUpdate=true;
    }

    public void playCard(Card card){
        if(card.getType().equalsIgnoreCase("Minion")){
            if(Administer.getInstance().canSummonMinion(this,(Minion)card)) {
                Administer.getInstance().summonMinion(this, (Minion) card);
                Administer.getInstance().removeFromHand(this,card);
                setMana(getMana() - card.getManaCost());
            }
            else{
                Administer.getInstance().setCantHappen(true);
            }
        }
        else if(card.getType().equalsIgnoreCase("Spell")){
            Administer.getInstance().useSpell(this,(Spell)card);
            Administer.getInstance().removeFromHand(this,card);
            if(this.getHero().getName().equalsIgnoreCase("Mage")) setMana(getMana() - (card.getManaCost()-2));
            else setMana(getMana() - card.getManaCost());
        }
        else if(card.getType().equalsIgnoreCase("Weapon")){
            Administer.getInstance().addWeapon((Weapon) card,this);
            Administer.getInstance().removeFromHand(this,card);
            setMana(getMana() - card.getManaCost());
        }
        else {
            //quest and reward
        }
    }
    public void removeFromHand(Card card){
        handCards.remove(card);
        changeNeedsUpdate(true);
    }
    public List<Card> getHandCards(){
        return handCards;
    }
    public int getdeckCnt(){
        return deckCards.size();
    }

    public int getMana(){
        return currentMana;
    }
    public void setMana(int mana){
        if(mana<10) this.currentMana=mana;
        else this.currentMana=10;
        changeNeedsUpdate(true);
    }

    public Weapon getWeapon(){
        return weapon;
    }
    public void addWeapon(Weapon weapon){
        this.weapon=weapon;
    }

    public void removeFromGround(Card card) {
        groundCards.remove(card);
    }
    public List<Card> getGroundCards(){
        return groundCards;
    }
    public void addToGroundCards(Card card){
        groundCards.add(card);
    }

    @Override
    public boolean isMyTurn(){
        return isMyTurn;
    }
    public void setTurn(boolean turn,boolean manaBurn){
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
            needsUpdate=true;
        }
        this.isMyTurn=turn;
    }

    @Override
    public void setPassive(Passive passive) {
        this.passive=passive;
        if(passive==Passive.MANAJUMP) level++;
    }
    public Passive getPassive(){
        return this.passive;
    }

    public boolean needsUpdate(){
        return needsUpdate;
    }
    public void changeNeedsUpdate(boolean b){
        this.needsUpdate=b;
    }


}
