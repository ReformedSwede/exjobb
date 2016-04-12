resource MorphoSwe = open Prelude in{
	param
		VForm = VInf | VPres | VPast | VPastPart | VPresPart ;

	oper 
		Verb : Type = {s : VForm => Str} ;

	mkVerb : (_,_,_,_,_ : Str) -> Verb = \gå,går,gick,gått,gående -> {
		s = table {
			VInf => gå ;
			VPres => går ;
			VPast => gick ;
			VPastPart => gått ;
			VPresPart => gående
		}
	} ;

	regVerb : Str -> Verb = \leva ->
		let lev = init leva 
		in
		mkVerb leva (lev + "ar") (lev + "de") (lev + "t") (leva + "nde");

	irregVerb : (_,_,_ : Str) -> Verb = \sjunga,sjöng,sjungit ->
		let v = regVerb sjunga
		in
		mkVerb sjunga (v.s ! VPres) sjöng sjungit (v.s ! VPresPart) ;

	oper mkV = overload {
		mkV : (bygga : Str) -> Verb = regVerb ;
		mkV : (sjunga,sjöng,sjungit : Str) -> Verb = irregVerb ;
		mkV : (gå,går,gick,gått,gående : Str) -> Verb = mkVerb ;
	};
}

