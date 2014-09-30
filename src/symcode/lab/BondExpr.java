/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import symcode.expr.Expression;

/**
 * The purpose of this class is to pass bond info from LabLoader to Molecule.
 * @author Ahmed Alshakh www.alshakh.net
 */
public class BondExpr {

	/**
	 *
	 */
	public static final BondExpr EMPTY = new BondExpr(
		new Expression("0"),
		new Expression("0"),
		new Expression("0"),
		new Expression("0")
	);
	private final Expression _x;	
	private final Expression _y;	
	private final Expression _h;	
	private final Expression _w;	

	/**
	 *
	 * @param x
	 * @param y
	 * @param h
	 * @param w
	 */
	public BondExpr(Expression x, Expression y, Expression h, Expression w){
		_x = x;
		_y = y;
		_h = h;
		_w = w;
	}
	public String toString(){
	return "x : " + _x.toString() + "\n"
	+ "y : " + _y.toString() + "\n"
	+ "h : " + _h.toString() + "\n"
	+ "w : " + _w.toString();

	}

	public Expression getX(){
		return _x;
	}
	public Expression getY(){
		return _y;
	}
	public Expression getH(){
		return _h;
	}
	public Expression getW(){
		return _w;
	}
}
