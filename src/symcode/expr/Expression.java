/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.expr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.*;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Expression {

	/**
	 *
	 */
	public static final Expression EMPTY_EXPRESSION = new Expression("0");
	// factory just creates new engines. no need to make many of them.
	private static final ScriptEngineManager factory = new ScriptEngineManager();
	private final String _exprStr;

	/**
	 *
	 * @param expr
	 */
	public Expression(String expr) {
		_exprStr = expr;
	}

	/**
	 *
	 */
	public Expression() {
		_exprStr = "";
	}

	/**
	 *
	 * @param environment
	 * @return
	 */
	public double eval(Environment environment) {
		// 
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		//
		Object result = null;
		try {
			if (environment != null) {
				environment.apply(engine);
			}
			// evaluate JavaScript code from String
			result = engine.eval(_exprStr);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		if (result == null) {
			return 0.0;
		}
		if (!result.getClass().equals(Double.class)) {
			return 0.0;
		}
		return (Double) result;
	}

	/**
	 *
	 * @return
	 */
	public Double eval() {
		return eval(null);
	}


	@Override
	public String toString() {
		return _exprStr;
	}

	/**
	 *
	 * @return
	 */
	public HashSet<String> extractReferences() {
		// clean comments, quotes and keywords
		String toCheckExprStr = cleanForExtracting(_exprStr);

		Pattern p = Pattern.compile("(\\$\\d+|[a-zA-z][\\w\\d\\_]*|\\.)+");
		Matcher m = p.matcher(toCheckExprStr);
		//
		HashSet<String> deps = new HashSet<String>();
		while (m.find()) {
			deps.add(toCheckExprStr.substring(m.start(), m.end()));
		}
		return deps;
	}
	/*
		Clean Expression
	*/	
	private static final String jsCleanPattern = 
		// regex for quoted text is ".+?[^\\]"
		"(\".+?[^\\\\]\")"
		// regex for comment is //.*$
		+"|"+ "(//.*$)"
		// JS keywords are break,case,class,catch,const,continue,debugger,default,delete,do,else,export,extends,finally,for,function,if,import,in,instanceof,let,new,return,super,switch,this,throw,try,typeof,var,void,while,with,yield
		+"|"+ "\\b(break|case|class|catch|const|continue|debugger|default|delete|do|else|export|extends|finally|for|function|if|import|in|instanceof|let|new|return|super|switch|this|throw|try|typeof|var|void|while|with|yield)\\b";
		//
	/**
	 * Clean quotes, comments and JS Keywords
	 * @param text
	 * @return 
	 */
	private static String cleanForExtracting(String text){
		Matcher m = Pattern.compile(jsCleanPattern,Pattern.MULTILINE).matcher(text);
		//
		StringBuilder newText = new StringBuilder();
		int goodStart = 0; // start of wanted text
		while (m.find()) {
			newText.append(text.substring(goodStart,m.start()));
			goodStart = m.end();
		}
		newText.append(text.substring(goodStart));

		/*
			return
		*/
		return newText.toString();
	}
}
