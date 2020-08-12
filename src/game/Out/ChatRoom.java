package game.Out;

import client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatRoom extends JPanel {

    private GameClient userClient;

    private List<String> participants;
    private HashMap<String,Color> colors;

    int width=250;
    int height=500;
    private JButton send;
    private JTextField textField;
    private JPanel southContainer;

    private JTextArea textArea;

    public ChatRoom(GameClient userClient){
        this.userClient=userClient;

        this.setLayout(new BorderLayout());

        init();
        addComponents();
        addMember(userClient.getUsername());

        setSize(width,height);

    }

    private void init(){
        participants=new ArrayList<>();
        colors=new HashMap<>();

        southContainer=new JPanel();
        southContainer.setLayout(new BorderLayout());
        send=new JButton("Send");
        send.addActionListener(e-> {
            String st=textField.getText();
            userClient.sendGameChatMsg(st);
            sendText(userClient.getUsername(),st);
        });
        textField=new JTextField();

        textArea = new JTextArea();

    }

    private void addComponents(){
        southContainer.add(textField,BorderLayout.CENTER);
        southContainer.add(send,BorderLayout.SOUTH);
        this.add(southContainer,BorderLayout.SOUTH);

        textArea.setLineWrap(true);
        // Sets JTextArea font and color.
        Font font = new Font("Segoe Script", Font.BOLD, 16);
        textArea.setFont(font);
        JScrollPane scrollPane = new JScrollPane(textArea);

        this.add(scrollPane, BorderLayout.CENTER);

    }

    public void addMember(String username){
        participants.add(username);
        //set color
        if(participants.size()%5==0) colors.put(username,Color.RED);
        else if(participants.size()%5==1) colors.put(username,Color.GREEN);
        else if(participants.size()%5==2) colors.put(username,Color.MAGENTA);
        else if(participants.size()%5==3) colors.put(username,Color.orange);
        else colors.put(username,Color.BLACK);
    }

    public void sendText(String username,String message){
        if(participants.contains(username)) {
            String st = "@" + username + ":" + message;
            textArea.setForeground(colors.get(username));
            textArea.append(st+"\n");
        }
        else{
            System.out.println("wrong username entered");
        }
    }
}
