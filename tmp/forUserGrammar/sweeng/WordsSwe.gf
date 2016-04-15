concrete WordsSwe of Words = open MorphoSwe in{

	lincat
		Noun, Adjective = {s : Str} ;
		V = Verb ;

	lin
		Wine = {s = "vin"} ;
		Cheese = {s = "ost"} ;
		Fish = {s = "fisk"} ;
		Fresh = {s = "fräsch"} ;
		Warm = {s = "varm"} ;
		Italian = {s = "italiensk"} ;
		Expensive = {s = "dyr"} ;
		Delicious = {s = "utsökt"} ;
		Boring = {s = "tråkig"} ;
		Car = {s = "bil"} ;
		Play = mkV "leka" ;
		Run = mkV "springa" "sprang" "sprungit" ;
		Go = mkV "gå" "gick" "gått" ;
		Eat = mkV "äta" "åt" "ätit" ;
}
