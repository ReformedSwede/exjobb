resource MorphoKoi = open Prelude in{
	flags coding = utf8 ;

	param
		VForm = VPres | VImpf | VAori ;

	oper 
		Verb : Type = {s : VForm => Str} ;

	mkVerb : (_,_,_ : Str) -> Verb = \λεγω,ελεγεν,ειπεω -> {
		s = table {
			VPres => λεγω ;
			VImpf => ελεγεν ;
			VAori => ειπεω 
		}
	} ;

	regVerb : Str -> Verb = \πιστευω ->
		let πιστευ = init πιστευω 
		in
		mkVerb πιστευω ("ε" + πιστευ + "εν") ("ε" + πιστευ + "σαν");

	oper mkV = overload {
		mkV : (πιστευω : Str) -> Verb = regVerb ;
		mkV : (λεγω,ελεγεν,ειπεω : Str) -> Verb = mkVerb ;
	};
}

