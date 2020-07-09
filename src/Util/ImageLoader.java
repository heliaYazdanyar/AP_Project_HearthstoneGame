package Util;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    private static ImageLoader ourInstance=new ImageLoader();


    public static ImageLoader getInstance(){ return ourInstance; }

    public BufferedImage loadImage(String name,String type,int width,int height){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(System.getProperty("user.dir")+File.separator+
                    "resources"+File.separator+"Images"+File.separator+name+"."+type));
        } catch (IOException e) { e.printStackTrace(); }

        BufferedImage resized = resize(image, width, height);

        return resized;
    }

    public ImageIcon loadIcon(String name,String type,int width,int height){
        ImageIcon imageIcon = null;
        try {
            BufferedImage image =ourInstance.loadImage(name,type,width,height);
            Image img = image.getScaledInstance(width,height,
                    Image.SCALE_SMOOTH);
            imageIcon=new ImageIcon(img);
        } catch (Exception e) { e.printStackTrace(); }

        return imageIcon;
    }

    private BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public ImageIcon bnwIcon(String name,String type,int width,int height){
        BufferedImage image = loadImage(name,type,width,height);

        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D graphic = result.createGraphics();
        graphic.drawImage(image, 0, 0, Color.WHITE, null);
        graphic.dispose();
        Image img = result.getScaledInstance(width,height,
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon=new ImageIcon(img);
        return imageIcon;

    }
}