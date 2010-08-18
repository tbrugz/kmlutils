package tbrugz.graphml;

import java.util.regex.Pattern;

public class Test {

	/**
	 * @param args
	 */
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
	}

}
