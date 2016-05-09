abstract Words = {

	flags
		startcat = Word ;

	cat
		Word ;
		Noun ;
		Verb ;
		NounForm ;
		VerbForm ;

	fun
		NFormFun : Noun -> NounForm -> Word ;
		VFormFun : Verb -> VerbForm -> Word ;
		Infinitive : VerbForm ;
		Past : VerbForm ;
		PPart : VerbForm ;
		Sing : NounForm ;
		Plur : NounForm ;
		Light : Noun ;
		Human: Noun ;
		NWUKT : Verb ;
		IDJM : Noun ;
		XJWJM : Noun ;
		DDNOYPT : Verb ;
		OUFFT : Verb ;
}
