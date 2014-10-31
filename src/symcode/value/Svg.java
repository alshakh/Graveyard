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
import javax.imageio.ImageIO;
import javax.swing.Icon;
import symcode.evaluator.Product;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Svg {
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
			p.getProperty("svg")._value.toString()
			, p.getProperty("x")._value.toDouble()
			, p.getProperty("y")._value.toDouble()
			, p.getProperty("h")._value.toDouble()
			, p.getProperty("w")._value.toDouble()
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
		if(x==0 && y==0) return subContent;
		//
		return "<g"
		       +" transform=\""
		       +" translate("+doubleToProperString(x)+", "+doubleToProperString(y)+")"
		       +"\""
		       //+" height=\""+doubleToProperString(height)+"\""
		       //+" width=\""+doubleToProperString(width)+"\""
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
		if(d == (long) d)
			return String.format("%d",(long)d);
		else
			return String.format("%s",d);
	}
	public Svg combine(Svg other){
		/*  #BUG: Svg(String) takes full content whare as the content 
		below is not full
		*/
		return new Svg(this.toStripString()+"\n"+other.toStripString());
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

	public String toFullString() {
		// TODO : replace by actual SvgCode
		if(_contentIsFull)
			return _content;
		else {
			return "<svg"
				+" width=\""+doubleToProperString(_width)+"\""
			       +" height=\""+doubleToProperString(_height)+"\""
			       +">"
			       +"\n"
			       +indentEveryLine(_content)
			       +"\n"
			       +"</svg>";
		}
	}

	
	@Override
	public String toString(){
		return toStripString();
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

	public Str toStr() {
		return new Str(_content);
	}
}
