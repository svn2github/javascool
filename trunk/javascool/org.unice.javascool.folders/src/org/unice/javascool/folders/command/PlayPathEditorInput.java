package org.unice.javascool.folders.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.filesystem.provider.FileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class PlayPathEditorInput extends FileStore {

	private File file;

	public PlayPathEditorInput(Path loc){
		file=new File(loc.toOSString());
	}

	@Override
	public String[] childNames(int options, IProgressMonitor monitor)
	throws CoreException {
		if (file.isDirectory()) {
			String[] collection = file.list();
			return collection;
		} else {
			return new String[0];
		}

	}

	@Override
	public IFileInfo fetchInfo(int options, IProgressMonitor monitor)
	throws CoreException {
		FileInfo info = new FileInfo(file.getName());
		if (file.isDirectory()) {
			info.setDirectory(true);
		} else {
			info.setDirectory(false);
			info.setLastModified(file.lastModified());
		}
		info.setExists(true);
		info.setAttribute(EFS.ATTRIBUTE_READ_ONLY, true);
		return info;

	}

	@Override
	public IFileStore getChild(String name) {
		if (file.isDirectory()) {
			String[] childs=null;
			try {
				childs = childNames(0,null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if(childs!=null)
				for(int i=0;i<childs.length;i++){
					if(childs[i].equals(name)) return new PlayPathEditorInput(new Path(file.getAbsolutePath()+File.separator+name));
				}
			return null;
		} else {
			return null;
		}
	}

	@Override
	public IFileStore getParent() {
		if(file.getParent()==null) return null;
		else return new PlayPathEditorInput(new Path(file.getParent()));
	}

	@Override
	public OutputStream openOutputStream(int options, IProgressMonitor monitor){
		try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			MessageBox mb = new MessageBox(new Shell());
			mb.setText("Alert");
			mb.setMessage("Impossible d'ouvrir le fichier\n");
			mb.open();
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public InputStream openInputStream(int options, IProgressMonitor monitor)
	throws CoreException {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			MessageBox mb = new MessageBox(new Shell());
			mb.setText("Alert");
			mb.setMessage("Impossible d'ouvrir le fichier\n");
			mb.open();
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public URI toURI() {
		return file.toURI();
	}

	@Override
	public String getName() {
		return file.getName();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
