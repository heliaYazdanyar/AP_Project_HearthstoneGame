package Out;

import client.GameClient;
import game.Out.PanelHandler;
import gamePlayers.Player;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static util.Logger.logger;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    static Player player;
    private GameClient client;



    private HashMap<String,JPanel> panels=new HashMap<>();
    private MainMenu mainMenu=new MainMenu();
    private Store store;
    private Collections collections;
    private Status status;
    private Rank rank;
    private String currentPanel;
    private Container contentPane= getContentPane();

    private PanelHandler panelHandler;

    private JPanel panel;

    private int width=500;
    private int height=500;

    public MainFrame(){
        this.panel=new MainMenu();
    }

    public MainFrame(String username, GameClient client){
        this.client=client;

//        player=new Player(username);

        player=client.getPlayer();
        instance=this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width,height);
        setTitle("HearthStone");
        setVisible(true);

        collections=new Collections(player);
        store=new Store(player);
        status=new Status(player);
        rank=new Rank(client);

        setPanel("MainMenu");
        client.start();
    }

    public static MainFrame getInstance(){
        return instance;
    }

    public PanelHandler getPanelHandler(){
        return panelHandler;
    }

    public void setPanel(String panelName){
        this.currentPanel=panelName;
        switch (panelName){
            case"MainMenu":
                logger(player.getUsername(),"GoTo","MainMenu");
                if(panel!=null)  this.remove(panel);
                this.panel=new MainMenu();
                this.add(panel);
                this.setSize(MainMenu.width,MainMenu.height);
                break;

            case "Collections":
                if(panel!=null) this.remove(panel);
                this.panel=collections;
                this.add(panel);
                this.setSize(Collections.width,Collections.height);
                break;
            case "Store":
                if(panel!=null) this.remove(panel);
                this.panel=store;
                this.add(panel);
                this.setSize(store.width,store.height);
                break;
            case "Status":
                if(panel!=null) this.remove(panel);
                this.panel=status;
                this.add(panel);
                this.setSize(status.width,status.height);
                break;
            case "PanelHandler":
                panelHandler=new PanelHandler(client);
                if(panel!=null) this.remove(panel);
                this.panel=panelHandler;
                this.add(panel);
                this.setSize(panelHandler.width,panelHandler.height);
                break;
            case "Rank":
                if(panel!=null) this.remove(panel);
                this.panel=rank;
                this.add(panel);
                this.setSize(rank.width,rank.height);
                break;
        }


    }

    public static void main(String[] args) {
        Start start= new Start();
        start.setVisible(true);
        start.setSize(450,200);
    }


}
