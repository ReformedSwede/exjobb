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
		Water = mkN "vatten" "vatten" ;
		Play = mkV "spela" ;
		Run = mkV "springa" "sprang" "sprungit" ;
		Fish = mkN "fisk" "fiskar" ;
		Lake = mkN "sjö" ;
		Read = mkV "läsa" "läste" "läst" ;
		Say = mkV "säga" "sade" "sagt" ;
		Computer = mkN "dator" "datorer" ;
		Cello = mkN "cello" "celli" ;
		Mouse = mkN "mus" "möss" ;
		Eat = mkV "äta" "åt" "ätit" ;
		Brush = mkV "borsta" ;
		Car = mkN "bil" ;
		Man = mkN "man" "män" ;
		Write = mkV "skriva" "skrev" "skrivit" ;
}
