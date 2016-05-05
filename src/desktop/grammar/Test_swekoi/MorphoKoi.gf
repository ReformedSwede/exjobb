resource MorphoKoi = open Prelude in{
	flags coding=utf8;
	param
		VForm = VPres | VImp;
		NForm = NNom | NGen;
		AForm = ASg | APl;
	oper
		V : Type = {s : VForm => Str};
		N : Type = {s : NForm => Str};
		A : Type = {s : AForm => Str};

	mkNoun : (_,_ : Str) -> N = \logos,logou -> {
		s = table {
			NNom => logos;
			NGen => logou
		}
	};

	regNoun : Str -> N = \logos ->
		let log = init logos 
		in
		mkNoun logos (log + "ου");

	oper mkN = overload {
		mkN : (logos : Str) -> N = regNoun ;
		mkN : (logos,logou : Str) -> N = mkNoun 
	};


	mkAdjective : (_,_ : Str) -> A = \megas,megaloi -> {
		s = table {
			ASg => megas;
			APl => megaloi
		}
	};

	regAdjective : Str -> A = \megas ->
		let mega = init megas 
		in
		mkAdjective megas (mega + "λοι");

	oper mkA = overload {
		mkA : (megas : Str) -> A = regAdjective ;
		mkA : (megas,megaloi : Str) -> A = mkAdjective 
	};


	mkVerb : (_,_ : Str) -> V = \lego,elegon -> {
		s = table {
			VPres => lego ;
			VImp => elegon 
		}
	} ;

	regVerb : Str -> V = \lego ->
		let leg = init lego 
		in
		mkVerb lego ("ε" + leg + "ον");

	oper mkV = overload {
		mkV : (lego : Str) -> V = regVerb ;
		mkV : (lego,elegon : Str) -> V = mkVerb 
	};
}
