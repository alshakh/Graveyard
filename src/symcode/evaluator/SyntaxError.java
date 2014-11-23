package symcode.evaluator;
import symcode.evaluator.Tokenizer.Token;
/**
 * Thrown to indicate an error in the parsing and matching with labs.
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