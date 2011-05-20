package tbrugz.geo.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.impl.Constants;

import tbrugz.geo.model.Group;
import tbrugz.geo.model.Point;
import tbrugz.geo.model.Polygon;
import tbrugz.geo.model.Root;
import tbrugz.xml.model.skel.Composite;

class SVGPathStringReader {
	StringBuffer sb;
	int pos = 0;

	static Log log = LogFactory.getLog(SVGPathStringReader.class);
	
	static final String SPACE = " ";
	static final String COMMA = ",";
	static List<String> NUMBERS = new ArrayList<String>(); // = {"0","1","2","3","4","5","6","7","8","9"};
	static List<String> LETTERS = new ArrayList<String>();
	static List<String> DELIMITERS = new ArrayList<String>();
	static List<String> NUMBERS_OR_DELIMITER = new ArrayList<String>();
	static List<String> LETTERS_OR_DELIMITER = new ArrayList<String>();
	
	static final String[] KNOWN_SVG_LETTERS = {"m", "l", "z", "c"};
	
	static {
		DELIMITERS.add(SPACE);
		DELIMITERS.add(COMMA);
		
		for(int i=0;i<=9;i++) {
			NUMBERS.add(String.valueOf(i));
		}
		NUMBERS.add("-");
		NUMBERS.add(".");

		for(String s: KNOWN_SVG_LETTERS) {
			LETTERS.add(s);
			LETTERS.add(s.toUpperCase());
		}		
		
		NUMBERS_OR_DELIMITER.addAll(NUMBERS);
		NUMBERS_OR_DELIMITER.addAll(DELIMITERS);
		
		LETTERS_OR_DELIMITER.addAll(LETTERS);
		LETTERS_OR_DELIMITER.addAll(DELIMITERS);
	}
	
	public SVGPathStringReader(String s) {
		sb = new StringBuffer(s);
	}
	
	static int indexOf(StringBuffer str, List<String> delimiters, int startWith) {
		int pos = str.length();
		for(String sd: delimiters) {
			int i = str.indexOf(sd, startWith);
			if(i!=-1 && i<pos) { pos = i; }
			//log.trace("indexOf: "+str+" \n  sd: "+sd+"; i:"+i+"; pos:"+pos+" // "+delimiters);
		}
		
		if(pos == str.length()) {
			return -1;
		}
		//log.trace("indexOf: "+str+" \n  pos:"+pos+" // "+delimiters);
		return pos;
	}
	
	/*public String readLetters() {
		int i = indexOf(sb, NUMBERS_OR_DELIMITER, pos);
		if(i==-1) return null;
		if(i==0) return "";
		
		String substr = sb.substring(pos, i);
		pos = i;
		//System.out.println("read: "+substr);
		return substr;
	}*/

	public String readLetter() {
		int i = indexOf(sb, NUMBERS_OR_DELIMITER, pos);
		if(i==-1) return null;
		if(i==0) return "";
		
		String substr = sb.substring(pos, pos+1);
		pos++;
		//log.trace("readL: "+substr);
		return substr;
	}
	
	public String readNumbers() {
		int firstNumber = indexOf(sb, NUMBERS, pos);
		if(firstNumber==-1) return null;
		
		int i = indexOf(sb, LETTERS_OR_DELIMITER, firstNumber);
		if(i==-1) return null;
		if(i==0) return "";

		//log.trace("readN: "+pos+"; "+i);
		String substr = sb.substring(firstNumber, i);
		pos = i;
		//log.trace("read: "+substr);
		return substr;
	}
}

public class SVGParser extends DefaultHandler {
	
	public static String SVG = "svg";
	public static String G = "g";
	public static String PATH = "path";
	
	public static String PATH_D = "d";
	public static String ANY_ID = "id";
	
	Root root = null;
	Stack<Composite> groupStack = new Stack<Composite>();
	int nestLevel = 0;
	
	static Log log = LogFactory.getLog(SVGParser.class);

