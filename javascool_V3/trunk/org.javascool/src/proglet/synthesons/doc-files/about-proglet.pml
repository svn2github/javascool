stdout: {p title={"La «proglet» Synthe"} {proglet mode={demo} name={Synthe} {Cette} {proglet} {footnote id={"1"}} {permet} {de} {"générer"} {une} {"séquence"} {de} {notes} {et} {"d'en"} {regarder} {le} {spectre} {de} {"fréquence."} {p {Pour} {"définir"} {son} {propre} {son} {il} {faut} {utiliser} {la} {construction} {javascool} {","} {par} {"exemple:"} {code {p {r {"@tone:"}} {"sqr(t)"} {"+"} {"noi(t)"}}} {"où"} {r {"@tone:"}} {est} {une} {"« macro-instruction »,"} {"suivie,"} {sur} {la} {"même"} {ligne} {"d'une"} {expression} {de} {la} {variable} {c {t}} {qui} {"définit"} {la} {forme} {du} {"son,"} {tandis} {que} {les} {fonctions} {suivantes} {sont} {"prédéfinies:"} {code {m {Sinusoide}} {p {r {double}} {r {sns}} {"(t)"}} {m {Signal} {"carré"}} {p {r {double}} {r {sqr}} {"(t)"}} {m {Signal} {triangulaire}} {p {r {double}} {r {tri}} {"(t)"}} {m {Souffle} {"aléatoire"}} {p {r {double}} {r {noi}} {"(t)"}}}} {p {Pour} {"écouter"} {le} {son} {ou} {la} {"séquence"} {de} {notes} {il} {faut} {utiliser} {la} {"fonction:"} {code {p {r {synthePlay}} {"();"}}}} {p {La} {visualisation} {du} {son} {"s'effectue"} {en} {"montrant:"} {ul {li {Les} {amplitudes} {des} {"fréquences"} {du} {son} {"(tracé"} {en} {"rouge,"} {"l'unité"} {est} {de} {a href={"http://fr.wikipedia.org/wiki/Décibel"} {"décibel"}} {","} {"renormalisé"} {pour} {offrir} {le} {meilleur} {"tracé)."} {br} {Les} {"fréquences"} {sont} {"tracées"} {entre} {le} {i {La}} {"sous-grave"} {"(octave"} {"0"} {"à"} {c {"27.5"} {a href={"http://fr.wikipedia.org/wiki/Hertz"} {Hz}}} {","} {"à"} {peine} {"audible)"} {et} {le} {i {La}} {"sur-aigu"} {"(octave"} {"9"} {"à"} {c {"6400"} {a href={"http://fr.wikipedia.org/wiki/Hertz"} {Hz}}} {"),"} {tandis} {que} {le} {milieu} {de} {"l'échelle"} {est} {le} {i {La}} {du} {diapason} {"(octave"} {"3"} {"à"} {c {"440"} {a href={"http://fr.wikipedia.org/wiki/Hertz"} {Hz}}} {")."}} {li {Le} {"début"} {du} {signal} {"(tracé"} {en} {"jaune),"} {permet} {de} {voir} {sa} {forme} {et} {son} {aspect} {"temporel."} {Les} {"premières"} {c {"11"}} {ms} {"("} {c {"0.011"}} {"secondes)"} {sont} {"tracées."}}}} {p {Pour} {"définir"} {une} {"séquence"} {de} {notes} {il} {faut} {utiliser} {la} {"fonction:"} {code {p {r {syntheSet}} {"("} {s {"séquence-de-note"}} {");"}}} {"où"} {la} {syntaxe} {de} {s {"séquence-de-note"}} {est} {"définie"} {dans} {la} {a href={" ../api/proglet/SoundBit.html#notes"} {documentation}} {de} {la} {"proglet."}} {p {Un} {exemple} {"d'utilisation:"} {en} {appuyant} {sur} {la} {touche} {"["} {img src={"img/demo.png"}} {b {Demo}} {"],"} {les} {"différents"} {"sons:"} {code {p {r {"@tone:"}} {"cos(t)"}} {p {r {synthePlay}} {"();"}}} {puis} {code {p {r {synthePlay}} {"();"}} {p {r {"@tone:"}} {"0.5"} {"*"} {"sqr(t)"}}} {puis} {code {p {r {synthePlay}} {"();"}} {p {r {"@tone:"}} {"0.8"} {"*"} {"tri(t)"} {"+"} {"0.2"} {"*"} {"noi(t)"}}} {puis} {code {p {r {synthePlay}} {"();"}} {p {r {"@tone:"}} {"0.3"} {"*"} {"sqr(t/2)"} {"*"} {"sin(t)"} {"+"} {"0.3"} {"*"} {"sin(2"} {"*"} {"t)"} {"+"} {"0.3"} {"*"} {"tri(3"} {"*"} {"t)"}}} {sont} {"présentés,"} {puis} {les} {"1ères"} {notes} {de} {quelque} {chose} {qui} {ressemble} {"à"} {la} {"«Lettre"} {"à"} {"Elise»"} {de} {"L.v.Beethoven"} {est} {"jouée"} {et} {son} {spectre} {"affiché"} {et} {ensuite} {br} {"."} {"."} {"à"} {vous} {de} {jouer} {"!"}}} {footnotes {p {Cette} {proglet} {est} {un} {a href={"http://fr.wikipedia.org/wiki/Open_source"} {code} {source} {libre}} {","} {"programmée"} {en} {Java} {et} {a href={" ../api/proglet/Synthe.html"} {"documentée"}} {pour} {sa} {libre} {"redistribution,"} {"l'accès"} {a href={" ../api/proglet/Synthe.java"} {au} {code} {source}} {","} {et} {permettre} {des} {"développements"} {"dérivés."}}}}
