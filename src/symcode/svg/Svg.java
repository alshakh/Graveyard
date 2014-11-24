/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.svg;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.app.beans.SVGIcon;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import symcode.value.Str;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Svg {
	public enum Type { FULL, STRIP}
	//
	public static final String INDENT_SPACE="    ";
	//
	public final String _content;
	/**
	 * to be used in toStringFull because it need to encode the width and height
	 */
	public final double _width, _height;

	public Svg(String content, double w, double h){
		_width = w;
		_height = h;
		_content = content;
	}

	@Override
	public abstract String toString();
	public abstract Svg combine(Svg other);
	public abstract FullSvg toFullSvg();
	public abstract StripSvg toStripSvg();

	public abstract Type getType();
	/**
	 * gets the svg as is as opposed to ready to draw svg.
	 * if content is full already, <code>toStringString</code> will be 
	 * identical to <code>toStringFull</code>
	 * @param subContent
	 * @param x
	 * @param y
	 * @return 
	 */
	protected static String processPosition(String subContent, double x, double y){
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
	public abstract Svg scale(double xFactor, double yFactor);
	
	protected static String indentEveryLine(String str){
		return indentEveryLine(str,1);
	}
	protected static String indentEveryLine(String str, int factor){
		StringBuilder gapBuilder = new StringBuilder();
		for(int i=0 ; i < factor ; i++){
			gapBuilder.append(INDENT_SPACE);
		}
		String gap = gapBuilder.toString();
		//
		return (gap + str.replaceAll("\n", "\n"+gap));
	}
	protected static String doubleToProperString(double d){
		if(d == (long) d)
			return String.format("%d",(long)d);
		else
			return String.format("%s",d);
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
		String svgToConvert = this.toFullSvg().toString();
		SVGIcon icon;
		StringReader reader = new StringReader(svgToConvert);
		SVGCache.getSVGUniverse().clear();
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
