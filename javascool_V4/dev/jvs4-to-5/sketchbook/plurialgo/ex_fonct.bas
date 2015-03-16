Option Explicit

' definition de la fonction calculer_prixTotal
function calculer(ByVal prixUnitaire as double, ByVal quantite as integer) as double
	Dim remise as double
	Dim prixTotal as double
	prixTotal = quantite * prixUnitaire 
	if (quantite = 1) then 
		remise = 0
	elseif (quantite <= 3) then 
		remise = 0.1 
	else
		remise = 0.2
	end if
	prixTotal = prixTotal - remise * prixTotal 
	calculer = prixTotal	' l'equivalent du return javascool
end function

' le programme principal doit se nommer main
sub main()
	Dim quantite as integer
	Dim prixUnitaire as double
	Dim prixTotal as double
	quantite = InputBox("quantite : ") 
	prixUnitaire = InputBox("prix unitaire : ") 
	prixTotal = calculer_prixTotal(prixUnitaire, quantite)	
	MsgBox("prix total : " & prixTotal) 
end sub