package edu.cmu.sphinx.demo.project2.tags;

import java.util.HashMap;

import javax.script.ScriptException;

import org.w3c.dom.Element;

import edu.cmu.sphinx.demo.project2.ScriptManager;

public class Filled {

    private static boolean parseCondition(String cond, HashMap<String,String> vars){
        String ret = null;
        try {
            ret = ScriptManager.createAndEvalScript(vars, cond);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return Boolean.parseBoolean(ret);
    }

    private static void executeClear(String clear, HashMap<String,String> vars){
        String[] toBeCleared = clear.split(" ");
        for(String c : toBeCleared)
            vars.remove(c);
    }

    public static void executeFilled(Element item, HashMap<String, String> vars){
        if(item.getElementsByTagName("if").getLength() > 0){
            Element ifNode = (Element) item.getElementsByTagName("if").item(0);
            if(parseCondition(ifNode.getAttribute("cond"), vars)){
                executeClear(ifNode.getElementsByTagName("clear").item(0).getAttributes().getNamedItem("namelist").getNodeValue().trim(), vars);
            }
        }
    }

}
