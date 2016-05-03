resource MorphoSwe = open Prelude in{
	param
		VForm = VPres | VImp;
		NForm = NNom | NGen;
		AForm = ASg | APl;
	oper
		V : Type = {s : VForm => Str};
		N : Type = {s : NForm => Str};
		A : Type = {s : AForm => Str};




	mkNoun : (_,_ : Str) -> N = \bil,bils -> {
		s = table {
			NNom => bil;
			NGen => bils
		}
	};

	regNoun : Str -> N = \bil ->
		mkNoun bil (bil + "s");

	smartNoun : Str -> N = \n -> case n of {
		_ + "s" => s_regNoun n ;
		_ => regNoun n
	} ;

	s_regNoun : Str -> N = \ljus ->
		mkNoun ljus ljus;

	mkN = overload {
		mkN : (bil : Str) -> N = smartNoun ;
		mkN : (ljus,ljus : Str) -> N = mkNoun;


		} ;

	mkA : (_ : Str) -> A = \stor -> {
		s = table {
			ASg => stor;
			APl => stor + "a"
		}
	};




	mkVerb : (_,_ : Str) -> V = \går,gick -> {
		s = table {
			VPres => går;
			VImp => gick
		}
	} ;

	regVerb : Str -> V = \öppna ->
		let öppn = init öppna 
		in
		mkVerb (öppn + "ar") (öppn + "ade");

	oper mkV = overload {
		mkV : (leta : Str) -> V = regVerb ;
		mkV : (går,gick : Str) -> V = mkVerb ;
	};
}
