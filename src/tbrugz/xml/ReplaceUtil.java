package tbrugz.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReplaceUtil {

	static final Log log = LogFactory.getLog(ReplaceUtil.class);

	final static Pattern numberPattern = Pattern.compile("\\{([0-9]+)}");
	final static Pattern randomFuncPattern = Pattern.compile("\\{random\\(([0-9]+)\\)}");

	String procFunc(String key, String... params) {
		Matcher matcher = numberPattern.matcher(key);
		if(matcher.find()) {
			String paramGroup = matcher.group(1);
			int number = Integer.parseInt(paramGroup);
			return number<params.length?params[number]:null;
		}

		matcher = randomFuncPattern.matcher(key);
		log.debug("matcher: "+matcher);
		if(matcher.find()) {
			String paramGroup = matcher.group(1);
			int number = Integer.parseInt(paramGroup);
			int random = (int) (Math.random()*number);
			log.debug("random-find: "+paramGroup+"; replacement: "+number+"; random: "+random);
			return String.valueOf(random);
		}
		
		return null;
	}
}
