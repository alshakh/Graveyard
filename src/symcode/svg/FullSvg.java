package symcode.svg;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class FullSvg extends Svg{
	public FullSvg(String content){
		this(content, 0, 0);
	}
	public FullSvg(String content, double w, double h){
		super(content, w, h);
	}
	@Override
	public Svg combine(Svg other) {
		throw new UnsupportedOperationException("combining Full SVGs is not allowed");
	}

	@Override
	public FullSvg toFullSvg() {
		return this;
	}

	@Override
	public StripSvg toStripSvg() {
		throw new UnsupportedOperationException(" Cannot convert Full to StripSvg"); 
	}

	@Override
	public String toString() {
		return super._content;
	}

	@Override
	public Type getType() {
		return Svg.Type.FULL;
	}
	
}
