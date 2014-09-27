/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Molecule extends Template implements Loadable  {
	//
	public static final HashSet<String> EMPTY_REFERENCES = new HashSet<String>();
	//
	private final BondExpr _bond;
	private final HashSet<String> _references;
	//
	/**
	 *
	 * @param id
	 * @param version
	 * @param constMap
	 * @param elementsSet
	 * @param bond
	 * @param references
	 */
	public Molecule(String id,String version, HashMap constMap, HashSet elementsSet, BondExpr bond, HashSet<String> references){
		super(id, version, constMap, elementsSet);
		_bond = bond;
		_references = references;
	}
	//
	public HashSet<String> getReferences(){
		return _references;
	}
	//
		
}
