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

	mkN : (_,_ : Str) -> Noun = \logos,logou -> {
		s = table {
			NNom => logos;
			NGen => logou
		}
	};

	mkA : (_,_ : Str) -> Adjective = \megas,megaloi -> {
		s = table {
			ASg => megas;
			APl => megaloi
		}
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
