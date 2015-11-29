package edu.cmu.sphinx.demo.project2.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.cmu.sphinx.demo.project2.Speaker;

public class Field {

    String name;
    Element prompt;
    HashMap<String, String> allowedVals;
    Element filled;

    public Field(Element node){
        name = node.getAttribute("name");
        // Parse Prompt
        prompt = (Element)node.getElementsByTagName("prompt").item(0);

        // Parse Item
        allowedVals = new HashMap<String, String>();
        Element oneOf = (Element) ((Element)node.getElementsByTagName("grammar").item(0)).getElementsByTagName("one-of").item(0);
        NodeList items = oneOf.getElementsByTagName("item");
        for(int i=0;i<items.getLength();i++){
            Item item = Item.parseItem(items.item(i));
            allowedVals.put(item.text, item.key);
        }

        // Parse Filled
        filled = (Element)node.getElementsByTagName("filled").item(0);
    }

    public String getName() {
        return name;
    }

    public void execute(Scanner sc, HashMap<String,String> variables, ArrayList<String> scripts) {

        do {
            // Prompt Text
            String promptText = Prompt.parsePrompt(prompt, variables, scripts) + " " + allowedVals.keySet();
            System.out.println(promptText);
            Speaker.speak(promptText);

            // Read Input
            String line = sc.nextLine().trim();
            if(allowedVals.containsKey(line)){
                String key = allowedVals.get(line);
                variables.put(name, key);
                break;
            }

        }while(true);

        // Execute the filled tag
        if(filled != null)
            Filled.executeFilled(filled, variables);
    }

}
