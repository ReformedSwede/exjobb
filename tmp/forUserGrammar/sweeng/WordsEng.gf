concrete WordsEng of Words =  open MorphoEng in {

	lincat
		Noun, Adjective = {s : Str} ;
		V = Verb;

	lin
		Wine = {s = "wine"} ;
		Cheese = {s = "cheese"} ;
		Fish = {s = "fish"} ;
		Fresh = {s = "fresh"} ;
		Warm = {s = "warm"} ;
		Italian = {s = "Italian"} ;
		Expensive = {s = "expensive"} ;
		Delicious = {s = "delicious"} ;
		Boring = {s = "boring"} ;
		Car = {s = "car"} ;
		Play = mkV "play" ;
		Run = mkV "run" "ran" "run" ;
		Go = mkV "go" "went" "gone" ;
		Eat = mkV "eat" "ate" "eaten" ;
}
