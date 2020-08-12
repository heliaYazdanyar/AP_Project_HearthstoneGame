package util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.HashMap;

public class SoundPlayer {
    private static SoundPlayer soundPlayer;
    private HashMap<String, Clip> sounds;


    //private e chon nemikhaym birune kelas btunim besazimesh
    private SoundPlayer(){
        sounds=new HashMap<String, Clip>();
    }

    public static SoundPlayer getInstance(){
        if(soundPlayer==null){
            soundPlayer=new SoundPlayer();
        }
        return soundPlayer;
    }
    public void load(String name){
        String resourcePath=System.getProperty("user.dir") +File.separator+"resources"+File.separator+"sounds"+File.separator+
                name+ ".wav";
        AudioInputStream input=null;
        try{
            input = AudioSystem.getAudioInputStream(new File(resourcePath).getAbsoluteFile());
        }catch (Exception e){ e.printStackTrace(); }
        try{
            Clip c=AudioSystem.getClip();
            c.open(input);
            sounds.put(name,c);
        }catch (Exception e){ e.printStackTrace();}
    }
    public void play(String name, int loopCnt){
        load(name);
        if(sounds.get(name).isRunning()){
            sounds.get(name).stop();
        }
        sounds.get(name).setFramePosition(0);
        sounds.get(name).loop(loopCnt);
    }


    public void adjustVolume(String name,int value){
        FloatControl control=(FloatControl)sounds.get(name).getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(value);
    }
}
