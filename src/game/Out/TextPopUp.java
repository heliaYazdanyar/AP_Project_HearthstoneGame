package game.Out;

import Models.Card;
import Models.Minion;
import Models.Weapon;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TextPopUp extends JLabel {
    private String name;
    private String info;
    Card card;


    public TextPopUp(String heroPowerName){
        this.name=heroPowerName;

        this.setBackground(Color.lightGray);
        this.setBorder( new LineBorder(Color.BLACK, 4));
        this.setForeground(Color.BLACK);
        this.setFont(new Font("Courier New", Font.ITALIC, 18));
        this.setOpaque(true);

        initHeroPowerPopUp();

    }
    public TextPopUp(Card card) {
        this.name = card.getName();
        if (card.getType().equalsIgnoreCase("Spell")){
            this.setBackground(Color.lightGray);
            this.setBorder(new LineBorder(Color.BLACK, 4));
            this.setForeground(Color.BLACK);
            this.setFont(new Font("Courier New", Font.ITALIC, 18));
            this.setOpaque(true);
            initSpellPopUp(card);
        }
        else if(card.getType().equalsIgnoreCase("Minion")){
            this.name=card.getName();

            this.setBackground(Color.lightGray);
            this.setBorder( new LineBorder(Color.BLACK, 4));
            this.setForeground(Color.BLACK);
            this.setFont(new Font("Courier New", Font.ITALIC, 18));
            this.setOpaque(true);

            initMinionPopUp((Minion) card);
        }
        else if(card.getType().equalsIgnoreCase("Weapon")){
            this.name=card.getName();

            this.setBackground(Color.lightGray);
            this.setBorder( new LineBorder(Color.BLACK, 4));
            this.setForeground(Color.BLACK);
            this.setFont(new Font("Courier New", Font.ITALIC, 18));
            this.setOpaque(true);

            initWeoponPopUp((Weapon) card);

        }else {
            //quest and reward
        }
    }
    public TextPopUp(String error,boolean errorTrue){
        this.info=error;

        this.setBackground(Color.PINK);
        this.setBorder( new LineBorder(Color.RED, 4));
        this.setForeground(Color.RED);
        this.setFont(new Font("Courier New", Font.ITALIC, 18));
        this.setOpaque(true);

        initErrorPopUp();
    }


    private void initHeroPowerPopUp(){

    }

    private void initSpellPopUp(Card card){
        this.card=card;
        String st="<html>*"+name+"<br>"+"$MANA:"+card.getManaCost()+"</html>";
        this.setText(st);
    }

    private void initMinionPopUp(Minion card){

        String st="<html>*"+name+"<br>"+"$MANA:"+card.getManaCost()+"<br>"+"*HP:"+card.getHP()+" *Attack:"+card.getAttack()+"</html>";
        this.setText(st);
    }

    private void initWeoponPopUp(Weapon weapon){

        String st="<html>*"+name+"<br>"+"$MANA:"+weapon.getManaCost()+"<br>"+"*ATTACK:"+weapon.getAttack()+"<br>"+"*SHIELD:"+
                weapon.getShield()+"</html>";
        this.setText(st);
    }

    private void initErrorPopUp(){
        String st="!!!"+info+"!!!";
        this.setText(st);
    }


}
