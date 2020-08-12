package Out;

import client.GameClient;
import gamePlayers.Player;
import util.Config;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Rank extends JPanel {
    int width=600;
    int height=700;
    private List<String> usernames;
    private List<String> topTen;


    private List<String> finalList;



    private JMenuBar menuBar;
    private JMenu rank = new JMenu("Rank");
    private JMenuItem top10 = new JMenuItem("Top10");
    private JMenuItem me = new JMenuItem("Me");

    private JButton mainMenu;
    private JPanel panel;

    private GameClient client;


    public Rank(GameClient client){
        this.client=client;
        this.setLayout(new BorderLayout());

        menuBar=new JMenuBar();
        top10.addActionListener(e->{
            loadTopTenPanel();
        });
        me.addActionListener(e->{
            loadMyList();
        });
        rank.add(top10);
        rank.add(me);
        menuBar.add(rank);
        mainMenu=new JButton("Back");
        mainMenu.addActionListener(e->MainFrame.getInstance().setPanel("MainMenu"));
        menuBar.add(mainMenu);

        this.add(menuBar, BorderLayout.NORTH);

        panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(Color.orange);
        loadTopTenPanel();

    }

    private void loadAll(){
        usernames= Config.loadAllPlayers();
    }
    private void loadTopTen(){
        loadAll();
        loadSorted();
        topTen=new ArrayList<>();
        topTen.removeAll(topTen);

        int max=0;
        if(finalList.size()<10) max=finalList.size();
        else max=10;
        for(int cnt=0;cnt<max;cnt++){
            int n=finalList.get(cnt).indexOf("*");
            String username=finalList.get(cnt).substring(n+1);
            topTen.add(username);
        }
    }

    public List<String> sort(List<String> toBeSorted){
        toBeSorted.sort((firstString, secondString) -> {
            int firstScore = Integer.parseInt(firstString.split("\\*")[0]);
            int secondScore = Integer.parseInt(secondString.split("\\*")[0]);
            return secondScore-firstScore;
        });
        return toBeSorted;
    }

    private void loadSorted(){
        List<String> list=new ArrayList<>();
        for (String name:
             usernames) {
            int cups=new Player(name).getPlayersInfo().getCups();
            list.add(cups+"*"+name);
        }

        finalList=sort(list);

    }

    private void loadMyList(){
        loadAll();
        loadSorted();
        String myString=client.getPlayer().getPlayersInfo().getCups()+"*"+client.getUsername();
        int myIndex=finalList.indexOf(myString);

        this.remove(panel);
        panel.removeAll();

        int min;
        if(myIndex>=5) min=myIndex-5;
        else min=0;

        JLabel[] upperLabels=new JLabel[5];
        JLabel[] lowerLabels=new JLabel[5];

        int cnt=0;
        for(int i=myIndex;i>=min;i--){
            String st=finalList.get(i);
            int index=st.indexOf('*');
            String cups=st.substring(0,index);
            String username=st.substring(index+1);

            upperLabels[cnt]=new JLabel("",SwingUtilities.CENTER);
            upperLabels[cnt].setFont(new Font("Courier New", Font.ITALIC, 20));
            upperLabels[cnt].setForeground(Color.BLACK);
            upperLabels[cnt].setBackground(Color.orange);
            upperLabels[cnt].setOpaque(true);
            upperLabels[cnt].setText(username+" : "+cups);


            panel.add(upperLabels[cnt]);
            cnt++;
        }

        JLabel myLabel=new JLabel("",SwingUtilities.CENTER);
        myLabel.setFont(new Font("Courier New", Font.ITALIC, 20));
        myLabel.setForeground(Color.GREEN);
        myLabel.setBackground(Color.GREEN);
        myLabel.setText(client.getUsername()+" : "+client.getPlayer().getPlayersInfo().getCups());
        myLabel.setOpaque(true);

        panel.add(myLabel);

        int max;
        if(myIndex+5>=finalList.size()) max=finalList.size()-1;
        else max=myIndex+5;
        cnt=0;
        for(int i=myIndex;i<=max;i++){
            String st=finalList.get(i);
            int index=st.indexOf('*');
            String cups=st.substring(0,index);
            String username=st.substring(index+1);

            lowerLabels[cnt]=new JLabel("",SwingUtilities.CENTER);
            lowerLabels[cnt].setFont(new Font("Courier New", Font.ITALIC, 20));
            lowerLabels[cnt].setForeground(Color.BLACK);
            lowerLabels[cnt].setBackground(Color.orange);
            lowerLabels[cnt].setOpaque(true);
            lowerLabels[cnt].setText(username+" : "+cups);


            panel.add(lowerLabels[cnt]);
            cnt++;
        }

        this.add(panel,BorderLayout.CENTER);



    }

    private void loadTopTenPanel(){
        loadAll();
        loadTopTen();

        this.remove(panel);
        panel.removeAll();

        JLabel[] nameLabels=new JLabel[10];
        for(int i=0;i<topTen.size();i++){
            String username=topTen.get(i);
            Player player=new Player(username);
            nameLabels[i]=new JLabel("",SwingUtilities.CENTER);
            nameLabels[i].setFont(new Font("Courier New", Font.ITALIC, 20));
            nameLabels[i].setForeground(Color.BLACK);
            nameLabels[i].setBackground(Color.orange);
            nameLabels[i].setOpaque(true);
            nameLabels[i].setText(username+" : "+player.getPlayersInfo().getCups());

            panel.add(nameLabels[i]);
        }

        this.add(panel,BorderLayout.CENTER);

    }



}
