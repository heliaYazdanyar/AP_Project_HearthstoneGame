package game.Out;

import models.Card;
import Out.MainFrame;
import util.ImageLoader;
import gamePlayers.PracticePlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChooseCard extends JPanel{

    PanelHandler panelHandler;
    PracticePlayer player;
    String frienemy="";

    JLabel[] labels=new JLabel[3];
    List<Card> cards=new ArrayList<>();
    boolean[] isChanged=new boolean[3];

    int width=1400;
    int height=800;
    private boolean added=false;

    private JPanel BGcontaner=new JPanel(){
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("gameBG","jpg",width,height);
            g.drawImage(background,0,0,1200,850,null);
        }
    };
    private JPanel menu=new JPanel();
    private JLabel name=new JLabel();

    private JButton ok=new JButton("It looks Good!");

    ChooseCard(PracticePlayer player,PanelHandler panelHandler,String frienemy){
        this.panelHandler=panelHandler;
        this.player=player;
        this.frienemy=frienemy;

        BGcontaner.setPreferredSize(new Dimension(1200,850));
        name.setText(frienemy);
        name.setHorizontalTextPosition(JLabel.CENTER);
        name.setVerticalTextPosition(JLabel.CENTER);
        name.setForeground(Color.BLACK);
        name.setBackground(Color.PINK);
        name.setFont(new Font("Courier New", Font.BOLD, 18));
        name.setOpaque(true);

        menu.setLayout(new BoxLayout(menu,BoxLayout.Y_AXIS));
        menu.setBackground(Color.lightGray.BLACK);

        for(int i=0;i<3;i++){
            isChanged[i]=false;
            cards.add(getRandomCard());
        }
        ok.setBackground(Color.GREEN);
        ok.setForeground(Color.BLACK);
        ok.setPreferredSize(new Dimension(100,300));
        ok.setOpaque(true);

        initCards();
        initChooseCardPanel();

    }

    private void initCards(){
        for(int i=0;i<3;i++) {
            labels[i]=new JLabel(ImageLoader.getInstance().loadIcon(cards.get(i).getName(),"jpeg",170,170));
        }
        initListeners();
    }
    private void initListeners(){
        for(int i=0;i<3;i++){
            final int t=i;
            labels[t].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(!isChanged[t]){
                        isChanged[t]=true;
                        List<Card> newCards=new ArrayList<>();
                        for(int j=0;j<3;j++){
                            if(j==t){
                                newCards.add(getRandomCard());
                            }else{
                                newCards.add(cards.get(j));
                            }
                        }
                        cards.removeAll(cards);
                        cards.addAll(newCards);
                        initCards();
                        initChooseCardPanel();
                    }
                    else {
                        JOptionPane.showMessageDialog(MainFrame.getInstance(),"You already changed this card",
                                "ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        ok.addActionListener(e -> {
            if(!added) {
                for (int i = 0; i < 3; i++) {
                    player.addToHand(cards.get(i));
                }
                added=true;
            }
            if(frienemy.equalsIgnoreCase("friend"))
            panelHandler.setPanel("GameView");
            else panelHandler.setPanel("ChooseCardFriend");
        });

    }
    private Card getRandomCard(){
        Random r=new Random();
        int size=player.getdeckCnt();
        return player.getDeckCards().get(r.nextInt(size));
    }

    private void initChooseCardPanel(){
        this.removeAll();
        menu.removeAll();

        menu.add(name);
        menu.add(Box.createRigidArea(new Dimension(10,10)));
        for(int i=0;i<3;i++) {
            menu.add(labels[i]);
            menu.add(Box.createRigidArea(new Dimension(10,10)));
        }
        menu.add(ok);

        this.add(menu,BorderLayout.WEST);
        this.add(BGcontaner,BorderLayout.CENTER);

    }



}
