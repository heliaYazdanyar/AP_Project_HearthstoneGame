package game.Out;

import util.ImageLoader;
import client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Waiting extends JPanel {
    private GameClient client;
    private PanelHandler panelHandler;

    private JMenuBar menuBar;
    private JButton cancel;

    private JPanel centerPanel;
    private JLabel waitingLabel;

    int width=1400;
    int height=850;

    Waiting(GameClient client,PanelHandler panelHandler){
        this.client=client;
        this.panelHandler=panelHandler;

        this.setLayout(new BorderLayout());
        menuBar=new JMenuBar();
        cancel=new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.cancelRequestForGame();
                panelHandler.setPanel("startGame");
            }
        });
        menuBar.add(cancel);
        this.add(menuBar,BorderLayout.NORTH);

        centerPanel=new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage background= ImageLoader.getInstance().loadImage("waiting","jpeg",width,height);
                g.drawImage(background,0,0,width,height,null);

                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 60));

                g.drawString("waiting for other player",500,200);
            }
        };
        this.add(centerPanel,BorderLayout.CENTER);

    }


}
