abstract Words = {

	flags
		startcat = Phrase ;

	cat
		Phrase ;
		Item ;
		Kind ;
		Quality ;

	fun
		Is : Item -> Quality -> Phrase ;
		This, That : Kind -> Item ;
		QKind : Quality -> Kind -> Kind ;
		Wine, Cheese, Fish : Kind ;
		Very : Quality -> Quality ;
		Fresh, Warm, Italian, Expensive, Delicious, Boring : Quality ;
		Soup : Kind ;
		Test : Kind ;
		Igen : Kind ;
		Nytt : Kind ;
		Hello : Noun ;
		Hello : Noun ;
		Hello : Noun ;
		Hello : Noun ;
		Hello : Noun ;
}
