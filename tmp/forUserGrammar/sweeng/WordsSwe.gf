concrete WordsSwe of Words = open CatSwe, ParadigmsSwe in {

	flags startcat = Noun ;

	lincat
		Noun = N ;
		Adjective = A ;
		Verb = V ;

	lin
		Wine = mkN "vin" ;
		Cheese = mkN "ost" ;
		Fish = mkN "fisk" ;
		Fresh = mkA "fräsch" ;
		Warm = mkA "varm" ;
		Expensive = mkA "dyr" ;
		Delicious = mkA "utsökt" ;
		Play = mkV "spela";
		See = mkV "se" "såg" "sett";
		Run = mkV "springa" "sprang" "sprungit";
}
