concrete WordsEng of Words =  open CatEng, ParadigmsEng in {
	flags startcat = Noun ;
	lincat
		Noun = N ;
		Adjective = A ;
		Verb = V ;

	lin
		Wine = mkN "wine" ;
		Cheese = mkN "cheese" ;
		Fish = mkN "fish" ;
		Fresh = mkA "fresh" ;
		Warm = mkA "warm" ;
		Expensive = mkA "expensive" ;
		Delicious = mkA "delicious" ;
		Play = mkV "play";
		See = mkV "see" "saw" "seen";
		Run = mkV "run" "ran" "run";
}
