resource MorphoSwe = open Prelude in{
	param
		VForm = VPres | VImp;
		NForm = NNom | NGen;
		AForm = ASg | APl;
	oper
		Verb : Type = {s : VForm => Str};
		Noun : Type = {s : NForm => Str};
		Adjective : Type = {s : AForm => Str};




	mkNoun : (_,_ : Str) -> Noun = \bil,bils -> {
		s = table {
			NNom => bil;
			NGen => bils
		}
	};

	regNoun : Str -> Noun = \bil ->
		mkNoun bil (bil + "s");

	smartNoun : Str -> Noun = \n -> case n of {
		_ + "s" => s_regNoun n ;
		_ => regNoun n
	} ;

	s_regNoun : Str -> Noun = \ljus ->
		mkNoun ljus ljus;

	mkN = overload {
		mkN : (bil : Str) -> Noun = smartNoun ;
		mkN : (ljus,ljus : Str) -> Noun = mkNoun;


		} ;

	mkA : (_ : Str) -> Adjective = \stor -> {
		s = table {
			ASg => stor;
			APl => stor + "a"
		}
	};




	mkVerb : (_,_,_ : Str) -> Verb = \gå,går,gick -> {
		s = table {
			VPres => går;
			VImp => gick
		}
	} ;

	regVerb : Str -> Verb = \öppna ->
		let öppn = init öppna 
		in
		mkVerb öppna (öppn + "ar") (öppn + "ade");

	oper mkV = overload {
		mkV : (leta : Str) -> Verb = regVerb ;
		mkV : (gå,går,gick : Str) -> Verb = mkVerb ;
	};
}
