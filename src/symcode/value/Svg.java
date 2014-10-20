/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.value;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Svg extends Expr implements Value {
	private final String _originalSvgString;
	public Svg(String svgStr) {
		super(convertSvgStringToScript(svgStr));
		_originalSvgString = svgStr;
	}

	public String toString(){
		return _originalSvgString;
	}

	private static  String convertSvgStringToScript(String inputStr) {
		// TODO : This code assumes valid and closed <<< >>>. Do other things
		StringBuilder scriptSB = new StringBuilder();
		scriptSB.append("\"");
		int startIdx = 0;
		while(inputStr.indexOf("<<<", startIdx)>=0){
			int endIdx = inputStr.indexOf("<<<",startIdx);
			scriptSB.append(escape(inputStr.substring(startIdx, endIdx)));
			startIdx = inputStr.indexOf(">>>",endIdx);
			
			scriptSB.append("\"+").append(inputStr.substring(endIdx+"<<<".length(),startIdx)).append("+\"");
			startIdx+=">>>".length();
		}
		scriptSB.append(escape(inputStr.substring(startIdx)));
		scriptSB.append("\"");
		
		return scriptSB.toString();
	}

	private static String escape(String str){
		return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
	}
}
