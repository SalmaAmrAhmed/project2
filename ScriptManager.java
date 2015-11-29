package edu.cmu.sphinx.demo.project2;

import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptManager {

    public static String createAndEvalScript(ArrayList<String> scripts, HashMap<String,String> vars, String eval) throws ScriptException{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        for(String s: scripts){
            engine.eval(s);
        }

        for(String key : vars.keySet()){
            engine.eval(key + "=" + vars.get(key));
        }


        return engine.eval(eval).toString();
    }

    public static String createAndEvalScript(HashMap<String,String> vars, String eval) throws ScriptException{
        return createAndEvalScript(new ArrayList<String>(), vars, eval);
    }
}
