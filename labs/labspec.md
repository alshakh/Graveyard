*) To load ref, all it's children all loaded and all its references in the parent trail. 
*) if duplicate reference, only one will be loaded (no specific rule)


const is Expr
svg is SemiExpr
x,y,h,w are Expr

In Svg if you sourrand expression with <<< EXPR >>> it will be evaluated.

svg will be used in atom and ignored with compound


The difference between Compount and a SingleAtom is "atoms":[...] property

properties with _ prefix is customProperty :::: if want to use backup property for a molecule, you MUST provide a backup properties.
	
Current implementation does not allow name duplication in const names in Compounds and Bonded Atom

Now, version property is not used or have any effect
