concrete WordsSwe of Words = open CommonScand, CatSwe, ParadigmsSwe in {

	lincat
		Word = Str ;
		Noun = N ;
		Verb = V ;
		VerbForm = {vf:VForm ;
		s:Str} ;
		NounForm = {nf:Number ;
		s:Str} ;

	lin
		Infinitive = {vf=VI (VInfin Act) ;
		s=""} ;
		Past = {vf=VF (VPret Act) ;
		s=""} ;
		PPart = {vf=VI (VSupin Act) ;
		s=""} ;
		Sing = {nf=Sg ;
		s=""} ;
		Plur = {nf=Pl ;
		s=""} ;
		VFormFun v f = f.s++v.s ! f.vf ;
		NFormFun n f = f.s++n.s ! f.nf ! Indef ! Nom ;
		Fish = mkN "fisk" ;
		Eat = mkV "äta" "åt" "ätit" ;
		Water = mkN "vatten" "vatten" ;
		Computer = mkN "dator" "datorer" ;
		See = mkV "se" "såg" "sett" ;
		Play = mkV "spela" ;
		Cube = mkN "kub" "kuber" ;
		Run = mkV "springa" "sprang" "sprungit" ;
}
