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
		Fish = mkN "fish" ;
		Eat = mkV "eat" "ate" "eaten" ;
		Water = mkN "water" "waters" ;
		Computer = mkN "computer" "computers" ;
		See = mkV "see" "saw" "seen" ;
		Play = mkV "play" ;
		Cube = mkN "cube" "cubes" ;
		Run = mkV "run" "ran" "run" ;
}
