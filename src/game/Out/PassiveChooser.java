package game.Out;

import Util.ImageLoader;
import gamePlayers.PracticePlayer;
import logic.Passive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PassiveChooser extends JPanel {
    public int width=1100;
    public int height=850;

    private PanelHandler panelHandler;
    PracticePlayer player;
    List<String> passives=new ArrayList<>();
    JLabel[] labels=new JLabel[3];

    PassiveChooser(PracticePlayer player,PanelHandler panelHandler){
        this.panelHandler=panelHandler;
        this.player=player;

        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

        passives.add("TWICEDRAW");
        passives.add("FREEPOWER");
        passives.add("WARRIORS");
        passives.add("MANAJUMP");
        passives.add("ZOMBIE");

        initPassives();
    }

    private void initPassives(){
        this.add(Box.createRigidArea(new Dimension(80,350)));
        for(int cnt=0;cnt<3;cnt++){
            final String p=getRandomPassive();
            labels[cnt]=new JLabel(ImageLoader.getInstance().loadIcon(p,"png",200,350));
            labels[cnt].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("inja");
                    Passive.valueOf(p).apply(player);
                    panelHandler.setPanel("ChooseCardEnemy");
                }
            });
            this.add(labels[cnt]);
            this.add(Box.createRigidArea(new Dimension(80,350)));
        }
    }
    private  String getRandomPassive(){
        Random r=new Random();
        return passives.get(r.nextInt(5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background= ImageLoader.getInstance().loadImage("gameBG","jpg",width,height);
        g.drawImage(background,0,0,this.width,this.height,null);
    }
}
