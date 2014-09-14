/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.expr;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Expression {
	private final String exprStr;
	public Expression(String expr){
		exprStr = expr;
	} 

	@Override
	public String toString(){
		return exprStr;
	}
}
