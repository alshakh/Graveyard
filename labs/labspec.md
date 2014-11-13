

*) To load ref, all it's children all loaded and all its references in the parent trail. 
*) if duplicate reference, only one will be loaded (no specific rule)


const is Doub
svg is SvgExpr
x,y,h,w are Expr

In Svg if you sourrand expression with <<< EXPR >>> it will be evaluated.

svg will be used in atom and ignored with compound


The difference between Compount and a SingleAtom is "atoms":[...] property

properties with _ prefix is customProperty :::: should always check if declared before using if (typeof v === 'undefined') v = 2; for two reasons :
	1) could be not declared => error
	2) choose a default value if not declared
	
Current implementation does not allow name duplication in const names in Compounds and Bonded Atom
