concrete WordsEng of Words = open ResEng, CatEng, ParadigmsEng in {

	lincat
		Word = Str ;
		Noun = N ;
		Verb = V ;
		VerbForm = {vf:VForm ;
		s:Str} ;
		NounForm = {nf:Number ;
		s:Str} ;

	lin
		Infinitive = {vf=VInf ;
		s=""} ;
		Past = {vf=VPast ;
		s=""} ;
		PPart = {vf=VPPart ;
		s=""} ;
		Sing = {nf=Sg ;
		s=""} ;
		Plur = {nf=Pl ;
		s=""} ;
		VFormFun v f = f.s++v.s ! f.vf ;
		NFormFun n f = f.s++n.s ! f.nf ! Nom ;
		Water = mkN "water" "waters" ;
		Play = mkV "play" ;
		Run = mkV "run" "ran" "run" ;
		Fish = mkN "fish" "fish" ;
		Lake = mkN "lake" ;
		Read = mkV "read" "read" "read" ;
		Say = mkV "say" "said" "said" ;
		Computer = mkN "computer" "computers" ;
		Cello = mkN "cello" "cellos" ;
		Mouse = mkN "mouse" "mice" ;
}
