package org.javascool.gui.editor;

import java.util.ArrayList;

/**
 * Ce Kit définit les fonctions de base du système d'onglet.
 * 
 */
public interface FileKit {

	/**
	 * Ferme le fichier en cours d'édition sans enregistrer.
	 * 
	 * @return true en cas de succès, sinon false.
	 */
	public abstract boolean closeCurrentFile();

	/**
	 * Retourne l'éditeur affiché à l'écran.
	 */
	public abstract EditorKit getCurrentEditor();

	/**
	 * Retourne l'éditeur ouvert pour le fichier donné.
	 * 
	 * @param file
	 *            La référance au fichier
	 * @return L'EditorKit qui y correspond ou null si le fichier n'est pas
	 *         ouvert.
	 */
	public abstract EditorKit getEditorForFile(FileReference file);

	/**
	 * Retourne la liste de tous les fichiers ouverts.
	 * 
	 * @return Les fichiers ouverts
	 */
	public abstract ArrayList<FileReference> getOpenedFiles();

	/**
	 * Dit si tous les fichiers sont enregistrés.
	 * 
	 * @return true, pour oui, false, pour non
	 */
	public abstract boolean isAllFilesSaved();

	/**
	 * Ouvre un fichier.
	 * 
	 * @param file
	 *            La référence à ouvrir
	 * @return true en cas de succès, sinon false.
	 */
	public abstract boolean openFile(FileReference file);

	/**
	 * Demande à l'utilisateur la sauvegarde des fichiers.
	 * 
	 * @return true si tous les fichiers sont sauvgardé, sinon false.
	 */
	public abstract boolean saveAllFiles();

	/**
	 * Enregistre sous le fichier en cours d'édition
	 * 
	 * @return true en cas de succès, sinon false.
	 */
	public abstract boolean saveAsCurrentFile();

	/**
	 * Enregistre le fichier en cours d'édition.
	 * 
	 * @return true en cas de succès, sinon false.
	 */
	public abstract boolean saveCurrentFile();

	/**
	 * Enregistre le fichier à un index donné.
	 * 
	 * @param index
	 *            L'index du fichier
	 * @return true en cas de succès, sinon false.
	 */
	public abstract boolean saveFileAtIndex(int index);

}