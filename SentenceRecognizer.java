package edu.cmu.sphinx.demo.project2;

import java.io.IOException;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import java.net.URL;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

public class SentenceRecognizer {
	
	private Microphone microphone;
    private Recognizer recognizer;
	
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
	
	public String Rec() {
		String resultText = "";
		if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }
            System.out.println("Start speaking. Press Ctrl-C to quit.\n");
            Result result = recognizer.recognize();
            if (result != null) {
                resultText = result.getBestFinalResultNoFiller();
                System.out.println("You said: " + resultText + '\n');
                return resultText;
            } else {
                System.out.println("I can't hear what you said.\n");
            }
        	microphone.stopRecording();
        	return null;
	}
}
