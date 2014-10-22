/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.value;

import java.util.List;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Svg extends Expr implements Value {
	public Svg(String svgStr) {
		super(convertSvgStringToExprString(svgStr));
	}

	private static String convertSvgStringToExprString(String inputStr) {
		/*
		 * The general format of input is TEXT <<< CODE >>> TEXT <<< CODE >>> TEXT etc.
		 *    the input will alternate between text and code
		 * The result is "TEXT " + CODE + "TEXT" + CODE ....
		 * 
		 * The method process pair of TEXT CODE and then TEXT CODE
		 * because it's easier if you know the indexes of <<< and >>>
		 */
		List<Integer> starts = getAllIndexesOf(inputStr, "<<<");
		List<Integer> ends = getAllIndexesOf(inputStr, ">>>");
		if(starts.size() != ends.size()) return "INVALID SVG"; 
		//          TODO : do something better than return text like throwing exception
		//
		StringBuilder script = new StringBuilder();
		int strStart = 0, strEnd, codeStart, codeEnd;
		for(int i = 0 ; i < starts.size() ; i++){
			strEnd = starts.get(i);
			codeStart = strEnd+"<<<".length();
			codeEnd = ends.get(i);
			//
			script.append(escape(inputStr.substring(strStart, strEnd)))
				.append("\"+")
				.append(inputStr.substring(codeStart,codeEnd))
				.append("+\"");
			//
			strStart = codeEnd+">>>".length();
		}
		script.append(escape(inputStr.substring(strStart))); // appending last part of the text
		script.append("\"").insert(0, "\"");
		//
		return script.toString();
	}
	private static List<Integer> getAllIndexesOf(String source, String term){
		java.util.List<Integer> indexesList = new java.util.ArrayList<Integer>();
		int idx = -1;
		idx = source.indexOf(term);
		while(idx >= 0){
			indexesList.add(idx);
			idx = source.indexOf(term, idx+term.length());
		}
		return indexesList;
	}

	private static String escape(String str){
		return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
	}
}
