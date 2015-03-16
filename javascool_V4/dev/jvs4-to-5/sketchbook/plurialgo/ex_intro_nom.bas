Option Explicit
' le programme principal doit se nommer main
sub main()
	Dim quantite as integer
	Dim prixUnitaire as double
	Dim prixTotal as double
	Dim nom as string
	' lectures
	nom = InputBox("Quel est le nom de l'article ?") 
	prixUnitaire = InputBox("Quel est son prix unitaire en euros ?") 
	quantite = InputBox("Quelle est la quantite achetee ?") 
	' calculs
	prixTotal = quantite * prixUnitaire 
	' ecritures 
	MsgBox("prix total : " & prixTotal) 
end sub