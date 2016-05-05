abstract Words = {

	flags
		startcat=Word ;

	cat
		Word ;
		Noun ;
		Verb ;
		VerbForm ;
		NounForm ;

	fun
		VFormFun : Verb -> VerbForm -> Word ;
		NFormFun : Noun -> NounForm -> Word ;
		Infinitive : VerbForm ;
		Past : VerbForm ;
		PPart: VerbForm ;
		Sing : NounForm ;
		Plur : NounForm ;
		Water : Noun ;
		Play : Verb ;
		Run : Verb ;
		Fish : Noun ;
		Lake : Noun ;
		Read : Verb ;
		Say : Verb ;
		Computer : Noun ;
		Cello : Noun ;
}
