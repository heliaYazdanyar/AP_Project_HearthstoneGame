package Out;

import game.Out.PanelHandler;
import gamePlayers.Player;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static Util.Logger.logger;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    static Player player;
    private HashMap<String,JPanel> panels=new HashMap<>();
    private MainMenu mainMenu=new MainMenu();
    private Store store;
    private Collections collections;
    private Game game;
    private Status status;
    private String currentPanel;
    private Container contentPane= getContentPane();

    private PanelHandler panelHandler;

    private JPanel panel;

    private int width=500;
    private int height=500;

    public MainFrame(){
        this.panel=new MainMenu();
    }

    public MainFrame(String username){
        player=new Player(username);
        instance=this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width,height);
        setTitle("HearthStone");
        setVisible(true);

        collections=new Collections(player);
        store=new Store(player);
        status=new Status(player);

        setPanel("MainMenu");
    }


    public static MainFrame getInstance(){
        return instance;
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
            case "Play":
                if(player.ifDeckSelected) {
                    game=new Game(player,player.getCurrentDeck());
                    if (panel != null) this.remove(panel);
                    this.panel = game;
                    this.add(panel);
                    this.setSize(game.width, game.height);
                    break;
                }
                else{
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),"You should Select a Deck first!",
                            "ERROR",JOptionPane.ERROR_MESSAGE);
                    break;
                }
            case "Status":
                if(panel!=null) this.remove(panel);
                this.panel=status;
                this.add(panel);
                this.setSize(status.width,status.height);
                break;
            case "PanelHandler":
                panelHandler=new PanelHandler(player);
                if(panel!=null) this.remove(panel);
                this.panel=panelHandler;
                this.add(panel);
                this.setSize(panelHandler.width,panelHandler.height);
                break;
        }


    }

    public static void main(String[] args) {
        Start start= new Start();
        start.setVisible(true);
        start.setSize(450,200);
    }


}
