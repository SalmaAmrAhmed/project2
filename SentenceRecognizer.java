package edu.cmu.sphinx.demo.project2;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.recognizer.Recognizer.State;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

public class SentenceRecognizer implements Runnable {

    private Microphone microphone;
    private Recognizer recognizer;
    private List<SentenceListener> sentenceListeners = new ArrayList<SentenceListener>();

    public SentenceRecognizer() throws IOException {
        try {
            URL url = this.getClass().getResource("helloworld.config.xml");
            if (url == null) {
                throw new IOException("Can't find helloworld.config.xml");
            }
            ConfigurationManager cm = new ConfigurationManager(url);
            recognizer = (Recognizer) cm.lookup("recognizer");
            microphone = (Microphone) cm.lookup("microphone");
        } catch (PropertyException e) {
            throw new IOException("Problem configuring SentenceRecognizer " + e);
        }
    }


    /** Turns on the microphone and starts recognition */
    public boolean microphoneOn() {
        if (microphone.getAudioFormat() == null) {
            return false;
        } else {
            new Thread(this).start();
            return true;
        }
    }


    /** Turns off the microphone, ending the current recognition in progress */
    public void microphoneOff() {
        microphone.stopRecording();
    }


    /** Allocates resources necessary for recognition. */
    public void startup() throws IOException {
        recognizer.allocate();
    }


    /** Releases recognition resources */
    public void shutdown() {
        microphoneOff();
        if (recognizer.getState() == State.ALLOCATED) {
            recognizer.deallocate();
        }
    }


    /** Performs a single recognition */
    @Override
    public void run() {
        microphone.clear();
        microphone.startRecording();
        Result result = recognizer.recognize();
        microphone.stopRecording();
        if (result != null) {
            String resultText = result.getBestFinalResultNoFiller();
            if (!resultText.isEmpty()) {
                fireListeners(resultText);
            } else {
                fireListeners(null);
            }
        }
    }

    public synchronized void addSentenceListener(SentenceListener sentenceListener) {
        sentenceListeners.add(sentenceListener);
    }

    public synchronized void removeSentenceListener(SentenceListener sentenceListener) {
        sentenceListeners.remove(sentenceListener);
    }


    private synchronized void fireListeners(String sentence) {
        for (SentenceListener ll : sentenceListeners)
            ll.notify(sentence);
    }
}