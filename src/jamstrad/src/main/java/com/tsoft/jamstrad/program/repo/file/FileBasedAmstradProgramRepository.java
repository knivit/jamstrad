package com.tsoft.jamstrad.program.repo.file;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.tsoft.jamstrad.AmstradFileType;
import com.tsoft.jamstrad.gui.FileBasedImageProxy;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.repo.AmstradProgramRepository;
import com.tsoft.jamstrad.program.repo.cover.CoverImage;
import com.tsoft.jamstrad.program.repo.cover.CoverImageImpl;
import com.tsoft.jamstrad.util.IOUtils;

public abstract class FileBasedAmstradProgramRepository extends AmstradProgramRepository {

	private FileBasedFolderNode rootNode;

	private boolean folderPerProgram;

	protected FileBasedAmstradProgramRepository(File rootFolder) {
		this(rootFolder, false);
		setFolderPerProgram(couldBeFolderPerProgram(rootFolder));
	}

	protected FileBasedAmstradProgramRepository(File rootFolder, boolean folderPerProgram) {
		if (!rootFolder.isDirectory())
			throw new IllegalArgumentException("The root folder must be a directory");
		setRootNode(new FileBasedFolderNode(rootFolder));
		setFolderPerProgram(folderPerProgram);
	}

	private boolean couldBeFolderPerProgram(File parentFolder) {
		boolean result = true;
		if (parentFolder.isDirectory()) {
			if (containsSubFolders(parentFolder) && selectProgramFileInFolder(parentFolder) != null) {
				result = false;
			} else {
				int programCount = 0;
				File[] files = parentFolder.listFiles();
				int i = 0;
				while (i < files.length && result) {
					File file = files[i++];
					if (file.isDirectory()) {
						result = couldBeFolderPerProgram(file);
					} else {
						if (isProgramFile(file) && !isRemasteredProgramFile(file)) {
							programCount++;
							if (programCount > 1)
								result = false;
						}
					}
				}
			}
		}
		return result;
	}

	private boolean containsSubFolders(File folder) {
		boolean result = false;
		File[] files = folder.listFiles();
		int i = 0;
		while (!result && i < files.length)
			result = files[i++].isDirectory();
		return result;
	}

	private File selectProgramFileInFolder(File folder) {
		File result = null;
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (isProgramFile(file)) {
				if (result == null) {
					result = file;
				} else if (isRemasteredProgramFile(file) && !isRemasteredProgramFile(result)) {
					result = file;
				}
			}
		}
		return result;
	}

	private File selectMetaDataFileInFolder(File folder) {
		return selectMetaDataFileInFolder(folder, null);
	}

	private File selectMetaDataFileInFolder(File folder, File companionProgramFileInFolder) {
		File result = null;
		File[] files = folder.listFiles();
		int i = 0;
		while (i < files.length && result == null) {
			File file = files[i++];
			if (isMetaDataFile(file)) {
				if (companionProgramFileInFolder == null
						|| equalFilenamesButExtension(file, companionProgramFileInFolder)) {
					result = file;
				}
			}
		}
		return result;
	}

	private File selectCoverImageFileInFolder(File folder) {
		File result = null;
		File[] files = folder.listFiles();
		int i = 0;
		while (i < files.length && result == null) {
			File file = files[i++];
			if (isCoverImageFile(file)) {
				result = file;
			}
		}
		return result;
	}

	private boolean equalFilenamesButExtension(File one, File other) {
		return IOUtils.stripExtension(one).equals(IOUtils.stripExtension(other));
	}

	protected abstract boolean isProgramFile(File file);

	protected abstract boolean isRemasteredProgramFile(File file);

	protected boolean isMetaDataFile(File file) {
		return AmstradFileType.AMSTRAD_METADATA_FILE.matches(file);
	}

	protected boolean isCoverImageFile(File file) {
		String name = file.getName().toLowerCase();
		return name.equals("cover.jpg") || name.equals("cover.png");
	}

	protected abstract AmstradProgram createProgram(String programName, File basicFile, File metadataFile);

	@Override
	public FolderNode getRootNode() {
		return rootNode;
	}

	private void setRootNode(FileBasedFolderNode rootNode) {
		this.rootNode = rootNode;
	}

	public boolean isFolderPerProgram() {
		return folderPerProgram;
	}

	private void setFolderPerProgram(boolean folderPerProgram) {
		this.folderPerProgram = folderPerProgram;
	}

	private class FileBasedFolderNode extends FolderNode {

		private File folder;

		public FileBasedFolderNode(File folder) {
			super(folder.getName());
			this.folder = folder;
		}

		@Override
		protected CoverImage readCoverImage() {
			CoverImage cover = null;
			File coverFile = selectCoverImageFileInFolder(getFolder());
			if (coverFile != null) {
				cover = new CoverImageImpl(this, new FileBasedImageProxy(coverFile));
			}
			return cover;
		}

		@Override
		protected List<Node> listChildNodes() {
			List<FileBasedFolderNode> childFolderNodes = new Vector<FileBasedFolderNode>();
			List<FileBasedProgramNode> childProgramNodes = new Vector<FileBasedProgramNode>();
			if (isFolderPerProgram() && !containsSubFolders(getFolder())) {
				File pf = selectProgramFileInFolder(getFolder());
				if (pf != null) {
					childProgramNodes.add(new FileBasedProgramNode(getFolder().getName(), pf));
				}
			} else {
				List<File> files = Arrays.asList(getFolder().listFiles());
				Collections.sort(files);
				for (File file : files) {
					if (isFolderPerProgram()) {
						if (file.isDirectory()) {
							if (!containsSubFolders(file)) {
								File pf = selectProgramFileInFolder(file);
								if (pf != null) {
									childProgramNodes.add(new FileBasedProgramNode(file.getName(), pf));
								}
							} else {
								childFolderNodes.add(new FileBasedFolderNode(file));
							}
						}
					} else {
						if (file.isDirectory()) {
							childFolderNodes.add(new FileBasedFolderNode(file));
						} else if (isProgramFile(file)) {
							childProgramNodes.add(new FileBasedProgramNode(file.getName(), file));
						}
					}
				}
			}
			// Sort: folders first
			List<Node> childNodes = new Vector<Node>(childFolderNodes.size() + childProgramNodes.size());
			childNodes.addAll(childFolderNodes);
			childNodes.addAll(childProgramNodes);
			return childNodes;
		}

		public File getFolder() {
			return folder;
		}

	}

	private class FileBasedProgramNode extends ProgramNode {

		private File file;

		public FileBasedProgramNode(String name, File file) {
			super(name);
			this.file = file;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(128);
			sb.append(super.toString());
			sb.append(" (").append(getFile().getPath()).append(')');
			return sb.toString();
		}

		@Override
		protected AmstradProgram readProgram() {
			return createProgram(getName(), getFile(), getCompanionMetaDataFile());
		}

		public File getCompanionMetaDataFile() {
			if (isFolderPerProgram()) {
				return selectMetaDataFileInFolder(getFile().getParentFile());
			} else {
				return selectMetaDataFileInFolder(getFile().getParentFile(), getFile());
			}
		}

		public File getFile() {
			return file;
		}

	}

}