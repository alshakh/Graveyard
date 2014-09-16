package symcode.lab;

import java.io.File;
import java.io.FileReader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
//
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//
import symcode.expr.Expression;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class LabLoader {
	
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
				return new Lab(id,version,loadConst(jsonObj),loadElements(jsonObj));
			case "molecule":
				return new Molecule(id,version,loadConst(jsonObj),loadElements(jsonObj),loadBond(jsonObj));
			case "product":
				return new Product(loadBond(jsonObj),readSimpleProperty(jsonObj,"content"));
			default:
				throw new InvalidLabException();
		}
	}
	//
	private static HashSet<Loadable> loadElements(JSONObject jsonObj) throws InvalidLabException{
		HashSet<Loadable> elementsSet = null;
		if( jsonObj.containsKey("elements")){
			elementsSet= new HashSet<Loadable>();
			JSONArray elementsJSONArray = (JSONArray)jsonObj.get("elements");
			for (Object elementJsonObj : elementsJSONArray) {
				elementsSet.add((Loadable) loadLoadable((JSONObject) elementJsonObj));
			}
			
		}
		return elementsSet;
	}
	//
	private static HashMap<String,Expression> loadConst(JSONObject jsonObj){
	
			HashMap<String,Expression> constMap = null;
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
	private static EnumMap<Bond,Expression> loadBond(JSONObject jsonObj){

			EnumMap<Bond,Expression> bondMap = null;
			if(jsonObj.containsKey("bond")){
				bondMap = new EnumMap<Bond, Expression>(Bond.class);
				JSONObject bondJSONObj = (JSONObject)jsonObj.get("bond");
				String jsonKey = "";
				for(Bond b : Bond.values()){
					if(b==Bond.X) jsonKey = "x";
					else if (b==Bond.Y) jsonKey = "y";
					else if (b==Bond.H) jsonKey = "h";
					else if (b==Bond.W) jsonKey = "w";
					//
					bondMap.put(b,new Expression((String) bondJSONObj.get(jsonKey)));
				}
			}
			return bondMap;
	}
	//
	//
	private static String readSimpleProperty(JSONObject matterObj, String property){
		if (!matterObj.containsKey(property)) {
			return "";
		}
		return (String) matterObj.get(property);
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
