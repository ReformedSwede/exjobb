abstract Words = {

	flags
		startcat = Word ;

	cat
		Word ;
		Noun ;
		NounForm ;
		Verb ;
		VerbForm ;

	fun
		NFormFun :Noun -> NounForm -> Word ;
		Sing : NounForm ;
		Plur : NounForm ;
		VFormFun :Verb -> VerbForm -> Word ;
		Infinitive : VerbForm ;
		Past : VerbForm ;
		PPart : VerbForm ;
		Dator : Noun ;
		Mus : Noun ;
		BDPW : Verb ;
		Spela : Verb ;
}
