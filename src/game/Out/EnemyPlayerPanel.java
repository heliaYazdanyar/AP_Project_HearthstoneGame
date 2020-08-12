package game.Out;

import util.ImageLoader;
import gamePlayers.InGamePlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class EnemyPlayerPanel extends JPanel implements PlayerPanel{
    private InGamePlayer player;
    private GameView gameView;


    private JLabel hpLabel;
    private JLabel manaLabel;
    private JLabel weaponLabel;
    private JLabel deckLabel;
    private JLabel heroPowerLabel;

    private JPanel handPanel;
    private JPanel hpAndManaPanel;

    int width=1100;
    int height=140;

    public EnemyPlayerPanel(InGamePlayer player,GameView gameView){
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.player=player;
        this.gameView=gameView;

        this.setPreferredSize(new Dimension(width,height));

        hpAndManaPanel=new JPanel();
        hpAndManaPanel.setLayout(new BoxLayout(hpAndManaPanel,BoxLayout.Y_AXIS));
        update();
    }


    private void initDeckLabel(){
        deckLabel=new JLabel(ImageLoader.getInstance().loadIcon("deck","png",50,140));
        deckLabel.setOpaque(true);
    }
    private void initHandCards(){
        handPanel=new JPanel();
        handPanel.setLayout(new BorderLayout());

        JLabel label=new JLabel(ImageLoader.getInstance().loadIcon("skinCards","png",500,140));
        label.setBackground(Color.darkGray);

        handPanel.add(label,BorderLayout.CENTER);
    }
    private void initManaPanel(){
        manaLabel=new JLabel(ImageLoader.getInstance().loadIcon("mana","png",65,65),SwingUtilities.CENTER);
        manaLabel.setText(player.getMana()+"");
        manaLabel.setHorizontalTextPosition(JLabel.CENTER);
        manaLabel.setVerticalTextPosition(JLabel.CENTER);
        manaLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        manaLabel.setForeground(Color.BLUE);
        manaLabel.setOpaque(true);
    }
    private void initWeapon(){
        if(player.getAdminister().hasWeapon(player)){
            weaponLabel=new JLabel(ImageLoader.getInstance().loadIcon(player.getWeapon().getName(),"jpeg",120,140));
            weaponLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    gameView.infoGiver.initWeaponInfo(player.getWeapon());
                }
            });

        }
        else{
            weaponLabel=new JLabel(ImageLoader.getInstance().loadIcon("emptyWeapon","jpg",120,140));
            weaponLabel.setBackground(Color.getHSBColor(106, 114, 124 ));
        }
    }
    private void initHeroPower(){
        heroPowerLabel=new JLabel(ImageLoader.getInstance().loadIcon(player.getHero().getHeroPowerName(),
                "jpeg",120,140));
        heroPowerLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                gameView.infoGiver.initHeroPowerInfo(player.getHero().getHeroPowerName());
            }

        });
    }
    private void initHpPanel(){
        hpLabel=new JLabel(ImageLoader.getInstance().loadIcon("hpIcon","png",65,65),SwingUtilities.CENTER);
        hpLabel.setText(player.getHero().getHP()+"");
        hpLabel.setHorizontalTextPosition(JLabel.CENTER);
        hpLabel.setVerticalTextPosition(JLabel.CENTER);
        hpLabel.setForeground(Color.BLACK);
        hpLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        hpLabel.setOpaque(true);
    }

    private void addComponents(){
        this.removeAll();
        this.add(weaponLabel);
        this.add(handPanel);
        this.add(deckLabel);

        hpAndManaPanel.removeAll();
        hpAndManaPanel.add(manaLabel);
        hpAndManaPanel.add(hpLabel);
        this.add(hpAndManaPanel);
        this.add(heroPowerLabel);
    }

    public void update(){
        initDeckLabel();
        initManaPanel();
        initHandCards();
        initWeapon();
        initHeroPower();
        initHpPanel();

        addComponents();
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponent(g);
        BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",
                width,height);
        g.drawImage(background,0,0,null);

    }
}
