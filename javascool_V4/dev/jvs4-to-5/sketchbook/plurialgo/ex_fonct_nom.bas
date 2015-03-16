Option Explicit

' definition de la fonction calculer_prixTotal
function calculer(ByVal prixUnitaire as double, ByVal quantite as integer) as double
	Dim prixTotal as double
	prixTotal = quantite * prixUnitaire 
	calculer = prixTotal	' l'equivalent du return javascool
end function

' le programme principal doit se nommer main
sub main()
	Dim quantite as integer
	Dim prixUnitaire as double
	Dim prixTotal as double
	Dim nom as string
	nom = InputBox("Quel est le nom de l'article ?") 
	prixUnitaire = InputBox("Quel est son prix unitaire en euros ?") 
	quantite = InputBox("Quelle est la quantite achetee ?") 
	prixTotal = calculer_prixTotal(prixUnitaire, quantite)	
	MsgBox("prix total : " & prixTotal) 
end sub