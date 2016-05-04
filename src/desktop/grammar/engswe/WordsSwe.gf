concrete WordsSwe of Words = open CommonScand, CatSwe, ParadigmsSwe in {

	lincat
		Word = Str ;
		Noun = N ;
		NounForm = {nf:Number ;
		s:Str} ;
		Verb = V ;
		VerbForm = {vf:VForm ;
		s:Str} ;

	lin
		NFormFun n f = f.s++n.s ! f.nf ! Indef ! Nom ;
		Sing = {nf=Sg ;
		s=""} ;
		Plur = {nf=Pl ;
		s=""} ;
		VFormFun v f = f.s++v.s ! f.vf ;
		Infinitive = {vf=(VI (VInfin Act)) ;
		s=""} ;
		Past = {vf=(VF (VImper Act)) ;
		s=""} ;
		PPart = {vf=(VI (VSupin Act)) ;
		s=""} ;
		LDOIA = mkN "värld" ;
		Luft = mkN "luft" ;
		Se = mkV "se" "såg" "sett" ;
}
