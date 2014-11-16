package info.pavie.osm2hive.controller;

import info.pavie.osm2hive.model.osm.Element;
import info.pavie.osm2hive.model.osm.Node;
import info.pavie.osm2hive.model.osm.Relation;
import info.pavie.osm2hive.model.osm.Way;
import info.pavie.osm2hive.model.xml.InvalidMarkupException;
import info.pavie.osm2hive.model.xml.Markup;

/**
 * This class parses OSM XML file, line by line.
 * It uses the XML {@link Markup} parser, and create {@link Element}s objects.
 * @author Adrien PAVIE
 */
public class OSMParser {
//ATTRIBUTES
	/** The element being parsed **/
	private Element current;
	
	/** Is the current element ready (ie completely parsed) ? **/
	private boolean isCurrentReady;

//CONSTRUCTORS
	/**
	 * Class constructor
	 */
	public OSMParser() {
		current = null;
		isCurrentReady = false;
	}
	
//ACCESSORS
	/**
	 * @return True if the current element is ready
	 */
	public boolean isElementReady() {
		return isCurrentReady;
	}
	
	/**
	 * @return The current element, or null if not ready.
	 */
	public Element getCurrentElement() {
		return (isCurrentReady) ? current : null;
	}

//OTHER METHODS
	/**
	 * Get an object ID, in this format: X000000, where X is the object type (N for nodes, W for ways, R for relations).
	 * @param type The object type (node, way or relation)
	 * @param ref The object ID in a given type
	 * @return The object ID, unique for all types
	 */
	public static String getId(String type, String ref) {
		String result = null;
		
		switch(type) {
			case "node":
				result="N"+ref;
				break;
			case "way":
				result="W"+ref;
				break;
			case "relation":
				result="R"+ref;
				break;
			default:
				throw new RuntimeException("Unknown element type: "+type);
		}
		
		return result;
	}
	
	/**
	 * Processes the current line, taking account of previous entries.
	 * @param line The read line from XML
	 * @throws InvalidMarkupException If the given line isn't a correct XML markup (see {@link Markup} for details)
	 */
	public void parse(String line) throws InvalidMarkupException {
		Markup m = new Markup(line); //Parse the line
		
		switch(m.getType()) {
			//Opening markup, for example <node>
			case Markup.START:
				startMarkup(m);
				break;
				
			//Ending markup, for example </node>
			case Markup.END:
				endMarkup(m);
				break;
				
			//Empty markup, for example <node />
			case Markup.EMPTY:
				startMarkup(m);
				endMarkup(m);
				break;
				
			//Complete markup, for example <node></node>
			case Markup.COMPLETE:
				startMarkup(m);
				endMarkup(m);
				break;
				
			//Declaration markup, for example <?xml version="1.0" ?>
			case Markup.DECLARATION:
				//Do nothing
				break;
				
			default:
				throw new InvalidMarkupException("Unknown type of markup");
		}
	}
	
	/**
	 * Analyzes the start markup
	 * @param m The markup to analyze
	 */
	private void startMarkup(Markup m) {
		isCurrentReady = false;
		
		//Case of node
		if(m.getName().equals("node")) {
			Node n = new Node(
							Long.parseLong(m.getAttribute("id")),
							Double.parseDouble(m.getAttribute("lat")),
							Double.parseDouble(m.getAttribute("lon"))
							);
			n.setUser(m.getAttribute("user"));
			
			if(m.getAttribute("uid") != null) {
				n.setUid(Long.parseLong(m.getAttribute("uid")));
			}
			
			n.setVisible(Boolean.parseBoolean(m.getAttribute("visible")));
			
			if(m.getAttribute("version") != null) {
				n.setVersion(Integer.parseInt(m.getAttribute("version")));
			}
			
			if(m.getAttribute("changeset") != null) {
				n.setChangeset(Long.parseLong(m.getAttribute("changeset")));
			}
			
			n.setTimestamp(m.getAttribute("timestamp"));
			current = n;
		}
		//Case of way
		else if(m.getName().equals("way")) {
			Way w = new Way(Long.parseLong(m.getAttribute("id")));
			w.setUser(m.getAttribute("user"));
			
			if(m.getAttribute("uid") != null) {
				w.setUid(Long.parseLong(m.getAttribute("uid")));
			}
			
			w.setVisible(Boolean.parseBoolean(m.getAttribute("visible")));
			
			if(m.getAttribute("version") != null) {
				w.setVersion(Integer.parseInt(m.getAttribute("version")));
			}
			
			if(m.getAttribute("changeset") != null) {
				w.setChangeset(Long.parseLong(m.getAttribute("changeset")));
			}
			
			w.setTimestamp(m.getAttribute("timestamp"));
			current = w;
		}
		//Case of way node
		else if(m.getName().equals("nd")) {
			((Way) current).addNode("N"+m.getAttribute("ref"));
		}
		//Case of relation
		else if(m.getName().equals("relation")) {
			Relation r = new Relation(Long.parseLong(m.getAttribute("id")));
			r.setUser(m.getAttribute("user"));
			
			if(m.getAttribute("uid") != null) {
				r.setUid(Long.parseLong(m.getAttribute("uid")));
			}
			
			r.setVisible(Boolean.parseBoolean(m.getAttribute("visible")));
			
			if(m.getAttribute("version") != null) {
				r.setVersion(Integer.parseInt(m.getAttribute("version")));
			}
			
			if(m.getAttribute("changeset") != null) {
				r.setChangeset(Long.parseLong(m.getAttribute("changeset")));
			}
			
			r.setTimestamp(m.getAttribute("timestamp"));
			current = r;
		}
		//Case of relation member
		else if(m.getName().equals("member")) {
			String refMember = getId(m.getAttribute("type"), m.getAttribute("ref"));
			
			//Add member to relation
			((Relation) current).addMember(m.getAttribute("role"), refMember);
		}
		//Case of tag
		else if(m.getName().equals("tag")) {
			if(current != null) {
				current.addTag(m.getAttribute("k"), m.getAttribute("v"));
			}
		}
	}
	
	/**
	 * Analyzes the end markup.
	 * @param m The markup to analyze
	 */
	private void endMarkup(Markup m) {
		if(m.getName().equals("node") || m.getName().equals("way") || m.getName().equals("relation")) {
			//Add element to list, and delete current
			if(current != null) {
				if( (m.getName().equals("way") && ((Way) current).getNodes().size() >= 2)
						|| m.getName().equals("node")
						|| (m.getName().equals("relation") && ((Relation) current).getMembers().size() > 0)) {
					
					isCurrentReady = true;
				}
			}
		} else {
			isCurrentReady = false;
		}
	}
}
