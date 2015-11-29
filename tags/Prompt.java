package edu.cmu.sphinx.demo.project2.tags;

import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import edu.cmu.sphinx.demo.project2.ScriptManager;

public class Prompt {

    public static String parsePrompt(Element elem, HashMap<String, String> vars, ArrayList<String> scripts){
        StringBuffer buf = new StringBuffer();
        for(int i=0;i<elem.getChildNodes().getLength();i++){
            Node node = elem.getChildNodes().item(i);
            if(node instanceof Text){
                buf.append(node.getTextContent().trim() + " ");
            }else if(node.getNodeName().startsWith("value")){
                String expr = node.getAttributes().getNamedItem("expr").getTextContent().trim();
                try {
                    buf.append(ScriptManager.createAndEvalScript(scripts, vars, expr) + " ");
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
        }
        return buf.toString();
    }
}
