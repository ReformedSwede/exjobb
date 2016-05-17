concrete WordsSwe of Words = open CommonScand, CatSwe, ParadigmsSwe in {

	lincat
		Word = Str ;
		Noun = N ;
		Verb = V ;
		VerbForm = {vf : VForm ;
		s : Str} ;
		NounForm = {nf : Number ;
		s : Str} ;

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
		Light = mkN "ljus" "ljus" ;
		Human = mkN "människa" ;
		NWUKT = mkV "älska" ;
		IDJM = mkN "son" "söner" ;
		XJWJM = mkN "ord" "ord" ;
		DDNOYPT = mkV "tro" "trodde" "trott" ;
		OUFFT = mkV "kasta" "kastade" "kastat" ;
		WULXDU = mkN "hjärta" "hjärtan" ;
		NEJPT = mkV "höra" "hörde" "hört" ;
		XPT = mkV "skilja" "skiljde" "skiljt" ;
		PLUQB = mkN "skrift" "skrifter" ;
}
