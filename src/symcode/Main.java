package symcode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import symcode.evaluator.EvalNode;
import symcode.evaluator.EvaluationError;
import symcode.evaluator.Evaluator;
import symcode.evaluator.Parser;
import symcode.evaluator.SyntaxError;
import symcode.lab.Lab;
import symcode.lab.LabLoader;
import symcode.lab.Molecule;
import symcode.lab.SingleAtom;
import symcode.lab.nativemolecule.WrapperCompound;
import symcode.value.Svg;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Main {

	/**
	 * @param args the command line arguments
	 * @throws symcode.evaluator.EvaluationError
	 */
	public static void main(String[] args) throws EvaluationError, SyntaxError {
		String code = "circle node(circle, circle, node(circle, circle, node(circle, circle, circle)))";
		code = "node(circle,node(circle,circle,circle),circle)";
		Lab lab = LabLoader.loadLab(new File("labs/demo.json"));
		Evaluator evaluator = new Evaluator(lab);
		System.out.println(new Svg(evaluator.eval(code)).toFullString());
		//
	}
	public static final Svg EXAMPLE_SVG =new Svg("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
		"<svg id=\"svg2\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns=\"http://www.w3.org/2000/svg\" height=\"88.208\" width=\"190.97\" version=\"1.1\" xmlns:cc=\"http://creativecommons.org/ns#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
		" <path transform=\"scale(10,10)\" id=\"path2996-8\" fill=\"#000\" d=\"M18.968,0.0005c-0.169-0.01-0.515,0.1263-1.097,0.3748-2.351,1.0021-5.94,1.5451-7.626,1.1541-0.8558-0.1985-1.0436-0.3239-1.3103-0.8736-0.1286-0.2653-0.2707-0.4816-0.3154-0.48-0.0447,0-0.2912,0.4042-0.5474,0.8925l-0.4665,0.8871,0.356,0.666c0.4201,0.7886,0.587,1.528,0.4314,1.9037-0.2293,0.5537-1.2056,1.2254-2.3917,1.6448-0.6247,0.221-0.9216,0.2735-1.7634,0.3074-1.301,0.053-1.8896-0.1306-2.6451-0.8143-0.94052-0.8511-0.9928-1.4601-0.2751-3.2168,0.4557-1.1153,0.517-1.386,0.3209-1.386-0.1875,0-0.3722,0.2643-0.73611,1.0408-0.89915,1.9188-1.1608,4.0195-0.62826,5.0504,0.1797,0.348,0.75007,0.9353,1.1487,1.1811,0.1253,0.077,0.4553,0.2171,0.7334,0.3128,1.6162,0.5559,4.3806-0.265,5.6165-1.6691,0.462-0.5248,1.1663-2.0596,1.3563-2.9553,0.0909-0.4283,0.2038-0.4745,0.8791-0.3721,1.0291,0.1561,1.1724,0.1597,2.1868,0.076,1.4729-0.1222,3.3722-0.6221,4.7025-1.2404,0.35738-0.1661,0.6391-0.25,0.68757-0.2049,0.051,0.047,0.0102,0.2235-0.10785,0.4665-0.24428,0.5028-0.24541,0.6639-0.003,0.6283,0.23182-0.034,0.26661-0.097,1.3994-2.5967,0.23774-0.5247,0.31184-0.7669,0.0944-0.7766z\"/>\n" +
		"</svg>") ;
}