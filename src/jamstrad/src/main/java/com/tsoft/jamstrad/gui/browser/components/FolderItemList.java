package com.tsoft.jamstrad.gui.browser.components;

import java.util.List;

import com.tsoft.jamstrad.gui.components.ScrollableItemList;
import com.tsoft.jamstrad.program.repo.AmstradProgramRepository.FolderNode;
import com.tsoft.jamstrad.program.repo.AmstradProgramRepository.Node;

public class FolderItemList extends ScrollableItemList<Node> {

	private FolderNode folderNode;

	public FolderItemList(FolderNode folderNode, int maxItemsShowing) {
		super(maxItemsShowing);
		this.folderNode = folderNode;
	}

	@Override
	public int size() {
		return getItems().size();
	}

	@Override
	public Node getItem(int index) {
		return getItems().get(index);
	}

	public List<Node> getItems() {
		return getFolderNode().getChildNodes();
	}

	private FolderNode getFolderNode() {
		return folderNode;
	}

}