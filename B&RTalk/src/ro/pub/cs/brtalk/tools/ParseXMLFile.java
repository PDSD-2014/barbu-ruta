package ro.pub.cs.brtalk.tools;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ParseXMLFile {
	
	private boolean message = false;
	private boolean friends = true;
	public String input = null;
	
	public ParseXMLFile(String input, boolean message, boolean friends){
		this.input = input;
		this.message = message;
		this.friends = friends;
	}
	
	public ArrayList<FriendInfo> getFriends(){
		
		String result = null;
		DocumentBuilder dBuilder;
		ArrayList<FriendInfo> friendsList = new ArrayList<FriendInfo>();
		
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(input));
			Document doc = dBuilder.parse(is);
			//result = doc.getDocumentElement().getNodeName();
			NodeList nList = doc.getElementsByTagName("friends");
			for (int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) nNode;
					String username = element.getElementsByTagName("username").item(0).getTextContent();
					String email = element.getElementsByTagName("email").item(0).getTextContent();
					String id = element.getElementsByTagName("id").item(0).getTextContent();
					friendsList.add(new FriendInfo(username, email, Integer.parseInt(id)));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return friendsList;
		
	}
	
	public ArrayList<Message> getMessages(){
		
		String result = null;
		DocumentBuilder dBuilder;
		ArrayList<Message> messageList = new ArrayList<Message>();
		
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(input));
			Document doc = dBuilder.parse(is);
			//result = doc.g	etDocumentElement().getNodeName();
			NodeList nList = doc.getElementsByTagName("message");
			for (int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) nNode;
					String id = element.getElementsByTagName("id").item(0).getTextContent();
					String from = element.getElementsByTagName("from").item(0).getTextContent();
					String text = element.getElementsByTagName("text").item(0).getTextContent();
					String when = element.getElementsByTagName("when").item(0).getTextContent();
					messageList.add(new Message(from, text, when,Integer.parseInt(id)));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return messageList;
		
	}

}
