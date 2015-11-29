package edu.cmu.sphinx.demo.project2.tags;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Item {
    String text;
    String key;

    public static Item parseItem(Node node){
        Item ret = new Item();
        Element e = (Element) node;
        // Parse text
        ret.text = e.getFirstChild().getTextContent().trim();
        // Parse key
        String k =  e.getChildNodes().item(1).getTextContent().trim();
        ret.key  = k.substring(2, k.length()-1);

        return ret;
    }

}
