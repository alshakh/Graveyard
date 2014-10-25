/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.value;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.app.beans.SVGIcon;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import symcode.evaluator.Product;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Svg implements Value {
	public static final String INDENT_SPACE="    ";
	//
	private final String _content;
	/**
	 * if true, content is a standalone drawable svg, no need to wrap it in
	 * any way in <code>toStringFull</code
	 */
	private final boolean _contentIsFull;
	/**
	 * to be used in toStringFull because it need to encode the width and height
	 */
	private final double _width, _height;

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
		_contentIsFull = true;
		_width=_height=0;// the value doesn't matter because they wont be used
	}
	private Svg(String svgSubContent, double x, double y, double h, double w){
		_content = processToString(svgSubContent, x, y, h, w);
		_contentIsFull = false;
		_width = w;
		_height = h;
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
	 * if content is full already, <code>toStringString</code> will be 
	 * identical to <code>toStringFull</code>
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
		if(_contentIsFull)
			return _content;
		else {
			return "<svg width=\""+doubleToProperString(_width)+"\n"
			       +"height=\""+doubleToProperString(_height)+"\""
			       +">"
			       +"\n"
			       +indentEveryLine(_content)
			       +"\n"
			       +"</svg>";
		}
	}

	
	@Override
	public String toString(){
		return toFullString();
	}
	/*
	 *******************************************************************
	 *******************************************************************
	 *********** WORKING WITH ******************************************
	 *********** SVG SALAMANDER*****************************************
	 *******************************************************************
	 *******************************************************************
	 */
	public Icon toIcon(){
		String svgToConvert = this.toFullString();
		SVGIcon icon;
		StringReader reader = new StringReader(svgToConvert);
		java.net.URI uri = SVGCache.getSVGUniverse().loadSVG(reader, "myImage"); 
		icon = new SVGIcon();
		icon.setSvgURI(uri);
		icon.setAntiAlias(true);
		return icon;
	}
	public BufferedImage toBufferedImage(){
		// TODO : better than this
		Icon icon = toIcon();
		BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D ig2 = bi.createGraphics();
		icon.paintIcon(null, ig2, 0,0);
		//ImageIO.write(bi, "PNG", new File("yourImageName.png"));
		return bi;
	}
	//
	public static void outputBufferedImage(BufferedImage bi, File f) throws IOException{
		ImageIO.write(bi, "PNG", f);
	}
}
