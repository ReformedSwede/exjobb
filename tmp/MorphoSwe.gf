resource MorphoSwe = {
	oper mkVerb : (_,_,_,_,_ : Str) -> Verb = \go,goes,went,gone,going -> {
		s = table {
			VInf => go ;
			VPres => goes ;
			VPast => went ;
			VPastPart => gone ;
			VPresPart => going
		}
	} ;

}