	public Root parseDocument(String file) {
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(false);  
		//spf.setNamespaceAware(false);
		//spf.setSchema(null);
		String LOAD_EXTERNAL_DTD_FEATURE = Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_EXTERNAL_DTD_FEATURE;
		
		/*
		 * about validating:
		 * LOAD_EXTERNAL_DTD_FEATURE = "http://apache.org/xml/features/"+"nonvalidating/load-external-dtd"
		 * http://www.coderanch.com/t/128153/XML/SAXParser-not-Validating
		 * http://sax.sourceforge.net/?selected=get-set
		 * http://www.ibm.com/developerworks/library/x-tipvalschm/
		 * http://www.ibm.com/developerworks/xml/library/x-tipsaxp/
		 * 
		 */

		try {
			spf.setFeature(LOAD_EXTERNAL_DTD_FEATURE, false);

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			// Turn off validation
			//sp.setProperty("http://xml.org/sax/features/validation", false); //setFeature("http://xml.org/sax/features/validation", true);

			log.info("is saxParser validating? " + sp.isValidating());
			
			//parse the file and also register this class for call backs
			sp.parse(file, this);

		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		
		return root;
	}

	//Event Handlers
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		String id = attributes.getValue(ANY_ID);

		if(qName.equalsIgnoreCase(SVG)) {
			if(root==null) {
				root = new Root();
				groupStack.add(root);
				root.setId(id);

				log.debug("<svg> processed, l:"+nestLevel);
				nestLevel++;
			}
			else {
				throw new RuntimeException("SVG already declared");
			}
		}
		
		if(qName.equalsIgnoreCase(G)) {
			Group g = new Group();
			groupStack.peek().getChildren().add(g);
			groupStack.push(g);
			g.setId(id);

			log.trace("<g> processed, l:"+nestLevel);
			nestLevel++;
		}

		if(qName.equalsIgnoreCase(PATH)) {
			Polygon p = new Polygon();
			groupStack.peek().getChildren().add(p);
			p.setId(id);
			String pointsStr = attributes.getValue(PATH_D);
			//TODO: polygonS
			procPolygon(p, pointsStr);
			
			log.trace("<path> processed, l:"+nestLevel+"; id:"+id);
			log.trace("<path>'s d:"+pointsStr);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase(SVG)) {
			nestLevel--;
			log.debug("</svg> processed, l:"+nestLevel);
			groupStack.pop();
			
			//DumpModel dm = new DumpModel();
			//dm.dumpModel(root);
		}

		if(qName.equalsIgnoreCase(G)) {
			nestLevel--;
			log.trace("</g> processed, l:"+nestLevel);
			groupStack.pop();
		}
		
		if(qName.equalsIgnoreCase(PATH)) {
			log.trace("</path> processed, l:"+nestLevel);
		}
	}

