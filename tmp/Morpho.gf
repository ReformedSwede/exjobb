resource Morpho = {
	param
		VForm = VInf | VPres | VPast | VPastPart | VPresPart ;

	oper 
		Verb : Type = {s : VForm => Str} ;
}

