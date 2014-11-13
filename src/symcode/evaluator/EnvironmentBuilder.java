/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptException;
import symcode.evaluator.EnvDef.BackupConstEnvDef;
import symcode.evaluator.EnvDef.BackupEnvDef;
import symcode.evaluator.EnvDef.BackupNormalEnvDef;
import symcode.evaluator.EnvDef.CallEnvDef;
import symcode.evaluator.EnvDef.ConstEnvDef;
import symcode.evaluator.EnvDef.NormalEnvDef;
import symcode.lab.Property;
import symcode.lab.Property.BackupConstProperty;
import symcode.lab.Property.BackupNormalProperty;
import symcode.lab.Property.Call;
import symcode.lab.Property.ConstProperty;
import symcode.lab.Property.NormalProperty;

/**
 * An instance of this class maintains the order of execution of properties in
 * order to get the right order of dependency until converting to actual
 * environment. Mutable object
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
class EnvironmentBuilder {

	public static final int DOES_NOT_EXIST = -1;

	private final ArrayList<EnvDef> _envDefs;
	private boolean _isCircularDependency = false ;
	private boolean _isMissingDependency = false;

	public EnvironmentBuilder() {
		_envDefs = new ArrayList<EnvDef>();
	}

	@Override
	public String toString() {
		return toEvaluableScript();
	}

	/**
	 * turning the environment builder into an actual environment.
	 *
	 * @param substituteMissing if true missing references will be
	 * substituted by empty values.
	 * @return
	 */
	public Environment toEnvironment() throws ScriptException {
		//throw new RuntimeException("Cannot convert invalid environment");
		return new Environment(toEvaluableScript());
	}

	/**
	 * precondition: all
	 *
	 * @return
	 */
	public String toEvaluableScript() {
		StringBuilder script = new StringBuilder();
		//+ collect object references
		Set<String> refs = new HashSet<String>();
		for (EnvDef e : _envDefs) {
			if (!e.needsJsObject()) {
				continue;
			}
			refs.add(e.getJsObjectName());
		}
		//-
		//+ add objects to script
		for (String ref : refs) {
			script.append("var ").append(ref).append(" = {}\n");
		}
		//-
		//+ add properties to script
		for (EnvDef e : _envDefs) {
			script.append(e.toEvaluableScript()).append("\n");
		}
		//-
		return script.toString();
	}

	public int getIndexOfReference(String reference) {
		for (int i = 0; i < _envDefs.size(); i++) {
			if (_envDefs.get(i)._reference.equals(reference)) {
				return i;
			}
		}
		return DOES_NOT_EXIST;
	}

	public EnvDef getEnvDefByReference(String reference) {
		int idx = getIndexOfReference(reference);
		if (idx == DOES_NOT_EXIST) {
			return null;
		}
		return _envDefs.get(idx);
	}

	public EnvDef makeEnvDef(Property property) {
		/*
			must check backupNormalProperty and backupConstProperty
		before checing constProperty and NormalProperty because
		backupNormalProperty is instance of NormalProeprty and it will match
		and the same thing with backupConstProperty
		*/
		if (property instanceof BackupNormalProperty) {
			return new BackupNormalEnvDef(property);
		} else if (property instanceof BackupConstProperty) {
			return new BackupConstEnvDef(property);
		} else if (property instanceof NormalProperty) {
			return new NormalEnvDef(property);
		} else if (property instanceof ConstProperty) {
			return new ConstEnvDef(property);
		} else if (property instanceof Call) {
			return new CallEnvDef(property);
		}
		throw new IllegalArgumentException("Illegal property type");
	}

	/**
	 * add Property to the environment. if new property replaces an existing
	 * backup Property it will replace it with real property.
	 *
	 * @param property
	 */
	public void addProperty(Property property) {
		EnvDef envDef = makeEnvDef(property);
		int idx = getIndexOfReference(envDef._reference);
		/*
		 if property reference exists
		 */
		if (idx != DOES_NOT_EXIST) {
			
			// If new envDef is backup, and real one exists don't add
			if (envDef instanceof BackupEnvDef) {
				return;
			}
			// check if existing EnvDef exisit
			if (_envDefs.get(idx) instanceof BackupEnvDef) {
				_envDefs.remove(idx);
				_envDefs.add(makeEnvDef(property));
			}
		} /*
		 if property doesn't exist
		 */ else {
			_envDefs.add(makeEnvDef(property));
		}
	}

	public void addPropertyCollection(Collection<Property> properties) {
		for (Property p : properties) {
			addProperty(p);
		}
	}

	public void addCallCollection(Collection<Call> calls) {
		for (Call c : calls) {
			addCall(c);
		}
	}

	public void addCall(Call call) {
		EnvDef ed = new CallEnvDef(call);
		if (_envDefs.contains(ed)) {
			return; // no duplication
		}
		_envDefs.add(ed);
	}

	public void prepare() {
		sortDefs(_envDefs);
	}

	public void processCalls() {
	/*	Set<String> callRefs = new HashSet<String>();
		for (EnvDef ed : _envDefs) {
			if (ed.isCall()) {
				callRefs.add(ed._reference);
			}
		}
		//
		if (callRefs.isEmpty()) {
			return;
		}
		//
		Set<String> processedCalls = new HashSet<String>();
		//
		Iterator callRefItr = callRefs.iterator();
		while (callRefItr.hasNext()) {
			EnvDef call = getEnvDefByReference(callRefs.iterator().next());
			Set<String> callDeps = call.getDependencies();
			for (String ref : callDeps) {
				if (processedCalls.contains(ref)) {
					continue;
				}
				EnvDef callDep = getEnvDefByReference(ref);
				if (callDep.isCall()) {

				}
			}
		}*/
	}

	public Product processCall(String callRef, Map<String, EnvDef> processedCalls) {
		return null;
	}

	public boolean hasReference(String ref) {
		for (EnvDef envRef : _envDefs) {
			if (envRef._reference.equals(ref)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasReferences(Collection<String> refs) {
		for (EnvDef ed : _envDefs) {
			if (refs.contains(ed._reference)) {
				refs.remove(ed._reference);
			}
		}
		return refs.isEmpty(); // empty means all references are found
	}

	public List<EnvDef> referencesToList(Set<String> references) {
		//
		List<EnvDef> tree = new ArrayList<EnvDef>();
		for (String ref : references) {
			tree.add(getEnvDefByReference(ref));
		}
		sortDefs(tree);
		return tree;
	}

	public Set<String> getDependenciesOfReference(String reference) {
		Set<String> deps = new HashSet<String>();
		Set<String> references = new HashSet<String>();

		deps.add(reference);
		while (!deps.isEmpty()) {
			String depRef = deps.iterator().next();
			if (!references.contains(depRef)) {
				deps.addAll(getEnvDefByReference(depRef).getDependencies());
				references.add(depRef);
			}
			deps.remove(depRef);
		}
		return references;
	}

	public Set<String> getMissingReferences() {
		Set<String> missingeReferences = new HashSet<String>();
		Set<String> allAvaliableReferences = new HashSet<String>();
		for (EnvDef ed : _envDefs) {
			allAvaliableReferences.add(ed._reference);
		}
		//
		for (EnvDef envDef : _envDefs) {
			for (String ref : envDef.getDependencies()) {
				if (!allAvaliableReferences.contains(ref)) {
					missingeReferences.add(ref);
				}
			}
		}
		return missingeReferences;
	}

	/*public Set<String> getCircularReferences(){
		
	 }*/
	private void sortDefs(List<EnvDef> defList) {
		/*
		 * The algorithm
		 * for Item in list (0 --> size)
		 *	if [ all depenencies of Item is before Item ]
		 * 		It is fine, move on to next
		 *	else
		 * 		Swap it with the first dependancy after Item.
		 * 		Check the new Item the same way until it stops on the lowest level of dependency
		 * 		If [ Same Item is fixed twice ]
		 * 			It's circular dependency
		 */
		// Reset circularDependancy varaible : It will be assigned in here
		for (int i = 0; i < defList.size(); i++) {
			EnvDef envDef = defList.get(i);
			/* fix element i*/
			{
				Set<String> depsCache = new HashSet<String>(); // new cache for every element
				while (!envDef.getDependencies().isEmpty()) {
					int depIdx = DOES_NOT_EXIST;
					boolean fixDependency = false;
					for (String depRef : envDef.getDependencies()) {
						depIdx = getIndexOfReference(depRef);
						if (depIdx != DOES_NOT_EXIST && depIdx > i) {
							fixDependency = true;
							break;
						}
					}
					if (!fixDependency) {
						break;
					}
					/* check if circule dependency */
					if (/* if not circule */!depsCache.contains(envDef._reference)) {

						depsCache.add(envDef._reference);
						Collections.swap(defList, i, depIdx);
						envDef = defList.get(i);
					}
				}
			} // END : fix element i
		}
		////
	}

	public boolean isCircularDependency() {
		return _isCircularDependency;
	}

	public boolean isMissingDependecy() {
		return _isMissingDependency;
	}
}
