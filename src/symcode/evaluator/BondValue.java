/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class BondValue {
	
	public final double _x;
	public final double _y;
	public final double _h;
	public final double _w;
	public final String _svg;

	/**
	 *
	 * @param svg
	 * @param x
	 * @param y
	 * @param h
	 * @param w
	 */
	public BondValue(String svg, double x, double y, double h, double w){
		_x = x;
		_y = y;
		_h = h;
		_w = w;
		_svg = svg;
	}

}
