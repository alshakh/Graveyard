/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanara.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sanara.evaluator.Tokenizer.Token;
import sanara.value.Doub;
import sanara.value.Str;
import sanara.value.Value;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Parser {

	public final ParseNode _parseTree;

	public Parser(String code) throws SyntaxError {
		_parseTree = toParseTree(new Tokenizer(code));
	}

	/**
	 * Checks the next token. Every method is required to go through this
	 * method.
	 *
	 * @param tokenizer
	 * @param type
	 * @return
	 */
	public boolean isNextToken(Tokenizer tokenizer, Token.Type type) {
		// TODO : Tokenizer needs to be more optimized if it is going to get next token and then revert.
		if (!tokenizer.hasNext()) {
			return false;
		}
		Token token = tokenizer.next();
		tokenizer.revert();
		return token._type == type;
	}

	/**
	 * Gets the type of the next token. returns Token.Type.End if no more
	 * tokens.
	 *
	 * @param tokenizer
	 * @return
	 */
	public Token.Type getNextTokenType(Tokenizer tokenizer) {
		if (!tokenizer.hasNext()) {
			return Token.Type.END;
		}
		//
		Token.Type type = tokenizer.next()._type;
		tokenizer.revert();
		return type;
	}

	private ParseNode toParseTree(Tokenizer tokenizer) throws SyntaxError {
		if (!tokenizer.hasNext()) {
			return null;
		}
		return parseWrapper(tokenizer, true);
	}

	private ParseNode parseWrapper(Tokenizer tokenizer) throws SyntaxError {
		return parseWrapper(tokenizer, false);
	}

	/**
	 * return a wrapper ParseNode with molecules as children or other parse
	 * node in case only one. Syntax Error * @param tokenizer
	 *
	 * @return
	 * @throws SyntaxError if found , OR ) in root wrapper or ( in any case
	 * because ( is expected to be processed by <code>parseMolecule</code>.
	 */
	private ParseNode parseWrapper(Tokenizer tokenizer, boolean isRootWrapper) throws SyntaxError {
		List<ParseNode> children = new ArrayList<ParseNode>();
		//
		while (tokenizer.hasNext()) {
			Token token = tokenizer.next();
			if ((token._type == Token.Type.IDENTIFIER)
			    || (token._type == Token.Type.NUMBER)
			    || (token._type == Token.Type.QUOTED)) {
				// continuable tokens
				tokenizer.revert();
				children.add(parse(tokenizer));
			} else if (token._type == Token.Type.OPEN_PARANTHESES) {
				throw SyntaxError.createError("This token is not expected", token);
			} else if (isRootWrapper) {
				// if not isRootWrapper means that wrapper has ended its job and should return to parent
				throw SyntaxError.createError("This token is not expected", token);
			} else {
				tokenizer.revert();
				break;
			}
		}
		//
		if (children.size() == 1) {
			return children.get(0);
		}
		//
		// TODO :Change when native molecules are working
		return new MoleculeParseNode(ParseNode.Type.WRAPPER, "WRAPPER", children);
	}

	/**
	 * parses the next parseNode.
	 *
	 * @param tokenizer
	 * @return
	 * @throws SyntaxError in case of [(],[,] or [)] because they should be
	 * processed in <code>parseMolecule</code> or unknown token.
	 */
	private ParseNode parse(Tokenizer tokenizer) throws SyntaxError {
		if (!tokenizer.hasNext()) {
			throw new SyntaxError("Expected more tokens");
		}
		//

		switch (getNextTokenType(tokenizer)) {
			case NUMBER:
			case QUOTED:
				return parseValue(tokenizer);
			case IDENTIFIER:
				return parseMolecule(tokenizer);
			/* ERRORS !!! */
			case OPEN_PARANTHESES:
			case COMMA:
			case CLOSE_PARANTHESES:
				throw SyntaxError.createUnexpectedTokenError(tokenizer.next());
			default: // for UNKOWN which is the only remaining type
				throw SyntaxError.createError("unkown token", tokenizer.next());
		}
	}

	private ParseNode parseMolecule(Tokenizer tokenizer) throws SyntaxError {
		if (getNextTokenType(tokenizer) != Token.Type.IDENTIFIER) {
			// the only way this could happen is if parseValue is called on something that is not molecule
			throw new RuntimeException("SOMETHING IS WRONG WITH THE PARSER -- parseMolecule():" + tokenizer.next());
		}
		//+ 
		Token moleculeToken = tokenizer.next();
		//-
		String moleculeName = moleculeToken._string;
		//+ determaine values and children
		Token.Type nextType = getNextTokenType(tokenizer);
		List<Value> values = null;
		List<ParseNode> children = null;
		if (nextType == Token.Type.OPEN_BRACKET) {
			values = parseMoleculeValueInput(tokenizer);
			nextType = getNextTokenType(tokenizer);
		}
		if (nextType == Token.Type.OPEN_PARANTHESES) {
			children = parseMoleculeChildrenInput(tokenizer);
		}
		return new MoleculeParseNode(ParseNode.Type.MOLECULE, moleculeName, values, children);
	}

	/**
	 * parse [...] for molecule. precondition: first token to encounter is
	 * OPEN_BRACKET
	 *
	 * @param tokenizer
	 * @return list of value inputs, null if no input
	 */
	private List<Value> parseMoleculeValueInput(Tokenizer tokenizer) throws SyntaxError {
		//+ skip first [
		tokenizer.next();
		//-
		List<Value> values = new ArrayList<Value>();
		//
		Token.Type nType =  getNextTokenType(tokenizer);
		// check if no input
		if (nType == Token.Type.CLOSE_BRACKET) {
			tokenizer.next(); // skip close bracket
			return null;
		}
		//+ evaluate first value before the loop
		if (nType == Token.Type.NUMBER) {
			values.add(new Doub(tokenizer.next()._string));
		} else if (nType == Token.Type.QUOTED) {
			values.add(new Str(processQuotedText(tokenizer.next()._string)));
		} else {
			throw SyntaxError.createUnexpectedTokenError(tokenizer.next());
		}
		//-
		//+ parse all values until ]
		while ((nType = getNextTokenType(tokenizer)) != Token.Type.CLOSE_BRACKET) {
			//+ skip comman :: not a comma ---> SyntaxError
			if (nType != Token.Type.COMMA) {
				throw SyntaxError.createUnexpectedTokenError(tokenizer.next());
			} else {
				tokenizer.next();
			}
			//-
			nType = getNextTokenType(tokenizer);

			if (nType == Token.Type.NUMBER) {
				values.add(new Doub(tokenizer.next()._string));
			} else if (nType == Token.Type.QUOTED) {
				values.add(new Str(processQuotedText(tokenizer.next()._string)));
			} else {
				throw SyntaxError.createUnexpectedTokenError(tokenizer.next());
			}
		}
		//-
		//+ skip CLOSE_BRACKET, since it's out of while loop
		tokenizer.next();
		//-
		return values;
	}
	/**
	 * parse () for molecule. precondition: first token to encounter is
	 * OPEN_PARANTHESES
	 *
	 * @param tokenizer
	 * @return list of children inputs, null if no input
	 */
	private List<ParseNode> parseMoleculeChildrenInput(Tokenizer tokenizer) throws SyntaxError {
		//+ skip first (
		tokenizer.next();
		//-
		List<ParseNode> children = new ArrayList<ParseNode>();
		//
		Token.Type nextType = getNextTokenType(tokenizer);
		// check if no input
		if (nextType == Token.Type.CLOSE_PARANTHESES) {
			tokenizer.next(); // skip close_prantheses
			return null;
		}
		//+ evaluate first child before the loop
		children.add(parseWrapper(tokenizer));
		//-
		//+ parse all values until )
		while ((nextType = getNextTokenType(tokenizer)) != Token.Type.CLOSE_PARANTHESES) {
			//+ skip comman :: not a comma ---> SyntaxError
			if (nextType != Token.Type.COMMA) {
				throw SyntaxError.createUnexpectedTokenError(tokenizer.next());
			} else { // skip comma
				tokenizer.next();
			}
			//-
			children.add(parseWrapper(tokenizer));
		}
		//-
		//+ skip CLOSE_PARANTHESES, since it's out of while loop
		tokenizer.next();
		//-
		return children;
	}

	private ParseNode parseValue(Tokenizer tokenizer) {
		Token token = tokenizer.next();
		ParseNode.Type type;
		String me;
		if (token._type == Token.Type.NUMBER) {
			type = ParseNode.Type.NUMBER;
			me = token._string;
		} else if (token._type == Token.Type.QUOTED) {
			type = ParseNode.Type.QUOTED;
			me = processQuotedText(token._string);
		} else {
			// the only way this could happen is if parseValue is called on something that is not value
			throw new RuntimeException("SOMETHING IS WRONG WITH THE PARSER -- parseValue(): " + token);
		}
		return new SimpleParseNode(type, me);
	}

	private static String processQuotedText(String str) {
		/*
		 Quoted texts always starts with " because that's the way to 
		 identify them.
		 However, ending " could not exist if the code ends inside quoted
		 text
		 */
		String strippedStr;
		if (str.endsWith("\"")) {
			strippedStr = str.substring(1, str.length() - 1);
		} else {
			strippedStr = str.substring(1);
		}
		//
		/*
		 replace \\ before \" to avoid \\" being treated like \" if \"
		 is treated again.
		 */
		return strippedStr.replace("\\\\", "\\").replace("\\\"", "\"");
	}

	//////////////////////////////////////////////
	//////////////////////////////////////////////
	//////////////////////////////////////////////
	public static abstract class ParseNode {

		public static enum Type {

			EMPTY, WRAPPER, MOLECULE, QUOTED, NUMBER;
		}
		//

		public static final ParseNode EMPTY_NODE = new SimpleParseNode(Type.EMPTY, "Empty");

		//
		public final String _me;
		public final Type _type;

		public ParseNode(Type type, String me) {
			_type = type;
			_me = me;
		}

		public abstract boolean isMolecule();

		@Override
		public String toString() {
			StringBuilder output = new StringBuilder();
			print("", true, output);
			return output.toString();
		}

		protected void print(String prefix, boolean isTail, StringBuilder outputBuilder) {
			outputBuilder.append(prefix).append(isTail ? "└── " : "├── ").append(_me).append("\n");
		}
	}

	public static class SimpleParseNode extends ParseNode {

		SimpleParseNode(ParseNode.Type type, String me) {
			super(type, me);
		}

		@Override
		public boolean isMolecule() {
			return false;
		}
	}

	public static class MoleculeParseNode extends ParseNode {

		public final List<ParseNode> _children;
		public final List<Value> _values;

		public MoleculeParseNode(ParseNode.Type type, String me, List<Value> values, List<ParseNode> children) {
			super(type, me);
			_children = (children == null ? null
				     : Collections.<ParseNode>unmodifiableList(children));
			_values = (values == null ? null
				   : Collections.<Value>unmodifiableList(values));
		}
		
		public MoleculeParseNode(ParseNode.Type type, String me, List<ParseNode> children) {
			this(type,me,null,children);
		}

		@Override
		public String toString() {
			StringBuilder output = new StringBuilder();
			print("", true, output);
			return output.toString();
		}

		protected void print(String prefix, boolean isTail, StringBuilder outputBuilder) {
			outputBuilder.append(prefix).append(isTail ? "└── " : "├── ").append(_me);
			if(_values!=null){
				outputBuilder.append(" (");
				for(int i = 0 ; i<_values.size() ; i++){
					if(i!=0) outputBuilder.append(", ");
					outputBuilder.append(_values.get(i).toString());
				}
				outputBuilder.append(")");
			}
			outputBuilder.append("\n");
			//
			//
			if (_children == null) {
				return;
			}
			for (int i = 0; i < _children.size() - 1; i++) {
				_children.get(i).print(prefix + (isTail ? "    " : "│   "), false, outputBuilder);
			}
			if (_children.size() > 0) {
				_children.get(_children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true, outputBuilder);
			}
		}

		@Override
		public boolean isMolecule() {
			return true;
		}
	}
}
