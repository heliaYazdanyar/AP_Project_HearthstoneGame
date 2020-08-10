//package logic;
//
//import Models.*;
//import Out.MainFrame;
//import client.GameClient;
//import game.Out.GameView;
//import gamePlayers.InGamePlayer;
//import gamePlayers.OnlineEnemy;
//import messages.GameMessage;
//
//import javax.swing.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class ClientAdminister implements Admin {
//
//    private GameClient client;
//    private GameView gameView;
//
//    private boolean deckReader=false;
//
//    private InGamePlayer friend;
//
//    private List<Card> friend_handCard;
//    private List<Card> friend_deck;
//    public List<Minion> friend_CardsOnGround;
//    private List<Minion> friend_RushCards;
//    private List<Minion> friend_tauntMinions;
//    private List<Minion> friend_permanentCause;
//    private boolean friend_hasWeapon;
//    private Weapon friend_weapon;
//    private Hero friend_hero;
//    private HeroPower friend_heroPower;
//    private boolean friend_manaBurn=false;
//    private boolean friend_manaJump=false;
//
//    private List<Minion> onceUsed;
//    private int numberUseOfHeroPower=0;
//    private boolean tombwarden=false;
//
//    private OnlineEnemy enemy;
//
//    private List<Card> enemy_handCard;
//    private List<Card> enemy_deck;
//    public List<Minion> enemy_CardsOnGround;
//    private List<Minion> enemy_RushCards;
//    private List<Minion> enemy_tauntMinions;
//    private List<Minion> enemy_permanentCause;
//    private boolean enemy_hasWeapon;
//    private Weapon enemy_weapon;
//    private Hero enemy_hero;
//    private HeroPower enemy_heroPower;
//    private boolean enemy_manaBurn=false;
//    private boolean enemy_manaJump=false;
//
//
//    private InGamePlayer victimOwner;
//    private MyCharacter victim;
//    private boolean isVictimChosen=false;
//    private InGamePlayer attackerOwner;
//    private MyCharacter attacker;
//    private boolean isAttackerChosen=false;
//    private boolean attackerIsWeapon=false;
//    private boolean attackerIsSpell=false;
//    private boolean attackerIsHeroPower=false;
//    private Spell attackerSpell;
//
//
//    private boolean cantHappen=false;
//    private String information="";
//    private boolean HPloss=false;
//
//    public ClientAdminister(GameClient client, OnlineEnemy enemy, GameView gameView){
//        this.client=client;
//        this.gameView=gameView;
//        this.enemy=enemy;
//
//        this.friend=client.getPracticePlayer();
//        friend_deck.addAll(friend.getDeckCards());
//
//        initAll();
//    }
//
//    public void initAll(){
//        friend_handCard=new ArrayList<>();
//        friend_deck=new ArrayList<>();
//        friend_CardsOnGround=new ArrayList<>();
//        friend_RushCards=new ArrayList<>();
//        friend_tauntMinions=new ArrayList<>();
//        friend_permanentCause=new ArrayList<>();
//        friend_hasWeapon=false;
//        friend_hero=friend.getHero();
//        friend_heroPower=friend.getHero().getHeroPower();
//
//        onceUsed=new ArrayList<>();
//
//
//        enemy_CardsOnGround=new ArrayList<>();
//        enemy_RushCards=new ArrayList<>();
//        enemy_tauntMinions=new ArrayList<>();
//        enemy_permanentCause=new ArrayList<>();
//        enemy_hasWeapon=false;
//        enemy_hero=enemy.getHero();
//        enemy_heroPower=enemy.getHero().getHeroPower();
//
//    }
//
//
//    public void setInformation(String info){
//        this.information=info;
//    }
//    public String getInformation(){ return this.information; }
//
//    public boolean isHPloss(){
//        return HPloss;
//    }
//    public void setHPloss(boolean b){
//        this.HPloss=b;
//    }
//
//    //turn
//    public void updateLists(){
//
//    }
//
//
//    public void newTurn(int turn){
//        //refresh
//        if(friend.getPassive()==Passive.MANAJUMP) friend_manaJump=true;
//
//        friend_RushCards.removeAll(friend_RushCards);
//        friend_RushCards.addAll(friend_CardsOnGround);
//
//
//        enemy_RushCards.removeAll(enemy_RushCards);
//        enemy_RushCards.addAll(enemy_CardsOnGround);
//
//        numberUseOfHeroPower=0;
//        onceUsed.removeAll(onceUsed);
//
//        if(friend_handCard.size()==0 && friend.getDeckCards().size()==0) lost(friend);
////        if(enemy_handCard.size()==0 && enemy.getDeckCards().size()==0) lost(enemy);
//
//
//        for (Card card:
//                friend_permanentCause) {
//            if(card.getName().equalsIgnoreCase("ForestGuide")){
//                friend.addToHand(friend.getRandomCard());
//                enemy.addToHand(enemy.getRandomCard());
//                gameView.infoGiver.logAction("Forest Guide was  active:","added 1 card to each","players hand");
//
//            }
//
//            if(card.getName().equalsIgnoreCase("Dreadscale")){
//                if(enemy_CardsOnGround.size()>0)
//                    for (Minion minion:
//                            enemy_CardsOnGround) {
//                        dealDamageToMinion(1,enemy,minion,"");
//                        gameView.infoGiver.logAction("DredScale active:","deal 1 damage to each","enemy minion");
//
//                    }
//            }
//        }
//        for (Card card:
//                enemy_permanentCause) {
//            if(card.getName().equalsIgnoreCase("ForestGuide")){
//                friend.addToHand(friend.getRandomCard());
//                enemy.addToHand(enemy.getRandomCard());
//                gameView.infoGiver.logAction("Forest Guide was  active:","added 1 card to each","players hand");
//
//            }
//
//            if(card.getName().equalsIgnoreCase("Dreadscale")){
//                if(friend_CardsOnGround.size()>0)
//                    for (Minion minion:
//                            friend_CardsOnGround) {
//                        dealDamageToMinion(1,friend,minion,"");
//                        gameView.infoGiver.logAction("DredScale active:","deal 1 damage to each","friend minion");
//
//                    }
//            }
//        }
//
//        if(turn==0){
//            //applying passives
//            if(friend.getPassive()==Passive.TWICEDRAW){
//                gameView.infoGiver.logAction("draw 2 Cards","because of passive-TwiceDraw","");
//                drawCard(friend,true);
//            }
//            if(enemy.getPassive()==Passive.NURSE){
//                gameView.infoGiver.logAction("passive Nurse","applied","");
//
//                Random r=new Random();
//                int t=r.nextInt(enemy_CardsOnGround.size());
//                enemy_CardsOnGround.get(t).setHP(enemy_CardsOnGround.get(t).getFinalHP());
//            }
//
//            drawCard(friend,true);
//            friend.setTurn(true,friend_manaBurn);
//            enemy.setTurn(false,enemy_manaBurn);
//            information="It's Your Turn";
//
//            friend_manaBurn=false;
//            enemy_manaBurn=false;
//        }
//        else{
//            //applying passives
//            if(enemy.getPassive()==Passive.TWICEDRAW){
//                gameView.infoGiver.logAction("draw 2 Cards","because of passive-TwiceDraw","");
//                drawCard(enemy,true);
//            }
//            if(friend.getPassive()==Passive.NURSE){
//                gameView.infoGiver.logAction("passive Nurse","applied","");
//
//                Random r=new Random();
//                int t=r.nextInt(friend_CardsOnGround.size());
//                friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getFinalHP());
//            }
//
//            drawCard(enemy,true);
//            enemy.setTurn(true,enemy_manaBurn);
//            friend.setTurn(false,friend_manaBurn);
//            information="Enemy's Turn";
//
//            friend_manaBurn=false;
//            enemy_manaBurn=false;
//        }
//    }
//
//
//
//    //card handling
//    public void drawCard(boolean fromDeck){
//         if(friend.getDeckCards().size()>0 && fromDeck){
//             gameView.infoGiver.logAction("No Card in","your deck","");
//             if(friend_handCard.size()<=0) lost(friend);
//         }else{
//                //tartib
//                int t = 0;
//                if (deckReader && fromDeck) t = 0;
//                else {
//                    Random r=new Random();
//                    t = r.nextInt(friend_deck.size());
//                }
//
//                if (friend_handCard.size() < 12) {
//                    //applying curiocollector
//                    for (Minion card :
//                            friend_CardsOnGround) {
//                        if (card.getName().equalsIgnoreCase("CurioCollector")) {
//                            card.setHP(card.getHP() + 1);
//                            card.setAttack(card.getAttack() + 1);
//                        }
//
//                    }
//
//                    addToHand(friend_deck.get(t), fromDeck);
//                } else {
//                    gameView.infoGiver.logAction("Hand is Full", "", "");
//                    if (fromDeck) removeFromDeck(friend_deck.get(t));
//                }
//
//         }
//
//    }
//    public void addToHand(Card card,boolean fromDeck){
//        friend.addToHand(card);
//        friend_handCard.add(card);
//        if(fromDeck)removeFromDeck(card);
//    }
//    public void addToDeck(Card card){
//        friend.addToDeck(card);
//        friend_deck.add(card);
//    }
//    public void removeFromHand(Card card){
//        friend_handCard.remove(card);
//        friend.removeFromHand(card);
//    }
//    public void removeFromDeck(Card card){
//        friend.removeFromDeck(card);
//        friend_deck.remove(card);
//    }
//
//    //handling one attack
//    public boolean isVictimChosen(){
//        return isVictimChosen;
//    }
//    public void setVictim(MyCharacter character){
//        this.victim=character;
//        isVictimChosen=true;
//
//        gameView.infoGiver.logAction("target is "+ victim.getName(),"","");
//
//        if(canAttack()) {
//            attack();
//        }
//
//        isAttackerChosen=false;
//        isVictimChosen=false;
//        attackerIsSpell=false;
//        attackerIsWeapon=false;
//        attackerIsHeroPower=false;
//    }
//    public void setIsVictimChosen(boolean b){
//        isVictimChosen=b;
//    }
//    @Override
//    public void setVictimOwner(InGamePlayer victimOwner) {
//
//    }
//    public InGamePlayer getVictimOwner() {
//        return null;
//    }
//
//    public boolean attackerIsWeapon(){
//        return attackerIsWeapon;
//    }
//    public void setAttackerIsWeapon(boolean b){
//        this.attackerIsWeapon=b;
//    }
//    @Override
//    public void setAttackerOwner(InGamePlayer attackerOwner) {
//
//    }
//    public boolean AttackerIsWeapon() {
//        return false;
//    }
//    public InGamePlayer getAttackerOwner() {
//        return null;
//    }
//
//    public boolean isAttackerChosen(){
//        return isAttackerChosen;
//    }
//    public void setAttacker(MyCharacter attacker){
//        gameView.infoGiver.logAction("attacker is"+attacker.getName(),"","");
//
//        this.attacker=attacker;
//        isAttackerChosen=true;
//    }
//    public void setIsAttackerChosen(boolean b){
//        this.isAttackerChosen=b;
//    }
//
//
//    //deal damage methods
//    public void dealDamageRandom(InGamePlayer damageTo,int damage){
//        Random r=new Random();
//        if(damageTo.getUsername().equals(friend.getUsername())){
//            int t=r.nextInt(friend_CardsOnGround.size()+1);
//            if(t==friend_CardsOnGround.size()) dealDamageToHero(damage,friend);
//            else dealDamageToMinion(damage,friend,friend_CardsOnGround.get(t),"");
//        }
//        //-B is attacking
//        else {
//            int t=r.nextInt(enemy_CardsOnGround.size()+1);
//            if(t==enemy_CardsOnGround.size()) dealDamageToHero(damage,enemy);
//            else dealDamageToMinion(damage,enemy,enemy_CardsOnGround.get(t),"");
//        }
//    }
//    public void dealDamageToHero(int damage,InGamePlayer victimPlayer){
//        if(victimPlayer.getUsername().equalsIgnoreCase(friend.getUsername())){
//            gameView.infoGiver.logAction("enemy deal "+damage," damage to your hero","");
//
//            friend.getHero().setHP(friend.getHero().getHP()-damage);
//            if(friend.getHero().getHP()<=0) lost(friend);
//            friend_hero=friend.getHero();
//        }
//        else{
//            gameView.infoGiver.logAction("You deal "+damage," damage to enemy hero","");
//
//            enemy.getHero().setHP(enemy.getHero().getHP()-damage);
//            if(enemy.getHero().getHP()<=0) lost(enemy);
//            enemy_hero=enemy.getHero();
//        }
//    }
//    public void dealDamageToMinion(int damage,InGamePlayer victimPlayer,Minion minion,String condition){
//        if(victimPlayer.getUsername().equalsIgnoreCase(friend.getUsername())){
//            gameView.infoGiver.logAction("enemy deal "+damage,"damage to your minion:",minion.getName());
//
//            minion.setHP(minion.getHP()-damage);
//            setHPloss(true);
//            if(minion.getHP()<=0) {
//                removeKilledMinion(friend,minion,attacker);
//                if(condition.equalsIgnoreCase("HolyWater")) addToHand(minion,true);
//            }
//
//            //side conditions
//            if(minion.getName().equalsIgnoreCase("SecurityRover")){
//                gameView.infoGiver.logAction("Your SecurityRover was ","activated to summon","Mech minion");
//
//                summonMinion((Minion)Card.getCard("Mech"));
//            }
//        }else{
//            gameView.infoGiver.logAction("you deal "+damage,"damage to enemy minion:",minion.getName());
//
//            minion.setHP(minion.getHP()-damage);
//            setHPloss(true);
//            if(minion.getHP()<=0){
//                removeKilledMinion(enemy,minion,attacker);
////                if(condition.equalsIgnoreCase("HolyWater")) addToHand(enemy,minion,true);
//            }
//
//            //side conditions
//            if(minion.getName().equalsIgnoreCase("SecurityRover")){
//                gameView.infoGiver.logAction("Enemy SecurityRover was ","activated to summon","Mech minion");
//
//                summonMinion((Minion)Card.getCard("Mech"));
//            }
//        }
//    }
//
//
//    //attack handling
//    public boolean canAttack(){
//        if(attackerIsWeapon){
//            if (enemy_tauntMinions.size() >= 1 && !enemy_tauntMinions.contains(victim)) {
//                this.information = "Taunt In Way!";
//                return false;
//            }
//
//        }
//
//        else if(attackerIsHeroPower){
//            if(friend.getPassive()==Passive.FREEPOWER){
//                if(numberUseOfHeroPower>=2){
//                    gameView.infoGiver.logAction("heropower has been","used twice","");
//                    return false;
//                }
//                else if(numberUseOfHeroPower>=1) {
//                    gameView.infoGiver.logAction("heropower has been","used once","");
//                    return false;
//                }
//            }
//            else{
//                if(numberUseOfHeroPower>=1) {
//                    gameView.infoGiver.logAction("heropower has been","used once","");
//                    return false;
//                }
//            }
//        }
//
//        else if(attackerIsSpell){
//            if(victim.getName().equalsIgnoreCase("BearShark")){
//                gameView.infoGiver.logAction("cant damage BearShark"," with spells","");
//                return false;
//            }
//        }
//
//        else if(onceUsed.contains(attacker)){
//            gameView.infoGiver.logAction("This minion was used Once","","");
//            return false;
//        }
//
//        else if(attacker.getType().equalsIgnoreCase("spell") &&
//                victim.getName().equalsIgnoreCase("BearShark")){
//            setInformation("cant damage BearShark with spells");
//            return false;
//        }
//
//        else {
//            if (attacker.getType().equalsIgnoreCase("Minion")) {
//                if (!friend_RushCards.contains(attacker)) {
//                    setInformation("Not Rush");
//                    return false;
//                }
//
//            }
//            if (victim.getType().equalsIgnoreCase("Minion")) {
//                if (enemy_tauntMinions.size() >= 1 && !enemy_tauntMinions.contains(victim)) {
//                    this.information = "Taunt Way!";
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    public void attack(){
//        if(attackerIsWeapon){
//            handleWeaponAttack();
//            setHPloss(true);
//        }
//
//        else if(attackerIsHeroPower){
//            if(friend_heroPower==HeroPower.FireBlast) {
//                if (victim.getType().equalsIgnoreCase("Minion"))
//                    dealDamageToMinion(1, victimOwner, (Minion) victim, "");
//                else
//                    dealDamageToHero(1,victimOwner);
//            }
//        }
//
//        else if (attackerIsSpell){
//            spellAttack(attackerSpell);
//            setHPloss(true);
//        }
//
//        else if(attacker.getType().equalsIgnoreCase("Minion")){
//            handleMinionsAttack();
//            setHPloss(true);
//        }
//    }
//
//    //tools
//    public void discover(Card caller){
//        Random r=new Random();
//        if(caller.getType().equalsIgnoreCase("Spell")){
//            if(caller.getName().equalsIgnoreCase("FriendlySmith")){
//                List<String> list=Card.getList("weapons");
//                Weapon weapon=(Weapon) Card.getCard(list.get(r.nextInt(3)));
//                weapon.setShield(weapon.getShield()+2);
//                weapon.setAttack(weapon.getAttack()+2);
//                addToDeck(weapon);
//            }
//
//            if(caller.getName().equalsIgnoreCase("MarkedShot")){
//                List<String> list=Card.getList("spells");
//                Spell spell=(Spell) Card.getCard(list.get(r.nextInt(14)));
//                addToDeck(spell);
//            }
//        }
//
//    }
//
//    //heroPower methods
//    public boolean canCallHeroPower(){
//        if(friend.getPassive()==Passive.FREEPOWER){
//            if (numberUseOfHeroPower>=2){
//                gameView.infoGiver.logAction("heropower has been","used twice","");
//                return false;
//            }
//        }
//        else if (numberUseOfHeroPower>=1){
//            gameView.infoGiver.logAction("heropower has been","used once","");
//
//            return false;
//        }
//        if(friend_heroPower==HeroPower.StealMaster)
//            if(friend.getMana()<3){
//                JOptionPane.showMessageDialog(MainFrame.getInstance(),"You dont have enough mana",
//                        "ERROR",JOptionPane.ERROR_MESSAGE);
//                return false;
//            }
//        else if(friend.getMana()<2){
//            JOptionPane.showMessageDialog(MainFrame.getInstance(),"You dont have enough mana",
//                    "ERROR",JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//
//        return true;
//    }
//    public void callHeroPower(){
//        numberUseOfHeroPower++;
//        Random r=new Random();
//        if(friend_hero.getName().equalsIgnoreCase("Mage")){
//            if(!(friend.getPassive()== Passive.FREEPOWER))
//                friend.setMana(friend.getMana()-2);
//
//                attackerIsHeroPower=true;
//                isAttackerChosen=true;
//                attackerOwner=friend;
//                setInformation("Choose target");
//            }
//
//        else if(friend_hero.getName().equalsIgnoreCase("rogue")){
//                if(!(friend.getPassive()== Passive.FREEPOWER))
//                    friend.setMana(friend.getMana()-3);
//
//
//                //az enemy bayad carte random begirim
//                if(enemy_handCard.size()>0) {
//                    int n = r.nextInt(enemy_handCard.size());
//                    Card card=enemy_handCard.get(n);
//                    addToHand(card,false);
//
//                    gameView.infoGiver.logAction("Card was stolen from","opponent's hand.","");
//                }
//                else
//                    setInformation("Nothind to steal");
//
//            }
//
//        else if(friend_hero.getName().equalsIgnoreCase("warlock")){
//                if(!(friend.getPassive()== Passive.FREEPOWER))
//                    friend.setMana(friend.getMana()-2);
//
//                int n=r.nextInt(2);
//                if(n%2==0 && friend_CardsOnGround.size()>0 ){
//                    friend.getHero().setHP(friend.getHero().getHP() - 2);
//                    friend_hero = friend.getHero();
//                    int t=r.nextInt(friend_CardsOnGround.size());
//                    friend_CardsOnGround.get(t).setAttack(friend_CardsOnGround.get(t).getAttack()+1);
//                    friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getHP()+1);
//
//                }
//                else{
//                    if(enemy_CardsOnGround.size()>0) {
//                        friend.getHero().setHP(friend.getHero().getHP() - 2);
//                        friend_hero = friend.getHero();
//                        int t=r.nextInt(enemy_CardsOnGround.size());
//                        enemy_CardsOnGround.get(t).setHP(0);
//
//                        client.sendAttack(false,true,0,"Your minion was killed with heroPower",
//                                enemy_CardsOnGround.get(t).getName(),t);
//
//
//                        setHPloss(true);
//                        addToHand(enemy_CardsOnGround.get(t),false);
//
//                        removeKilledMinion(enemy,enemy_CardsOnGround.get(t),friend_hero);
//                    }
//                    else setInformation("No minion to lifetab");
//
//                }
//            }
//
//        else if(friend_hero.getName().equalsIgnoreCase("Hunter")){}
//
//        else{
//            if(!(friend.getPassive()== Passive.FREEPOWER))
//                friend.setMana(friend.getMana()-2);
//
//            if(friend_CardsOnGround.size()>0) {
//                int n = r.nextInt(friend_CardsOnGround.size());
//                friend_CardsOnGround.get(n).setHP(friend_CardsOnGround.get(n).getHP() + 4);
//
//                gameView.infoGiver.logAction("Heal affected:", friend_CardsOnGround.get(n).getName(), " +4 HP");
//            }
//        }
//    }
//
//    //minion methods
//    public boolean canSummonMinion(Minion minion){
//        if(friend_CardsOnGround.size()>=7) return false;
//
//        return true;
//    }
//    public void summonMinion(Minion minion){
//        gameView.infoGiver.logAction("You summon:",minion.getName(),"");
//
//        //check tasiire baghie ro jadid
//        for (Card card:
//                friend_permanentCause) {
//            if(card.getName().equalsIgnoreCase("HighPriestAmet")){
//                minion.setHP(((Minion)card).getHP());
//                gameView.infoGiver.logAction("HighPriestAmet changed ","new minion's HP","");
//            }
//        }
//
//        //check the new minion
//        if(minion.isTaunt()){
//            friend_tauntMinions.add(minion);
//        }
//
//        if(minion.isBattlecry()){
//            Random r=new Random();
//            if(minion.getName().equalsIgnoreCase("BewitchedGuardian")){
//                //gain +1 hp for each card in your hand
//                minion.setHP(minion.getHP()+friend_CardsOnGround.size());
//                gameView.infoGiver.logAction("BewitchedGaurdin's HP","was increased","(Battlecry)");
//            }
//
//            else  if(minion.getName().equalsIgnoreCase("BlinkFox")){
//                Card card=Card.getCard(enemy_deck.get(r.nextInt(enemy_deck.size())).getName());
//                addToHand(card,false);
//                gameView.infoGiver.logAction("BlinkFox copied","one card from","oponent'd Deck");
//            }
//
//            else if(minion.getName().equalsIgnoreCase("KabalTalonpriest")){
//                if(friend_CardsOnGround.size()>0) {
//                    int t = r.nextInt(friend_CardsOnGround.size());
//                    friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getHP() + 3);
//                    gameView.infoGiver.logAction("KabalTalonpriest gave ",friend_CardsOnGround.get(t).getName()
//                            ,"+3 HP");
//                    }
//                }
//
//            else if(minion.getName().equalsIgnoreCase("RockpoolHunter")){
//                    if(friend_CardsOnGround.size()>0) {
//                        int t = r.nextInt(friend_CardsOnGround.size());
//                        friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getHP() + 1);
//                        friend_CardsOnGround.get(t).setAttack(friend_CardsOnGround.get(t).getAttack() + 1);
//                        gameView.infoGiver.logAction("RockpoolHunter gave ",friend_CardsOnGround.get(t).getName()
//                                ,"+1/+1");
//                    }
//             }
//
//            else if(minion.getName().equalsIgnoreCase("Squashling")){
//                    int t=r.nextInt(friend_CardsOnGround.size());
//                    friend_CardsOnGround.get(t).setHP(friend_CardsOnGround.get(t).getHP()+2);
//                    gameView.infoGiver.logAction("Squashling gave ",friend_CardsOnGround.get(t).getName()
//                            ,"+1 HP");
//            }
//
//                else if(minion.getName().equalsIgnoreCase("TombWarden")){
//                    if(!tombwarden) {
//                        summonMinion((Minion) Card.getCard("TombWarden"));
//                        gameView.infoGiver.logAction("TombWarden summoned ", "another TombWarden", "");
//                        tombwarden=true;
//                    }
//                    else tombwarden=false;
//                }
//            }
//
//            if(minion.isRush() || friend_hero.getName().equalsIgnoreCase("Hunter")){
//                friend_RushCards.add(minion);
//            }
//
//            if(minion.isLifeSteal()){
//
//            }
//
//            if(minion.isPermanentCause()){
//                friend_permanentCause.add(minion);
//            }
//
//
//            if(minion.getName().equalsIgnoreCase("HoundMasterShaw")){
//                friend_RushCards.removeAll(friend_RushCards);
//                friend_RushCards.addAll(friend_CardsOnGround);
//                gameView.infoGiver.logAction("HoundMasterShaw changed ","other minions to","Rush");
//            }
//            friend_CardsOnGround.add(minion);
//
//            if(!friend.getGroundCards().contains(minion)) friend.addToGroundCards(minion);
//
//    }
//    public void handleMinionsAttack(){
//        onceUsed.add((Minion)attacker);
//        //-A is attacking
//        if(attackerOwner.getUsername().equals(friend.getUsername())){
//            if(victim.getType().equalsIgnoreCase("Minion")){
//                if(enemy_tauntMinions.contains(victim)){
//                    enemy_tauntMinions.remove(victim);
//                    ((Minion)victim).setTaunt(false);
//                    information="taunt down";
//                    attacker.setHP(attacker.getHP()-victim.getAttack());
//                    HPloss=true;
//                    if(attacker.getHP()<=0) removeKilledMinion(attackerOwner,(Minion) attacker,victim);
//                }
//                else{
//                    dealDamageToMinion(attacker.getAttack(),enemy,(Minion)victim,"");
//                    dealDamageToMinion(victim.getAttack(),friend,(Minion)attacker,"");
//
//                    information="attacked";
//                    HPloss=true;
//                }
//            }
//            else{
//                dealDamageToHero(attacker.getAttack(),enemy);
//            }
//            if(attacker.getName().equalsIgnoreCase("WickerflameBurnbristle")){
//                friend_hero.setHP(friend_hero.getHP()+attacker.getAttack());
//            }
//
//            for (Minion card:
//                    enemy_permanentCause) {
//                if(card.getName().equalsIgnoreCase("SwampKingDred")){
//                    setVictimOwner(friend);
//                    setAttackerOwner(enemy);
//                    setAttacker(card);
//                    setVictim(attacker);
//                }
//            }
//            if(enemy_heroPower==HeroPower.Caltropes){
//                dealDamageToMinion(1,friend,(Minion) attacker,"");
//                gameView.infoGiver.logAction("Caltropes was activated.","Attacked:"+attacker.getName(),"");
//            }
//        }
//        //-B is attacking
//        else {
//            if(victim.getType().equalsIgnoreCase("Minion")){
//                if(friend_tauntMinions.contains(victim)){
//                    friend_tauntMinions.remove(victim);
//                    ((Minion)victim).setTaunt(false);
//
//                    information="taunt down";
//                    attacker.setHP(attacker.getHP()-victim.getAttack());
//                    HPloss=true;
//                    if(attacker.getHP()<=0) removeKilledMinion(attackerOwner,(Minion) attacker,victim);
//                }
//                else{
//                    dealDamageToMinion(attacker.getAttack(),friend,(Minion)victim,"");
//                    dealDamageToMinion(victim.getAttack(),enemy,(Minion)attacker,"");
//
//                    information="attacked";
//                    HPloss=true;
//                }
//            }
//            else{
//                dealDamageToHero(attacker.getAttack(),friend);
//            }
//            if(attacker.getName().equalsIgnoreCase("WickerflameBurnbristle")){
//                enemy_hero.setHP(enemy_hero.getHP()+attacker.getAttack());
//            }
//
//            for (Minion card:
//                    friend_permanentCause) {
//                if(card.getName().equalsIgnoreCase("SwampKingDred")){
//                    setVictimOwner(enemy);
//                    setAttackerOwner(friend);
//                    setAttacker(card);
//                    setVictim(attacker);
//                }
//            }
//            if(friend_heroPower==HeroPower.Caltropes){
//                dealDamageToMinion(1,enemy,(Minion) attacker,"");
//                gameView.infoGiver.logAction("Caltropes was activated.","Attacked:"+attacker.getName(),"");
//            }
//
//        }
//    }
//    public void removeKilledMinion(InGamePlayer minionOwner,Minion minion,MyCharacter killer){
//        if(minionOwner.getUsername().equalsIgnoreCase(friend.getUsername())){
//            if(minion.isPermanentCause()) friend_permanentCause.remove(minion);
//            if(minion.isTaunt()) friend_tauntMinions.remove(minion);
//            if(friend_RushCards.contains(minion)) friend_RushCards.remove(minion);
//            friend_CardsOnGround.remove(minion);
//            friend.removeFromGround(minion);
//
//            for (Card card:
//                    friend_permanentCause) {
//                if(card.getName().equalsIgnoreCase("WrathscaleNaga")){
//                    dealDamageRandom(enemy,3);
//                }
//            }
//
//            if(friend.getPassive()==Passive.WARRIORS) {
//                friend.getHero().setHP(friend.getHero().getHP()+2);
//                friend_hero.setHP(friend_hero.getHP()+2);
//                gameView.infoGiver.logAction("2 defence added to your","hero because of","WARRIORS passive.");
//            }
//
//        }
//        else{
//            if(minion.isPermanentCause()) enemy_permanentCause.remove(minion);
//            if(minion.isTaunt()) enemy_tauntMinions.remove(minion);
//            if(enemy_RushCards.contains(minion)) enemy_RushCards.remove(minion);
//            enemy_CardsOnGround.remove(minion);
//            enemy.removeFromGround(minion);
//
//            for (Card card:
//                    enemy_permanentCause) {
//                if(card.getName().equalsIgnoreCase("WrathscaleNaga")){
//                    dealDamageRandom(friend,3);
//                }
//            }
//        }
//
//        gameView.playGround.update();
//
//    }
//
//    //weapon methods
//    public boolean hasWeapon(InGamePlayer player){
//        if(player.getUsername().equalsIgnoreCase(friend.getUsername())){
//            return friend_hasWeapon;
//        }else{
//            return enemy_hasWeapon;
//        }
//    }
//    public void addWeapon(Weapon weapon,InGamePlayer player){
//        if(player.getUsername().equals(friend.getUsername())){
//            friend.addWeapon(weapon);
//            friend_hasWeapon=true;
//            friend_weapon=weapon;
//        }
//        else{
//            enemy.addWeapon(weapon);
//            enemy_hasWeapon=true;
//            enemy_weapon=weapon;
//        }
//
//    }
//    public void handleWeaponAttack(){
//        //-A is attacking
//        if(attackerOwner.getUsername().equals(friend.getUsername())){
//            weaponAttack(friend_weapon.getAttack(),enemy);
//            if (friend_weapon.getShield() == 0) {
//                removeWeapon(friend);
//                friend_hasWeapon = false;
//            }
//        }
//        //-B is attacking
//        else {
//            weaponAttack(enemy_weapon.getAttack(),friend);
//            if (enemy_weapon.getShield() == 0) {
//                removeWeapon(enemy);
//                enemy_hasWeapon = false;
//            }
//
//        }
//    }
//    public void weaponAttack(int k,InGamePlayer victimOwner){
//        if(victimOwner.getUsername().equalsIgnoreCase(friend.getUsername())){
//            enemy_weapon.setShield(enemy_weapon.getShield()-1);
//            if(enemy_weapon.getShield()<=0) removeWeapon(enemy);
//
//        }
//        else {
//            friend_weapon.setShield(friend_weapon.getShield()-1);
//            if(friend_weapon.getShield()<=0) removeWeapon(friend);
//        }
//
//        if(victim.getType().equalsIgnoreCase("Minion")){
//            dealDamageToMinion(k,victimOwner,(Minion) victim,"");
//        }
//        else{
//            dealDamageToHero(k,victimOwner);
//        }
//
//    }
//    public void removeWeapon(InGamePlayer player){
//        //-A
//        if(player.getUsername().equals(friend.getUsername())){
//            friend_hasWeapon=false;
//            friend_weapon=null;
//        }
//        //-B
//        else {
//            enemy_weapon=null;
//            enemy_hasWeapon=false;
//        }
//    }
//
//    //spell methods
//    public void useSpell(Spell spell) {
//        if(spell.dealsDamage()){
//                if(spell.getName().equalsIgnoreCase("HolyWater")){
//                    gameView.infoGiver.logAction("Attacking with HolyWater ","","");
//
//                    Random r=new Random();
//                    int t=r.nextInt(enemy_CardsOnGround.size());
//                    dealDamageToMinion(4,friend,enemy_CardsOnGround.get(t),"HolyWater");
//                }
//
//                else if(spell.getName().equalsIgnoreCase("MarkedShot")){
//                    gameView.infoGiver.logAction("Attacking with MarkedShot ","","");
//
//                    Random r=new Random();
//                    int t=r.nextInt(enemy_CardsOnGround.size());
//                    dealDamageToMinion(4,friend,enemy_CardsOnGround.get(t),"");
//                }
//
//                else if(spell.getName().equalsIgnoreCase("Swipe")){
//                    gameView.infoGiver.logAction("Attacking with Swipe ","","");
//                    for (Minion minion:
//                            enemy_CardsOnGround) {
//                        dealDamageToMinion(1,enemy,minion,"");
//                    }
//                    dealDamageToHero(4,enemy);
//                }
//
//            }
//
//        if(spell.changesMinion()){
//                if(spell.getName().equalsIgnoreCase("Polymorph")) {
//                    gameView.infoGiver.logAction("Attacking with Polymorph ", "", "");
//
//                    isAttackerChosen = true;
//                    attackerIsSpell = true;
//                    attackerOwner = friend;
//                    attackerSpell = spell;
//                    setInformation("Choose Target");
//                }
//
//            }
//
//        if(spell.isDiscover()){
//                discover(spell);
//            }
//
//        if(spell.isDivineShield()){ }
//
//        if(spell.summonCards()){
//            if(spell.getName().equalsIgnoreCase("SwarmOfLocusts")) {
//                gameView.infoGiver.logAction("SwarmOfLocust is","summoning 7 locusts","");
//
//                for (int cnt = 0; cnt < 7; cnt++)
//                    if(canSummonMinion((Minion)Card.getCard("Locust")))
//                        summonMinion((Minion) Card.getCard("Locust"));
//            }
//
//            else if(spell.getName().equalsIgnoreCase("WisperingWoods")){
//                gameView.infoGiver.logAction("Wisperingwoods is","summoning wisp","");
//
//                for (Card card:
//                        friend_handCard) {
//                    if(canSummonMinion((Minion)Card.getCard("Wisp")))
//                        summonMinion((Minion)Card.getCard("Wisp"));
//                }
//            }
//
//        }
//
//        if(spell.isManaBurn()){
//            gameView.infoGiver.logAction("ManaBurn activated for","enemy","");
//
//            enemy_manaBurn=true;
//        }
//
//        if (spell.isDrawCard()){
//            if (spell.getName().equalsIgnoreCase("BookOfSpecters")){
//                gameView.infoGiver.logAction("BookOfSpecters:you","draw 3 cards","");
//
//                drawCard(true); drawCard(true); drawCard(true);
//            }
//
//            else if(spell.getName().equalsIgnoreCase("FeastOfSouls")){
//                gameView.infoGiver.logAction("FeastOfSouls:you","draw a cards for","each friendly minion");
//
//                for (Minion minion :
//                        friend_CardsOnGround) {
//                    drawCard(true);
//                }
//            }
//
//            else if(spell.getName().equalsIgnoreCase("witchwoodApple")){
//                gameView.infoGiver.logAction("WitchwoodApple:you","draw 3 Treants","");
//
//                addToHand(Card.getCard("Treant"),false);
//                addToHand(Card.getCard("Treant"),false);
//                addToHand(Card.getCard("Treant"),false);
//            }
//
//            else if(spell.getName().equalsIgnoreCase("Sprint")){
//                gameView.infoGiver.logAction("Sprint: you","draw 4 crads","");
//
//                drawCard(true); drawCard(true); drawCard(true);
//                drawCard(true);
//            }
//
//
//        }
//        if(spell.getName().equalsIgnoreCase("Thoughtsteal")){
//            gameView.infoGiver.logAction("Thoughtsteal: you","copy 2 crads","from enemy's deck");
//
//            Random r=new Random();
//            Card card=Card.getCard(enemy_deck.get(r.nextInt(enemy_deck.size())).getName());
//            addToHand(card,false);
//            Card card2=Card.getCard(enemy_deck.get(r.nextInt(enemy_deck.size())).getName());
//            addToHand(card2,false);
//        }
//
//    }
//    public void spellAttack(Spell spell){
//        if(attackerOwner.getUsername().equalsIgnoreCase(friend.getUsername())){
//            if (spell.getName().equalsIgnoreCase("Polymorph")){
//                if(victim.getType().equalsIgnoreCase("Minion")) {
//                    if(((Minion)victim).isTaunt()) enemy_tauntMinions.remove(victim);
//                    if(((Minion)victim).isPermanentCause()) enemy_permanentCause.remove(victim);
//                    enemy_CardsOnGround.remove(victim);
//                    Minion sheep=(Minion) Card.getCard("Sheep");
//                    enemy.addToGroundCards(sheep);
//                    enemy_CardsOnGround.add(sheep);
//                }
//                else{
//                    cantHappen=true;
//                }
//            }
//
//            attackerIsSpell=false;
//        }
//        else{
//            if (spell.getName().equalsIgnoreCase("Polymorph")){
//                if(victim.getType().equalsIgnoreCase("Minion")) {
//                    if(((Minion)victim).isTaunt()) friend_tauntMinions.remove(victim);
//                    if(((Minion)victim).isPermanentCause()) friend_permanentCause.remove(victim);
//                    friend_CardsOnGround.remove(victim);
//                    Minion sheep=(Minion) Card.getCard("Sheep");
//                    friend.addToGroundCards(sheep);
//                    friend_CardsOnGround.add(sheep);
//                }
//                else{
//                    cantHappen=true;
//                }
//            }
//
//            attackerIsSpell=false;
//        }
//
//    }
//
//
//
//    public void lost(InGamePlayer player){
//        if(player.getUsername().equals(friend.getUsername())){
//            client.sendGameMessage("youWin","");
//        }
//        else{
//            client.sendGameMessage("youLost","");
//        }
//    }
//
//
//
//}
