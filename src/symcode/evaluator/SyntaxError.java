/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;
import symcode.evaluator.Tokenizer.Token;
/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class SyntaxError extends Exception{
	public SyntaxError(String msg){
		super(msg);
	}
	public static SyntaxError createError(String msg, Token token){
		return new SyntaxError(msg+" "+token._string+" at(row:"+token._posRow +" col:"+token._posCol+")");
	}
	public static SyntaxError createUnexpectedTokenError(Token token){
		return new SyntaxError("Unexpected token "+token._string+" at(row:"+token._posRow +" col:"+token._posCol+")");
	}
}