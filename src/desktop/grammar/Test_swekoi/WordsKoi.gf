concrete WordsKoi of Words = open MorphoKoi in {

	flags
		coding=utf8 ;

	lincat
		Word = Str ;
		Noun = N ;
		Verb = V ;
		VerbForm = {vf: VForm ;
		s : Str} ;
		NounForm = {nf : NForm ;
		s : Str} ;

	lin
		Infinitive = {vf= DictForm ;
		s=""} ;
		Past = {vf= VPast ;
		s=""} ;
		PPart = {vf= VPart ;
		s=""} ;
		Sing = {nf= Sg ;
		s=""} ;
		Plur = {nf= Pl ;
		s=""} ;
		VFormFun v f = f.s++v.s ! f.vf ;
		NFormFun n f = f.s++n.s ! f.nf ;
		Light = mkN "φως" "φωτα" ;
		Human = mkN "ανθροπως" "ανθροποι" ;
		NWUKT = mkV "αγαπω" "ηγαπησα" "ηγαπηκα" ;
		IDJM = mkN "υιος" "υιοι" ;
		XJWJM = mkN "λογος" "λογοι" ;
		DDNOYPT = mkV "πιστευω" "επιστευσα" "πεπιστευκα" ;
		OUFFT = mkV "βαλλω" "εβαλα" "βέβληκα" ;
}
