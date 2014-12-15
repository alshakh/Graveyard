/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanara.evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.script.ScriptException;
import sanara.evaluator.EnvDef.BackupEnvDef;
import sanara.evaluator.EnvDef.CallEnvDef;
import sanara.evaluator.EnvDef.EnvDefFactory;
import sanara.lab.Property;
import sanara.lab.Property.ProductProperty;

/**
 * An instance of this class maintains the order of execution of properties in
 * order to get the right order of dependency until converting to actual
 * environment. Mutable object
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EnvironmentBuilder {
	public static interface EnvSortable {
		public boolean matchReference(String ref);
		public String getReference();
		public Set<String> getDependencies();
	}

	public static final int DOES_NOT_EXIST = -1;

	private final ArrayList<EnvDef> _envDefs;

	public EnvironmentBuilder() {
		_envDefs = new ArrayList<EnvDef>();
	}
	public EnvironmentBuilder(List<EnvDef> list){
		_envDefs = new ArrayList<EnvDef>(list);
	}

	@Override
	public String toString() {
		return toEvaluableScript();
	}

	/**
	 * turning the environment builder into an actual environment.
	 *
	 * @return
	 */
	public Environment toEnvironment(String symId) throws EvaluationError {
		try {
			return new Environment(toEvaluableScript());
		} catch (ScriptException ex) {
			throw new EvaluationError("Problem with preparing the environment to evaluate " + symId);
		}
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

	/**
	 * add Property to the environment. if new property replaces an existing
	 * backup Property it will replace it with real property.
	 *
	 * @param property
	 */
	public void addProperty(Property property) {
		EnvDef envDef = EnvDefFactory.create(property);
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
				_envDefs.add(EnvDefFactory.create(property));
			}
		} /*
		 if property doesn't exist
		 */ else {
			_envDefs.add(EnvDefFactory.create(property));
		}
	}

	public void addPropertyCollection(Collection<Property> properties) {
		for (Property p : properties) {
			addProperty(p);
		}
	}

	public void prepare(Evaluator evaluator) throws SyntaxError, EvaluationError {
		processCalls(evaluator);	
		sort(_envDefs);
	}

	private void processCalls(Evaluator evaluator)  throws SyntaxError, EvaluationError{
		List<CallEnvDef> calls =  getAndSortCallEnvDef();
		_envDefs.removeAll(calls);
		//
		for(CallEnvDef call : calls){
			Set<String> deps = getDependenciesOfReferences(call.getDependencies());
			List<EnvDef> depTree = referencesToList(deps);
	
			Environment env;
			try {
				env = new EnvironmentBuilder(depTree).toEnvironment(call._reference);
			} catch (EvaluationError ex) {
				throw new EvaluationError("Error while processing the call "+call._reference+" : " + ex.getMessage());
			}
			Product product;
			try {
				product = evaluator.eval(env.resolveReference(call._property._value.toEvaluableScript()).toString());
			} catch (SyntaxError ex) {
				throw new SyntaxError("SyntaxError while processing the call "+call._reference+" : "  + ex.getMessage());
			} catch (EvaluationError ex) {
				throw new EvaluationError("EvaluationError while processing the call "+call._reference+" : "  + ex.getMessage());
			}
			for(ProductProperty prop: product._properties){
				_envDefs.add(EnvDefFactory.create(prop.toEvaluableProperty(call._reference)));
			}
		}
	}

	/**
	 * Gets all callEnvDefs and sort them by dependency. to be used in
	 * processCalls
	 *
	 * @return
	 */
	private List<CallEnvDef> getAndSortCallEnvDef() {
		// Get List of all Calls
		List<CallEnvDef> callList = new ArrayList<CallEnvDef>();
		for (EnvDef envDef : _envDefs) {
			if (envDef instanceof CallEnvDef) {
				callList.add((CallEnvDef) envDef);
			}
		}
		if (callList.isEmpty()) {
			return callList;
		}
		// Sort dependencies
		/*
		 Calls depend on three things:
		 1) properties of other calls {a: circle}{b: enlarge[a.x]}
		 2) other properties.
		 */
		Set<String> depsCache = new HashSet<String>();
		for (int i = 0; i < callList.size(); i++) {
			EnvDef envDef = callList.get(i);
			if (envDef.getDependencies().isEmpty()) {
				continue;
			}
			//
			depsCache.clear(); // new cache for every element
			//
			while (true) {
				int depIdx = DOES_NOT_EXIST;
				boolean fixDependency = false;
				for (String depRef : envDef.getDependencies()) {
					// Get the object from dependency
					if(depRef.contains(".")){
						depRef = depRef.substring(0, depRef.indexOf('.'));
					}
					//
					{ // get index of call in list
						for(int j = 0 ; j < callList.size() ; j++){
							if(!(callList.get(j) instanceof CallEnvDef)) continue;
							if(callList.get(j)._reference.equals(depRef)){
								depIdx = j;
								break;
							}	
						}
					}
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
					Collections.swap(callList, i, depIdx);
					envDef = callList.get(i);
				}
			}
		}
		return callList;
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

	public List<EnvDef> referencesToList(Set<String> references) throws EvaluationError {
		//
		List<EnvDef> tree = new ArrayList<EnvDef>();
		for (String ref : references) {
			tree.add(getEnvDefByReference(ref));
		}
		sort(tree);
		return tree;
	}

	public Set<String> getDependenciesOfReferences(Set<String> depOrig) {
		Set<String> deps = new HashSet<String>(depOrig);
		Set<String> references = new HashSet<String>();

		while (!deps.isEmpty()) {
			String depRef = deps.iterator().next();
			if (!references.contains(depRef)){
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

	
	public static <K extends EnvSortable> void sort(List<K> callList) throws EvaluationError {
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
		class SortInterface {

			private final List<K> _list;

			public SortInterface(List<K> list) {
				_list = list;
			}

			/**
			 * POTENTIAL ERROR : if a callProperty with the
			 * reference aaa and then a property with reference
			 * aaa.bbb the search will return the callProperty not
			 * the property
			 *
			 * @param reference
			 * @return
			 */
			public K getByReference(String reference) {
				for (K item : _list) {
					if (item.matchReference(reference)) {
						return item;
					}
				}
				return null;
			}

			public K getByIndex(int index) {
				return _list.get(index);
			}

			public int getIndexOfReference(String reference) {
				for (int i = 0; i < _list.size(); i++) {
					if (_list.get(i).matchReference(reference)) {
						return i;
					}
				}
				return -1;
			}

			public void swap(String ref1, String ref2) {
				int idx1 = -1, idx2 = -1;
				for (int i = 0; i < _list.size(); i++) {
					if (idx1 == -1 && _list.get(i).matchReference(ref1)) {
						idx1 = i;
					} else if (idx2 == -1 && _list.get(i).matchReference(ref2)) {
						idx2 = i;
					}
					if (idx1 != -1 && idx2 != -1) {
						break;
					}
				}
				Collections.swap(_list, idx1, idx2);
			}

			public void swap(int idx1, String ref2) {
				int idx2 = getIndexOfReference(ref2);
				Collections.swap(_list, idx1, idx2);
			}

			public int getSize() {
				return _list.size();
			}
		}
		class SortUtil {

			/**
			 *
			 * @param sortMap
			 * @param i
			 * @return first unsatisfied dependency or null if all
			 * deps are good
			 */
			private String firstUnsatisfiedDep(SortInterface sortMap, int i) throws EvaluationError {
				int depIndex;
				for (String dep : sortMap.getByIndex(i).getDependencies()) {
					depIndex = sortMap.getIndexOfReference(dep);
					if (depIndex > i) {
						return dep;
					} else if (depIndex == -1) {
						throw new EvaluationError("lab error : missing dependency ["+dep+"]");
					}
				}
				return null;
			}

			public void fixIndex(SortInterface sortMap, int i, Set<String> depCache) throws EvaluationError {
				//EnvSortable envDef = sortMap.getByIndex(i);
				String dep = firstUnsatisfiedDep(sortMap, i);
				if (dep == null) {
					return; // all deps are good
				} else if (depCache.contains(dep)) {
					throw new EvaluationError("lab error : circular dependency ["+dep+"]");
				} else {
					depCache.add(dep);
				}

				sortMap.swap(i, dep);
				fixIndex(sortMap, i, depCache);
			}
		}
		SortUtil sortUtil = new SortUtil();
		SortInterface sortMap = new SortInterface(callList);
		Set<String> depsCache = new HashSet<String>();
		for (int idxToFix = 0; idxToFix < callList.size(); idxToFix++) {
			EnvSortable envDef = sortMap.getByIndex(idxToFix);
			if (envDef.getDependencies().isEmpty()) {
				continue;
			}
			depsCache.clear();
			sortUtil.fixIndex(sortMap, idxToFix, depsCache);
		}
	}
}