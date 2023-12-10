package jemu.core.samples;

/**
 *
 * @author Markus
 */

import java.io.*;
import java.net.URL;

import javax.sound.sampled.*;

/**
 * This enum encapsulates all the sound effects
 */

public enum Samples {
   BREAK("/wav/breakpoint.wav"),
   BREAKI("/wav/instruction.wav"),
   EJECT("/wav/eject.wav"),
   INSERT("/wav/insert.wav"),
   MOTOR("/wav/motor.wav"),
   SEEK("/wav/seek.wav"),
   SEEKBACK("/wav/seekback.wav"),
   TRACK("/wav/track.wav"),
   TRACKBACK("/wav/trackback.wav"),
   DEGAUSS("/wav/monitoron.wav"),
   RELAIS("/wav/relon.wav"),
   RELAISOFF("/wav/reloff.wav"),
   TAPEMOTOR("/wav/tapmotor.wav"),
   WINDMOTOR("/wav/wind.wav"),
   REWINDMOTOR("/wav/rewind.wav"),
   TAPEKEY("/wav/tapekey.wav"),
   TAPESTOP("/wav/tapestop.wav"),
   TAPEINSERT("/wav/tape_insert.wav"),
   PRINTER("/wav/printer.wav"),
   TAPEEJECT("/wav/tape_eject.wav"),
   KEY("/wav/cpckey.wav"),
   ENTER("/wav/cpcenter.wav"),
   SPACE("/wav/cpcspace.wav");

   // Nested class for specifying volume
   public static enum Volume {
      MUTE, LOW, MEDIUM, HIGH
   }

   public static Volume volume = Volume.HIGH;

   // Each sound effect has its own clip, loaded with its own sound file.
   private Clip clip;

   // Constructor to construct each element of the enum with its own sound file.
   Samples(String soundFileName) {
      try {
         // Use URL (instead of File) to read from disk and JAR.
         URL url = getClass().getResource(soundFileName);
         // Set up an audio input stream piped from the sound file.
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
         // Get a clip resource.
         clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioInputStream);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }

   // Play or Re-play the sound effect from the beginning, by rewinding.
   public void play() {
      if (volume != Volume.MUTE) {
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
         clip.setFramePosition(0); // rewind to the beginning
         clip.start();     // Start playing
      }
   }
   public void loop() {
      if (volume != Volume.MUTE) {
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
         clip.setFramePosition(0); // rewind to the beginning
         clip.loop(Clip.LOOP_CONTINUOUSLY);     // Start playing
      }
   }
   
   public void loop2() {
      if (volume != Volume.MUTE) {
         if (!clip.isRunning()){
         clip.setFramePosition(0); // rewind to the beginning
         clip.loop(Clip.LOOP_CONTINUOUSLY);     // Start playing
      }
      }
   }
   public void stop() {
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
         clip.setFramePosition(0); // rewind to the beginning
         clip.stop();     // Start playing
   }

   // Optional static method to pre-load all the sound files.
   static void init() {
      values(); // calls the constructor for all the elements
   }
}

