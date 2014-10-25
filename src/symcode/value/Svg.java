/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.value;

import java.util.Collections;
import java.util.Set;
import symcode.evaluator.Product;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Svg implements Value {
	public static final String INDENT_SPACE="    ";
	//
	private final String _content;

	public Svg(Product p){
		this(
			p._svg.toStripString()
			, p._x.toDouble()
			, p._y.toDouble()
			, p._h.toDouble()
			, p._w.toDouble()
		);
	}
	/**
	 * to be used if you want to treat string as Svg Obj. 
	 * @param fullContent 
	 */
	public Svg(String fullContent){
		_content = fullContent;
	}
	private Svg(String svgSubContent, double x, double y, double h, double w){
		_content = processToString(svgSubContent, x, y, h, w);
	}
	private static String processToString(String subContent, double x, double y, double width, double height){
		return "<g"
		       +" x=\""+doubleToProperString(x)+"\""
		       +" y=\""+doubleToProperString(y)+"\""
		       +" height=\""+doubleToProperString(height)+"\""
		       +" width=\""+doubleToProperString(width)+"\""
		       +">"
		       +"\n"
		       +indentEveryLine(subContent)
		       +"\n"
		       +"</g>";

	}
	private static String indentEveryLine(String str){
		return indentEveryLine(str,1);
	}
	private static String indentEveryLine(String str, int factor){
		StringBuilder gapBuilder = new StringBuilder();
		for(int i=0 ; i < factor ; i++){
			gapBuilder.append(INDENT_SPACE);
		}
		String gap = gapBuilder.toString();
		//
		return (gap + str.replaceAll("\n", "\n"+gap));
	}
	private static String doubleToProperString(double d){
		// TODO : no floating point except when neccesary 
		if(d == (long) d)
			return String.format("%d",(long)d);
		else
			return String.format("%s",d);
	}
	public Svg combine(Svg other){
		return new Svg(this.toStripString()+"\n"+this.toStripString());
	}

	/**
	 * gets the svg as is as opposed to ready to draw svg.
	 * @return 
	 */
	public String toStripString() {
		return _content;
	}

	@Override
	public Set<String> getNeededProperties() {
		return Collections.<String>emptySet();
	}

	@Override
	public String toEvaluableScript() {
		return "\""+Str.escapeString(_content)+"\"";
	}

	public String toFullString() {
		// TODO : replace by actual SvgCode
		return "<SVG-BEGIN>\n"+indentEveryLine(_content)+"\n</SVG-END>";
	}

	
	@Override
	public String toString(){
		return toFullString();
	}
}
