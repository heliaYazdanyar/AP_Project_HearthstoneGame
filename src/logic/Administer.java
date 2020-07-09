package logic;

import Models.*;
import game.Out.GameView;
import gamePlayers.InGamePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Administer {

    private static Administer instance;
    private GameView gameView;

    private InGamePlayer friend;
    private List<Card> friend_handCard;
    private List<Card> friend_deck;
    public List<Minion> friend_CardsOnGround;
    private List<Minion> friend_RushCards;
    private List<Minion> friend_tauntMinions;
    private List<Minion> friend_permanentCause;
    private boolean friend_hasWeapon;
    private Weapon friend_weapon;
    private Hero friend_hero;
    private String friend_heroPower;
    private boolean friend_manaBurn=false;
    private boolean friend_manaJump=false;

    private InGamePlayer enemy;
    private List<Card> enemy_handCard;
    private List<Card> enemy_deck;
    public List<Minion> enemy_CardsOnGround;
    private List<Minion> enemy_RushCards;
    private List<Minion> enemy_tauntMinions;
    private List<Minion> enemy_permanentCause;
    private boolean enemy_hasWeapon;
    private Weapon enemy_weapon;
    private Hero enemy_hero;
    private String enemy_heroPower;
    private boolean enemy_manaBurn=false;
    private boolean enemy_manaJump=false;


    private InGamePlayer victimOwner;
    private MyCharacter victim;
    private boolean isVictimChosen=false;
    private InGamePlayer attackerOwner;
    private MyCharacter attacker;
    private boolean isAttackerChosen=false;
    private boolean attackerIsWeapon=false;
    private boolean attackerIsSpell=false;
    private boolean attackerIsHeroPower=false;
    private Spell attackerSpell;


    private boolean cantHappen=false;
    private String information="";
    private boolean HPloss=false;



    public Administer(InGamePlayer friend, InGamePlayer enemy, GameView gameView){
        this.friend=friend;
        this.enemy=enemy;
        this.gameView=gameView;

        initAll();

        friend_deck.addAll(friend.getDeckCards());
        enemy_deck.addAll(enemy.getDeckCards());
    }

    public void initAll(){
        friend_handCard=new ArrayList<>();
        friend_deck=new ArrayList<>();
        friend_CardsOnGround=new ArrayList<>();
        friend_RushCards=new ArrayList<>();
        friend_tauntMinions=new ArrayList<>();
        friend_permanentCause=new ArrayList<>();
        friend_hasWeapon=false;
        friend_hero=friend.getHero();
        friend_heroPower=friend.getHero().getHeroPower();


        enemy_handCard=new ArrayList<>();
        enemy_deck=new ArrayList<>();
        enemy_CardsOnGround=new ArrayList<>();
        enemy_RushCards=new ArrayList<>();
        enemy_tauntMinions=new ArrayList<>();
        enemy_permanentCause=new ArrayList<>();
        enemy_hasWeapon=false;
        enemy_hero=enemy.getHero();
        enemy_heroPower=enemy.getHero().getHeroPower();

    }

    //singleton
    public static void newGameAdminister(InGamePlayer p1, InGamePlayer p2, GameView gameView){
        instance=new Administer(p1,p2,gameView);

    }
    public static Administer getInstance(){
        return instance;
    }


    //information about states
    public boolean cantHappen(){
        return cantHappen;
    }
    public void setCantHappen(boolean b){
        this.cantHappen=b;
    }

    public void setInformation(String info){
        this.information=info;
    }
    public String getInformation(){ return this.information; }

    public boolean isHPloss(){
        return HPloss;
    }
    public void setHPloss(boolean b){
        this.HPloss=b;
    }

    //turn
    public void newTurn(int turn){
        friend_RushCards.removeAll(friend_RushCards);
        friend_RushCards.addAll(friend_CardsOnGround);


        enemy_RushCards.removeAll(enemy_RushCards);
        enemy_RushCards.addAll(enemy_CardsOnGround);


        for (Card card:
             friend_permanentCause) {
            if(card.getName().equalsIgnoreCase("ForestGuide")){
                friend.addToHand(friend.getRandomCard());
                enemy.addToHand(enemy.getRandomCard());
            }

            if(card.getName().equalsIgnoreCase("Dreadscale")){
                for (Minion minion:
                     enemy_CardsOnGround) {
                    dealDamageToMinion(1,enemy,minion,"");
                }
            }
        }
        for (Card card:
                enemy_permanentCause) {
            if(card.getName().equalsIgnoreCase("ForestGuide")){
                friend.addToHand(friend.getRandomCard());
                enemy.addToHand(enemy.getRandomCard());
            }

            if(card.getName().equalsIgnoreCase("Dreadscale")){
                for (Minion minion:
                        enemy_CardsOnGround) {
                    dealDamageToMinion(1,enemy,minion,"");
                }
            }
        }

        if(turn==0){
            drawCard(friend);
            friend.setTurn(true,friend_manaBurn,friend_manaJump);
            enemy.setTurn(false,enemy_manaBurn,enemy_manaJump);
            information="It's Your Turn";

            friend_manaBurn=false;
            enemy_manaBurn=false;
        }
        else{
            drawCard(enemy);
            enemy.setTurn(true,enemy_manaBurn,enemy_manaJump);
            friend.setTurn(false,friend_manaBurn,friend_manaJump);
            information="Enemy's Turn";

            friend_manaBurn=false;
            enemy_manaBurn=false;
        }
    }

    //card handling
    public void drawCard(InGamePlayer player){
        Random r=new Random();
        if(player.getUsername().equalsIgnoreCase(friend.getUsername())){
            int t=r.nextInt(friend_deck.size());
            if(friend_handCard.size()<12){
                //applying curiocollector
                for (Minion card:
                     friend_CardsOnGround) {
                    if(card.getName().equalsIgnoreCase("CurioCollector")) {
                        card.setHP(card.getHP()+1);
                        card.setAttack(card.getAttack()+1);
                    }

                }

                addToHand(friend,friend_deck.get(t),true);
            }
            else removeFromDeck(friend,friend_deck.get(t));


        }else{
            int t=r.nextInt(enemy_deck.size());
            if(enemy_handCard.size()<12){
                //applying curiocollector
                for (Minion card:
                        enemy_CardsOnGround) {
                    if(card.getName().equalsIgnoreCase("CurioCollector")) {
                        card.setHP(card.getHP()+1);
                        card.setAttack(card.getAttack()+1);
                    }

                }

                addToHand(enemy,enemy_deck.get(t),true);
            }
            else removeFromDeck(enemy,enemy_deck.get(t));

        }
    }
    public void addToHand(InGamePlayer forWho,Card card,boolean fromDeck){
        if(forWho.getUsername().equalsIgnoreCase(friend.getUsername())){
            friend.addToHand(card);
            friend_handCard.add(card);
            if(fromDeck)removeFromDeck(friend,card);
        }else{
            enemy.addToHand(card);
            enemy_handCard.add(card);
            if(fromDeck) removeFromDeck(enemy,card);
        }
    }
    public void addToDeck(InGamePlayer forWho,Card card){
        if(forWho.getUsername().equalsIgnoreCase(friend.getUsername())){
            friend.addToDeck(card);
            friend_deck.add(card);
        }else{
            enemy.addToDeck(card);
            enemy_deck.add(card);
        }
    }
    public void removeFromHand(InGamePlayer forWho,Card card){
        if(forWho.getUsername().equalsIgnoreCase(friend.getUsername())){
            friend_handCard.remove(card);
            friend.removeFromHand(card);
        }else{
            enemy_handCard.remove(card);
            enemy.removeFromHand(card);
        }
    }
    public void removeFromDeck(InGamePlayer forWho,Card card){
        if(forWho.getUsername().equalsIgnoreCase(friend.getUsername())){
            friend_deck.remove(card);
        }else{
            enemy_deck.remove(card);
        }

    }

    //handling one attack
    public void setVictimOwner(InGamePlayer victimOwner){
        System.out.println("victim owner setted: "+victimOwner.getUsername());
        this.victimOwner=victimOwner;
    }
    public InGamePlayer getVictimOwner(){
        return victimOwner;
    }
    public boolean isVictimChosen(){
        return isVictimChosen;
    }
    public void setVictim(MyCharacter character){
        this.victim=character;
        isVictimChosen=true;

        gameView.infoGiver.logAction("target is "+ victim.getName(),"","");

        if(canAttack()) {
            System.out.println("attack called");
            attack();
        }

        isAttackerChosen=false;
        isVictimChosen=false;
        attackerIsSpell=false;
        attackerIsWeapon=false;
        attackerIsHeroPower=false;

    }
    public void setIsVictimChosen(boolean b){
        isVictimChosen=b;
    }

    public boolean AttackerIsWeapon(){ return attackerIsWeapon; }
    public void setAttackerIsWeapon(boolean b){
        this.attackerIsWeapon=b;
    }


    public void setAttackerOwner(InGamePlayer attackerOwner){
        System.out.println("attacker owner setted: "+attackerOwner.getUsername());

        this.attackerOwner=attackerOwner;
    }
    public InGamePlayer getAttackerOwner(){
        return attackerOwner;
    }
    public boolean isAttackerChosen(){ return isAttackerChosen; }
    public void setAttacker(MyCharacter attacker){
        gameView.infoGiver.logAction("attacker is"+attacker.getName(),"","");

        this.attacker=attacker;
        isAttackerChosen=true;
    }
    public void setIsAttackerChosen(boolean b){
        this.isAttackerChosen=b;
    }


    //deal damage methods
    public void dealDamageRandom(InGamePlayer damageTo,int damage){
        Random r=new Random();
        if(damageTo.getUsername().equals(friend.getUsername())){
            int t=r.nextInt(friend_CardsOnGround.size()+1);
            if(t==friend_CardsOnGround.size()) dealDamageToHero(damage,friend);
            else dealDamageToMinion(damage,friend,friend_CardsOnGround.get(t),"");
        }
        //-B is attacking
        else {
            int t=r.nextInt(enemy_CardsOnGround.size()+1);
            if(t==enemy_CardsOnGround.size()) dealDamageToHero(damage,friend);
            else dealDamageToMinion(damage,enemy,enemy_CardsOnGround.get(t),"");
        }
    }
    public void dealDamageToHero(int damage,InGamePlayer victimPlayer){
        if(victimPlayer.getUsername().equalsIgnoreCase(friend.getUsername())){
            friend.getHero().setHP(friend.getHero().getHP()-damage);
            if(friend.getHero().getHP()<=0) lost(friend);
            friend_hero=friend.getHero();
        }
        else{
            enemy.getHero().setHP(enemy.getHero().getHP()-damage);
            if(enemy.getHero().getHP()<=0) lost(enemy);
            enemy_hero=enemy.getHero();
        }
    }
    public void dealDamageToMinion(int damage,InGamePlayer victimPlayer,Minion minion,String condition){
        if(victimPlayer.getUsername().equalsIgnoreCase(friend.getUsername())){
            minion.setHP(minion.getHP()-damage);
            setHPloss(true);
            if(minion.getHP()<=0) {
                removeKilledMinion(friend,minion,attacker);
                if(condition.equalsIgnoreCase("HolyWater")) addToHand(friend,minion,true);
            }

            //side conditions
            if(minion.getName().equalsIgnoreCase("SecurityRover")){
                summonMinion(friend,(Minion)Card.getCard("Mech"));
            }
        }else{
            minion.setHP(minion.getHP()-damage);
            setHPloss(true);
            if(minion.getHP()<=0){
                removeKilledMinion(enemy,minion,attacker);
                if(condition.equalsIgnoreCase("HolyWater")) addToHand(enemy,minion,true);
            }

            //side conditions
            if(minion.getName().equalsIgnoreCase("SecurityRover")){
                summonMinion(enemy,(Minion)Card.getCard("Mech"));
            }
        }
    }


    public boolean canAttack(){
        if(!attackerIsHeroPower && !attackerIsWeapon
                && attacker.getType().equalsIgnoreCase("spell") &&
                victim.getName().equalsIgnoreCase("BearShark")){
            setInformation("cant damage BearShark with spells");
            return false;
        }

        if(attackerIsWeapon){
            if(attackerOwner.getUsername().equalsIgnoreCase(friend.getUsername())){
                for (Minion minion:
                     enemy_CardsOnGround) {
                    if (enemy_tauntMinions.size() >= 1 && !enemy_tauntMinions.contains(victim)) {
                        this.information = "Taunt Way!";
                        return false;
                    }
                }
            }
            else {
                if (victim.getType().equalsIgnoreCase("Minion")) {
                    if (friend_tauntMinions.size() >= 1 && !friend_tauntMinions.contains(victim)) {
                        this.information = "Taunt In Way!";
                        return false;
                    }

                }
            }

        }

        else if(attackerIsHeroPower){

        }

        else {
            if (attackerOwner.getUsername().equalsIgnoreCase(friend.getUsername())) {
                if (attacker.getType().equalsIgnoreCase("Minion")) {
                    if (!friend_RushCards.contains(attacker)) {
                        setInformation("Not Rush");
                        return false;
                    }

                }
                if (victim.getType().equalsIgnoreCase("Minion")) {
                    if (enemy_tauntMinions.size() >= 1 && !enemy_tauntMinions.contains(victim)) {
                        this.information = "Taunt Way!";
                        return false;
                    }

                }
            } else {
                if (attacker.getType().equalsIgnoreCase("Minion")) {
                    if (!enemy_RushCards.contains(attacker)) {
                        this.information = "Not Rush";
                        return false;
                    }
                }
                if (victim.getType().equalsIgnoreCase("Minion")) {
                    if (friend_tauntMinions.size() >= 1 && !friend_tauntMinions.contains(victim)) {
                        this.information = "Taunt In Way!";
                        return false;
                    }

                }

            }
        }

        return true;
    }
    public void attack(){

        if(attackerIsWeapon){
            handleWeaponAttack();
            setHPloss(true);
        }
        else if(attackerIsHeroPower){
            if(attackerOwner.getUsername().equalsIgnoreCase(friend.getUsername())) {
                if(friend_heroPower.equalsIgnoreCase("FireBlast")) {
                    if (victim.getType().equalsIgnoreCase("Minion"))
                        dealDamageToMinion(1, victimOwner, (Minion) victim, "");
                    else
                        dealDamageToHero(1,victimOwner);
                }
            }
            else {
                if(enemy_heroPower.equalsIgnoreCase("FireBlast")) {
                    if (victim.getType().equalsIgnoreCase("Minion"))
                        dealDamageToMinion(1, victimOwner, (Minion) victim, "");
                    else
                        dealDamageToHero(1,victimOwner);
                }

            }
        }
        else if(attacker.getType().equalsIgnoreCase("Minion")){
            handleMinionsAttack();
            setHPloss(true);
        }

        else if (attackerIsSpell){
            spellAttack(attackerSpell);
            setHPloss(true);
        }

    }


    //tools
    public void discover(InGamePlayer forWho,Card caller){
        Random r=new Random();
        if(forWho.getUsername().equalsIgnoreCase(friend.getUsername())){
            if(caller.getType().equalsIgnoreCase("Spell")){

                if(caller.getName().equalsIgnoreCase("FriendlySmith")){
                    List<String> list=Card.getList("weapons");
                    Weapon weapon=(Weapon) Card.getCard(list.get(r.nextInt(3)));
                    weapon.setShield(weapon.getShield()+2);
                    weapon.setAttack(weapon.getAttack()+2);
                    addToDeck(friend,weapon);
                }

                if(caller.getName().equalsIgnoreCase("MarkedShot")){
                    List<String> list=Card.getList("weapons");
                    Spell spell=(Spell) Card.getCard(list.get(r.nextInt(14)));
                    addToDeck(friend,spell);
                }


            }


        }else{

        }
    }

    //heroPower methods
    public void callHeroPower(InGamePlayer player){
        Random r=new Random();
        if(player.getUsername().equalsIgnoreCase(friend.getUsername())){

            if(friend_hero.getName().equalsIgnoreCase("Mage")){
                attackerIsHeroPower=true;
                isAttackerChosen=true;
                attackerOwner=friend;
                setInformation("Choose target");
            }
            else if(friend_hero.getName().equalsIgnoreCase("rogue")){
                if(enemy_handCard.size()>0) {
                    int n = r.nextInt(enemy_handCard.size());
                    Card card=enemy_handCard.get(n);
                    removeFromHand(enemy,card);
                    addToHand(friend,card,false);
                    gameView.infoGiver.logAction("Card was stolen from","opponent's hand.","");

                }
                else
                    setInformation("Nothind to steal");

            }
            else if(friend_hero.getName().equalsIgnoreCase("warlock")){
                int n=r.nextInt(2);
                if(n%2==0 && friend_CardsOnGround.size()>0 ){
                    friend.getHero().setHP(friend.getHero().getHP() - 2);
                    friend_hero = friend.getHero();
                    int t=r.nextInt(friend_CardsOnGround.size());
                    friend_CardsOnGround.get(t).setAttack(friend_CardsOnGround.get(t).getAttack()+1);
                    friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getHP()+1);

                }else{
                    if(enemy_CardsOnGround.size()>0) {
                        friend.getHero().setHP(friend.getHero().getHP() - 2);
                        friend_hero = friend.getHero();
                        int t=r.nextInt(enemy_CardsOnGround.size());
                        enemy_CardsOnGround.get(t).setHP(0);
                        setHPloss(true);
                        addToHand(friend,enemy_CardsOnGround.get(t),false);
                        removeKilledMinion(enemy,enemy_CardsOnGround.get(t),friend_hero);
                    }
                    else setInformation("No minion to lifetab");

                }
            }
            else if(friend_hero.getName().equalsIgnoreCase("Hunter")){}
            else{
                if(friend_CardsOnGround.size()>0) {
                    int n = r.nextInt(friend_CardsOnGround.size());
                    friend_CardsOnGround.get(n).setHP(friend_CardsOnGround.get(n).getHP() + 4);
                    gameView.infoGiver.logAction("Heal affected:", friend_CardsOnGround.get(n).getName(), " +4 HP");
                }
            }

        }
        else{

            if(enemy_hero.getName().equalsIgnoreCase("Mage")){
                attackerIsHeroPower=true;
                isAttackerChosen=true;
                attackerOwner=enemy;
                setInformation("Choose target");
            }
            else if(enemy_hero.getName().equalsIgnoreCase("rogue")){
                if(friend_handCard.size()>0) {
                    int n = r.nextInt(friend_handCard.size());
                    Card card=friend_handCard.get(n);
                    removeFromHand(friend,card);
                    addToHand(enemy,card,false);
                    gameView.infoGiver.logAction("Card was stolen from","opponent's hand.","");

                }
                else
                    setInformation("Nothind to steal");
            }
            else if(enemy_hero.getName().equalsIgnoreCase("warlock")){
                int n=r.nextInt(2);
                if(n%2==0 && enemy_CardsOnGround.size()>0 ){
                    enemy.getHero().setHP(enemy.getHero().getHP() - 2);
                    enemy_hero = enemy.getHero();
                    int t=r.nextInt(enemy_CardsOnGround.size());
                    enemy_CardsOnGround.get(t).setAttack(enemy_CardsOnGround.get(t).getAttack()+1);
                    enemy_CardsOnGround.get(t).setHP(enemy_CardsOnGround.get(t).getHP()+1);

                }else{
                    if(friend_CardsOnGround.size()>0) {
                        enemy.getHero().setHP(enemy.getHero().getHP() - 2);
                        enemy_hero = enemy.getHero();
                        int t=r.nextInt(friend_CardsOnGround.size());
                        friend_CardsOnGround.get(t).setHP(0);
                        setHPloss(true);
                        addToHand(enemy,friend_CardsOnGround.get(t),false);
                        removeKilledMinion(friend,friend_CardsOnGround.get(t),enemy_hero);
                    }
                    else setInformation("No minion to lifetab");

                }
            }
            else if(friend_hero.getName().equalsIgnoreCase("Hunter")){}
            else{
                if(enemy_CardsOnGround.size()>0) {
                    int n = r.nextInt(enemy_CardsOnGround.size());
                    enemy_CardsOnGround.get(n).setHP(enemy_CardsOnGround.get(n).getHP() + 4);
                    gameView.infoGiver.logAction("Heal affected:", enemy_CardsOnGround.get(n).getName(), " +4 HP");
                }
            }

        }

    }

    //minion methods
    public boolean canSummonMinion(InGamePlayer player,Minion minion){
        if(player.getUsername().equals(friend.getUsername())) {
            if(friend_CardsOnGround.size()>=7) return false;
        }
        else {
            if(enemy_CardsOnGround.size()>=7) return false;

        }
        return true;
    }
    public void summonMinion(InGamePlayer player,Minion minion){
        gameView.infoGiver.logAction(player.getUsername()+" summon:",minion.getName(),"");

        if(player.getUsername().equals(friend.getUsername())){
            //check tasiire baghie ro jadid
            for (Card card:
                    friend_permanentCause) {
                if(card.getName().equalsIgnoreCase("HighPriestAmet")){
                    minion.setHP(((Minion)card).getHP());
                    gameView.infoGiver.logAction("HighPriestAmet changed ","new minion's HP","");
                }
            }

            //check the new minion
            if(minion.isTaunt()){
                friend_tauntMinions.add(minion);
            }

            if(minion.isBattlecry()){
                Random r=new Random();
                if(minion.getName().equalsIgnoreCase("BewitchedGuardian")){
                    //gain +1 hp for each card in your hand
                    minion.setHP(minion.getHP()+friend_CardsOnGround.size());
                    gameView.infoGiver.logAction("BewitchedGaurdian increased","its HP","");

                }

                else  if(minion.getName().equalsIgnoreCase("BlinkFox")){
                    Card card=Card.getCard(enemy_deck.get(r.nextInt(enemy_deck.size())).getName());
                    addToHand(friend,card,false);
                    gameView.infoGiver.logAction("BlinkFox copied","one card from","oponent'd Deck");

                }

                else if(minion.getName().equalsIgnoreCase("KabalTalonpriest")){
                    if(friend_CardsOnGround.size()>0) {
                        int t = r.nextInt(friend_CardsOnGround.size());
                        friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getHP() + 3);
                    }
                }

                else if(minion.getName().equalsIgnoreCase("RockpoolHunter")){
                    if(friend_CardsOnGround.size()>0) {
                        int t = r.nextInt(friend_CardsOnGround.size());
                        friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getHP() + 1);
                        friend_CardsOnGround.get(t).setAttack(friend_CardsOnGround.get(t).getAttack() + 1);
                    }

                }

                else if(minion.getName().equalsIgnoreCase("Squashling")){
                    int t=r.nextInt(friend_CardsOnGround.size());
                    friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getHP()+2);
                }

                else if(minion.getName().equalsIgnoreCase("TombWarden")){
                    summonMinion(friend,(Minion)Card.getCard("TombWarden"));
                }




            }

            if(minion.isRush() || friend_hero.getName().equalsIgnoreCase("Hunter")){
                System.out.println("Rush minion added:"+minion.getName());
                friend_RushCards.add(minion);
            }

            if(minion.isLifeSteal()){

            }

            if(minion.isPermanentCause()){
                friend_permanentCause.add(minion);
            }
            if(minion.getName().equalsIgnoreCase("HoundMasterShaw")){
                friend_RushCards.removeAll(friend_RushCards);
                friend_RushCards.addAll(friend_CardsOnGround);
            }
            friend_CardsOnGround.add(minion);
            System.out.println("added to cards On Ground: "+ minion.getName());
            if(!friend.getGroundCards().contains(minion)) friend.addToGroundCards(minion);
        }
        else{
            //check tasiire baghie ro jadid
            for (Card card:
                    enemy_permanentCause) {
                if(card.getName().equalsIgnoreCase("HighPriestAmet")){
                    minion.setHP(((Minion)card).getHP());
                }
            }

            //check the new minion
            if(minion.isTaunt()){
                enemy_tauntMinions.add(minion);
            }

            if(minion.isBattlecry()){
                Random r=new Random();
                if(minion.getName().equalsIgnoreCase("BewitchedGuardian")){
                    //gain +1 hp for each card in your hand
                    minion.setHP(minion.getHP()+enemy_CardsOnGround.size());
                }

                else  if(minion.getName().equalsIgnoreCase("BlinkFox")){
                    Card card=Card.getCard(friend_deck.get(r.nextInt(friend_deck.size())).getName());
                    addToHand(enemy,card,false);
                }

                else if(minion.getName().equalsIgnoreCase("KabalTalonpriest")){
                    if(enemy_CardsOnGround.size()>0) {
                        int t = r.nextInt(enemy_CardsOnGround.size());
                        enemy_CardsOnGround.get(t).setHP(enemy_CardsOnGround.get(t).getHP() + 3);
                    }
                }

                else if(minion.getName().equalsIgnoreCase("RockpoolHunter")){
                    if(enemy_CardsOnGround.size()>0) {
                        int t = r.nextInt(enemy_CardsOnGround.size());
                        enemy_CardsOnGround.get(t).setHP(enemy_CardsOnGround.get(t).getHP() + 1);
                        enemy_CardsOnGround.get(t).setAttack(enemy_CardsOnGround.get(t).getAttack() + 1);
                    }

                }

                else if(minion.getName().equalsIgnoreCase("Squashling")){
                    int t=r.nextInt(enemy_CardsOnGround.size());
                    enemy_CardsOnGround.get(t).setHP(enemy_CardsOnGround.get(t).getHP()+2);
                }

                else if(minion.getName().equalsIgnoreCase("TombWarden")){
                    summonMinion(enemy,(Minion)Card.getCard("TombWarden"));
                }

            }

            if(minion.isRush() || friend_hero.getName().equalsIgnoreCase("Hunter")){
                System.out.println("Rush minion added:"+minion.getName());
                enemy_RushCards.add(minion);
            }

            if(minion.isLifeSteal()){

            }

            if(minion.isPermanentCause()){
                enemy_permanentCause.add(minion);
            }
            if(minion.getName().equalsIgnoreCase("HoundMasterShaw")){
                enemy_RushCards.removeAll(enemy_RushCards);
                enemy_RushCards.addAll(enemy_CardsOnGround);
            }
            enemy_CardsOnGround.add(minion);
            System.out.println("added to cards On Ground: "+ minion.getName());
            if(!enemy.getGroundCards().contains(minion)) enemy.addToGroundCards(minion);

        }
    }
    public void handleMinionsAttack(){
        //-A is attacking
        if(attackerOwner.getUsername().equals(friend.getUsername())){
            if(victim.getType().equalsIgnoreCase("Minion")){
               if(enemy_tauntMinions.contains(victim)){
                   enemy_tauntMinions.remove(victim);
                   ((Minion)victim).setTaunt(false);
                   information="taunt down";
                   attacker.setHP(attacker.getHP()-victim.getAttack());
                   HPloss=true;
                   if(attacker.getHP()<=0) removeKilledMinion(attackerOwner,(Minion) attacker,victim);
               }
               else{
                   dealDamageToMinion(attacker.getAttack(),enemy,(Minion)victim,"");
                   dealDamageToMinion(victim.getAttack(),friend,(Minion)attacker,"");

                   information="attacked";
                   HPloss=true;
               }
           }
           else{
               dealDamageToHero(attacker.getAttack(),enemy);
           }
           if(attacker.getName().equalsIgnoreCase("WickerflameBurnbristle")){
               friend_hero.setHP(friend_hero.getHP()+attacker.getAttack());
           }

            for (Minion card:
                    enemy_permanentCause) {
                if(card.getName().equalsIgnoreCase("SwampKingDred")){
                    setVictimOwner(friend);
                    setAttackerOwner(enemy);
                    setAttacker(card);
                    setVictim(attacker);
                }
            }
            if(enemy_heroPower.equalsIgnoreCase("Caltropes")){
                dealDamageToMinion(1,friend,(Minion) attacker,"");
                gameView.infoGiver.logAction("Caltropes was activated.","Attacked:"+attacker.getName(),"");
            }
        }
        //-B is attacking
        else {
            if(victim.getType().equalsIgnoreCase("Minion")){
                if(friend_tauntMinions.contains(victim)){
                    friend_tauntMinions.remove(victim);
                    ((Minion)victim).setTaunt(false);

                    information="taunt down";
                    attacker.setHP(attacker.getHP()-victim.getAttack());
                    HPloss=true;
                    if(attacker.getHP()<=0) removeKilledMinion(attackerOwner,(Minion) attacker,victim);
                }
                else{
                    dealDamageToMinion(attacker.getAttack(),friend,(Minion)victim,"");
                    dealDamageToMinion(victim.getAttack(),enemy,(Minion)attacker,"");

                    information="attacked";
                    HPloss=true;
                }
            }
            else{
                dealDamageToHero(attacker.getAttack(),friend);
            }
            if(attacker.getName().equalsIgnoreCase("WickerflameBurnbristle")){
                enemy_hero.setHP(enemy_hero.getHP()+attacker.getAttack());
            }

            for (Minion card:
                    friend_permanentCause) {
                if(card.getName().equalsIgnoreCase("SwampKingDred")){
                    setVictimOwner(enemy);
                    setAttackerOwner(friend);
                    setAttacker(card);
                    setVictim(attacker);
                }
            }
            if(friend_heroPower.equalsIgnoreCase("Caltropes")){
                dealDamageToMinion(1,enemy,(Minion) attacker,"");
                gameView.infoGiver.logAction("Caltropes was activated.","Attacked:"+attacker.getName(),"");
            }

        }
    }
    public void removeKilledMinion(InGamePlayer minionOwner,Minion minion,MyCharacter killer){
        if(minionOwner.getUsername().equalsIgnoreCase(friend.getUsername())){
            if(minion.isPermanentCause()) friend_permanentCause.remove(minion);
            if(minion.isTaunt()) friend_tauntMinions.remove(minion);
            if(friend_RushCards.contains(minion)) friend_RushCards.remove(minion);
            friend_CardsOnGround.remove(minion);
            friend.removeFromGround(minion);

            for (Card card:
                 friend_permanentCause) {
                if(card.getName().equalsIgnoreCase("WrathscaleNaga")){
                    dealDamageRandom(enemy,3);
                }
            }
        }else{
            if(minion.isPermanentCause()) enemy_permanentCause.remove(minion);
            if(((Minion)victim).isTaunt()) enemy_tauntMinions.remove(minion);
            if(enemy_RushCards.contains(minion)) enemy_RushCards.remove(minion);
            enemy_CardsOnGround.remove(minion);
            enemy.removeFromGround(minion);

            for (Card card:
                    enemy_permanentCause) {
                if(card.getName().equalsIgnoreCase("WrathscaleNaga")){
                    dealDamageRandom(friend,3);
                }
            }
        }
    }


    //weapon methods
    public boolean hasWeapon(InGamePlayer player){
        if(player.getUsername().equalsIgnoreCase(friend.getUsername())){
            return friend_hasWeapon;
        }else{
            return enemy_hasWeapon;
        }
    }
    public void addWeapon(Weapon weapon,InGamePlayer player){
        if(player.getUsername().equals(friend.getUsername())){
            friend.addWeapon(weapon);
            friend_hasWeapon=true;
            friend_weapon=weapon;
        }
        else{
            enemy.addWeapon(weapon);
            enemy_hasWeapon=true;
            enemy_weapon=weapon;
        }

    }
    public void handleWeaponAttack(){
        //-A is attacking
        if(attackerOwner.getUsername().equals(friend.getUsername())){
            weaponAttack(friend_weapon.getAttack(),enemy);
            if (friend_weapon.getShield() == 0) {
                removeWeapon(friend);
                friend_hasWeapon = false;
            }
        }
        //-B is attacking
        else {
            weaponAttack(enemy_weapon.getAttack(),friend);
            if (enemy_weapon.getShield() == 0) {
                removeWeapon(enemy);
                enemy_hasWeapon = false;
            }

        }
    }
    public void weaponAttack(int k,InGamePlayer victimOwner){
        if(victimOwner.getUsername().equalsIgnoreCase(friend.getUsername())){
            enemy_weapon.setShield(enemy_weapon.getShield()-1);
            if(enemy_weapon.getShield()<=0) removeWeapon(enemy);

        }
        else {
            friend_weapon.setShield(friend_weapon.getShield()-1);
            if(friend_weapon.getShield()<=0) removeWeapon(friend);
        }

        if(victim.getType().equalsIgnoreCase("Minion")){
            dealDamageToMinion(k,victimOwner,(Minion) victim,"");
        }
        else{
            dealDamageToHero(k,victimOwner);
        }

    }
    public void removeWeapon(InGamePlayer player){
        //-A
        if(player.getUsername().equals(friend.getUsername())){
            friend_hasWeapon=false;
            friend_weapon=null;
        }
        //-B
        else {
            enemy_weapon=null;
            enemy_hasWeapon=false;
        }
    }

    //spell methods
    public void useSpell(InGamePlayer player,Spell spell) {
        if(player.getUsername().equals(friend.getUsername())){
            if(spell.dealsDamage()){
                if(spell.getName().equalsIgnoreCase("HolyWater")){
                    Random r=new Random();
                    int t=r.nextInt(enemy_CardsOnGround.size());
                    dealDamageToMinion(4,friend,enemy_CardsOnGround.get(t),"HolyWater");
                }

                else if(spell.getName().equalsIgnoreCase("MarkedShot")){
                    Random r=new Random();
                    int t=r.nextInt(enemy_CardsOnGround.size());
                    dealDamageToMinion(4,friend,enemy_CardsOnGround.get(t),"");
                }

                else if(spell.getName().equalsIgnoreCase("Swipe")){
                    for (Minion minion:
                         enemy_CardsOnGround) {
                        dealDamageToMinion(1,enemy,minion,"");
                    }
                    dealDamageToHero(4,enemy);
                }



            }

            if(spell.changesMinion()){
                if(spell.getName().equalsIgnoreCase("Polymorph")){
                    isAttackerChosen=true;
                    attackerIsSpell=true;
                    attackerOwner=friend;
                    attackerSpell=spell;
                    setInformation("Choose Target");
                }

            }

            if(spell.isDiscover()){
                discover(friend,spell);
            }

            if(spell.isDivineShield()){

            }

            if(spell.summonCards()){
                if(spell.getName().equalsIgnoreCase("SwarmOfLocusts")) {
                    for (int cnt = 0; cnt < 7; cnt++)
                        summonMinion(friend, (Minion) Card.getCard("Locust"));
                }

                else if(spell.getName().equalsIgnoreCase("WisperingWoods")){
                    for (Card card:
                         friend_handCard) {
                    if(canSummonMinion(friend,(Minion)Card.getCard("Wisp")))
                    summonMinion(friend,(Minion)Card.getCard("Wisp"));
                    }
                }

            }

            if(spell.isManaBurn()){
                enemy_manaBurn=true;
            }

            if (spell.isDrawCard()){
                if (spell.getName().equalsIgnoreCase("BookOfSpecters")){
                    drawCard(friend); drawCard(friend); drawCard(friend);
                }

                else if(spell.getName().equalsIgnoreCase("FeastOfSouls")){
                    for (Minion minion :
                            friend_CardsOnGround) {
                        drawCard(friend);
                    }
                }

                else if(spell.getName().equalsIgnoreCase("witchwoodApple")){
                    addToHand(friend,Card.getCard("Treant"),false);
                    addToHand(friend,Card.getCard("Treant"),false);
                    addToHand(friend,Card.getCard("Treant"),false);
                }

                else if(spell.getName().equalsIgnoreCase("Sprint")){
                    drawCard(friend); drawCard(friend); drawCard(friend); drawCard(friend);
                }


            }

            if(spell.getName().equalsIgnoreCase("Thoughtsteal")){
                Random r=new Random();
                Card card=Card.getCard(enemy_deck.get(r.nextInt(enemy_deck.size())).getName());
                addToHand(friend,card,false);
                Card card2=Card.getCard(enemy_deck.get(r.nextInt(enemy_deck.size())).getName());
                addToHand(friend,card2,false);
            }

        }else {
            if(spell.dealsDamage()){
                if(spell.getName().equalsIgnoreCase("HolyWater")){
                    Random r=new Random();
                    int t=r.nextInt(friend_CardsOnGround.size());
                    dealDamageToMinion(4,enemy,friend_CardsOnGround.get(t),"HolyWater");
                }

                else if(spell.getName().equalsIgnoreCase("MarkedShot")){
                    Random r=new Random();
                    int t=r.nextInt(friend_CardsOnGround.size());
                    dealDamageToMinion(4,enemy,friend_CardsOnGround.get(t),"");
                }

                else if(spell.getName().equalsIgnoreCase("Swipe")){
                    for (Minion minion:
                            friend_CardsOnGround) {
                        dealDamageToMinion(1,friend,minion,"");
                    }
                    dealDamageToHero(4,friend);
                }

            }

            if(spell.changesMinion()){
                if(spell.getName().equalsIgnoreCase("Polymorph")){
                    isAttackerChosen=true;
                    attackerIsSpell=true;
                    attackerOwner=enemy;
                    attackerSpell=spell;
                    setInformation("Choose Target");
                }

            }

            if(spell.isDiscover()){
                discover(enemy,spell);
            }

            if(spell.isDivineShield()){

            }

            if(spell.summonCards()){
                if(spell.getName().equalsIgnoreCase("SwarmOfLocusts")) {
                    for (int cnt = 0; cnt < 7; cnt++)
                        summonMinion(friend, (Minion) Card.getCard("Locust"));
                }

                else if(spell.getName().equalsIgnoreCase("WisperingWoods")){
                    for (Card card:
                            enemy_handCard) {
                        if(canSummonMinion(enemy,(Minion)Card.getCard("Wisp")))
                            summonMinion(enemy,(Minion)Card.getCard("Wisp"));
                    }
                }

            }

            if(spell.isManaBurn()){
                friend_manaBurn=true;
            }

            if (spell.isDrawCard()){
                if (spell.getName().equalsIgnoreCase("BookOfSpecters")){
                    drawCard(enemy); drawCard(enemy); drawCard(enemy);
                }

                else if(spell.getName().equalsIgnoreCase("FeastOfSouls")){
                    for (Minion minion :
                            enemy_CardsOnGround) {
                        drawCard(enemy);
                    }
                }

                else if(spell.getName().equalsIgnoreCase("witchwoodApple")){
                    addToHand(enemy,Card.getCard("Treant"),false);
                    addToHand(enemy,Card.getCard("Treant"),false);
                    addToHand(enemy,Card.getCard("Treant"),false);
                }

                else if(spell.getName().equalsIgnoreCase("Sprint")){
                    drawCard(enemy); drawCard(enemy); drawCard(enemy); drawCard(enemy);
                }


            }

            if(spell.getName().equalsIgnoreCase("Thoughtsteal")){
                Random r=new Random();
                Card card=Card.getCard(friend_deck.get(r.nextInt(friend_deck.size())).getName());
                addToHand(enemy,card,false);
                Card card2=Card.getCard(friend_deck.get(r.nextInt(friend_deck.size())).getName());
                addToHand(enemy,card2,false);
            }

        }
    }
    public void spellAttack(Spell spell){
        if(attackerOwner.getUsername().equalsIgnoreCase(friend.getUsername())){
            if (spell.getName().equalsIgnoreCase("Polymorph")){
                if(victim.getType().equalsIgnoreCase("Minion")) {
                    enemy_CardsOnGround.remove(victim);
                    Minion sheep=(Minion) Card.getCard("Sheep");
                    enemy.addToGroundCards(sheep);
                    enemy_CardsOnGround.add(sheep);
                }
                else{
                    cantHappen=true;
                }
            }


            attackerIsSpell=false;
        }else{
            if (spell.getName().equalsIgnoreCase("Polymorph")){
                if(victim.getType().equalsIgnoreCase("Minion")) {
                    friend_CardsOnGround.remove(victim);
                    Minion sheep=(Minion) Card.getCard("Sheep");
                    friend.addToGroundCards(sheep);
                    friend_CardsOnGround.add(sheep);
                }
                else{
                    cantHappen=true;
                }
            }

            attackerIsSpell=false;
        }

    }

    public void lost(InGamePlayer loser){

    }

}
