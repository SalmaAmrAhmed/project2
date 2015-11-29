package edu.cmu.sphinx.demo.project2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.cmu.sphinx.demo.project2.tags.Block;
import edu.cmu.sphinx.demo.project2.tags.Field;


public class Simulate {

    ArrayList<Object> systemCommands;
    ArrayList<String> scripts;

    public Simulate(){
        systemCommands = new ArrayList<Object>();
        parseXML();
    }

	public void parseXML() {
		try {
			File inputFile = new File( System.getProperty("user.dir")+ "/dialog1.vxml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// Parse Scripts
			scripts = new ArrayList<String>();
			NodeList scriptsNode = doc.getElementsByTagName("script");
			for(int i=0;i<scriptsNode.getLength();i++){
			    scripts.add(scriptsNode.item(i).getTextContent().trim());
			}

			// Parse Form
			Element form = (Element)doc.getElementsByTagName("form").item(0);

			for(int i=0;i<form.getChildNodes().getLength();i++){
			    switch(form.getChildNodes().item(i).getNodeName()){
			    case "block":
			        parseAndAddBlock((Element)form.getChildNodes().item(i));
			        break;
			    case "field":
			        parseAndAddBField((Element)form.getChildNodes().item(i));
			        break;
			    }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseAndAddBField(Element item) {
        systemCommands.add(new Field(item));
    }

    private void parseAndAddBlock(Element item) {
        systemCommands.add(new Block(item));
    }

    public void run(){
        Scanner sc = new Scanner(System.in);
        HashMap<String, String> variables = new HashMap<String,String>();

        do {
            int i;
            for(i=0;i<systemCommands.size();i++){
                if(systemCommands.get(i) instanceof Block){
                    Block b = (Block) systemCommands.get(i);
                    if(!b.isDone())
                        b.execute(variables, scripts);
                }else{
                    Field f = (Field) systemCommands.get(i);
                    if(!variables.containsKey(f.getName())){
                        f.execute(sc, variables, scripts);
                        break;
                    }
                }
            }
            if(i == systemCommands.size())
                break;
            i = 0;
        }while(true);
	}

	public static void main(String[] args) {


		Simulate sim = new Simulate();
		sim.run();

	}
}
