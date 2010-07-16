package org.calgb.test.performance.html;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlParse {

	String html;

	public HtmlParse(String html) {
		this.html = html;
	}

	HashMap<String, String> getAttributes(RegexMatch match, String tagName) {
		HashMap<String, String> attrPairs = new HashMap<String, String>();
		String tagDeclaration = match.find("<" + tagName + ".*?>").get(0)
				.getText();
		tagDeclaration = tagDeclaration.replaceAll("(<form)|(\\/*>$)|\\\"", "");
		String[] tagPairs = tagDeclaration.split("\\s");
		for (String tagPair : tagPairs) {
			if (tagPair.contains("=")) {
				int eqlIdx = tagPair.indexOf("=");
				attrPairs.put(tagPair.substring(0, eqlIdx), tagPair
						.substring(eqlIdx + 1));
			}
		}
		return attrPairs;
	}

	public Vector<HtmlContent> getForms() {
		Vector<HtmlContent> formContent = new Vector<HtmlContent>();

		Vector<RegexMatch> forms = new RegexMatch(html
				.replaceAll("\\n|\\r", "")).find("<form.*?>.*?<\\/form>");

		for (RegexMatch f : forms) {
			formContent.add(new HtmlContent(new HtmlElement(getAttributes(f,
					"form")), f.getText()));
		}
		return formContent;

	}

	public static void main(String[] args) throws IOException {
		String html = readFileAsString("src/some.html");
		Vector<HtmlContent> forms = new HtmlParse(html).getForms();
		HtmlContent secondForm = forms.get(1);
		System.out.println(secondForm.getElement().getAttribute("action"));
		System.out.println(secondForm.find("input", "name", "_sourcePage").getAttribute("value"));
	}

	private static String readFileAsString(String filePath)
			throws java.io.IOException {
		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = new BufferedInputStream(new FileInputStream(
				filePath));
		f.read(buffer);
		return new String(buffer);
	}

}
