package edu.cmu.sphinx.demo.project2;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Speaker {

    static Voice speaker;


    public static void speak(final String s){
        VoiceManager voiceManager = VoiceManager.getInstance();
        speaker = voiceManager.getVoice("kevin16");
        //speaker.setAudioPlayer(new JavaClipAudioPlayer());
        //speaker.allocate();

        //speaker.speak(s);
        //speaker.deallocate();
    }

}