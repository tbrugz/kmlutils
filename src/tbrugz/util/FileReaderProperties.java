package tbrugz.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileReaderProperties extends Properties {
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(FileReaderProperties.class);
	
	@Override
	public synchronized void load(InputStream inStream) throws IOException {
		super.load(inStream);
		resolveFileProperties();
	}
	
	@Override
	public synchronized void load(Reader reader) throws IOException {
		super.load(reader);
		resolveFileProperties();
	}
	
	static String SUFFIX_FILE = ".file";
	
	protected void resolveFileProperties() {
		Enumeration<Object> en = this.keys();
		//for(Object o: keySet()) {
		while(en.hasMoreElements()) {
			String s = (String) en.nextElement();
			if(s.endsWith(SUFFIX_FILE)) {
				String propkey = s.substring(0, s.length() - SUFFIX_FILE.length());
				String fileName = getProperty(s);
				log.debug("key='"+propkey+"': file is '"+fileName+"'");
				String value;
				try {
					value = readFile(new FileReader(fileName));
					setProperty(propkey, value);
				} catch (FileNotFoundException e) {
					log.warn(e.toString());
					//e.printStackTrace();
				} catch (IOException e) {
					log.warn(e.toString());
					//e.printStackTrace();
				}
			}
		}
	}
	
	// from IOUtil
	static int BUFFER_SIZE = 1024*8;
	protected static String readFile(Reader reader) throws IOException {
		StringWriter sw = new StringWriter();
		
		char[] cbuf = new char[BUFFER_SIZE];
		
		int iread = reader.read(cbuf);
		
		while(iread>0) {
			sw.write(cbuf, 0, iread);
			iread = reader.read(cbuf);
		}

		return sw.toString();
	}	
}
