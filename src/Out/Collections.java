package Out;

import Models.Card;
import Models.Deck;
import Util.ImageLoader;
import Util.SoundPlayer;
import gamePlayers.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static Util.Logger.logger;

public class Collections extends JPanel implements ActionListener {
    private static Collections instance;
    private SoundPlayer sound= SoundPlayer.getInstance();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("Menu");
    private JMenuItem mage = new JMenuItem("Mage");
    private JMenuItem rogue = new JMenuItem("Rogue");
    private JMenuItem warlock = new JMenuItem("Warlock");
    private JMenuItem hunter = new JMenuItem("Hunter");
    private JMenuItem priest = new JMenuItem("Priest");
    private JMenuItem neutral = new JMenuItem("Neutral");
    private JButton mainMenu = new JButton("Main Menu");

    //cards filters
    private JPanel filters=new JPanel();
    private JLabel manaCostFilter=new JLabel("Mana Cost:");
    private ButtonGroup manaGroup=new ButtonGroup();
    private JRadioButton[] m=new JRadioButton[11];
    private ButtonGroup filteringCards=new ButtonGroup();
    private JRadioButton myCardsFilter=new JRadioButton("My Cards",false);
    private JRadioButton notMyCards=new JRadioButton("Cards I Don't Own",false);

    //search area
    private JTextField searchText=new JTextField();
    private JButton search=new JButton("Search");

    private List<Card> mageCards=new ArrayList<>();
    private List<Card> warlockCards=new ArrayList<>();
    private List<Card> rogueCards=new ArrayList<>();
    private List<Card> hunterCards=new ArrayList<>();
    private List<Card> priestCards=new ArrayList<>();
    private List<Card> neutralCards=new ArrayList<>();
    private List<String> playersCards=new ArrayList<>();
    private List<String> allCards=new ArrayList<>();

    static int width=800;
    static int height=700;
    private GridBagConstraints gbc=new GridBagConstraints();
    private CardLayout cardLayout=new CardLayout();
    private NeutralPanel neutralPanel;
    public DeckPanel deckPanel;
    private String settedPanel="";
    private static JPanel currentPanel=new JPanel();
    private Player currentPlayer;

    private boolean deckSelected=false;
    public int currentDeckCnt=-1;
    private Deck currentDeck;
    private boolean playingDeckSelected=false;
    private Deck playingDeck;

    private boolean MCardsFilter=false;
    private boolean OCardsFilter=false;
    private boolean[] mana=new boolean[11];

    public Collections(Player currentPlayer){
        this.currentPlayer=currentPlayer;

        instance=this;
        this.setLayout(new BorderLayout());
        initFilters();
        initListeners();

        try{
            allCards= Files.readAllLines(Paths.get(System.getProperty("user.dir")+ File.separator+"resources"+File.separator+
                    "allCards"+File.separator+"Lists"+File.separator+"allCards.txt"));
        }catch (IOException e){
            e.printStackTrace();
        }

        loadAllCards();
        deckPanel=new DeckPanel(currentPlayer);

        initPanels();
        setPanel("Mage");
        this.add(currentPanel,BorderLayout.CENTER);

        this.add(deckPanel,BorderLayout.EAST);

    }

    //panels
    class MagePanel extends JPanel implements ActionListener{
        private JPanel[] panels=new JPanel[2];
        private JLabel[] labels=new JLabel[2];
        private JButton[] addToDeck=new JButton[2];

