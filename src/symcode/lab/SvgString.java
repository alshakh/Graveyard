/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.EnumMap;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class SvgString {
	private final String _content;

	/**
	 *
	 * @param content
	 */
	public SvgString(String content){
		_content = content;
	}

	/**
	 *
	 * @return
	 */
	public String getContent(){
		return _content;
	}

	//public SvgString applyBond(EnumMap<Bond, Double> bond){
	//}
}
