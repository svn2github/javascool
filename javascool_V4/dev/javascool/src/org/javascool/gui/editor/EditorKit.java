package org.javascool.gui.editor;

/**
 * Ce kit définit les fonctions de base d'un Editeur de Code. Les fonctions de
 * EditorKit sont définit de sorte a donner une base à un editeur de code qui
 * serais créé pour Java's cool
 */
public interface EditorKit {

	/**
	 * Donne la référence du fichier en cours d'édition.
	 * 
	 * @return La référence du fichier, ne peut pas être nul.
	 */
	public abstract FileReference getFile();

	/**
	 * Donne le nom à afficher pour l'éditeur en cours. Ce nom est utilisé par
	 * exemple dans le titre de l'onglet
	 * 
	 * @return Le nom de l'éditeur
	 */
	public abstract String getName();

	/**
	 * Retourne le texte de l'éditeur
	 * 
	 * @return Le code de l'éditeur
	 */
	public abstract String getText();

	/**
	 * Dit si l'éditeur doit être sauvegarder. Cette fonction est utilisé par
	 * Java's Cool lors de sa fermeture afin d'être sûr que les fichiers ont été
	 * sauvegardé
	 * 
	 * @return true si il doit être sauvegarder, false dans le cas contraire
	 */
	public abstract Boolean hasToSave();

	/**
	 * Supprime toute les lignes mises en évidence dans l'éditeur.
	 * 
	 * @see signalLine(int line)
	 */
	public abstract void removeLineSignals();

	/**
	 * Sauvegarde le contenu en cours d'édition dans le fichier. Si ce fichier
	 * est temporaire, la fonction demande à l'utilisateur le sauvegarder
	 * 
	 * @return true si le fichier est sauvegardé, sinon, false
	 */
	public abstract Boolean save();

	/**
	 * Sauvegarde une copie du fichier Agit comme save() et en considérent le
	 * fichier comme temporaire même si il ne l'est pas.
	 * 
	 * @return true si le fichier est sauvegardé, sinon, false
	 */
	public abstract boolean saveAs();

	/**
	 * Demande la sauvegarde du fichier avant la fermeture Cette fonction doit
	 * demander à l'utilisateur si il veut enregistrer
	 * 
	 * @return true si l'utilisateur a refusé l'enregistrement ou que le fichier
	 *         a été sauvegardé, sinon, false
	 */
	public abstract boolean saveBeforeClose();

	/**
	 * Modifie le fichier en cours d'édition. Assigne une nouvelle référance à
	 * l'éditeur, le contenu de ce dernier doit alors changer
	 * 
	 * @param file
	 *            Le fichier à assigner
	 */
	public abstract void setFile(FileReference file);

	/**
	 * Assigne un texte à l'éditeur
	 * 
	 * @param text
	 *            Le text à assigner
	 */
	public abstract void setText(String text);

	/**
	 * Met en évidence une ligne de l'éditeur
	 * 
	 * @param line
	 *            La ligne à mettre en évidence
	 */
	public abstract void signalLine(int line);

}