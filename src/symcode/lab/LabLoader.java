package symcode.lab;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import symcode.expr.Expression;

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
		//validate lab
		//
		try {
			return (Lab) loadLoadable(jsonObj);
		} catch (InvalidLabException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * recursively load matter. usually called only once on a lab.
	 *
	 * @param jsonObj
	 * @return
	 */
	private static Loadable loadLoadable(JSONObject jsonObj)
		throws InvalidLabException {
		//
		String id,version,type;
		//
		type = readSimpleProperty(jsonObj,"type").toLowerCase();

		//
		id = readSimpleProperty(jsonObj,"id");
		version = readSimpleProperty(jsonObj,"version").toLowerCase();
		//
		//
		switch (type) {
			case "lab":
				return new Lab(id,version,loadConst(jsonObj),loadMolecule(jsonObj));
			case "compound":
				return new Compound(id,version,loadConst(jsonObj),loadMolecule(jsonObj),loadBond(jsonObj), loadReferences(jsonObj));
			case "atom":
				return new Atom(id,version,loadConst(jsonObj),loadBond(jsonObj),loadReferences(jsonObj), new SvgString(readSimpleProperty(jsonObj,"svg")));
			default:
				throw new InvalidLabException();
		}
	}
	//
	private static HashSet<Molecule> loadMolecule(JSONObject jsonObj) throws InvalidLabException{
		HashSet<Molecule> elementsSet = Template.EMPTY_ELEMENTS;
		if( jsonObj.containsKey("elements")){
			elementsSet= new HashSet<Molecule>();
			JSONArray elementsJSONArray = (JSONArray)jsonObj.get("elements");
			for (Object elementJsonObj : elementsJSONArray) {
				elementsSet.add((Molecule) loadLoadable((JSONObject) elementJsonObj));
			}
		}
		return elementsSet;
	}
	//
	private static HashMap<String,Expression> loadConst(JSONObject jsonObj){
	
			HashMap<String,Expression> constMap = Template.EMPTY_CONST;
			if(jsonObj.containsKey("const")){
				constMap = new HashMap<String,Expression>();
				HashMap constJSONHashMap = (HashMap)jsonObj.get("const");//
				//
				Iterator it = constJSONHashMap.keySet().iterator();
				while(it.hasNext()){
					String k = (String)it.next();
					constMap.put(k, new Expression((String)constJSONHashMap.get(k)));
				}
			}
			return constMap;
	}
	//
	private static HashSet<String> loadReferences(JSONObject jsonObj){
		HashSet<String> refSet = Molecule.EMPTY_REFERENCES;
		if(jsonObj.containsKey("references")){
			refSet = new HashSet<String>();	
			String[] refStrArr = ((String)jsonObj.get("references")).split(",");
			for(String s : refStrArr){
				refSet.add(s.trim());
			}
		}
		return refSet;
	}
	//
	private static BondExpr loadBond(JSONObject jsonObj){

			BondExpr bond = BondExpr.EMPTY;
			if(jsonObj.containsKey("bond")){
				JSONObject jsonBondObj = (JSONObject)jsonObj.get("bond");
				// 
				bond = new BondExpr(
				new Expression(readSimpleProperty(jsonBondObj,"x")),
				new Expression(readSimpleProperty(jsonBondObj,"y")),
				new Expression(readSimpleProperty(jsonBondObj,"h")),
				new Expression(readSimpleProperty(jsonBondObj,"w"))
				);
			}
			return bond;
	}
	//
	//
	private static String readSimpleProperty(JSONObject jsonObj, String property){
		if (!jsonObj.containsKey(property)) {
			return "";
		}
		return (String) jsonObj.get(property);
	}
	//	
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
