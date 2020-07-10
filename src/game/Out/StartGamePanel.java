package game.Out;

import Out.MainFrame;
import Util.ImageLoader;
import gamePlayers.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static Util.Logger.logger;

public class StartGamePanel extends JPanel {

    private GridBagConstraints gbc=new GridBagConstraints();

    private JButton backToMenu;
    private JLabel label;
    private JButton withDeckReader;
    private JButton withRandomEnemyDeck;
    private JButton playWithBoth;
    private JButton online;

    public int width=1400;
    public int height=850;
    private PanelHandler panelHandler;

    private Player player;

    public StartGamePanel(Player player,PanelHandler panelHandler){
        this.panelHandler=panelHandler;
        this.player=player;
        backToMenu=new JButton("MainMenu");

        label=new JLabel("Play with:",SwingConstants.CENTER);
        label.setBackground(Color.lightGray);
        label.setForeground(Color.BLUE);
        label.setFont(new Font("Courier New", Font.ITALIC, 20));
        label.setOpaque(true);

        withDeckReader=new JButton("Both Players(with deck reader)");

        withRandomEnemyDeck=new JButton("Both PLayers(random deck)");

        playWithBoth=new JButton("Bot");

        online=new JButton("online");

        initListeners();
        initPanel();
    }

    private void initListeners(){
        withDeckReader.addActionListener(e -> {
            logger(player.getUsername(),"Started game:","with deck reader");

            panelHandler.addPracticeWithDeckReader(player);
            panelHandler.setPanel("Passives");
        });

        withRandomEnemyDeck.addActionListener(e -> {
            if(player.ifDeckSelected){
                logger(player.getUsername(),"Started game:","with random deck");

                panelHandler.addPracticeWithRandomEnemy(player);
                panelHandler.setPanel("Passives");
            }
            else{
                JOptionPane.showMessageDialog(MainFrame.getInstance(),"You should select a deck first",
                        "ERROR",JOptionPane.ERROR_MESSAGE);
            }

        });

        playWithBoth.addActionListener(e -> {
            logger(player.getUsername(),"Started game:","with bot");

            panelHandler.addPracticeWithBot(player);
            panelHandler.setPanel("Passives");
        });

        online.addActionListener(e -> {
            logger(player.getUsername(),"Started game:","online game");

        });

        backToMenu.addActionListener(e -> MainFrame.getInstance().setPanel("MainMenu"));

    }

    private void initPanel(){
        this.setSize(width,height);
        this.setLayout(new GridBagLayout());

        gbc.weightx=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridy=6;
        this.add(withDeckReader,gbc);
        gbc.gridy=7;
        this.add(withRandomEnemyDeck,gbc);
        gbc.gridy=8;
        this.add(playWithBoth,gbc);
        gbc.gridy=9;
        this.add(online,gbc);
        gbc.gridy=10;
        this.add(backToMenu,gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background= ImageLoader.getInstance().loadImage("gameBG","jpg",width,height);
        g.drawImage(background,0,0,this.width,this.height,null);
    }

}