	void procPolygon(Polygon polygon, String pointsStr) {
		//TODO: add polygon attrs.
		SVGPathStringReader sr = new SVGPathStringReader(pointsStr);
		
		/*
		 * state's:
		 * 
		 * 0 - wait for letter
		 * 1 - wait for 1st point
		 * 2 - wait for 1st point
		 * 9 - end
		 * 
		 */
		int state = 0;
		char lastLetter = '0';
		boolean absoluteRef = true;
		Point point = new Point();
		Point previousPoint = null;
		String token = "";
		
		for(;token!=null;) {
			if(state==0) {
				token = sr.readLetter();
				//log.warn("token = "+token);
				
				if(token==null) { break; }
				if(token.equals("") || token.equals(" ")) {
					//do nothing... ?
					if(lastLetter == '0') {
						log.warn("PATH.D: token list must start with letter [token="+token+"]");
					}
					state = 1;
				}
				else if(token.equalsIgnoreCase("L")) {
					state = 1;
					lastLetter = token.charAt(0);
				}
				else if(token.equalsIgnoreCase("M")) {
					//TODO: differentiate L and M in SVG - M should start a new polygon
					if(lastLetter=='0') {
						state = 1;
					}
					else {
						//XXX: start a new Polygon
						state = 9;
					}
					
					lastLetter = token.charAt(0);
				}
				else if(token.equalsIgnoreCase("Z")) {
					//XXXxx: do not add last point - it should be done in KMLDump
					//polygon.add1stPointAgain();
					//polygon.points.add(polygon.points.get(0)); //what if it has no points?
					state = 9; //TODO: state = 0? after Z should be a M or terminate 
					lastLetter = token.charAt(0);
				}
				else if(token.equalsIgnoreCase("C")) {
					log.warn("PATH.D: token ["+token+"] processed as L");
					state = 1;
					lastLetter = token.charAt(0);
				}
				else {
					log.warn("PATH.D: unkown token: ["+token+"] [state0]");
				}
				
				if(!token.equals("") && !token.equals(" ")) {
					if(isUpperCase(token)) {
						absoluteRef = true;
					}
					else {
						absoluteRef = false;
					}
				}
				
			}
			
			if(state==1) {
				token = sr.readNumbers();
				if(token==null) { break; }
				point.x = Float.parseFloat(token);

				state = 2;
			}

			if(state==2) {
				token = sr.readNumbers();
				point.y = Float.parseFloat(token);
				Point newPoint = (Point)point.clone();
				if(!absoluteRef) {
					if(previousPoint==null) {
						log.warn("PATH.D: relative point declared with no previous point");
					}
					newPoint.addPoint(previousPoint);
				}
				polygon.addPoint(newPoint);
				previousPoint = newPoint;
				//polygon.points.add((Point)point.clone());
				setXYMaxMin(point);

				state = 0;
			}
			
			if(state!=0 && state!=1 && state!=2) {
				if(state == 9) {
					//do nothing
					break;
				}
				else {
					log.warn("PATH.D: Unespected state: "+state+", token: "+token);
				}
			}
		}
		
		//log.info("poly 1st and last ponits: "+polygon.points.get(0)+"; "+polygon.points.get(polygon.points.size()-1));
		//setPolygonCentre(polygon);
		polygon.setPolygonCentre();
	}
	
	boolean isUpperCase(String s) {
		if(Character.isUpperCase(s.charAt(0))) {
			return true;
		}
		return false;
	}
	
	/*
	 * state's:
	 * 
	 * 0 - wait for letter
	 * 1 - wait for points (eg: 123,123)
	 * 2 - end
	 * 
	 */
	/*
	@Deprecated
	void oldProcPolygon(Polygon polygon, String pointsStr) {
		String[] ps = pointsStr.split("\\s");
		//Point firstPoint = null;
		
		int state = 0;
		for(String token: ps) {
			if(state==0) {
				if(token.equalsIgnoreCase("L") || token.equalsIgnoreCase("M")) {
					state = 1;
				}
				else if(token.equalsIgnoreCase("Z")) {
					polygon.points.add(polygon.points.get(0)); //what if it has no points?
					state = 2;
				}
				else {
					log.warn("PATH.D: unkown token: "+token);
				}
			}
			else if(state==1) {
				String[] pts = token.split(",");
				Point point = new Point();
				point.x = Float.parseFloat(pts[0]);
				point.y = Float.parseFloat(pts[1]);
				polygon.points.add(point);
				setXYMaxMin(point);

				state = 0;
			}
			else {
				log.warn("PATH.D: Unespected state: "+state+", token: "+token);
			}
		}
		
		//setPolygonCentre(polygon);
		polygon.setPolygonCentre();
	}*/
	
	/*void setPolygonCentre(Polygon polygon) {
		float sumX=0, sumY=0;
		for(Point p: polygon.points) {
			sumX+=p.x;
			sumY+=p.y;
		}
		float avgX = sumX/polygon.points.size();
		float avgY = sumY/polygon.points.size();
		polygon.centre = new Point();
		polygon.centre.x = avgX;
		polygon.centre.y = avgY;
	}*/
	
