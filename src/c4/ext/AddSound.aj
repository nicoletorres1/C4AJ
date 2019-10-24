

package c4.ext;

import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import c4.base.C4Dialog;

privileged aspect AddSound {
                
    // Directory
    private static final String SOUND_DIR = "/sounds/"; 

        //plays sound
        after(C4Dialog c4Dialog) : execution(* C4Dialog.makeMove(int)) && this(c4Dialog) {  
                if(c4Dialog.player.name() == "Blue"){
                     	playAudio("yay.wav");
                }
                if(c4Dialog.player.name() == "Green"){
                        playAudio("yes-1.wav");
                }
        }
     
    public static void playAudio(String filename) {
      try {
          AudioInputStream audioIn = AudioSystem.getAudioInputStream(
            AddSound.class.getResource(SOUND_DIR + filename));
          Clip clip = AudioSystem.getClip();
          clip.open(audioIn);
          clip.start();
      } catch (UnsupportedAudioFileException 
            | IOException | LineUnavailableException e) {
          e.printStackTrace();
      }
    }
}