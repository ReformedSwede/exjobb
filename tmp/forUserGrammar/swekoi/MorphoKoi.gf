resource MorphoKoi = open Prelude in{
	flags coding=utf8;
	param
		VForm = VPres | VImp;
		NForm = NNom | NGen;
		AForm = ASg | APl;
	oper
		Verb : Type = {s : VForm => Str};
		Noun : Type = {s : NForm => Str};
		Adjective : Type = {s : AForm => Str};

	mkNoun : (_,_ : Str) -> Noun = \logos,logou -> {
		s = table {
			NNom => logos;
			NGen => logou
		}
	};

	regNoun : Str -> Noun = \logos ->
		let log = init logos 
		in
		mkNoun logos (log + "ου");

	oper mkN = overload {
		mkN : (logos : Str) -> Noun = regNoun ;
		mkN : (logos,logou : Str) -> Noun = mkNoun 
	};


	mkAdjective : (_,_ : Str) -> Adjective = \megas,megaloi -> {
		s = table {
			ASg => megas;
			APl => megaloi
		}
	};

	regAdjective : Str -> Adjective = \megas ->
		let mega = init megas 
		in
		mkAdjective megas (mega + "λοι");

	oper mkA = overload {
		mkA : (megas : Str) -> Adjective = regAdjective ;
		mkA : (megas,megaloi : Str) -> Adjective = mkAdjective 
	};


	mkVerb : (_,_ : Str) -> Verb = \lego,elegon -> {
		s = table {
			VPres => lego ;
			VImp => elegon 
		}
	} ;

	regVerb : Str -> Verb = \lego ->
		let leg = init lego 
		in
		mkVerb lego ("ε" + leg + "ον");

	oper mkV = overload {
		mkV : (lego : Str) -> Verb = regVerb ;
		mkV : (lego,elegon : Str) -> Verb = mkVerb 
	};
}
