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
		fun0 : Noun ;
		fun1 : Noun ;
		fun2 : Noun ;
		fun3 : Noun ;
		fun4 : Verb ;
}
