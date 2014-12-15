package sanara.lab;

import sanara.value.SemiExpr;
import sanara.value.Expr;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sanara.lab.Property.BackupConstProperty;
import sanara.lab.Property.BackupNormalProperty;
import sanara.lab.Property.Call;
import sanara.lab.Property.ConstProperty;
import sanara.lab.Property.NormalProperty;

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

	private static TemplateInfo procTemplateInfo(JSONObject jsonObj) {
		String id = readSimpleJsonProperty(jsonObj, "id");
		if (id.isEmpty()) {
			id = Util.generateRandomId(); // doublication will be a problems. Everything in the same scope should have a unique id.
		}
		String version = readSimpleJsonProperty(jsonObj, "version").toLowerCase();
		return new TemplateInfo(id, version);
	}

	private static Lab procLab(JSONObject jsonObj) {
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
		return new Lab(tInfo._id, tInfo._version, processProperties(tInfo._id, jsonObj), elements);
	}

	private static Molecule procMoleculeInLab(JSONObject jsonObj) {
		if (jsonObj.containsKey("atoms")) {
			return procCompound(jsonObj);
		} else { // SingleAtom
			return procSingleAtom(jsonObj);
		}
	}

	private static SingleAtom procSingleAtom(JSONObject jsonObj) {
		TemplateInfo tInfo = procTemplateInfo(jsonObj);
		return new SingleAtom(tInfo._id, tInfo._version, processProperties(tInfo._id, jsonObj));
	}

	private static Compound procCompound(JSONObject jsonObj) {
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
		return new Compound(tInfo._id, tInfo._version, processProperties(tInfo._id, jsonObj), atoms);
	}

	private static BondedAtom procDoubleAtom(JSONObject jsonObj) {
		TemplateInfo tInfo = procTemplateInfo(jsonObj);
		return new BondedAtom(tInfo._id, tInfo._version, processProperties(tInfo._id, jsonObj));
	}

	private static Set<Property> processProperties(final String objId, JSONObject jsonObj) {
		final Set<Property> properties = new HashSet<Property>();
		//+ constants
		final String CONST = "const";
		if (jsonObj.containsKey(CONST)) {
			JSONObject constJsonObj = (JSONObject) jsonObj.get(CONST);
			for (Object key : constJsonObj.keySet()) {
				properties.add(new ConstProperty(key.toString(), new Expr(constJsonObj.get(key.toString()).toString())));
			}
		}
		//-
		//+ Normal properties
		//+ mandatory properties ( position and shape => expressions )
		for (String pName : NormalProperty.MANDATORY_PROPERTIES.keySet()) {
			if (jsonObj.containsKey(pName)) {
				properties.add(new NormalProperty(objId, pName, new Expr((String) jsonObj.get(pName))));
			} else {
				properties.add(new NormalProperty(objId, pName, NormalProperty.MANDATORY_PROPERTIES.get(pName)));
			}
		}
		//-
		//+ svg property ( => svg)
		final String SVG = "svg";
		if (jsonObj.containsKey(SVG)) {
			properties.add(new NormalProperty(objId, "svg", new SemiExpr((String) jsonObj.get(SVG))));
		}
		//-
		//+ custom properties
		for (Object key : jsonObj.keySet()) {
			if (key.toString().startsWith("_")) {
				properties.add(new NormalProperty(
					objId, key.toString(),
					new Expr(jsonObj.get(key.toString()).toString())));
			}
		}
		//-
		//- normal
		//+ BackupProperties
		final String BACKUP = "backupProperties";
		if (jsonObj.containsKey(BACKUP)) {
			JSONObject backupJson = (JSONObject) jsonObj.get(BACKUP);
			for (Object key : backupJson.keySet()) {
				if (key.toString().contains(".")) {
					String[] parts = key.toString().split("\\.");
					properties.add(new BackupNormalProperty(parts[0], parts[1], new Expr(backupJson.get(key).toString())));
				} else {
					properties.add(new BackupConstProperty(key.toString(), new Expr(backupJson.get(key).toString())));
				}
			}
		}
		//-
		//+ Calls
		final String CALLS = "calls";
		if (jsonObj.containsKey(CALLS)) {
			JSONObject jsonCalls = (JSONObject) jsonObj.get(CALLS);
			for (Object key : jsonCalls.keySet()) {
				properties.add(new Call(key.toString(), new SemiExpr(jsonCalls.get(key).toString())));
			}
		}
		//-
		return properties;
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
