package ro.pub.cs.brtalk.tools;

public class Message {

	private String from;
	private String text;
	private String when;
	
	public Message(String from, String text, String when){
		this.from = from;
		this.text = text;
		this.when = when;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getWhen() {
		return when;
	}

	public void setWhen(String when) {
		this.when = when;
	}
	
	
	
}