        private MouseListener listener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < mageCards.size(); i++) {
                    final int t = i;
                    if (e.getSource() == labels[t]) {
                        if (!playersCards.contains(mageCards.get(t).getName())) {
                            logger(currentPlayer.getUsername(),"click_card","go_to_shop");
                            MainFrame.getInstance().setPanel("Store");
                        } else {
                            JOptionPane.showMessageDialog(instance,"You already have this card",
                                    "Error",JOptionPane.ERROR_MESSAGE);

                        }
                    }
                }
            }
        };

        public MagePanel(){
            this.setLayout(new GridLayout(1,2));
            this.setSize(new Dimension(400,700));

            boolean noCardFilter=true;
            if(MCardsFilter || OCardsFilter) noCardFilter=false;
            for(int i=0;i<2;i++) {
                if (mana[10] || mana[mageCards.get(i).getManaCost() - 1]) {
                    panels[i] = new JPanel();
                    panels[i].setLayout(new GridBagLayout());
                    if (playersCards.contains(mageCards.get(i).getName()) && (MCardsFilter || noCardFilter)) {
                        labels[i] = new JLabel(ImageLoader.getInstance().loadIcon(mageCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i] = new JButton("Add To Deck");
                        addToDeck[i].addActionListener(this);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        panels[i].add(labels[i], gbc);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        panels[i].add(addToDeck[i], gbc);
                        panels[i].setBackground(Color.BLACK);
                        this.add(panels[i]);
                    }
                    else if(OCardsFilter || noCardFilter){
                        labels[i] = new JLabel(ImageLoader.getInstance().bnwIcon(mageCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i] = new JButton("Add To Deck");
                        addToDeck[i].addActionListener(this);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        panels[i].add(labels[i], gbc);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        panels[i].add(addToDeck[i], gbc);
                        panels[i].setBackground(Color.BLACK);
                        this.add(panels[i]);
                    }

                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0;i<2;i++) {
                final int t=i;
                if (e.getSource() == addToDeck[t]) {
                    if(deckSelected && currentDeck.addPossible(mageCards.get(t))){
                        currentDeck.addCard(mageCards.get(t));
                    }
                    else {
                        JOptionPane.showMessageDialog(instance,"Not Possible!",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",600,700);
            g.drawImage(background,0,0,null);
        }
    }

    class WarlockPanel extends JPanel implements ActionListener{
        private JPanel[] panels=new JPanel[2];
        private JLabel[] labels=new JLabel[2];
        private JButton[] addToDeck=new JButton[2];

        private MouseListener listener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < warlockCards.size(); i++) {
                    final int t = i;
                    if (e.getSource() == labels[t]) {
                        if (!playersCards.contains(warlockCards.get(t).getName())) {
                            logger(currentPlayer.getUsername(),"click_card","go_to_shop");
                            MainFrame.getInstance().setPanel("Store");
                        } else {
                            JOptionPane.showMessageDialog(instance,"You already have this card",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };

        public WarlockPanel(){
            this.setLayout(new GridLayout(1,2));
            this.setSize(new Dimension(400,700));


            boolean noCardFilter=true;
            if(MCardsFilter || OCardsFilter) noCardFilter=false;

            for(int i=0;i<2;i++){
                if (mana[10] || mana[warlockCards.get(i).getManaCost() - 1]) {
                    panels[i]=new JPanel();
                    panels[i].setLayout(new GridBagLayout());

                    if(playersCards.contains(warlockCards.get(i).getName()) &&(noCardFilter||MCardsFilter)) {
                        labels[i]=new JLabel(ImageLoader.getInstance().loadIcon(warlockCards.get(i).getName(),"jpeg",200,200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i]=new JButton("Add To Deck"); addToDeck[i].addActionListener(this);
                        gbc.fill=GridBagConstraints.HORIZONTAL;
                        gbc.gridx=0; gbc.gridy=0;
                        panels[i].add(labels[i],gbc);
                        gbc.gridx=0; gbc.gridy=1;
                        panels[i].add(addToDeck[i],gbc);
                        panels[i].setBackground(Color.white);
                        this.add(panels[i]);
                    }
                    else if(noCardFilter||OCardsFilter) {
                        labels[i] = new JLabel(ImageLoader.getInstance().bnwIcon(warlockCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i]=new JButton("Add To Deck"); addToDeck[i].addActionListener(this);
                        gbc.fill=GridBagConstraints.HORIZONTAL;
                        gbc.gridx=0; gbc.gridy=0;
                        panels[i].add(labels[i],gbc);
                        gbc.gridx=0; gbc.gridy=1;
                        panels[i].add(addToDeck[i],gbc);
                        panels[i].setBackground(Color.white);
                        this.add(panels[i]);
                    }

                }
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0;i<2;i++) {
                final int t=i;
                if (e.getSource() == addToDeck[t]) {
                    if(deckSelected && currentDeck.addPossible(warlockCards.get(t))){
                        currentDeck.addCard(warlockCards.get(t));
                    }
                    else {
                        JOptionPane.showMessageDialog(instance,"Not Possible!",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",
                    600,700);
            g.drawImage(background,0,0,null);
        }
    }

    class HunterPanel extends JPanel implements ActionListener{
        private JPanel[] panels=new JPanel[2];
        private JLabel[] labels=new JLabel[2];
        private JButton[] addToDeck=new JButton[2];

        private MouseListener listener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < hunterCards.size(); i++) {
                    final int t = i;
                    if (e.getSource() == labels[t]) {
                        if (!playersCards.contains(hunterCards.get(t).getName())) {
                            logger(currentPlayer.getUsername(),"click_card","go_to_shop");
                            MainFrame.getInstance().setPanel("Store");
                        } else {
                            JOptionPane.showMessageDialog(instance,"You already have this card",
                                    "Error",JOptionPane.ERROR_MESSAGE);

                        }
                    }
                }
            }
        };

        HunterPanel() {
            this.setLayout(new GridLayout(1, 2));
            this.setSize(new Dimension(400, 700));

            boolean noCardFilter = true;
            if (MCardsFilter || OCardsFilter) noCardFilter = false;

            for (int i = 0; i < 2; i++) {
                if (mana[10] || mana[hunterCards.get(i).getManaCost() - 1]) {
                    panels[i] = new JPanel();
                    panels[i].setLayout(new GridBagLayout());

                    if (playersCards.contains(hunterCards.get(i).getName())&&(noCardFilter||MCardsFilter)) {
                        labels[i] = new JLabel(ImageLoader.getInstance().loadIcon(hunterCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i] = new JButton("Add To Deck");
                        addToDeck[i].addActionListener(this);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        panels[i].add(labels[i], gbc);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        panels[i].add(addToDeck[i], gbc);
                        panels[i].setBackground(Color.pink);
                        this.add(panels[i]);
                    }
                    else if(OCardsFilter || noCardFilter){
                        labels[i] = new JLabel(ImageLoader.getInstance().bnwIcon(hunterCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i] = new JButton("Add To Deck");
                        addToDeck[i].addActionListener(this);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        panels[i].add(labels[i], gbc);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        panels[i].add(addToDeck[i], gbc);
                        panels[i].setBackground(Color.pink);
                        this.add(panels[i]);
                    }

                }
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0;i<2;i++) {
                final int t=i;
                if (e.getSource() == addToDeck[t]) {
                    if(deckSelected && currentDeck.addPossible(hunterCards.get(t))){
                        currentDeck.addCard(hunterCards.get(t));
                    }
                    else {
                        JOptionPane.showMessageDialog(instance,"Not Possible!",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",
                    600,700);
            g.drawImage(background,0,0,null);
        }
    }

    class RoguePanel extends JPanel implements ActionListener{
        private JPanel[] panels=new JPanel[2];
        private JLabel[] labels=new JLabel[2];
        private JButton[] addToDeck=new JButton[2];

        private MouseListener listener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < rogueCards.size(); i++) {
                    final int t = i;
                    if (e.getSource() == labels[t]) {
                        if (!playersCards.contains(rogueCards.get(t).getName())) {
                            logger(currentPlayer.getUsername(),"click_card","go_to_shop");
                            MainFrame.getInstance().setPanel("Store");
                        } else {
                            JOptionPane.showMessageDialog(instance,"You already have this card",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };

        public RoguePanel() {
            this.setLayout(new GridLayout(1, 2));
            this.setSize(new Dimension(400, 700));

            boolean noCardFilter = true;
            if (MCardsFilter || OCardsFilter) noCardFilter = false;

            for (int i = 0; i < 2; i++) {
                if (mana[10] || mana[rogueCards.get(i).getManaCost() - 1]) {
                    panels[i] = new JPanel();
                    panels[i].setLayout(new GridBagLayout());

                    if (playersCards.contains(rogueCards.get(i).getName())&&(noCardFilter||MCardsFilter)) {
                        labels[i] = new JLabel(ImageLoader.getInstance().loadIcon(rogueCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i] = new JButton("Add To Deck");
                        addToDeck[i].addActionListener(this);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        panels[i].add(labels[i], gbc);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        panels[i].add(addToDeck[i], gbc);
                        panels[i].setBackground(Color.BLACK);
                        this.add(panels[i]);
                    }
                    else if(OCardsFilter||noCardFilter){
                        labels[i] = new JLabel(ImageLoader.getInstance().bnwIcon(rogueCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i] = new JButton("Add To Deck");
                        addToDeck[i].addActionListener(this);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        panels[i].add(labels[i], gbc);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        panels[i].add(addToDeck[i], gbc);
                        panels[i].setBackground(Color.BLACK);
                        this.add(panels[i]);
                    }

                }
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0;i<2;i++) {
                final int t = i;
                if (addToDeck[t] != null) {
                    if (e.getSource() == addToDeck[t]) {
                        if (deckSelected && currentDeck.addPossible(rogueCards.get(t))) {
                            currentDeck.addCard(rogueCards.get(t));
                        } else {
                            JOptionPane.showMessageDialog(instance, "Not Possible!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",600,700);
            g.drawImage(background,0,0,null);
        }
    }

    class PriestPanel extends JPanel implements ActionListener{
        private JPanel[] panels=new JPanel[2];
        private JLabel[] labels=new JLabel[2];
        private JButton[] addToDeck=new JButton[2];

        private MouseListener listener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < priestCards.size(); i++) {
                    final int t = i;
                    if (e.getSource() == labels[t]) {
                        if (!playersCards.contains(priestCards.get(t).getName())) {
                            logger(currentPlayer.getUsername(),"click_card","go_to_shop");
                            MainFrame.getInstance().setPanel("Store");
                        } else {
                            JOptionPane.showMessageDialog(instance,"You already have this card",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };

        public PriestPanel() {
            this.setLayout(new GridLayout(1, 2));
            this.setSize(new Dimension(400, 700));


            boolean noCardFilter = true;
            if (MCardsFilter || OCardsFilter) noCardFilter = false;

            for (int i = 0; i < 2; i++) {
                if (mana[10] || mana[priestCards.get(i).getManaCost() - 1]) {
                    panels[i] = new JPanel();
                    panels[i].setLayout(new GridBagLayout());
                    if (playersCards.contains(priestCards.get(i).getName())&& (MCardsFilter||noCardFilter)) {
                        labels[i] = new JLabel(ImageLoader.getInstance().loadIcon(priestCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i] = new JButton("Add To Deck");
                        addToDeck[i].addActionListener(this);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        panels[i].add(labels[i], gbc);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        panels[i].add(addToDeck[i], gbc);
                        panels[i].setBackground(Color.BLACK);
                        this.add(panels[i]);
                    }
                    else if(OCardsFilter||noCardFilter) {
                        labels[i] = new JLabel(ImageLoader.getInstance().bnwIcon(priestCards.get(i).getName(), "jpeg", 200, 200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i] = new JButton("Add To Deck");
                        addToDeck[i].addActionListener(this);
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        panels[i].add(labels[i], gbc);
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        panels[i].add(addToDeck[i], gbc);
                        panels[i].setBackground(Color.BLACK);
                        this.add(panels[i]);
                    }

                }
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0;i<2;i++) {
                final int t=i;
                if (e.getSource() == addToDeck[t]) {
                    if(deckSelected &&currentDeck.addPossible(priestCards.get(t))){
                        currentDeck.addCard(priestCards.get(t));
                    }
                    else {
                        JOptionPane.showMessageDialog(instance,"Not Possible!",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",600,700);
            g.drawImage(background,0,0,null);
        }
    }

    class NeutralPanel extends JPanel implements ActionListener{ //baraye ina bayad scroll pane biruneshun bezari
        private JPanel[] panels=new JPanel[30];
        private JLabel[] labels=new JLabel[30];
        private JButton[] addToDeck=new JButton[30];
        private JPanel innerPanel=new JPanel();
        private JScrollPane scrollPane;

        private MouseListener listener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < neutralCards.size(); i++) {
                    final int t = i;
                    if (e.getSource() == labels[t]) {
                        if (!playersCards.contains(neutralCards.get(t).getName())) {
                            logger(currentPlayer.getUsername(),"click_card","go_to_shop");
                            MainFrame.getInstance().setPanel("Store");
                        } else {
                            JOptionPane.showMessageDialog(instance,"You already have this card",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };

        public NeutralPanel(){
            this.setLayout(new BorderLayout());
            this.setPreferredSize(new Dimension(1000,3000));
            innerPanel.setLayout(new GridLayout(12,3));

            boolean noCardFilter=true;
            if(MCardsFilter || OCardsFilter) noCardFilter=false;

            for(int i=0;i<27;i++){
                if(mana[10] || mana[neutralCards.get(i).getManaCost()-1]){
                    panels[i]=new JPanel();
                    panels[i].setLayout(new GridBagLayout());

                    if(playersCards.contains(neutralCards.get(i).getName()) &&(noCardFilter||MCardsFilter)) {
                        labels[i]=new JLabel(ImageLoader.getInstance().loadIcon(neutralCards.get(i).getName(),"jpeg",200,200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i]=new JButton("Add To Deck"); addToDeck[i].addActionListener(this);
                        gbc.fill=GridBagConstraints.HORIZONTAL;
                        gbc.gridx=0; gbc.gridy=0;
                        panels[i].add(labels[i],gbc);
                        gbc.gridx=0; gbc.gridy=1;
                        panels[i].add(addToDeck[i],gbc);
                        panels[i].setBackground(Color.BLACK);
                        innerPanel.add(panels[i]);
                    }
                    else if(noCardFilter ||OCardsFilter){
                        labels[i]=new JLabel(ImageLoader.getInstance().bnwIcon(neutralCards.get(i).getName(),"jpeg",200,200));
                        labels[i].addMouseListener(listener);
                        addToDeck[i]=new JButton("Add To Deck"); addToDeck[i].addActionListener(this);
                        gbc.fill=GridBagConstraints.HORIZONTAL;
                        gbc.gridx=0; gbc.gridy=0;
                        panels[i].add(labels[i],gbc);
                        gbc.gridx=0; gbc.gridy=1;
                        panels[i].add(addToDeck[i],gbc);
                        panels[i].setBackground(Color.BLACK);
                        innerPanel.add(panels[i]);
                    }
                }
            }
            scrollPane=new JScrollPane(innerPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(scrollPane,BorderLayout.CENTER);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 27; i++) {
                final int t = i;
                if (addToDeck[t] != null) {
                    if (e.getSource() == addToDeck[t]) {
                        if (deckSelected && currentDeck.addPossible(neutralCards.get(t))) {
                            currentDeck.addCard(neutralCards.get(t));
                        } else {
                            JOptionPane.showMessageDialog(instance, "Not Possible!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",600,700);
            g.drawImage(background,0,0,null);
        }
    }

    class Search extends JPanel implements ActionListener{
        private JPanel[] panels=new JPanel[30];
        private JLabel[] labels=new JLabel[30];
        private JButton[] addToDeck=new JButton[30];
        private JPanel innerPanel=new JPanel();
        private JScrollPane scrollPane;

        List<String> searchResult=new ArrayList<>();

        private MouseListener listener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < searchResult.size(); i++) {
                    final int t = i;
                    if (e.getSource() == labels[t]) {
                        if (!playersCards.contains(searchResult.get(t))) {
                            logger(currentPlayer.getUsername(),"click_card","go_to_shop");
                            MainFrame.getInstance().setPanel("Store");
                        } else {
                            JOptionPane.showMessageDialog(instance,"You already have this card",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };
        Search(String word){
            this.setSize(600,700);
            if(exist(word)){
                searchResult.removeAll(searchResult);
                searchResult.addAll(searching(word));
                int cnt=searchResult.size();

                this.setLayout(new BorderLayout());
                this.setPreferredSize(new Dimension(700,1000));
                innerPanel.setLayout(new GridLayout(12,3));
                innerPanel.setBackground(Color.blue);

                for(int i=0;i<cnt;i++){
                    panels[i]=new JPanel();
                    panels[i].setLayout(new GridBagLayout());
                    if(playersCards.contains(searchResult.get(i)))
                        labels[i]=new JLabel(ImageLoader.getInstance().loadIcon(searchResult.get(i),"jpeg",200,200));
                    else
                        labels[i]=new JLabel(ImageLoader.getInstance().bnwIcon(searchResult.get(i),"jpeg",200,200));

                    labels[i].addMouseListener(listener);
                    addToDeck[i]=new JButton("Add To Deck"); addToDeck[i].addActionListener(this);
                    gbc.fill=GridBagConstraints.HORIZONTAL;
                    gbc.gridx=0; gbc.gridy=0;
                    panels[i].add(labels[i],gbc);
                    gbc.gridx=0; gbc.gridy=1;
                    panels[i].add(addToDeck[i],gbc);
                    panels[i].setBackground(Color.BLUE);
                    innerPanel.add(panels[i]);
                }
                scrollPane=new JScrollPane(innerPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                this.add(scrollPane,BorderLayout.CENTER);

            }
            else{
                this.setLayout(new BorderLayout());
                JLabel label=new JLabel("--Nothing Found!--");
                label.setFont(new Font("Courier New", Font.ITALIC, 30));
                label.setForeground(Color.black);
                this.add(label,BorderLayout.NORTH);

            }

        }

        public boolean exist(String word){
            for (String cardName:
                    allCards) {
                if((cardName.toUpperCase()).startsWith(word.toUpperCase())){
                    return true;
                }
            }
            return false;
        }
        public List<String> searching(String word){
            List<String> names=new ArrayList<>();

            for (String cardName:
                    allCards) {
                if((cardName.toUpperCase()).startsWith(word.toUpperCase())){
                    names.add(cardName);
                }
            }
            return names;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < searchResult.size(); i++) {
                final int t = i;
                if (e.getSource() == addToDeck[t]) {
                    if (deckSelected && currentDeck.addPossible(Card.getCard(searchResult.get(t)))) {
                        currentDeck.addCard(Card.getCard(searchResult.get(t)));
                    } else {
                        JOptionPane.showMessageDialog(instance, "Not Possible!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",600,700);
            g.drawImage(background,0,0,null);
        }
    }

    public class DeckPanel extends  JPanel implements ActionListener{

        public CardLayout cardLayout1=new CardLayout();
        public JPanel allDecks=new JPanel();
        private JScrollPane deckScroll;
        private JPanel buttonsPanel=new JPanel();
        private JButton[] deckButtons=new JButton[15];
        private Deck[] decks=new Deck[15];
        private JLabel selectedDeck;

        private List<String> listOfDecks;

        private int w=150;
        private int h=700;

        public DeckPanel(Player player){
            allDecks.setSize(w,h);
            allDecks.setBackground(Color.orange);
            this.setLayout(new BorderLayout());

            initDecks(player);
            initPanel();
            loadDecks(player);
            cardLayout1.show(allDecks,"all");
        }

        private void initPanel(){
            allDecks.removeAll();
            allDecks.setLayout(cardLayout1);
            allDecks.add("all",buttonsPanel);
        }
        private void loadDecks(Player player){
            List<String> list=player.getDecksList();
            for(int cnt=0;cnt<list.size();cnt++){
                decks[cnt]=new Deck(player,list.get(cnt));
                allDecks.add(cnt+"",decks[cnt]);
            }
        }
        private void initDecks(Player player){
            listOfDecks=player.getDecksList();
            buttonsPanel.setLayout(new GridBagLayout());
            buttonsPanel.setBackground(Color.orange);

            gbc.fill=GridBagConstraints.HORIZONTAL; gbc.gridx=0; gbc.gridy=0;

            if(playingDeckSelected) {
                JLabel l1=new JLabel("Selected Deck:",SwingConstants.CENTER);
                l1.setFont(new Font("Courier New", Font.ITALIC, 20));
                l1.setForeground(Color.BLUE);
                selectedDeck = new JLabel(playingDeck.getName(),SwingConstants.CENTER);
                selectedDeck.setFont(new Font("Courier New", Font.ITALIC, 20));
                selectedDeck.setForeground(Color.BLUE);
                buttonsPanel.add(l1, gbc);
                gbc.gridy++;
                buttonsPanel.add(selectedDeck, gbc);
            }
            gbc.gridy++;
            JLabel label=new JLabel("Decks:");
            label.setFont(new Font("Courier New", Font.ITALIC, 20));
            label.setForeground(Color.black);
            buttonsPanel.add(label,gbc);

            for(int i=0;i<listOfDecks.size();i++){
                gbc.gridy++;
                deckButtons[i]=new JButton(listOfDecks.get(i));
                deckButtons[i].addActionListener(this);
                buttonsPanel.add(deckButtons[i],gbc);
            }
            if(listOfDecks.size()<15){
                for(int i=listOfDecks.size();i<15;i++){
                    gbc.gridy++;
                    deckButtons[i]=new JButton("+New");
                    deckButtons[i].addActionListener(this);
                    buttonsPanel.add(deckButtons[i],gbc);
                }
            }
            deckScroll=new JScrollPane(allDecks,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(deckScroll,BorderLayout.CENTER);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0;i<listOfDecks.size();i++){
                final int t=i;
                if(e.getSource()==deckButtons[t]){
                    logger(currentPlayer.getUsername(),"click_deck_button",decks[t].getName());
                    getInstance().setCurrentDeck(decks[t]);
                    cardLayout1.show(allDecks,t+"");
                    currentDeckCnt=t;
                }
            }
            if(listOfDecks.size()<15){
                for(int i=listOfDecks.size();i<15;i++){
                    final int t=i;
                    if(e.getSource()==deckButtons[t]){
                        String str = JOptionPane.showInputDialog("Enter Name:");
                        String strH=JOptionPane.showInputDialog("Enter Hero Name:");
                        if(strH.equalsIgnoreCase("Mage")|| strH.equalsIgnoreCase("Warlock")||
                                strH.equalsIgnoreCase("Priest")||strH.equalsIgnoreCase("Hunter")||
                                strH.equalsIgnoreCase("Rogue")) {
                            if(currentPlayer.newDeckPossible(str,strH)) {
                                logger(currentPlayer.getUsername(),"build_new_deck","name:"+str+"_hero:"+strH);
                                currentPlayer.newDeck(str, strH);
                                currentDeckCnt=t;
                                Collections.getInstance().update();
                                decks[t]=new Deck(currentPlayer,str);
                                getInstance().setCurrentDeck(decks[t]);
                                cardLayout1.show(allDecks,t+"");
                            }
                        }
                        else  JOptionPane.showMessageDialog(MainFrame.getInstance(),"There is no such hero!",
                                "ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }


    public static Collections getInstance(){
        return instance;
    }

    void initPanels(){
        neutralPanel=new NeutralPanel();
        currentPanel.removeAll();
        currentPanel.setLayout(cardLayout);
        currentPanel.add("Warlock",new WarlockPanel());
        currentPanel.add("Mage",new MagePanel());
        currentPanel.add("Rogue",new RoguePanel());
        currentPanel.add("Hunter",new HunterPanel());
        currentPanel.add("Priest",new PriestPanel());
        currentPanel.add("Neutral",neutralPanel);

    }

    private void initFilters(){
        this.add(menuBar,BorderLayout.NORTH);
        menuBar.add(menu);  menuBar.add(mainMenu);
        menu.add(mage); menu.add(rogue); menu.add(warlock);
        menu.add(priest); menu.add(hunter); menu.add(neutral);

        MCardsFilter=false; OCardsFilter=false;
        for (int i=0;i<10;i++){
            mana[i]=false;
            m[i]=new JRadioButton(i+1+"",false);
            manaGroup.add(m[i]);
        }
        m[10]=new JRadioButton("All",true);
        manaGroup.add(m[10]);

        JPanel mana1=new JPanel();
        JPanel mana2=new JPanel();
        JPanel mana3=new JPanel();
        filters.setLayout(new GridLayout(2,5));
        filters.add(searchText); filters.add(search);

        filters.add(myCardsFilter); filters.add(notMyCards);
        filteringCards.add(myCardsFilter); filteringCards.add(notMyCards);

        filters.add(manaCostFilter);
        mana1.setLayout(new GridLayout(1,4)); mana1.add(m[0]); mana1.add(m[1]); mana1.add(m[2]);mana1.add(m[3]);
        mana2.setLayout(new GridLayout(1,4)); mana2.add(m[4]);mana2.add(m[5]); mana2.add(m[6]); mana2.add(m[7]);
        mana3.setLayout(new GridLayout(1,3)); mana3.add(m[8]); mana3.add(m[9]); mana3.add(m[10]);
        filters.add(mana1); filters.add(mana2); filters.add(mana3);
        this.add(filters,BorderLayout.SOUTH);

    }

    private void initListeners(){
        mage.addActionListener(this);
        rogue.addActionListener(this);
        warlock.addActionListener(this);
        neutral.addActionListener(this);
        search.addActionListener(this);
        hunter.addActionListener(this);
        mainMenu.addActionListener(this);
        priest.addActionListener(this);
    }

    void loadAllCards(){
        playersCards.removeAll(playersCards);
        playersCards.addAll(currentPlayer.getMyCardsNames());

        loadCards("Mage"); loadCards("Rogue"); loadCards("Warlock");
        loadCards("Priest"); loadCards("Hunter"); loadCards("Neutral");
    }
    private void loadCards(String section){
        List<String> list=new ArrayList<>();
        try{
            list= Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "allCards"+File.separator+"Lists"+File.separator+section+".txt"));
        }catch (Exception e){e.printStackTrace();}

        for (String name:
                list) {
            switch (section) {
                case "Mage":
                    mageCards.add(Card.getCard(name));
                    break;
                case "Warlock":
                    warlockCards.add(Card.getCard(name));
                    break;
                case "Rogue":
                    rogueCards.add(Card.getCard(name));
                    break;
                case "Priest":
                    priestCards.add(Card.getCard(name));
                    break;
                case "Hunter":
                    hunterCards.add(Card.getCard(name));
                    break;
                case "Neutral":
                    neutralCards.add(Card.getCard(name));
                    break;
            }
        }
    }

    private void setPanel(String panelName){
        settedPanel=panelName;
        logger(currentPlayer.getUsername(),"click_menu_button","panel:"+panelName);
        switch (panelName){
            case"Mage":
                cardLayout.show(currentPanel,"Mage");
                break;
            case"Rogue":
                cardLayout.show(currentPanel,"Rogue");
                break;
            case"Warlock":
                cardLayout.show(currentPanel,"Warlock");
                break;
            case"Hunter":
                cardLayout.show(currentPanel,"Hunter");
                break;
            case"Priest":
                cardLayout.show(currentPanel,"Priest");
                break;
            case"Neutral":
                cardLayout.show(currentPanel,"Neutral");
                break;
            case"Search":
                cardLayout.show(currentPanel,"Search");
                break;
        }
    }

    private void setCurrentDeck(Deck deck){
        this.currentDeck=new Deck(currentPlayer,deck.getName());
        deckSelected=true;
    }
    public void unSelect(){
        deckSelected=false;
        this.currentDeckCnt=-1;
    }

    public void setPlayingDeck(Deck deck){
        playingDeckSelected=true;
        this.playingDeck=new Deck(currentPlayer,deck.getName());
        this.update();
    }
    public void unSelectPlayingDeck(Deck deck){
        playingDeckSelected=false;
        deck.isSelectedForPlaying=false;
    }

    public void update(){
        this.setLayout(new BorderLayout());
        this.add(menuBar,BorderLayout.NORTH);
        this.add(filters,BorderLayout.SOUTH);
        this.remove(deckPanel);
        deckPanel=new DeckPanel(currentPlayer);

        initPanels();
        setPanel(settedPanel);
        this.add(currentPanel,BorderLayout.CENTER);
        this.add(deckPanel,BorderLayout.EAST);
        this.deckPanel.cardLayout1.show(this.deckPanel.allDecks,currentDeckCnt+"");
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        //checking filters
        if (myCardsFilter.isSelected() && !MCardsFilter) {
            logger(currentPlayer.getUsername(),"use_filter","myCardsFilter");
            MCardsFilter=true;
            OCardsFilter=false;
            initPanels();
            update();
        } else if (notMyCards.isSelected() &&!OCardsFilter) {
            logger(currentPlayer.getUsername(),"use_filter","notMyCardsFilter");
            MCardsFilter=false;
            OCardsFilter=true;
            initPanels();
            update();
        }
//        else {
//            logger(currentPlayer.getUsername(),"unSelect","Cards_Filters");
//            MCardsFilter=false;
//            OCardsFilter=false;
//            initPanels();
//            update();
//        }

        //mana filter
        for(int i=0;i<11;i++){
            final int t=i;
            if(m[t].isSelected()){
                logger(currentPlayer.getUsername(),"mana_filter","mana:"+t);
                if(!mana[t]) {
                    mana[t] = true;
                    for (int j = 0; j < 11; j++) {
                        if (j != t) {
                            mana[j] = false;
                        }
                    }
                    initPanels();
                    update();
                }
            }
        }
        //checking Menus-changing panels
        if(e.getSource()==mage) setPanel("Mage");
        else if(e.getSource()==warlock) setPanel("Warlock");
        else if(e.getSource().equals(rogue)) setPanel("Rogue");
        else if(e.getSource()==priest) setPanel("Priest");
        else if(e.getSource()==hunter) setPanel("Hunter");
        else if(e.getSource()==neutral) setPanel("Neutral");


        //checking search button
        if (e.getSource() == search) {
            String text = searchText.getText();
            logger(currentPlayer.getUsername(),"click_search_button","search for:"+text);
            currentPanel.add("Search",new Search(text));
            setPanel("Search");
        }

        //back to menu
        if(e.getSource()==mainMenu){
            sound.play("click",0);
            MainFrame.getInstance().setPanel("MainMenu");
        }

    }

}
