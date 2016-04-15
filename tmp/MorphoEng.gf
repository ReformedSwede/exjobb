resource MorphoEng = open Prelude in{
	param
		VForm = VInf | VPres | VPast | VPastPart | VPresPart ;

	oper 
		Verb : Type = {s : VForm => Str} ;

	mkVerb : (_,_,_,_,_ : Str) -> Verb = \go,goes,went,gone,going -> {
		s = table {
			VInf => go ;
			VPres => goes ;
			VPast => went ;
			VPastPart => gone ;
			VPresPart => going
		}
	} ;

	regVerb : Str -> Verb = \walk ->
		mkVerb walk (walk + "s") (walk + "ed") (walk + "ed") (walk + "ing") ;

	s_regVerb : Str -> Verb = \kiss ->
		mkVerb kiss (kiss + "es") (kiss + "ed") (kiss + "ed") (kiss + "ing") ;

	e_regVerb : Str -> Verb = \use ->
		let us = init use
		in mkVerb use (use + "s") (us + "ed") (us + "ed") (us + "ing") ;

	y_regVerb : Str -> Verb = \cry ->
		let cr = init cry
		in
		mkVerb cry (cr + "ies") (cr + "ied") (cr + "ied") (cry + "ing") ;

	ie_regVerb : Str -> Verb = \die ->
		let dy = Predef.tk 2 die + "y"
		in
		mkVerb die (die + "s") (die + "d") (die + "d") (dy + "ing") ;

	smartVerb : Str -> Verb = \v -> case v of {
		_ + ("s"|"z"|"x"|"ch") => s_regVerb v ;
		_ + "ie" => ie_regVerb v ;
		_ + "ee" => ee_regVerb v ;
		_ + "e" => e_regVerb v ;
		_ + ("a"|"e"|"o"|"u") + "y" => regVerb v ;
		_ + "y" => y_regVerb v ;
		_ + ("a"|"e"|"i"|"o"|"u") + ("b"|"d"|"g"|"m"|"n"|"p"|"r"|"s"|"t") => dupRegVerb v ;
		_ + ("ea"|"ee"|"ie"|"oa"|"oo"|"ou") + ? => regVerb v ;
		_ => regVerb v
	} ;

	dupRegVerb : Str -> Verb = \stop ->
		let stopp = stop + last stop
		in
		mkVerb stop (stop + "s") (stopp + "ed") (stopp + "ed") (stopp + "ing") ;

	irregVerb : (_,_,_ : Str) -> Verb = \sing,sang,sung ->
		let v = smartVerb sing
		in
		mkVerb sing (v.s ! VPres) sang sung (v.s ! VPresPart) ;

	mkV = overload {
		mkV : (cry : Str) -> Verb = smartVerb ;
		mkV : (sing,sang,sung : Str) -> Verb = irregVerb ;
		mkV : (go,goes,went,gone,going : Str) -> Verb = mkVerb ;
		} ;
}

