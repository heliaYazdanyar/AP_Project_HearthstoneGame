package Out;

import client.GameClient;
import util.Config;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Social extends JPanel {
    private GameClient client;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem players;
    private JMenuItem chats;

    private JButton back=new JButton("Back");
    private JButton refresh=new JButton("Refresh");

    private List<String> onlines;
    private List<String> offlines;

    private CardLayout cardLayout=new CardLayout();
    private JPanel centerPanel=new JPanel();


    private PlayersPanel playersPanel;

    public Social(GameClient client){
        onlines=new ArrayList<>();
        onlines=new ArrayList<>();

        this.setLayout(new BorderLayout());
        centerPanel.setLayout(cardLayout);

    }

    class PlayersPanel extends JPanel{

        List<JLabel> names;
        List<JLabel> status;
        List<JButton> friends;
        List<JButton> chat;

        PlayersPanel(){
            int n= Config.loadAllPlayers().size();
            this.setLayout(new GridLayout(n,4));


            for (String st:
                 onlines) {

            }

        }



    }


    private void reloadLists(){
        onlines.removeAll(onlines);
        offlines.removeAll(offlines);

        onlines=client.getOnlineNames();
        offlines=client.getOfflineNames();

    }

    private void initPanel(){
        menuBar=new JMenuBar();
        menu=new JMenu();
        players=new JMenuItem();
        chats=new JMenuItem();
        players.addActionListener(e->{

        });

        refresh.addActionListener(e->{

        });
        back.addActionListener(e->
                MainFrame.getInstance().setPanel("MainMenu"));

        menu.add(players); menu.add(chats);
        menuBar.add(menu); menuBar.add(refresh); menuBar.add(back);

    }
}
