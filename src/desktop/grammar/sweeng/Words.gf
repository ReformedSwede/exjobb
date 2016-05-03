abstract Words = {
  flags startcat=Word;
  cat
    Word; Noun; Verb;
    VerbForm; NounForm;
  fun
    VF : Verb -> VerbForm -> Word;
    NF : Noun -> NounForm -> Word;
      
    Fish : Noun ;
    Eat : Verb ;

    Infinitive : VerbForm;
    Past : VerbForm;
    PPart: VerbForm;

    Sing : NounForm;
    Plur : NounForm;    
}
