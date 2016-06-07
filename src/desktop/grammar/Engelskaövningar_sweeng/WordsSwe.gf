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
		Past = {vf=(VF (VPret Act)) ;
		s=""} ;
		PPart = {vf=(VI (VSupin Act)) ;
		s=""} ;
		fun0 = mkN "pil" ;
		fun1 = mkN "pil" ;
		fun2 = mkN "pil" ;
		fun3 = mkN "fisk" "fiskar" ;
}
