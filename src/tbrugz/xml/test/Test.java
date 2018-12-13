package tbrugz.xml.test;

import java.util.Properties;
import java.util.regex.Pattern;

import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Element;

class DumpTest extends AbstractDump {

	@Override
	public void dumpModel(Element elem) {
	}
	
	Properties getSnippets() {
		return snippets;
	}
}

public class Test {

	public static void main(String[] args) {
		String s = "aaa {0} aaa";
		String pattern = "\\{0}";
		s = s.replaceAll(pattern, "lala");
		//System.out.println(pattern+"; "+s);
		
		String scontent = " lala /* bbb \n vvv */ ccc \n\n vvv ";
		pattern = "/\\*.*?\\*/";
		scontent = Pattern.compile(pattern, Pattern.DOTALL).matcher(scontent).replaceAll("");
		//scontent = scontent.replaceAll(, "");
		//System.out.println("scontent: "+scontent);
		
		scontent = " lala //bbb \n ccc";
		scontent = scontent.replaceAll("//.*", "");
		//scontent = scontent.replaceAll("//.*?\\n", "");
		System.out.println("scontent: "+scontent);
		
		Test t = new Test();
		t.dumpTest();
	}

	void dumpTest() {
		DumpTest dt = new DumpTest();
		dt.loadSnippets("test-snippets.properties");
		dt.dumpModel(null, System.out);
		for(int i=0;i<20;i++) {
			System.out.println("["+i+"] snippet: "+dt.getSnippets().getProperty("lala"));
			dt.outSnippet("lala", 0, "zero", "one");
		}
	}
}
