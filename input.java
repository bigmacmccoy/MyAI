import java.util.ArrayList;


public class Input {
	private String original;
	private ArrayList<String> keywords = new ArrayList<String>();
	private boolean multipleActions = false;
	
	public Input(String in) {
		this.setOriginal(in);
	}

	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public boolean hasMultipleActions() {
		return multipleActions;
	}

	public void hasMultipleActions(boolean multipleActions) {
		this.multipleActions = multipleActions;
	}

}
