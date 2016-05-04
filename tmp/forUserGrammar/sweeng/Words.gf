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
		Fish : Noun ;
		Eat : Verb ;
		Infinitive : VerbForm ;
		Past : VerbForm ;
		PPart: VerbForm ;
		Sing : NounForm ;
		Plur : NounForm ;
		Water : Noun ;
		Computer : Noun ;
		See : Verb ;
		Play : Verb ;
		Cube : Noun ;
		Run : Verb ;
}
