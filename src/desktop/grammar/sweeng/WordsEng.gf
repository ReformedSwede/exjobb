concrete WordsEng of Words = open ResEng, CatEng, ParadigmsEng in {
  lincat
    Word = Str;
    Noun = N;
    Verb = V;

    VerbForm = {vf:VForm;s:Str};
    NounForm = {nf:Number;s:Str};

  lin
    Infinitive = {vf=VInf;s=""};
    Past = {vf=VPast;s=""};
    PPart = {vf=VPPart;s=""};

    Sing = {nf=Sg;s=""};
    Plur = {nf=Pl;s=""};
    
    VF v f = f.s++v.s ! f.vf;
    NF n f = f.s++n.s ! f.nf ! Nom;

    Fish = mkN "fish" ;
    Eat = mkV "eat" "ate" "eaten";
}
