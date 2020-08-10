package Out;

import Util.Config;
import Util.SoundPlayer;
import client.GameClient;
import gamePlayers.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Start extends JFrame implements ActionListener {


    private JButton login;
    private JButton signin;

    private JPanel panel;
    private JLabel username,password,serverPort,serverIP;
    private JLabel image1,image2;
    private JTextField txt1,txt2,port,ip;
    private SoundPlayer sound=SoundPlayer.getInstance();

    private GameClient client;

    public Start(){
       initComponents();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel,BorderLayout.CENTER);
        signin.addActionListener(this);
        login.addActionListener(this);
        setTitle("HearthStone");
    }

    private void initComponents(){
        username=new JLabel();
        username.setText("Username:");
        txt1=new JTextField();

        password=new JLabel();
        password.setText("Username:");
        txt2=new JTextField();

        serverPort=new JLabel("port:");
        port=new JTextField();

        serverIP=new JLabel("IP:");
        ip=new JTextField();

        login=new JButton("Login");
        signin=new JButton("Signin");

        image1=new JLabel();
        image1.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\resources\\Images\\HearthStone.jpg"));
        image2=new JLabel();
        image2.setIcon(null);

        panel=new JPanel(new GridLayout(6,1));
        panel.setBackground(Color.YELLOW);
        panel.add(image1); panel.add(image2);
        panel.add(username); panel.add(txt1);
        panel.add(password); panel.add(txt2);
        panel.add(serverPort); panel.add(port);
        panel.add(serverIP); panel.add(ip);
        panel.add(login); panel.add(signin);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user=txt1.getText();
        String pass=txt2.getText();
        String sPort=port.getText();
        String sIP=ip.getText();

        if(e.getSource()==signin){
            sound.play("click",0);
            if(Player.signIn(user,pass)){
                if(sPort.equals(Config.getPort()) && sIP.equals("localhost")) {
                    setVisible(false);
                    dispose();
                    MainFrame mainframe = null;
                    try {
                        mainframe = new MainFrame(user,new GameClient(sIP, Integer.parseInt(sPort),user));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    mainframe.setVisible(true);
                }
                else{
                    JOptionPane.showMessageDialog(this,"Can't connect to server.",
                            "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(this,"this username already exists.",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getSource()==login){
            sound.play("click",0);
            if(Player.logIn(user,pass)){
                if(sPort.equals(Config.getPort()) && sIP.equals("localhost")) {
                    setVisible(false);
                    dispose();
                    MainFrame mainframe = null;
                    try {
                        mainframe = new MainFrame(user,new GameClient(sIP, Integer.parseInt(sPort),user));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    mainframe.setVisible(true);
                }
                else{
                    JOptionPane.showMessageDialog(this,"Can't connect to server.",
                            "Error",JOptionPane.ERROR_MESSAGE);
                }

            }
            else{
                JOptionPane.showMessageDialog(this,"Incorrect username Or password",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
