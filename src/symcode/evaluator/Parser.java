/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import symcode.evaluator.Tokenizer.Token;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Parser {

	public final ParseNode _parseTree;

	public Parser(String code) throws SyntaxError {
		_parseTree = toParseTree(new Tokenizer(code));
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
	 * @throws SyntaxError if found [,] OR [)] in root wrapper or [(] in any
	 * case because [(] is expected to be processed by
	 * <code>parseMolecule</code>.
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
			} else if (token._type == Token.Type.OPEN_PARA) {
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
		return new ParseNode(ParseNode.Type.WRAPPER, "WRAPPER", children);
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
		Token token = tokenizer.next();
		switch (token._type) {
			case NUMBER:
			case QUOTED:
				tokenizer.revert();
				return parseValue(tokenizer);
			case IDENTIFIER:
				tokenizer.revert();
				return parseMolecule(tokenizer);
			case OPEN_PARA:
			case COMMA:
			case CLOSE_PARA:
				throw SyntaxError.createError("This token is not expected", token);
			default: // for UNKOWN which is the only remaining type
				throw SyntaxError.createError("unkown token", token);
		}
	}

	private ParseNode parseMolecule(Tokenizer tokenizer) throws SyntaxError {
		Token token = tokenizer.next();
		if (token._type != Token.Type.IDENTIFIER) {
			// the only way this could happen is if parseValue is called on something that is not molecule
			throw new RuntimeException("SOMETHING IS WRONG WITH THE PARSER -- parseMolecule():" + token);
		}
		//
		String moleculeName = token._string;
		//
		boolean hasChildren = false;
		// Check for children
		if(tokenizer.hasNext()){
			Token nextToken = tokenizer.next();
			tokenizer.revert();
			// if no children, return the molecule
			if (nextToken._type == Token.Type.OPEN_PARA) {
				// bypass OPEN_PARA
				tokenizer.next();
				hasChildren = true;
			}
		}
		//
		if(!hasChildren){
			return new ParseNode(ParseNode.Type.MOLECULE, moleculeName);
		}
		// If children, Process children
		List<ParseNode> children = new ArrayList<ParseNode>();
		//
		if (!tokenizer.hasNext()) {
			throw SyntaxError.createError("cannot end code with", token);
		}
		//
		while (tokenizer.hasNext()) {
			token = tokenizer.next();
			if (token._type == Token.Type.COMMA) {
				if (tokenizer.previousToken()._type == Token.Type.COMMA || // this case ,, => empty child
					tokenizer.previousToken()._type == Token.Type.OPEN_PARA) { // this case (, =>  empty child
					children.add(ParseNode.EMPTY_NODE);
				}
				continue;
			} else if (token._type == Token.Type.CLOSE_PARA) {
				if (tokenizer.previousToken()._type == Token.Type.COMMA) { // this case ,) => empty child
					children.add(ParseNode.EMPTY_NODE);
				}
				break;
			}
			tokenizer.revert();
			children.add(parseWrapper(tokenizer));
		}
		//
		return new ParseNode(ParseNode.Type.MOLECULE, moleculeName, children);
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
		return new ParseNode(type, me);
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

	public static class ParseNode {

		public static final ParseNode EMPTY_NODE = new ParseNode(Type.EMPTY, "Empty");

		//
		public static enum Type {

			EMPTY,WRAPPER, MOLECULE, QUOTED, NUMBER;
		}
		//
		public final List<ParseNode> _children;
		public final String _me;
		public final Type _type;

		ParseNode(Type type, String me) {
			_type = type;
			_me = me;
			_children = null;
		}

		ParseNode(Type type, String me, List<ParseNode> children) {

			_children = (children == null ? null : Collections.<ParseNode>unmodifiableList(children));
			_me = me;
			_type = type;
		}

		boolean isLeaf() {
			return (_children == null);
		}

		@Override
		public String toString() {
			StringBuilder output = new StringBuilder();
			print("", true, output);
			return output.toString();
		}

		private void print(String prefix, boolean isTail, StringBuilder outputBuilder) {
			outputBuilder.append(prefix).append(isTail ? "└── " : "├── ").append(_me).append("\n");
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
	}
}
