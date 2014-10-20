package symcode.lab;

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import symcode.value.*;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class LabLoader {

	/**
	 *
	 * @param labFile
	 * @return
	 */
	public static Lab loadLab(File labFile) {
		JSONObject jsonObj = readLabFile(labFile);
		if (jsonObj == null) {
			return null;
		}
		//
		//TODO :validate lab
		//
		try {
			return (Lab) loadTemplate(jsonObj);
		} catch (InvalidLabException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * recursively load Template. usually called only once on a lab.
	 *
	 * @param jsonObj
	 * @return
	 */
	private static Template loadTemplate(JSONObject jsonObj)
		throws InvalidLabException {
		//
		String id, version, type;
		//
		type = readSimpleProperty(jsonObj, "type").toLowerCase();

		//
		id = readSimpleProperty(jsonObj, "id");
		if(id.isEmpty()) id=Util.generateRandomId(); // doublication will be a problems. Everything in the same scope should have a unique id.
		version = readSimpleProperty(jsonObj, "version").toLowerCase();
		//
		//
		switch (type) {
			case "lab":
				return new Lab(id, version, loadProperties(id, jsonObj), loadConsts(jsonObj), loadElements(jsonObj));
			case "compound":
				return new Compound(id, version, loadProperties(id, jsonObj), loadConsts(jsonObj), loadElements(jsonObj), loadDependencies(jsonObj));
			case "atom":
				return new Atom(id, version, loadProperties(id, jsonObj),loadConsts(jsonObj), loadDependencies(jsonObj));
			default:
				throw new InvalidLabException();
		}
	}

	private static Set<Molecule> loadElements(JSONObject jsonObj) throws InvalidLabException {
		Set<Molecule> elementsSet = Template.EMPTY_ELEMENTS;
		if (jsonObj.containsKey("elements")) {
			elementsSet = new HashSet<Molecule>();
			JSONArray elementsJSONArray = (JSONArray) jsonObj.get("elements");
			for (Object elementJsonObj : elementsJSONArray) {
				elementsSet.add((Molecule) loadTemplate((JSONObject) elementJsonObj));
			}
		}
		return elementsSet;
	}

	private static Set<Property> loadProperties(String objId, JSONObject jsonObj) {
		Set<Property> ps = new HashSet<Property>();
		//+ mandatory properties ( position and shape => expressions )
		String[] mandatoryProperties = new String[]{"x", "y", "h", "w"};
		for (String pName : mandatoryProperties) {
			if (jsonObj.containsKey(pName)) {
				ps.add(new Property(objId + "." + pName, new Expr((String) jsonObj.get(pName))));
			} else {
				ps.add(new Property(objId + "." + pName, new Doub("0")));
			}
		}
		//-
		//+ svg property ( => svg)
		if (jsonObj.containsKey("svg")) {
			ps.add(new Property(objId + ".svg", new Svg((String) jsonObj.get("svg"))));
		}
		//-
		return ps;
	}

	private static Set<Property> loadConsts(JSONObject jsonObj){
		Set<Property> ps = new HashSet<Property>();
		//+ constants are properties ( ==> Doub)
		if (jsonObj.containsKey("const")) {
			JSONObject constJsonObj = (JSONObject)jsonObj.get("const");
			Iterator itr = constJsonObj.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				ps.add(new Property(key, new Doub(constJsonObj.get(key).toString())));
			}
		}
		//-
		return ps;
	}

	private static Set<String> loadDependencies(JSONObject jsonObj) {
		Set<String> refSet = Molecule.EMPTY_DEPENDENCIES;
		if (jsonObj.containsKey("dependsOn")) {
			refSet = new HashSet<String>();
			String[] refStrArr = ((String) jsonObj.get("dependsOn")).split(",");
			for (String s : refStrArr) {
				refSet.add(s.trim());
			}
		}
		return refSet;
	}

	private static String readSimpleProperty(JSONObject jsonObj, String property) {
		if (!jsonObj.containsKey(property)) {
			return "";
		}
		return (String) jsonObj.get(property);
	}

	private static JSONObject readLabFile(File labFile) {

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(labFile));
			JSONObject jsonObject = (JSONObject) obj;
			return jsonObject;

		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

}
