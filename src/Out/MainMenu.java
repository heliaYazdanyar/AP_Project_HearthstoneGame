package Out;

import util.ImageLoader;
import util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static util.Logger.logger;

public class MainMenu extends JPanel implements ActionListener {

    private JButton play=new JButton("Play");
    private JButton status=new JButton("Status");
    private JButton shop=new JButton("Shop");
    private JButton collections=new JButton("Collections");
    private JButton quit=new JButton("Quit");
    private JButton setting=new JButton("Setting");
    private JButton rank=new JButton("Rank");
    private GridBagConstraints gbc=new GridBagConstraints();
    private SoundPlayer sound= SoundPlayer.getInstance();


    static int height=330;
    static int width=350;


    public MainMenu(){
        //creating a box layout in mainMenu vertically
        this.setLayout(new GridBagLayout());

        gbc.weightx=2;
        gbc.fill=GridBagConstraints.HORIZONTAL;

        gbc.gridx=2; gbc.gridy=5;
        this.add(play,gbc);
        play.addActionListener(this);

        gbc.gridx=2; gbc.gridy=6;
        this.add(status,gbc);
        status.addActionListener(this);

        gbc.gridx=2; gbc.gridy=7;
        this.add(shop,gbc);
        shop.addActionListener(this);

        gbc.gridx=2; gbc.gridy=8;
        this.add(collections,gbc);
        collections.addActionListener(this);

        gbc.gridx=2; gbc.gridy=9;
        this.add(rank,gbc);
        rank.addActionListener(e->{
            logger(MainFrame.player.getUsername(),"ClickButton","Rank");
            MainFrame.getInstance().setPanel("Rank");
        });

        gbc.gridx=2; gbc.gridy=10;
        this.add(setting,gbc);
        setting.addActionListener(this);

        gbc.gridx=2; gbc.gridy=11;
        this.add(quit,gbc);
        quit.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==play){
            sound.play("click",0);
            logger(MainFrame.player.getUsername(),"ClickButton","play");
            MainFrame.getInstance().setPanel("PanelHandler");
        }
        else if(e.getSource()==status){
            sound.play("click",0);
            logger(MainFrame.player.getUsername(),"ClickButton","status");
            MainFrame.getInstance().setPanel("Status");

        }
        else if(e.getSource()==shop){
            sound.play("click",0);
            logger(MainFrame.player.getUsername(),"ClickButton","shop");
            MainFrame.getInstance().setPanel("Store");

        }
        else if(e.getSource()==collections){
            sound.play("click",0);
            logger(MainFrame.player.getUsername(),"ClickButton","collection");
            MainFrame.getInstance().setPanel("Collections");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",width*2,height*2);
        g.drawImage(background,0,0,null);
    }

}
