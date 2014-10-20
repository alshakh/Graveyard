/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import symcode.value.Value;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Property {
	public final String _id;
	public final Value _value;

	public Property(String id, Value value){
		_id = id;
		_value = value;
	}



	/**
	 * object reference is defined as the object. so, if property is a.b the reference is a.
	 * @return 
	 */
	public String getObjectReference(){
		if(!isObjectMember()) return "";
		// there is a dot
		return _id.substring(0,_id.indexOf("."));
	}



	/**
	 * object members defined as having an id with dot in it. a.b
	 * @return 
	 */
	public boolean isObjectMember(){
		return _id.contains(".");
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 59 * hash + this._id.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Property other = (Property) obj;
		if (!this._id.equals(other._id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return (_id+"= "+_value.toString());
	}


}