	void setXYMaxMin(Point p) {
		if(p.x > root.maxX) root.maxX = p.x;
		if(p.y > root.maxY) root.maxY = p.y;
		if(p.x < root.minX) root.minX = p.x;
		if(p.y < root.minY) root.minY = p.y;
	}
	
}

/*

SVG:
<path
   id="mun_4310462"
   class="fil4 str0"
   d="M 192241,50204 L 192616,49571 L 192787,49590 L 192763,49757 L 192940,49545 L 193381,49534 L 194014,49150 L 194569,49115 L 194928,48712 L 195021,48362 L 195636,47995 L 195830,47415 L 196207,47325 L 196341,47293 L 196495,47194 L 196555,47219 L 197939,47784 L 198110,48095 L 198436,47962 L 198497,48296 L 198670,48261 L 198864,48562 L 198681,48599 L 198598,49236 L 198088,49244 L 197960,49918 L 198576,50445 L 199072,50482 L 198998,50678 L 199240,51577 L 198977,51595 L 198976,51784 L 198592,51761 L 198310,51977 L 198099,51664 L 197945,51713 L 197863,51596 L 197548,51819 L 197118,51736 L 196819,51829 L 196696,52122 L 196169,52036 L 196161,51897 L 196009,51974 L 195890,51823 L 195759,51939 L 195661,51852 L 195525,52069 L 195235,51678 L 195025,51868 L 194896,51570 L 194451,51457 L 194299,51617 L 194263,51320 L 194075,51363 L 194046,50938 L 193374,50968 L 193322,50577 L 193140,50575 L 193021,50381 L 192645,50535 L 192553,50465 L 192744,50346 L 192437,50303 L 192242,50204 L 192241,50204 z"
   style="fill:#b3b3b3;fill-opacity:1;stroke:#e6e6e6;stroke-width:148.63531494;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" />

KML:

      <Polygon id="khPolygon683">
        <outerBoundaryIs>
          <LinearRing id="khLinearRing684">
            <coordinates>
-72.05222000000001,-10.00471,0 -72.14389,-10.00471,0 -72.18583,-10.0025,0 -72.16833,-9.954440999999999,0 -72.15078,-9.887022,0 -72.16473000000001,-9.834714,0 -72.18028,-9.803402999999999,0 -72.22653,-9.77778,0 -72.26035,-9.759168000000001,0 -72.26931,-9.730556999999999,0 -72.25764,-9.692577,0 -72.25251,-9.656946,0 -72.30153,-9.533187,0 -72.37167,-9.492633,0 -72.40224000000001,-9.483606,0 -72.60693999999999,-9.446111999999999,0 -72.73000999999999,-9.413055,0 -72.84084,-9.41166,0 -72.86194999999999,-9.412227,0 -72.98083,-9.410831999999999,0 -73.2007,-9.400760999999999,0 -73.18292,-9.365004000000001,0 -73.15807000000001,-9.344448,0 -73.11945,-9.313335,0 -73.02361000000001,-9.221112,0 -72.96639999999999,-9.143055,0 -72.95528,-9.119160000000001,0 -72.94917,-9.090135,0 -72.94806,-9.066528,0 -72.95113000000002,-9.045837000000001,0 -72.96236000000002,-8.988471000000001,0 -73.00577,-8.930349,0 -73.05882,-8.902431,0 -73.12195,-8.807499,0 -73.13278,-8.790552,0 -73.14194999999999,-8.762229,0 -73.15251000000001,-8.735004,0 -73.16667,-8.71416,0 -73.18389999999999,-8.700138000000001,0 -73.21557,-8.686107,0 -73.24097999999999,-8.679582,0 -73.27334000000001,-8.674236000000001,0 -73.34569999999999,-8.602361999999999,0 -73.40473000000002,-8.450559,0 -73.47862000000001,-8.398611000000001,0 -73.53319999999999,-8.352359999999999,0 -73.5414,-8.302778999999999,0 -73.55668,-8.249174999999999,0 -73.59569999999999,-8.121392999999999,0 -73.65167,-8.01666,0 -73.70988,-7.983891000000001,0 -73.75632,-7.967079000000001,0 -73.77196000000001,-7.948053000000001,0 -73.77702000000001,-7.872885,0 -73.72539,-7.879581,0 -73.6982,-7.846182,0 -73.69376,-7.803333000000002,0 -73.70666,-7.776387000000001,0 -73.72417,-7.764444,0 -73.75196,-7.756947000000001,0 -73.82153,-7.731666,0 -73.84223,-7.717365000000002,0 -73.85196000000001,-7.699581,0 -73.86556,-7.675281000000001,0 -73.93250999999999,-7.609167,0 -73.97417,-7.574445,0 -74.00458999999999,-7.554375000000001,0 -74.00216,-7.530453000000001,0 -73.98715,-7.528833,0 -73.96243,-7.527366,0 -73.93053000000001,-7.442991000000002,0 -73.93112000000001,-7.359165000000001,0 -73.73820000000001,-7.333956000000002,0 -73.70583000000001,-7.309233,0 -73.72,-7.223058,0 -73.77557,-7.137216,0 -73.79514,-7.122627,0 -73.67307,-7.175835000000001,0 -73.42722999999999,-7.279164,0 -73.12779000000001,-7.400835,0 -72.99834,-7.451109000000001,0 -72.85417,-7.510284000000001,0 -72.64501,-7.600977000000001,0 -72.53362,-7.628337000000001,0 -72.42249,-7.655004000000001,0 -72.08891,-7.734996000000001,0 -71.97056000000001,-7.763058000000002,0 -71.65028,-7.838892,0 -71.37194,-7.904727,0 -71.28862,-7.924446,0 -71.17751,-7.950834,0 -71.12889,-7.962498,0 -71.0475,-7.982784000000001,0 -70.97057,-8.002503000000001,0 -70.35151999999999,-8.162919000000001,0 -70.25500000000001,-8.213607,0 -69.94945,-8.377497,0 -69.78223,-8.467226999999999,0 -69.68501000000001,-8.520003000000003,0 -69.65696,-8.534997000000001,0 -69.35751000000001,-8.694729000000001,0 -69.0089,-8.880281999999999,0 -68.98056,-8.895284999999999,0 -68.65723,-9.066393,0 -68.56668000000001,-9.106947000000002,0 -68.29667000000001,-9.225558,0 -68.16348000000002,-9.283338000000001,0 -67.94501,-9.376947,0 -67.8539,-9.414720000000003,0 -67.75612,-9.454725,0 -67.56612,-9.533052,0 -67.14001,-9.710001,0 -67.08362,-9.733608,0 -67.03445000000001,-9.754721999999999,0 -66.99277000000001,-9.772218000000002,0 -66.92945,-9.798606,0 -66.85196999999999,-9.830285999999999,0 -66.83084,-9.838332000000001,0 -66.83056000000001,-9.838053,0 -66.82861,-9.839448000000001,0 -66.78639,-9.856674,0 -66.64584000000001,-9.913338,0 -66.64026,-9.91827,0 -66.64251,-9.930132,0 -66.65924,-9.948546,0 -66.69347999999999,-9.963189,0 -66.73223,-9.976941,0 -66.76028,-9.990837000000001,0 -66.89375,-10.09153,0 -66.92528,-10.12319,0 -66.95001000000001,-10.15139,0 -66.97083000000001,-10.17222,0 -67.04640000000001,-10.24361,0 -67.07696,-10.26876,0 -67.18321,-10.32368,0 -67.22542,-10.31195,0 -67.30029,-10.31666,0 -67.32326999999999,-10.32348,0 -67.32666999999999,-10.35896,0 -67.44945,-10.44166,0 -67.50917,-10.47278,0 -67.52916999999999,-10.48111,0 -67.58251,-10.50445,0 -67.60973,-10.53014,0 -67.675,-10.62,0 -67.70334,-10.69473,0 -67.74236999999999,-10.71292,0 -67.78722000000001,-10.68416,0 -67.81751,-10.66528,0 -67.83668,-10.65931,0 -67.86584000000001,-10.65695,0 -68.01765,-10.66028,0 -68.0611,-10.67639,0 -68.08223,-10.69028,0 -68.10196999999999,-10.70528,0 -68.11897,-10.73194,0 -68.1246,-10.75931,0 -68.23027999999999,-10.91166,0 -68.28091000000001,-10.97973,0 -68.31305999999999,-10.99667,0 -68.34404000000001,-11.00687,0 -68.40251000000001,-11.01778,0 -68.52257,-11.05931,0 -68.55222000000001,-11.08889,0 -68.57644999999999,-11.10308,0 -68.62083,-11.11639,0 -68.65472,-11.12444,0 -68.70056,-11.13473,0 -68.76671,-11.13712,0 -68.76183,-11.00332,0 -68.84889,-11.01611,0 -69.07613000000001,-10.96723,0 -69.22278,-10.95084,0 -69.41223000000001,-10.93778,0 -69.44765,-10.94764,0 -69.48694999999999,-10.95111,0 -69.54055,-10.95222,0 -69.5675,-10.95055,0 -69.58611999999999,-10.95028,0 -69.66612000000001,-10.95333,0 -69.68861,-10.95861,0 -69.73792,-10.9616,0 -69.78021,-10.92472,0 -69.92599,-10.91403,0 -69.95,-10.92,0 -69.96946,-10.92861,0 -70,-10.94694,0 -70.05168000000001,-10.97917,0 -70.15416999999999,-11.03,0 -70.175,-11.0375,0 -70.22651999999999,-11.05278,0 -70.3289,-11.06917,0 -70.35278,-11.06611,0 -70.40251000000001,-11.04556,0 -70.43668,-11.02694,0 -70.45694,-11.00166,0 -70.47112,-10.98111,0 -70.49930999999999,-10.95348,0 -70.52641,-10.93569,0 -70.58168000000001,-10.97111,0 -70.63139,-11.00916,0 -70.63196000000001,-10.97528,0 -70.63196000000001,-10.91306,0 -70.63085,-10.78167,0 -70.63028,-10.60861,0 -70.62946,-10.33861,0 -70.62918000000001,-10.18639,0 -70.62946,-10.06889,0 -70.62862,-9.948492,0 -70.6289,-9.895833,0 -70.62556000000001,-9.825975,0 -70.61264,-9.798759,0 -70.59529000000001,-9.781389000000001,0 -70.59084,-9.631602000000001,0 -70.60759,-9.575666999999999,0 -70.57722,-9.538605,0 -70.5453,-9.500832000000001,0 -70.51466000000002,-9.428004,0 -70.56806,-9.433476000000002,0 -70.58862000000001,-9.440837999999999,0 -70.64334,-9.490833,0 -70.77916999999999,-9.605556,0 -70.84361,-9.645552,0 -70.88196000000001,-9.669995999999999,0 -70.96932,-9.763893,0 -70.98536,-9.796734000000001,0 -71.01396,-9.820836,0 -71.13855,-9.863820000000001,0 -71.1589,-9.890001,0 -71.18041,-9.932229,0 -71.29639,-9.995418000000001,0 -71.36806,-10.00389,0 -71.45916,-10.00417,0 -71.64194999999999,-10.00445,0 -71.93028,-10.00445,0 -71.97917000000001,-10.00445,0 -72.01028,-10.00445,0 -72.05222000000001,-10.00471,0 
            </coordinates>
          </LinearRing>
        </outerBoundaryIs>
      </Polygon>

TODO Error? no... how to treat multiple polygons from SVG?
L 189100,180556 z M 194569,178630 L 195008,178605
   
*/
