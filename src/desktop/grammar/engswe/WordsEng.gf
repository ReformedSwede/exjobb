concrete WordsEng of Words = open ResEng, CatEng, ParadigmsEng in {

	lincat
		Word = Str ;
		Noun = N ;
		NounForm = {nf:Number ;
		s:Str} ;
		Verb = V ;
		VerbForm = {vf:VForm ;
		s:Str} ;

	lin
		NFormFun n f = f.s++n.s ! f.nf ! Nom ;
		Sing = {nf=Sg ;
		s=""} ;
		Plur = {nf=Pl ;
		s=""} ;
		VFormFun v f = f.s++v.s ! f.vf ;
		Infinitive = {vf=VInf ;
		s=""} ;
		Past = {vf=VPast ;
		s=""} ;
		PPart = {vf=VPPart ;
		s=""} ;
		Dator = mkN "computer" "computers" ;
		Mus = mkN "mouse" "mice" ;
		BDPW = mkV "read" "read" "read" ;
		Spela = mkV "play" ;
}
