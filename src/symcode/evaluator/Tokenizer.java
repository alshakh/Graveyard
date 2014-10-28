/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import java.util.Stack;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Tokenizer {

	public final String _code;
	private final Stack<Integer> _prevPositions;
	private int _pos = 0; // next _pos

	public Tokenizer(String code) {
		_code = code;
		_prevPositions = new Stack<Integer>();
	}

	public boolean hasNext() {
		return (skipWhitespace(_pos) != _code.length());
	}

	public Token next() {
		_prevPositions.push(_pos);
		//
		int startPos = skipWhitespace(_pos);
		ReturnTuple rt = nextHelper(startPos);
		int endPos = rt._endPos;
		Token.Type type = rt._type;
		String token = _code.substring(startPos, endPos);
		_pos = endPos;
		//
		String codePortion = _code.substring(0, startPos);
		return new Token(token, type, rowFromPos(codePortion), colsFromPos(codePortion));
	}

	/**
	 * back the tokenizer by one token. revert then next will return to the
	 * same state.
	 */
	public void revert() {
		if (_prevPositions.empty()) {
			_pos = 0;
			return;
		}
		_pos = _prevPositions.pop();
	}

	/**
	 * No effect.
	 *
	 * @return
	 */
	public Token previousToken() {
		revert();
		revert();
		Token t = next();
		next();
		return t;
	}

	/**
	 * No effect.
	 *
	 * @return
	 */
	public Token currentToken() {
		revert();
		return next();
	}

	private static int rowFromPos(String codePortion) {
		int rows = 0;
		int lastPos = codePortion.indexOf("\n");

		while (lastPos != -1) {
			rows++;
			lastPos = codePortion.indexOf("\n", lastPos + 1);
		}
		return rows;
	}

	private static int colsFromPos(String codePortion) {
		int lastLine = codePortion.lastIndexOf("\n");
		if (lastLine == -1) {
			return codePortion.length();
		} else {
			return codePortion.length() - lastLine - 1;
		}
	}

	private int skipWhitespace(int pos) {
		if (pos == _code.length()) {
			return pos;
		}
		if (Character.isWhitespace(_code.charAt(pos))) {
			return skipWhitespace(pos + 1);
		} else {
			return pos;
		}
	}

	// everything else will be read one char at a time!

	private ReturnTuple nextHelper(int pos) {
		char c = _code.charAt(pos);
		if (Character.isAlphabetic(c)) {
			return nextHelper(pos + 1, Token.Type.IDENTIFIER);
		} else if (Character.isDigit(c)) {
			return nextHelper(pos + 1, Token.Type.NUMBER);
		} else if (c == '"') {
			return nextHelper(pos + 1, Token.Type.QUOTED);
		} else if (c == '(') {
			return nextHelper(pos + 1, Token.Type.OPEN_PARA);
		} else if (c == ')') {
			return nextHelper(pos + 1, Token.Type.CLOSE_PARA);
		} else if (c == ',') {
			return nextHelper(pos + 1, Token.Type.COMMA);
		} else {
			return new ReturnTuple(Token.Type.UNKOWN, pos + 1); // if nothing matches, advance one character
		}
	}

	/**
	 * Get last position of token. First called on char of index 1 i.e
	 * second char in token.
	 *
	 * @param pos
	 * @param type
	 * @return the last position appropriate for cutting the token with
	 * subsrting
	 */
	private ReturnTuple nextHelper(int pos, Token.Type type) {
		if (pos >= _code.length()) {
			return new ReturnTuple(type, _code.length());
		}
		//
		char c = _code.charAt(pos);
		switch (type) {
			case NUMBER:
				if (Character.isDigit(c) || c == '.') {
					return nextHelper(pos + 1, type);
				} else {
					return new ReturnTuple(type, pos);
				}
			case IDENTIFIER:

				if (Character.isDigit(c) || Character.isAlphabetic(c)) {
					return nextHelper(pos + 1, type);
				} else {
					return new ReturnTuple(type, pos);
				}
			case QUOTED: {
				if (c == '\\') {
					return nextHelper(pos + 2, type); // skip the char after '\'
				} else if (c == '"') {
					return new ReturnTuple(type, pos + 1); // quotation mark counts from this Token
				} else {
					return nextHelper(pos + 1, type);
				}
			}
			case OPEN_PARA:
			case CLOSE_PARA:
			case COMMA:
				return new ReturnTuple(type, pos); // openPara,closePara,comma are only one char long
			default:
				return new ReturnTuple(type, pos);
		}
	}

	//

	public class ReturnTuple {

		public final Token.Type _type;
		public final int _endPos;

		public ReturnTuple(Token.Type type, int endPos) {
			_type = type;
			_endPos = endPos;
		}
	}

	public String toString() {
		return _code;
	}

	public static class Token {

		public static enum Type {

			NUMBER, IDENTIFIER, QUOTED, OPEN_PARA, CLOSE_PARA, COMMA, UNKOWN;
		}

		public final String _string;
		public final Token.Type _type;
		public final int _posRow;
		public final int _posCol;

		public Token(String token, Token.Type type, int row, int col) {
			_string = token;
			_type = type;
			_posRow = row;
			_posCol = col;
		}

		@Override
		public String toString() {
			return _type + " (at:" + _posRow + ", " + _posCol + "): " + _string;
		}
	}
}
