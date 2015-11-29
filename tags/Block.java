package edu.cmu.sphinx.demo.project2.tags;

import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import edu.cmu.sphinx.demo.project2.ScriptManager;

public class Block {
    Element node;
    boolean done;

    public boolean isDone() {
        return done;
    }

    public Block(Node node){
        this.node = (Element)node;
        done = false;
    }

    public void execute(HashMap<String, String> vars, ArrayList<String> scripts) {
        StringBuffer buf = new StringBuffer();
        Element elem = node;
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
        System.out.println(buf.toString());
        done = true;
    }

}
