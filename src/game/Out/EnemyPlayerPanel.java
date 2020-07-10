package game.Out;

import Models.Hero;
import Models.Weapon;
import Util.ImageLoader;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EnemyPlayerPanel extends JPanel{
    private Hero hero;
    private int deckCnt;
    private int currentMana;

    private boolean hasWeapon;
    private Weapon weapon;

    private JLabel weaponLabel;
    private JLabel heroPowerLabel;
    private JLabel heroFaceLabel;
    private JPanel manaPanel;
    private JLabel HPLabel;
    private JLabel deckLabel;
//    private JLabel handCntLabel;
    private JLabel skinCards;


    private GridBagConstraints gbc=new GridBagConstraints();

    public EnemyPlayerPanel(int deckCnt, Hero hero){
        this.setLayout(new GridBagLayout());
        this.hero=hero;
        this.deckCnt=deckCnt;

        initLabels();
    }

    private void initLabels(){
        heroFaceLabel=new JLabel(ImageLoader.getInstance().loadIcon(hero.getName(),"jpeg",250,350));

        skinCards=new JLabel(ImageLoader.getInstance().loadIcon("skinCards","png",300,100));

        deckLabel=new JLabel(ImageLoader.getInstance().loadIcon("deck","png",90,190));
        deckLabel.setText(deckCnt+"");
        deckLabel.setHorizontalTextPosition(JLabel.CENTER);
        deckLabel.setVerticalTextPosition(JLabel.CENTER);
        deckLabel.setFont(new Font("Courier New", Font.ITALIC, 18));
        deckLabel.setForeground(Color.darkGray);
        deckLabel.setOpaque(true);

        heroPowerLabel=new JLabel(ImageLoader.getInstance().loadIcon(hero.getHeroPowerName(),"jpeg",200,300));
        heroPowerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        manaPanel=new JPanel();
        Border b1 = new LineBorder(Color.blue, 5);
        manaPanel.setBorder(b1);
        manaPanel.setLayout(new GridLayout(10,1));
        manaPanel.setBackground(Color.lightGray);
        setMane(1);


//        HPLabel=new JLabel(ImageLoader.getInstance().loadIcon());


    }

    private void setMane(int cnt){
        currentMana=cnt;
        manaPanel.removeAll();
        ImageIcon manaIcon= ImageLoader.getInstance().loadIcon("Mana","png",20,20);
        if(cnt<10){
            for (int i=0;i<cnt;i++) {
                manaPanel.add(new JLabel(manaIcon));
            }
            for(int i=cnt;i<10;i++){
                manaPanel.add(new JLabel(""));
            }
        }
        else{
            for(int i=0;i<10;i++){
                manaPanel.add(new JLabel(manaIcon));
            }
        }

    }

    public void update(int mana,boolean hasWeapon,Weapon weapon,int deckCnt){
        if(hasWeapon){
            this.hasWeapon=true;
            this.weapon=weapon;
            weaponLabel=new JLabel(ImageLoader.getInstance().loadIcon(weapon.getName(),"jpeg",200,300));
            weaponLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

            //inja addesh kon
        }else{
            hasWeapon=false;
            this.remove(weaponLabel);
        }

        setMane(mana);

        this.deckCnt=deckCnt;

    }



}
