package symcode.svg;

import symcode.evaluator.Product;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class StripSvg extends Svg {

	public StripSvg(Product p) {
		this(
			p.getProperty("svg")._value.toString()
			, p.getProperty("x")._value.toDouble()
			, p.getProperty("y")._value.toDouble()
			, p.getProperty("w")._value.toDouble()
			, p.getProperty("h")._value.toDouble()
		);
	}

	public StripSvg(String content, double x, double y, double w, double h) {
		super(Svg.processPosition(content, x, y), w, h);
	}
	public StripSvg(String content, double w, double h) {
		super(content, w, h);
	}

	@Override
	public FullSvg toFullSvg() {
		return new FullSvg("<svg"
		       + " width=\"" + doubleToProperString(super._width) + "\""
		       + " height=\"" + doubleToProperString(super._height) + "\""
		       + ">"
		       + "\n"
		       + indentEveryLine(super._content)
		       + "\n"
		       + "</svg>", super._width, super._height);
	}

	@Override
	public Svg combine(Svg other) {
		if(other.getType() == Svg.Type.FULL){
			throw new UnsupportedOperationException("combining full SVGs is not allowed");
		}
		
		return new StripSvg(this.toString() + "\n" + other.toString()
			, Math.max(this._width, other._width) 
			, Math.max(this._height, other._height)
		);
	}

	@Override
	public StripSvg toStripSvg() {
		return this;
	}

	@Override
	public String toString() {
		return super._content;
	}

	@Override
	public Type getType() {
		return Svg.Type.STRIP;
	}

}
