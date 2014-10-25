/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.value;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Expr implements Value {

	private final String _expr;
	private final Set<String> _neededProperties;

	public Expr(String string) {
		_expr = string;
		_neededProperties = Collections.unmodifiableSet(ExtractVarsReferred.extractVarsReferred(_expr));
	}

	/**
	 * return a set of all variable that need values in the expression.
	 * example "2*x+name" will return "x" and "name"
	 *
	 * @return immutable Set<String> of variables
	 */
	@Override
	public Set<String> getNeededProperties() {
		return _neededProperties;
	}

	public String toString() {
		return _expr;
	}

	@Override
	public String toEvaluableScript() {
		return _expr;
	}


	private static class ExtractVarsReferred {

		/**
		 *
		 * @return
		 */
		public static Set<String> extractVarsReferred(String exprStr) {
			// clean comments, quotes and keywords
			String toCheckExprStr = cleanForExtraction(exprStr);

			Pattern p = Pattern.compile("(\\$\\w+|[a-zA-z][\\w\\d\\_]*|\\.)+");
			Matcher m = p.matcher(toCheckExprStr);
			//
			Set<String> deps = new HashSet<String>();
			while (m.find()) {
				deps.add(toCheckExprStr.substring(m.start(), m.end()));
			}
			return deps;
		}
		/*
		 Clean Expression
		 */
		private static final String jsCleanPattern
			= // regex for quoted text is "(?:[^"\\]|\\.)*"
			"(\"(?:[^\"\\\\]|\\\\.)*\")"
			// regex for comment is //.*$
			+ "|" + "(//.*$)"
			// JS keywords are break,case,class,catch,const,continue,debugger,default,delete,do,else,export,extends,finally,for,function,if,import,in,instanceof,let,new,return,super,switch,this,throw,try,typeof,var,void,while,with,yield
			+ "|" + "\\b(break|case|class|catch|const|continue|debugger|default|delete|do|else|export|extends|finally|for|function|if|import|in|instanceof|let|new|return|super|switch|this|throw|try|typeof|var|void|while|with|yield)\\b";

		//

		/**
		 * Clean quotes, comments and JS Keywords
		 *
		 * @param text
		 * @return
		 */
		private static String cleanForExtraction(String text) {
			return  text.replaceAll(jsCleanPattern, "");
		}
	}
}
