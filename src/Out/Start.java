package Out;

import Util.SoundPlayer;
import gamePlayers.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JFrame implements ActionListener {
    private JButton login;
    private JButton signin;
    private JPanel panel;
    private JLabel username,password;
    private JLabel image1,image2;
    private final JTextField txt1,txt2;
    private SoundPlayer sound=SoundPlayer.getInstance();

    public Start(){
        username=new JLabel();
        username.setText("Username:");
        txt1=new JTextField();

        password=new JLabel();
        password.setText("Username:");
        txt2=new JTextField();

        login=new JButton("Login");
        signin=new JButton("Signin");

        image1=new JLabel();
        image1.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\resources\\Images\\HearthStone.jpg"));
        image2=new JLabel();
        image2.setIcon(null);

        panel=new JPanel(new GridLayout(4,1));
        panel.setBackground(Color.YELLOW);
        panel.add(image1); panel.add(image2);
        panel.add(username); panel.add(txt1);
        panel.add(password); panel.add(txt2);
        panel.add(login); panel.add(signin);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel,BorderLayout.CENTER);
        signin.addActionListener(this);
        login.addActionListener(this);
        setTitle("HearthStone");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user=txt1.getText();
        String pass=txt2.getText();
        if(e.getSource()==signin){
            sound.play("click",0);
            if(Player.signIn(user,pass)){
                setVisible(false);
                dispose();
                MainFrame mainframe=new MainFrame(user);
                mainframe.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(this,"this username already exists.",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getSource()==login){
            sound.play("click",0);
            if(Player.logIn(user,pass)){
                setVisible(false);
                dispose();
                MainFrame mainframe=new MainFrame(user);
                mainframe.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(this,"Incorrect username Or password",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
