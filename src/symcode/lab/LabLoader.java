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
import symcode.lab.Property.ConstProperty;
import symcode.lab.Property.NormalProperty;
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
		return procLab(jsonObj);
	}
	private static TemplateInfo procTemplateInfo(JSONObject jsonObj){
		String id = readSimpleJsonProperty(jsonObj, "id");
		if(id.isEmpty()) id=Util.generateRandomId(); // doublication will be a problems. Everything in the same scope should have a unique id.
		String version = readSimpleJsonProperty(jsonObj, "version").toLowerCase();
		return new TemplateInfo(id, version);
	}
	private static Lab procLab(JSONObject jsonObj){
		TemplateInfo tInfo = procTemplateInfo(jsonObj);
		//+ proc LabElements
		Set<Molecule> elements = Lab.EMPTY_ELEMENTS;
		if (jsonObj.containsKey("elements")) {
			elements = new HashSet<Molecule>();
			JSONArray elementsJSONArray = (JSONArray) jsonObj.get("elements");
			for (Object elementJsonObj : elementsJSONArray) {
				elements.add(procMoleculeInLab((JSONObject) elementJsonObj));
			}
		}
		//-
		return new Lab(tInfo._id, tInfo._version, procConsts(jsonObj), elements);
	}
	private static Molecule procMoleculeInLab(JSONObject jsonObj){
		if(jsonObj.containsKey("atoms")){
			return procCompound(jsonObj);
		} else { // SingleAtom
			return procSingleAtom(jsonObj);
		}
	}

	private static SingleAtom procSingleAtom(JSONObject jsonObj){
		TemplateInfo tInfo = procTemplateInfo(jsonObj);
		return new SingleAtom(tInfo._id, tInfo._version, procNormalProperties(tInfo._id,jsonObj),procConsts(jsonObj), procDependencies(jsonObj));
	}
	private static Compound procCompound(JSONObject jsonObj){
		TemplateInfo tInfo = procTemplateInfo(jsonObj);
		//
		Set<BondedAtom> atoms = Compound.EMPTY_ATOMS_SET;
		if (jsonObj.containsKey("atoms")) {
			atoms = new HashSet<BondedAtom>();
			JSONArray atomsJSONArray = (JSONArray) jsonObj.get("atoms");
			for (Object atomObj : atomsJSONArray) {
				atoms.add(procDoubleAtom((JSONObject) atomObj));
			}
		}
		//
		return new Compound(tInfo._id, tInfo._version, procNormalProperties(tInfo._id,jsonObj),procConsts(jsonObj), atoms, procDependencies(jsonObj));
	}

	private static BondedAtom procDoubleAtom(JSONObject jsonObj){
		TemplateInfo tInfo = procTemplateInfo(jsonObj);
		return new BondedAtom(tInfo._id, tInfo._version, procNormalProperties(tInfo._id,jsonObj),procConsts(jsonObj), procDependencies(jsonObj));
	}

	private static Set<ConstProperty> procConsts(JSONObject jsonObj){
		Set<ConstProperty> ps =Template.EMPTY_CONSTS;
		//+ constants are properties ( ==> Doub)
		if (jsonObj.containsKey("const")) {
			ps = new HashSet<ConstProperty>();
			JSONObject constJsonObj = (JSONObject)jsonObj.get("const");
			Iterator itr = constJsonObj.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				ps.add(new ConstProperty(key, new Doub(constJsonObj.get(key).toString())));
			}
		}
		//-
		return ps;
	}

	private static Set<NormalProperty> procNormalProperties(String objId, JSONObject jsonObj) {
		Set<NormalProperty> ps = new HashSet<NormalProperty>();
		//+ mandatory properties ( position and shape => expressions )
		for (String pName : NormalProperty.MANDATORY_PROPERTIES.keySet()) {
			if (jsonObj.containsKey(pName)) {
				if(Property.isSvgProperty(pName)){
					ps.add(new NormalProperty(objId, pName, new SvgExpr(jsonObj.get(pName).toString())));
				} else {
					ps.add(new NormalProperty(objId, pName, new Expr((String) jsonObj.get(pName))));
				}
			} else {
				ps.add(new NormalProperty(objId, pName, NormalProperty.MANDATORY_PROPERTIES.get(pName)));
			}
		}
		//-
		//+ svg property ( => svg)
		if (jsonObj.containsKey("svg")) {
			ps.add(new NormalProperty(objId, "svg", new SvgExpr((String) jsonObj.get("svg"))));
		}
		//-
		//+ custom properties
		Iterator itr = jsonObj.keySet().iterator();
		while(itr.hasNext()){
			String key = itr.next().toString();
			if(key.startsWith("_")){
				ps.add(new NormalProperty(objId, key, new Expr(jsonObj.get(key).toString())));
			}
		}
		//-
		return ps;
	}

	private static Set<String> procDependencies(JSONObject jsonObj) {
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

	private static String readSimpleJsonProperty(JSONObject jsonObj, String property) {
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

class TemplateInfo {
	public final String _id;
	public final String _version;
	
	public TemplateInfo(String _id, String _version) {
		this._id = _id;
		this._version = _version;
	}
}
