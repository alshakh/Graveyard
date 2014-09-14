package symcode.lab;

import java.io.File;
import java.io.FileReader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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

	public static Lab loadLab(File labFile) {
		JSONObject jsonObj = readLabFile(labFile);
		if (jsonObj == null) {
			return null;
		}
		//
		//validate if lab
		//
		try {
			return (Lab) loadMatter(jsonObj);
		} catch (InvalidLabException ex) {
			Logger.getLogger(LabLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 * recursively load matter. usually called only once on a lab.
	 *
	 * @param matterJSONObj
	 * @return
	 */
	private static Matter loadMatter(JSONObject matterJSONObj)
		throws InvalidLabException {
		//
		String id,version,type;// exception if no id,version or type
		HashMap<String,Expression> constMap;
		HashSet<Molecule> elementsSet;
		EnumMap<Bond,Expression> bondMap;
		//
		type = readSimpleProperty(matterJSONObj,"type").toLowerCase();
		if(type.equals("outputclass"))
			return null;//TODO
		//
		id = readSimpleProperty(matterJSONObj,"id").toLowerCase();
		version = readSimpleProperty(matterJSONObj,"version").toLowerCase();
		//
		constMap = null;
		if(matterJSONObj.containsKey("const")){
			constMap = new HashMap<String,Expression>();
			HashMap constJSONHashMap = (HashMap)matterJSONObj.get("const");//
			//
			Iterator it = constJSONHashMap.keySet().iterator();
			while(it.hasNext()){
				String k = (String)it.next();
				constMap.put(k, new Expression((String)constJSONHashMap.get(k)));
			}
		}
		//
		elementsSet = null;
		if( matterJSONObj.containsKey("elements")){
			elementsSet= new HashSet<Molecule>();
			JSONArray elementsJSONArray = (JSONArray)matterJSONObj.get("elements");
			for (Object elementJSONObj : elementsJSONArray) {
				elementsSet.add((Molecule) loadMatter((JSONObject) elementJSONObj));
			}
		}
		//
		bondMap = null;
		if(matterJSONObj.containsKey("bond")){
			bondMap = new EnumMap<Bond, Expression>(Bond.class);
			JSONObject bondJSONObj = (JSONObject)matterJSONObj.get("bond");
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
		//
		switch (type) {
			case "lab":
				return new Lab(id,version,constMap,elementsSet);
			case "atom":
				return new Atom(id,version,constMap,elementsSet,bondMap);
			case "compound":
				return new Compound(id,version,constMap,elementsSet,bondMap);
			case "outputclass":
				break;
			default:
				throw new InvalidLabException();
		}
		return null;
	}
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
