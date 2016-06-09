resource MorphoKoi = open Prelude, Predef in{
	flags coding=utf8;
	param
		VForm = DictForm | VPast | VPart;
		NForm = Pl | Sg;
	oper
		V : Type = {s : VForm => Str};
		N : Type = {s : NForm => Str};

	mkNoun : (_,_ : Str) -> N = \logos,logoi -> {
		s = table {
			Sg => logos;
			Pl => logoi
		}
	};

	regNoun : Str -> N = \logos ->
		let log = init logos 
		in
		mkNoun logos (log + "οι");

	oper mkN = overload {
		mkN : (logos : Str) -> N = regNoun ;
		mkN : (logos,logoi : Str) -> N = mkNoun 
	};


	mkVerb : (_,_,_ : Str) -> V = \pisteuo,episteusa,pepisteuka -> {
		s = table {
			DictForm => pisteuo;
			VPast => episteusa;
			VPart => pepisteuka
		}
	} ;

	regVerb : Str -> V = \pisteuo ->
		let pisteu = init pisteuo 
		in
		mkVerb pisteuo ("ε" + pisteu + "σα") (take 1 pisteu + "ε" + pisteu + "κα");

	oper mkV = overload {
		mkV : (pisteuo : Str) -> V = regVerb ;
		mkV : (pisteuo,episteusa,pepisteuka : Str) -> V = mkVerb 
	};
}
