package Out;

import Models.Deck;
import gamePlayers.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Status extends JPanel implements ActionListener {
    public static Status instance;
    private Player currentPlayer;
    private List<Deck> decks;
    private List<Deck> topTen;

    private JMenuBar menu=new JMenuBar();
    private JButton mainMenu=new JButton("MainMenu");
    private JPanel currentPanel;
    private JPanel list;
    private JPanel[] deckPanels=new JPanel[10];
    private JButton[] backButtons=new JButton[10];
    private JLabel[] deckLabels=new JLabel[10];
    private CardLayout cardLayout=new CardLayout();

    int width=500;
    int height=700;

    private MouseListener listener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            for(int i=0;i<topTen.size();i++){
                final int t=i;
                if(e.getSource()==deckLabels[t]){

                    cardLayout.show(currentPanel,t+"");
                }
            }

        }
    };

    Status(Player player){
        instance=this;
        decks=new ArrayList<>();
        this.setLayout(new BorderLayout());
        this.currentPlayer=player;
        currentPanel=new JPanel();
        initListPanel();
        initPanels();

        mainMenu.addActionListener(this);
        menu.add(mainMenu);
        this.add(menu,BorderLayout.NORTH);

        cardLayout.show(currentPanel,"list");

    }

    public static Status getInstance(){
        return instance;
    }

    private void initPanels(){
        currentPanel.removeAll();
        currentPanel.setLayout(cardLayout);
        currentPanel.add(list,"list");
        for(int i=0;i<topTen.size();i++){
            currentPanel.add(deckPanels[i],i+"");
        }

        this.add(currentPanel,BorderLayout.CENTER);
    }

    private void initListPanel(){
        if(list!=null) list.removeAll();
        this.decks.removeAll(decks);
        currentPlayer.update();
        this.decks=currentPlayer.allDecks;
        topTen=new ArrayList<>();
        list=new JPanel();
        list.setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0; gbc.gridy=0;
        list.setBackground(Color.gray);

        int cnt=0;
        topTen=comparDecks();
        for(int i=0;i<topTen.size();i++){
            if(decks.get(i).isSelectedForPlaying) deckLabels[i]=new JLabel("**"+(1+i)+"-"+decks.get(i).getName()+"**");
            else  deckLabels[i]=new JLabel((1+i)+"-"+decks.get(i).getName());
            deckLabels[i].setFont(new Font("Courier New", Font.ITALIC, 20));
            deckLabels[i].setBackground(Color.orange);
            deckLabels[i].setForeground(Color.black);
            deckLabels[i].setOpaque(true);
            deckLabels[i].addMouseListener(listener);
            list.add(deckLabels[i],gbc);
            gbc.gridy++;
            cnt++;
        }

        if(cnt<10)
        for(int i=cnt;i<11;i++){
            JLabel label=new JLabel((i+1)+"-");
            label.setBackground(Color.orange);
            label.setOpaque(true);
            list.add(label,gbc);
            gbc.gridy++;
        }
        initDeckPanels();
    }
    private void initDeckPanels(){
        for(int i=0;i<topTen.size();i++){
            deckPanels[i]=new JPanel();
            deckPanels[i].setLayout(new GridLayout(6,1));
            deckPanels[i].setBackground(Color.YELLOW);

            JLabel l1=new JLabel("Name:");
            l1.setFont(new Font("Courier New", Font.ITALIC, 18));
            l1.setForeground(Color.BLUE);
            deckPanels[i].add(l1);

            JLabel l2=new JLabel(topTen.get(i).getName());
            l2.setFont(new Font("Courier New", Font.ITALIC, 18));
            l2.setForeground(Color.BLUE);
            deckPanels[i].add(l2);

            JLabel l3=new JLabel("Hero:");
            l3.setFont(new Font("Courier New", Font.ITALIC, 18));
            l3.setForeground(Color.BLUE);
            deckPanels[i].add(l3);

            JLabel l4=new JLabel(topTen.get(i).getHeroName()+"");
            l4.setFont(new Font("Courier New", Font.ITALIC, 18));
            l4.setForeground(Color.BLUE);
            deckPanels[i].add(l4);

            JLabel l5=new JLabel("Wins:");
            l5.setFont(new Font("Courier New", Font.ITALIC, 18));
            l5.setForeground(Color.BLUE);
            deckPanels[i].add(l5);

            JLabel l6=new JLabel(topTen.get(i).deckInfo.getWins()+"");
            l6.setFont(new Font("Courier New", Font.ITALIC, 18));
            l6.setForeground(Color.BLUE);
            deckPanels[i].add(l6);

            JLabel l7=new JLabel("Number of all games:");
            l7.setFont(new Font("Courier New", Font.ITALIC, 18));
            l7.setForeground(Color.BLUE);
            deckPanels[i].add(l7);

            JLabel l8=new JLabel(topTen.get(i).deckInfo.getNumberOfGames()+"");
            l8.setFont(new Font("Courier New", Font.ITALIC, 18));
            l8.setForeground(Color.BLUE);
            deckPanels[i].add(l8);

            JLabel l9=new JLabel("Average Mana cost");
            l9.setFont(new Font("Courier New", Font.ITALIC, 18));
            l9.setForeground(Color.BLUE);
            deckPanels[i].add(l9);

            JLabel l0=new JLabel(topTen.get(i).deckInfo.getAverageMana(currentPlayer)+"");
            l0.setFont(new Font("Courier New", Font.ITALIC, 18));
            l0.setForeground(Color.BLUE);
            deckPanels[i].add(l0);

            backButtons[i]=new JButton("Back");
            backButtons[i].addActionListener(this);
            deckPanels[i].add(backButtons[i]);
        }

    }
    private List<Deck> comparDecks(){
        list.removeAll();
        List<Deck> topTen=new ArrayList<>();
        Deck minDeck = null;
        int min=0;
        int cnt=0;
        for(int i=0;i<decks.size();i++){
            if(topTen.size()<10){
                topTen.add(decks.get(i));
            }
            else {
                if(cnt<11){
                    topTen.add(decks.get(i));
                    cnt++;
                    if(decks.get(i).deckInfo.getNumberOfGames()>min){
                        minDeck=decks.get(i);
                        min=decks.get(i).deckInfo.getNumberOfGames();
                    }

                }
               else
                if (decks.get(i).deckInfo.getNumberOfGames() > min) {
                    if (minDeck != null) topTen.remove(minDeck);
                    topTen.add(decks.get(i));
                    minDeck = decks.get(i);
                    min = decks.get(i).deckInfo.getNumberOfGames();
                }
            }
        }
        return topTen;
    }

    public void update(){
        initListPanel();
        initPanels();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==mainMenu) MainFrame.getInstance().setPanel("MainMenu");

        for(int i=0;i<topTen.size();i++){
            final int t=i;
            if(e.getSource()==backButtons[t]){
                cardLayout.show(currentPanel,"list");
            }
        }

    }
}
