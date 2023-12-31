package com.tsoft.jamstrad.gui.browser.components;

import java.util.List;
import java.util.Vector;

import com.tsoft.jamstrad.gui.browser.ProgramBrowserDisplaySource;
import com.tsoft.jamstrad.gui.components.ScrollableItemList;
import com.tsoft.jamstrad.program.AmstradProgram;

public class ProgramMenu extends ScrollableItemList<ProgramMenuItem> {

	private ProgramBrowserDisplaySource browser;

	private AmstradProgram program;

	private List<ProgramMenuItem> menuItems;

	private static int DEFAULT_MAX_ITEMS_SHOWING = 7;

	public ProgramMenu(ProgramBrowserDisplaySource browser, AmstradProgram program) {
		this(browser, program, DEFAULT_MAX_ITEMS_SHOWING);
	}

	public ProgramMenu(ProgramBrowserDisplaySource browser, AmstradProgram program, int maxItemsShowing) {
		super(maxItemsShowing);
		this.browser = browser;
		this.program = program;
		this.menuItems = new Vector<ProgramMenuItem>();
		populateMenu();
	}

	private void populateMenu() {
		boolean primaryDisplayCentric = getBrowser().getMode().isPrimaryDisplayCentric();
		addMenuItem(new ProgramRunMenuItem(this));
		if (primaryDisplayCentric) {
			addMenuItem(new ProgramLoadMenuItem(this));
		}
		addMenuItem(new ProgramInfoMenuItem(this));
		addMenuItem(new ProgramImagesMenuItem(this));
		if (primaryDisplayCentric) {
			addMenuItem(new ProgramFileReferencesMenuItem(this));
		}
		addMenuItem(new ProgramCloseMenuItem(this));
	}

	private void addMenuItem(ProgramMenuItem menuItem) {
		getMenuItems().add(menuItem);
	}

	public void addReturnMenuItem() {
		if (getItemTyped(ProgramReturnMenuItem.class) != null)
			return; // already in the menu
		int insertionIndex = getMenuItems().indexOf(getItemTyped(ProgramRunMenuItem.class));
		if (insertionIndex >= 0) {
			getMenuItems().add(insertionIndex, new ProgramReturnMenuItem(this));
			browseTo(insertionIndex); // make it the selected item
		}
	}

	@Override
	public int size() {
		return getMenuItems().size();
	}

	@Override
	public ProgramMenuItem getItem(int index) {
		return getMenuItems().get(index);
	}

	public <T extends ProgramMenuItem> T getItemTyped(Class<T> type) {
		T item = null;
		int i = 0;
		while (item == null && i < size()) {
			if (type.isAssignableFrom(getItem(i).getClass())) {
				item = type.cast(getItem(i));
			} else {
				i++;
			}
		}
		return item;
	}

	public ProgramBrowserDisplaySource getBrowser() {
		return browser;
	}

	public AmstradProgram getProgram() {
		return program;
	}

	public List<ProgramMenuItem> getMenuItems() {
		return menuItems;
	}

}