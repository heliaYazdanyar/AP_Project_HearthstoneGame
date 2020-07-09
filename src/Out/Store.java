package Out;

import Models.Card;
import Util.ImageLoader;
import gamePlayers.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Store extends JPanel implements ActionListener{

    private static Store instance;

    private JMenuBar menuBar=new JMenuBar();
    private JMenu menu=new JMenu("Collections");
    private JMenuItem myCards=new JMenuItem("My Cards");
    private JMenuItem newCards=new JMenuItem("New Cards");
    private JButton mainMenu=new JButton("MainMenu");
    private JLabel diamonds;
    private JPanel currentPanel=new JPanel();
    static int width=500;
    static int height=740;

    private HashMap<String, Card> Cards=new HashMap<>();
    private List<String> allCards=new ArrayList<>();
    private List<String> allMyCards=new ArrayList<>();
    private Player currentPlayer;
    private List<String> otherCards=new ArrayList<>();
    private CardLayout cardLayout=new CardLayout();


    Store(Player player){
        instance=this;
        this.currentPlayer=player;
        this.setLayout(new BorderLayout());
        loadCards();
        initPanels();

        menu.add(myCards); menu.add(newCards); myCards.addActionListener(this); newCards.addActionListener(this);
        menuBar.add(menu);
        diamonds=new JLabel(); diamonds.setText("   Diamonds:"+currentPlayer.getPlayersInfo().getDiamonds()+"    "); menuBar.add(diamonds);
        menuBar.add(mainMenu); mainMenu.addActionListener(this);
        this.add(menuBar,BorderLayout.NORTH);

        cardLayout.show(currentPanel,"CardToSell");
        this.add(currentPanel,BorderLayout.CENTER);
    }

    public static Store getInstance(){
        return instance;
    }

    class CardsToSell extends JPanel implements ActionListener {
        private JScrollPane scrollPane;
        private JPanel innerPanel=new JPanel();
        private int cnt=0;

        private JLabel[] images=new JLabel[37];
        private JPanel[] imagesPanels=new JPanel[37];
        private JPanel[] gridPanels=new JPanel[37];
        private JPanel[] containers=new JPanel[37];
        private JButton[] sell=new JButton[37];
        public CardsToSell(){
            cnt=allMyCards.size();
            this.setLayout(new BorderLayout());
            this.setSize(width,height);
            initPanels();


        }

        public void initPanels(){
            cnt=allMyCards.size();
            innerPanel.setLayout(new GridLayout(cnt/2,1));
            innerPanel.setBackground(Color.PINK);
            GridBagConstraints gbc=new GridBagConstraints();

            for(int i=0;i<cnt;i++){
                gbc.fill=GridBagConstraints.HORIZONTAL;
                containers[i]=new JPanel(); containers[i].setLayout(new GridBagLayout());
                containers[i].setSize(100,250);

                gbc.gridx=0; gbc.gridy=0;
                imagesPanels[i]=new JPanel(); imagesPanels[i].setLayout(new GridBagLayout());
                images[i]=new JLabel(ImageLoader.getInstance().loadIcon(allMyCards.get(i),"jpeg",200,200));
                imagesPanels[i].add(images[i]); imagesPanels[i].setBackground(Color.PINK);
                containers[i].add(imagesPanels[i],gbc); containers[i].setBackground(Color.PINK);

                gridPanels[i]=new JPanel(); gridPanels[i].setLayout(new GridLayout(1,2));
                gridPanels[i].add(new JLabel(Cards.get(allMyCards.get(i)).getPrice()+""));
                sell[i]=new JButton("sell"); sell[i].addActionListener(this);
                gridPanels[i].add(sell[i]);
                gbc.gridx=0; gbc.gridy=1;
                containers[i].add(gridPanels[i],gbc);

                innerPanel.add(containers[i]);
            }

            scrollPane=new JScrollPane(innerPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.add(scrollPane,BorderLayout.CENTER);

        }


        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0;i<cnt;i++) {
                final int t=i;
                if (e.getSource() == sell[t]) {
                    currentPlayer.sellCard(Card.getCard(allMyCards.get(t)));
                }
            }

        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",width,height);
            g.drawImage(background,0,0,null);
        }
    }

    class CardsToBuy extends JPanel implements ActionListener{
        private JScrollPane scrollPane;
        private JPanel innerPanel=new JPanel();
        private int cnt=0;

        private JLabel[] images=new JLabel[37];
        private JPanel[] imagesPanels=new JPanel[37];
        private JPanel[] gridPanels=new JPanel[37];
        private JPanel[] containers=new JPanel[37];
        private JButton[] buy=new JButton[37];

        public CardsToBuy(){
            cnt=otherCards.size();
            this.setLayout(new BorderLayout());
            this.setSize(width,height);
            initPanels();


        }

        public void initPanels(){
            cnt=otherCards.size();
            innerPanel.setLayout(new GridLayout(cnt/2,1));
            innerPanel.setBackground(Color.PINK);

            GridBagConstraints gbc=new GridBagConstraints();

            for(int i=0;i<cnt;i++){
                gbc.fill=GridBagConstraints.HORIZONTAL;
                containers[i]=new JPanel(); containers[i].setLayout(new GridBagLayout());
                containers[i].setSize(100,250);

                gbc.gridx=0; gbc.gridy=0;
                imagesPanels[i]=new JPanel(); imagesPanels[i].setLayout(new GridBagLayout());
                images[i]=new JLabel(ImageLoader.getInstance().loadIcon(otherCards.get(i),"jpeg",200,200));
                imagesPanels[i].add(images[i]); imagesPanels[i].setBackground(Color.PINK);
                containers[i].add(imagesPanels[i],gbc); containers[i].setBackground(Color.PINK);

                gridPanels[i]=new JPanel(); gridPanels[i].setLayout(new GridLayout(1,2));
                gridPanels[i].add(new JLabel(Cards.get(otherCards.get(i)).getPrice()+""));
                buy[i]=new JButton("Buy"); buy[i].addActionListener(this);
                gridPanels[i].add(buy[i]);
                gbc.gridx=0; gbc.gridy=1;
                containers[i].add(gridPanels[i],gbc);

                innerPanel.add(containers[i]);
            }

            scrollPane=new JScrollPane(innerPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.add(scrollPane,BorderLayout.CENTER);

        }

        @Override
        public
        void actionPerformed(ActionEvent e) {
            for(int i=0;i<cnt;i++) {
                final int t=i;
                if (e.getSource() == buy[t]) {
                    currentPlayer.buyCard(Card.getCard(otherCards.get(t)));
                }
            }

        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",width,height);
            g.drawImage(background,0,0,null);
        }

    }

    private void loadCards(){
        if(currentPlayer!=null) this.allMyCards=currentPlayer.getMyCardsNames();
        try{
            Path p= Paths.get(System.getProperty("user.dir")+ File.separator+"resources"+File.separator
                    +"allCards"+File.separator+"Lists"+File.separator+"allCards.txt");
            allCards= Files.readAllLines(p);
        }catch (IOException e){
            e.printStackTrace();
        }
        otherCards.removeAll(otherCards); otherCards.addAll(allCards); otherCards.removeAll(allMyCards);
        for (String name:
                allCards) {
            Cards.put(name,Card.getCard(name));
        }
    }

    private void initPanels(){
        currentPanel.removeAll();
        currentPanel.setLayout(cardLayout);
        currentPanel.add("CardsToSell",new CardsToSell());
        currentPanel.add("CardsToBuy",new CardsToBuy());

    }

    public void update(String panelName){
        loadCards();
        Collections.getInstance().loadAllCards();
        Collections.getInstance().initPanels();
        Collections.getInstance().update();
        diamonds.setText("   Diamonds:"+currentPlayer.getPlayersInfo().getDiamonds()+"    ");
        initPanels();
        cardLayout.show(currentPanel,panelName);
        repaint();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==newCards){
            cardLayout.show(currentPanel,"CardsToBuy");

        }
        else if(e.getSource()==myCards){
            cardLayout.show(currentPanel,"CardsToSell");
        }
        if(e.getSource()==mainMenu){
            MainFrame.getInstance().setPanel("MainMenu");
        }

    }
}
