package edu.cmu.sphinx.demo.project2;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


public class Simulate {

	HashMap<Integer, String> system = new HashMap<Integer, String>();
	HashMap<String, String> size = new HashMap<String, String>();
	HashMap<String, String> toppings = new HashMap<String, String>();
	HashMap<String, String> crust = new HashMap<String, String>();
	HashMap<String, String> thickness = new HashMap<String, String>();
	String reqSize = "";
	String reqToppings = "";
	String reqCrust = "";
	String reqThickness = "";
    Scanner scanner = new Scanner(System.in);
    String confirm = "";


	public void parseText() {
		try {
			File inputFile = new File("dialog1.vxml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
//			System.out.println("Root element :"
//					+ doc.getDocumentElement().getNodeName());

			// parse script
			NodeList nScriptList = doc.getElementsByTagName("script");
			parseScript(nScriptList);

			// parse form
			NodeList nList = doc.getElementsByTagName("form");
//			System.out.println("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
//				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					// get form ID
					Element eElement = (Element) nNode;
//					System.out.println("Form ID no : "
//							+ eElement.getAttribute("id"));

					// parse block_1

					String block1 = eElement.getElementsByTagName("block")
							.item(0).getTextContent();
					system.put(0, block1);
//					System.out.println("Block1:" + block1);

					// parse block_2

					String block2 = eElement.getElementsByTagName("block")
							.item(1).getTextContent();
					system.put(9, block2);
//					System.out.println("Block2:" + block2);

					// parse field
					NodeList nFieldList = doc.getElementsByTagName("field");
					parseField(nFieldList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseScript(NodeList nScriptList) {
		for (int temp = 0; temp < nScriptList.getLength(); temp++) {
			Node nNode = nScriptList.item(temp);
//			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
//				System.out.println(nNode.getTextContent());
			}
		}
	}

	public void parseField(NodeList nFieldList) {
		for (int temp = 0; temp < nFieldList.getLength(); temp++) {
//			System.out.println("Temp: " + temp);

			Node nNode = nFieldList.item(temp);
//			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				// get field name
				Element eElement = (Element) nNode;
				String fieldName = eElement.getAttribute("name");
				String oneOf = eElement.getElementsByTagName("one-of").item(0)
						.getTextContent();
//				System.out.println("Field name: " + fieldName);
//				System.out.println("One-of: " + oneOf);

				if (fieldName.startsWith("size")) {
					size.put("big", "big");
					size.put("regular", "regular");
					size.put("small", "small");
					size.put("medium", "medium");
				} else if (fieldName.startsWith("toppings")) {
					toppings.put("cheese", "cheese");
					toppings.put("mushrooms", "mushrooms");
					toppings.put("pepperoni", "pepperoni");
					toppings.put("chicken", "chicken");
					toppings.put("seafood", "seafood");
				} else if (fieldName.startsWith("crust")) {
					crust.put("pan", "pan");
					crust.put("stuffed", "stuffed");
				} else if (fieldName.startsWith("thickness")) {
					thickness.put("thick", "thick");
					thickness.put("thin", "thin");
				}

				// System.out.println("Field condition : "
				// + eElement.getAttribute("cond"));

				String prompt = eElement.getElementsByTagName("prompt").item(0)
						.getTextContent();
				system.put(temp + 1, prompt);

			}
		}
	}
	
	public int getPrice(String size, String crust, String thickness, String toppings) {
		int price = 20;
		if (size.equals("big")) 
			price += 5;
		else if (size.equals("medium"))
			price += 3;
		else if (size.equals("regular"))
			price += 2;
		if (toppings.equals("chicken"))
			price += 3;
		else if (toppings.equals("cheese"))
			price += 1;
		else if (toppings.equals("mushrooms"))
			price += 2;
		else if (toppings.equals("seafood"))
			price += 6;
		else if (toppings.equals("pepperoni"))
			price += 3;
		if (crust.equals("pan"))
			price += 3;
		else if (crust.equals("stuffed"))
			price += 1;
			price += 3;
		return price;
	}
	
	public void fetchSize(Voice helloVoice) {
		helloVoice.speak(system.get(1));
		reqSize = scanner.nextLine();
		if (reqSize != null && 
				(reqSize.equalsIgnoreCase("big") ||
				reqSize.equalsIgnoreCase("regular") ||
				reqSize.equalsIgnoreCase("small") ||
				reqSize.equalsIgnoreCase("medium"))) {
			String[] s = system.get(2).split(" ");
			for (int i = 0; i < s.length - 1; i++) {
					helloVoice.speak(s[i]);	
			}
			helloVoice.speak(reqSize + s[s.length - 1]);
			confirm = scanner.nextLine();
			confirmSize(confirm, helloVoice);
		} else {
			helloVoice.speak("Can't understand please repeat !");
			fetchSize(helloVoice);

		}
	}
	
	public void confirmSize(String line, Voice helloVoice) {
		if (line != null && line.equalsIgnoreCase("yes")) {
			fetchTopping(helloVoice);
		}
		 else if (line != null && line.equalsIgnoreCase("no")) {
			 fetchSize(helloVoice);
		 }
		 else {
			 helloVoice.speak("Can't understand please repeat !");
			 fetchSize(helloVoice);
		 }
	}
	
	public void confirmToppings(String line, Voice helloVoice) {
		if (line != null && line.equalsIgnoreCase("yes")) {
			fetchCrust(helloVoice);
		}
		 else if (line != null && line.equalsIgnoreCase("no")) {
			 fetchTopping(helloVoice);
		 }
		 else {
			 helloVoice.speak("Can't understand please repeat !");
			 fetchTopping(helloVoice);
		 }
	}
	public void confirmCrust(String line, Voice helloVoice) {
		if (line != null && line.equalsIgnoreCase("yes")) {
			fetchThickness(helloVoice);
		}
		 else if (line != null && line.equalsIgnoreCase("no")) {
			 fetchCrust(helloVoice);
		 }
		 else {
			 helloVoice.speak("Can't understand please repeat !");
			 fetchCrust(helloVoice);
		 }
	}
	
	public void confirmThickness(String line, Voice helloVoice) {
		if (line != null && line.equalsIgnoreCase("yes")) {
			int price = getPrice(reqSize, reqCrust, reqThickness, reqToppings);
			fetchBlock(helloVoice, price);
		}
		 else if (line != null && line.equalsIgnoreCase("no")) {
			 fetchThickness(helloVoice);
		 }
		 else {
			 helloVoice.speak("Can't understand please repeat !");
			 fetchThickness(helloVoice);
		 }
	}
	 
	public void fetchTopping(Voice helloVoice) {
		helloVoice.speak(system.get(3));
		reqToppings = scanner.nextLine();
		if (reqToppings != null && 
				(reqToppings.equalsIgnoreCase("cheese") ||
				reqToppings.equalsIgnoreCase("mushrooms") ||
				reqToppings.equalsIgnoreCase("pepperoni") ||
				reqToppings.equalsIgnoreCase("seafood") ||
				reqToppings.equalsIgnoreCase("chicken"))) {
			String[] s = system.get(4).split(" ");
			for (int i = 0; i < s.length - 1; i++) {
					helloVoice.speak(s[i]);	
			}
			helloVoice.speak(reqToppings + s[s.length - 1]);
			confirm = scanner.nextLine();
			confirmToppings(confirm, helloVoice);
//			System.out.println();
		} else {
			helloVoice.speak("Can't understand please repeat");
			fetchTopping(helloVoice);
		}
	}
	
	public void fetchCrust(Voice helloVoice) {
		helloVoice.speak(system.get(5));
		reqCrust = scanner.nextLine();
		if (reqCrust != null && 
				(reqCrust.equalsIgnoreCase("pan") ||
				reqCrust.equalsIgnoreCase("stuffed"))) {
			String[] s = system.get(6).split(" ");
			for (int i = 0; i < s.length - 1; i++) {
					helloVoice.speak(s[i]);	
			}
			helloVoice.speak(reqCrust + s[s.length - 1]);
			confirm = scanner.nextLine();
//			System.out.println(system.get(6));
		} else {
			helloVoice.speak("Can't understand please repeat");
			fetchCrust(helloVoice);
		}
	}
	
	public void fetchThickness(Voice helloVoice) {
		helloVoice.speak(system.get(7));
		reqThickness = scanner.nextLine();
		if (reqThickness != null && 
				(reqThickness.equalsIgnoreCase("thick") ||
				reqToppings.equalsIgnoreCase("thin"))) {
			String[] s = system.get(8).split(" ");
			for (int i = 0; i < s.length - 1; i++) {
					helloVoice.speak(s[i]);	
			}
			helloVoice.speak(reqThickness + s[s.length - 1]);
			confirm = scanner.nextLine();
//			System.out.println(system.get(8));
		} else {
			helloVoice.speak("Can't understand please repeat");
			fetchThickness(helloVoice);
		}
	}
	
	public void fetchBlock(Voice helloVoice, int price) {
		String[] block = system.get(9).split(".");
		helloVoice.speak(block[0] + block [1] + price + block [2]);
	}
	
	public void manageDialogue(String[] args) {
		String voiceName = (args.length > 0) ? args[0] : "kevin16";
		VoiceManager voiceManager = VoiceManager.getInstance();
		Voice helloVoice = voiceManager.getVoice(voiceName);
		if (helloVoice == null) { 
			System.err.println("Cannot find a voice named " + voiceName
					+ ".  Please specify a different voice.");
			System.exit(1);
		}
		
		helloVoice.allocate();
		helloVoice.speak(system.get(0));
		fetchSize(helloVoice);
		
//		helloVoice.deallocate();
//		System.exit(0);
	}

	public static void main(String[] args) {
		Simulate soso = new Simulate();
		soso.parseText();
		soso.manageDialogue(args);
	}
}
