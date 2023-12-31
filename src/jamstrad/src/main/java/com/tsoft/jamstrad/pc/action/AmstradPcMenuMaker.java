package com.tsoft.jamstrad.pc.action;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.tsoft.jamstrad.gui.UIResources;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitor;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitorAdapter;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitorMode;
import com.tsoft.jamstrad.pc.monitor.display.AmstradSystemColors;

public class AmstradPcMenuMaker {

	private AmstradPcActions actions;

	private LookAndFeel lookAndFeel;

	private Font emulatorMenuItemFont;

	private static final int EMULATOR_LAF_COLOR_BACKGROUND = 0;

	private static final int EMULATOR_LAF_COLOR_FOREGROUND = 26;

	private static final int EMULATOR_LAF_COLOR_BORDER = 3;

	private static final int EMULATOR_LAF_COLOR_SELECTION_BG = 3;

	private static final int EMULATOR_LAF_COLOR_SELECTION_FG = 25;

	public AmstradPcMenuMaker(AmstradPcActions actions, LookAndFeel lookAndFeel) {
		this.actions = actions;
		this.lookAndFeel = lookAndFeel;
		initLookAndFeel();
	}

	private void initLookAndFeel() {
		if (isEmulatorLookAndFeel()) {
			UIManager.put("Menu.selectionBackground", getSystemColors().getColor(EMULATOR_LAF_COLOR_SELECTION_BG));
			UIManager.put("Menu.selectionForeground", getSystemColors().getColor(EMULATOR_LAF_COLOR_SELECTION_FG));
			UIManager.put("Menu.arrowIcon", UIResources.menuArrowIcon);
			UIManager.put("MenuItem.selectionBackground", getSystemColors().getColor(EMULATOR_LAF_COLOR_SELECTION_BG));
			UIManager.put("MenuItem.selectionForeground", getSystemColors().getColor(EMULATOR_LAF_COLOR_SELECTION_FG));
			UIManager.put("CheckBoxMenuItem.selectionBackground",
					getSystemColors().getColor(EMULATOR_LAF_COLOR_SELECTION_BG));
			UIManager.put("CheckBoxMenuItem.selectionForeground",
					getSystemColors().getColor(EMULATOR_LAF_COLOR_SELECTION_FG));
			UIManager.put("CheckBoxMenuItem.checkIcon", UIResources.checkBoxMenuItemIcon);
			UIManager.put("RadioButtonMenuItem.selectionBackground",
					getSystemColors().getColor(EMULATOR_LAF_COLOR_SELECTION_BG));
			UIManager.put("RadioButtonMenuItem.selectionForeground",
					getSystemColors().getColor(EMULATOR_LAF_COLOR_SELECTION_FG));
			UIManager.put("RadioButtonMenuItem.checkIcon", UIResources.radioButtonMenuItemIcon);
		}
	}

	public JMenuBar createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		menubar.add(createFileMenu());
		menubar.add(createEmulatorMenu());
		menubar.add(createMonitorMenu());
		menubar.add(createWindowMenu());
		return updateMenuBarLookAndFeel(menubar);
	}

	public JPopupMenu createFullPopupMenu() {
		JPopupMenu popup = new JPopupMenu("Amstrad Menu");
		popup.add(createFileMenu());
		popup.add(createEmulatorMenu());
		popup.add(createMonitorMenu());
		popup.add(createWindowMenu());
		return updatePopupMenuLookAndFeel(popup);
	}

	public JPopupMenu createStandardPopupMenu() {
		JPopupMenu popup = new JPopupMenu("Amstrad Menu");
		popup.add(createProgramBrowserMenuItem());
		popup.add(createProgramBrowserSetupMenuItem());
		popup.add(createProgramInfoMenuItem());
		popup.add(createAudioMenuItem());
		popup.add(createPauseResumeMenuItem());
		popup.add(new JSeparator());
		popup.add(createScreenshotMenuItem());
		popup.add(createScreenshotWithMonitorEffectMenuItem());
		popup.add(createMonitorModeMenu());
		popup.add(createMonitorEffectsMenu());
		popup.add(createMonitorFullscreenMenuItem());
		popup.add(new JSeparator());
		popup.add(createQuitMenuItem());
		return updatePopupMenuLookAndFeel(popup);
	}

	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(createProgramBrowserMenuItem());
		menu.add(createProgramBrowserSetupMenuItem());
		menu.add(createProgramInfoMenuItem());
		menu.add(new JSeparator());
		menu.add(createLoadBasicSourceFileMenuItem());
		menu.add(createLoadBasicBinaryFileMenuItem());
		menu.add(createLoadSnapshotFileMenuItem());
		menu.add(new JSeparator());
		menu.add(createSaveBasicSourceFileMenuItem());
		menu.add(createSaveBasicBinaryFileMenuItem());
		menu.add(createSaveSnapshotFileMenuItem());
		menu.add(new JSeparator());
		menu.add(createQuitMenuItem());
		return updateMenuLookAndFeel(menu);
	}

	private JMenuItem createProgramBrowserMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getProgramBrowserAction());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
		return updateMenuItemLookAndFeel(item, UIResources.basicOrBrowserIcon);
	}

	private JMenuItem createProgramBrowserSetupMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getProgramBrowserSetupAction());
		item.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		return updateMenuItemLookAndFeel(item, UIResources.browserSetupIcon);
	}

	private JMenuItem createProgramInfoMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getProgramInfoAction());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		return updateMenuItemLookAndFeel(item, UIResources.infoIcon);
	}

	private JMenuItem createLoadBasicSourceFileMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getLoadBasicSourceFileAction()));
	}

	private JMenuItem createLoadBasicBinaryFileMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getLoadBasicBinaryFileAction()));
	}

	private JMenuItem createLoadSnapshotFileMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getLoadSnapshotFileAction()));
	}

	private JMenuItem createSaveBasicSourceFileMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getSaveBasicSourceFileAction()));
	}

	private JMenuItem createSaveBasicBinaryFileMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getSaveBasicBinaryFileAction()));
	}

	private JMenuItem createSaveSnapshotFileMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getSaveSnapshotFileAction()));
	}

	private JMenuItem createQuitMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getQuitAction());
		item.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		return updateMenuItemLookAndFeel(item, UIResources.quitIcon);
	}

	private JMenu createEmulatorMenu() {
		JMenu menu = new JMenu("Emulator");
		menu.add(createAmstradSystemColorsDisplayMenuItem());
		menu.add(createBasicMemoryDisplayMenuItem());
		menu.add(createShowJavaConsoleMenuItem());
		menu.add(new JSeparator());
		menu.add(createAutoTypeFileMenuItem());
		menu.add(createBreakEscapeMenuItem());
		menu.add(new JSeparator());
		menu.add(createAudioMenuItem());
		menu.add(new JSeparator());
		menu.add(createPauseResumeMenuItem());
		menu.add(createRebootMenuItem());
		return updateMenuLookAndFeel(menu);
	}

	private JMenuItem createAmstradSystemColorsDisplayMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getAmstradSystemColorsDisplayAction()));
	}

	private JMenuItem createBasicMemoryDisplayMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getBasicMemoryDisplayAction()));
	}

	private JMenuItem createShowJavaConsoleMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getShowJavaConsoleAction()));
	}

	private JMenuItem createAutoTypeFileMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getAutoTypeFileAction()));
	}

	private JMenuItem createBreakEscapeMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getBreakEscapeAction()));
	}

	private JMenuItem createAudioMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getAudioAction());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		return updateMenuItemLookAndFeel(item, UIResources.audioIcon);
	}

	private JMenuItem createPauseResumeMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getPauseResumeAction());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAUSE, 0));
		return updateMenuItemLookAndFeel(item, UIResources.pauseResumeIcon);
	}

	private JMenuItem createRebootMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getRebootAction());
		item.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		return updateMenuItemLookAndFeel(item);
	}

	private JMenu createMonitorMenu() {
		JMenu menu = new JMenu("Monitor");
		menu.add(createMonitorModeMenu());
		menu.add(createMonitorEffectsMenu());
		menu.add(new JSeparator());
		menu.add(createMonitorSizeMenu());
		menu.add(createMonitorFullscreenMenuItem());
		menu.add(new JSeparator());
		menu.add(createScreenshotMenuItem());
		menu.add(createScreenshotWithMonitorEffectMenuItem());
		return updateMenuLookAndFeel(menu);
	}

	private JMenuItem createScreenshotMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getScreenshotAction());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
		return updateMenuItemLookAndFeel(item, UIResources.screenshotIcon);
	}

	private JMenuItem createScreenshotWithMonitorEffectMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getScreenshotWithMonitorEffectAction());
		item.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		return updateMenuItemLookAndFeel(item, UIResources.screenshotWithMonitorIcon);
	}

	private JMenu createMonitorModeMenu() {
		JMenu menu = new JMenu("Monitor type");
		JRadioButtonMenuItem colorMode = createMonitorModeColorMenuItem();
		JRadioButtonMenuItem greenMode = createMonitorModeGreenMenuItem();
		JRadioButtonMenuItem grayMode = createMonitorModeGrayMenuItem();
		menu.add(colorMode);
		menu.add(greenMode);
		menu.add(grayMode);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(colorMode);
		buttonGroup.add(greenMode);
		buttonGroup.add(grayMode);
		MonitorModeMenuHelper helper = new MonitorModeMenuHelper(buttonGroup, getMonitor());
		return updateMenuLookAndFeel(menu, UIResources.monitorModeIcon);
	}

	private JRadioButtonMenuItem createMonitorModeColorMenuItem() {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(getActions().getMonitorModeColorAction());
		return (JRadioButtonMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JRadioButtonMenuItem createMonitorModeGreenMenuItem() {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(getActions().getMonitorModeGreenAction());
		return (JRadioButtonMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JRadioButtonMenuItem createMonitorModeGrayMenuItem() {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(getActions().getMonitorModeGrayAction());
		return (JRadioButtonMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JMenu createMonitorEffectsMenu() {
		JMenu menu = new JMenu("Monitor effects");
		menu.add(new MonitorEffectMenuHelper(createMonitorEffectMenuItem(), getMonitor()).getCheckbox());
		menu.add(new MonitorScanLinesEffectMenuHelper(createMonitorScanLinesEffectMenuItem(), getMonitor())
				.getCheckbox());
		menu.add(
				new MonitorBilinearEffectMenuHelper(createMonitorBilinearEffectMenuItem(), getMonitor()).getCheckbox());
		menu.add(new MonitorGateArrayMenuHelper(createMonitorGateArrayMenuItem(), getMonitor()).getCheckbox());
		menu.add(new JSeparator());
		menu.add(new MonitorShowSystemStatsMenuHelper(createMonitorShowSystemStatsMenuItem(), getMonitor())
				.getCheckbox());
		return updateMenuLookAndFeel(menu, UIResources.monitorEffectIcon);
	}

	private JCheckBoxMenuItem createMonitorEffectMenuItem() {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(getActions().getMonitorEffectAction());
		return (JCheckBoxMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JCheckBoxMenuItem createMonitorScanLinesEffectMenuItem() {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(getActions().getMonitorScanLinesEffectAction());
		return (JCheckBoxMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JCheckBoxMenuItem createMonitorBilinearEffectMenuItem() {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(getActions().getMonitorBilinearEffectAction());
		return (JCheckBoxMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JCheckBoxMenuItem createMonitorGateArrayMenuItem() {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(getActions().getMonitorGateArrayAction());
		return (JCheckBoxMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JCheckBoxMenuItem createMonitorShowSystemStatsMenuItem() {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(getActions().getMonitorShowSystemStatsAction());
		return (JCheckBoxMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JMenu createMonitorSizeMenu() {
		JMenu menu = new JMenu("Monitor size");
		JRadioButtonMenuItem singleSize = createMonitorSingleSizeMenuItem();
		JRadioButtonMenuItem doubleSize = createMonitorDoubleSizeMenuItem();
		JRadioButtonMenuItem tripleSize = createMonitorTripleSizeMenuItem();
		menu.add(singleSize);
		menu.add(doubleSize);
		menu.add(tripleSize);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(singleSize);
		buttonGroup.add(doubleSize);
		buttonGroup.add(tripleSize);
		MonitorSizeMenuHelper helper = new MonitorSizeMenuHelper(buttonGroup, getMonitor());
		return updateMenuLookAndFeel(menu, UIResources.windowedIcon);
	}

	private JRadioButtonMenuItem createMonitorSingleSizeMenuItem() {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(getActions().getMonitorSingleSizeAction());
		return (JRadioButtonMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JRadioButtonMenuItem createMonitorDoubleSizeMenuItem() {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(getActions().getMonitorDoubleSizeAction());
		return (JRadioButtonMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JRadioButtonMenuItem createMonitorTripleSizeMenuItem() {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(getActions().getMonitorTripleSizeAction());
		return (JRadioButtonMenuItem) updateMenuItemLookAndFeel(item);
	}

	private JMenuItem createMonitorFullscreenMenuItem() {
		JMenuItem item = new JMenuItem(getActions().getMonitorFullscreenAction());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
		return updateMenuItemLookAndFeel(item, UIResources.windowIcon);
	}

	private JMenu createWindowMenu() {
		JMenu menu = new JMenu("Window");
		menu.add(new WindowAlwaysOnTopMenuHelper(createWindowAlwaysOnTopMenuItem(), getMonitor()).getCheckbox());
		menu.add(createWindowCenterOnScreenMenuItem());
		menu.add(new JSeparator());
		menu.add(createAboutMenuItem());
		return updateMenuLookAndFeel(menu);
	}

	private JCheckBoxMenuItem createWindowAlwaysOnTopMenuItem() {
		return (JCheckBoxMenuItem) updateMenuItemLookAndFeel(
				new JCheckBoxMenuItem(getActions().getWindowAlwaysOnTopAction()));
	}

	private JMenuItem createWindowCenterOnScreenMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getWindowCenterOnScreenAction()));
	}

	private JMenuItem createAboutMenuItem() {
		return updateMenuItemLookAndFeel(new JMenuItem(getActions().getAboutAction()));
	}

	private JMenuBar updateMenuBarLookAndFeel(JMenuBar menubar) {
		return menubar; // nothing special
	}

	private JPopupMenu updatePopupMenuLookAndFeel(JPopupMenu menu) {
		if (isEmulatorLookAndFeel()) {
			menu.setBackground(getSystemColors().getColor(EMULATOR_LAF_COLOR_BACKGROUND));
			menu.setForeground(getSystemColors().getColor(EMULATOR_LAF_COLOR_FOREGROUND));
			menu.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(getSystemColors().getColor(EMULATOR_LAF_COLOR_BACKGROUND), 1),
					BorderFactory.createLineBorder(getSystemColors().getColor(EMULATOR_LAF_COLOR_BORDER), 4)));
			menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		return menu;
	}

	private JMenu updateMenuLookAndFeel(JMenu menu) {
		return updateMenuLookAndFeel(menu, null);
	}

	private JMenu updateMenuLookAndFeel(JMenu menu, Icon icon) {
		updateMenuItemLookAndFeel(menu, icon);
		return menu;
	}

	private JMenuItem updateMenuItemLookAndFeel(JMenuItem item) {
		return updateMenuItemLookAndFeel(item, null);
	}

	private JMenuItem updateMenuItemLookAndFeel(JMenuItem item, Icon icon) {
		if (isEmulatorLookAndFeel()) {
			item.setBackground(getSystemColors().getColor(EMULATOR_LAF_COLOR_BACKGROUND));
			item.setForeground(getSystemColors().getColor(EMULATOR_LAF_COLOR_FOREGROUND));
			item.setFont(getFontForMenuItem(item));
			item.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
			item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			if (icon != null) {
				item.setIcon(icon);
			}
		}
		return item;
	}

	private Font getFontForMenuItem(JMenuItem item) {
		if (isEmulatorLookAndFeel()) {
			if (emulatorMenuItemFont == null) {
				emulatorMenuItemFont = getMonitor().getGraphicsContext().getSystemFont().deriveFont(16f);
			}
			return emulatorMenuItemFont;
		} else {
			return item.getFont();
		}
	}

	private boolean isEmulatorLookAndFeel() {
		return LookAndFeel.EMULATOR.equals(getLookAndFeel());
	}

	private AmstradSystemColors getSystemColors() {
		return AmstradSystemColors.getSystemColors(AmstradMonitorMode.COLOR);
	}

	private AmstradMonitor getMonitor() {
		return getActions().getAmstradPc().getMonitor();
	}

	public AmstradPcActions getActions() {
		return actions;
	}

	public LookAndFeel getLookAndFeel() {
		return lookAndFeel;
	}

	public static enum LookAndFeel {

		EMULATOR,

		JAVA;

	}

	private static abstract class MonitorMenuHelper extends AmstradMonitorAdapter {

		protected MonitorMenuHelper() {
		}

		protected abstract void syncMenu(AmstradMonitor monitor);

	}

	private static class MonitorModeMenuHelper extends MonitorMenuHelper {

		private ButtonGroup buttonGroup;

		public MonitorModeMenuHelper(ButtonGroup buttonGroup, AmstradMonitor monitor) {
			this.buttonGroup = buttonGroup;
			syncMenu(monitor);
			monitor.addMonitorListener(this);
		}

		@Override
		public void amstradMonitorModeChanged(AmstradMonitor monitor) {
			syncMenu(monitor);
		}

		@Override
		protected void syncMenu(AmstradMonitor monitor) {
			AmstradMonitorMode monitorMode = monitor.getMode();
			for (Enumeration<AbstractButton> en = getButtonGroup().getElements(); en.hasMoreElements();) {
				AbstractButton button = en.nextElement();
				if (((MonitorModeAction) button.getAction()).getMode().equals(monitorMode)) {
					button.setSelected(true);
				}
			}
		}

		private ButtonGroup getButtonGroup() {
			return buttonGroup;
		}

	}

	private static class MonitorSizeMenuHelper extends MonitorMenuHelper {

		private ButtonGroup buttonGroup;

		public MonitorSizeMenuHelper(ButtonGroup buttonGroup, AmstradMonitor monitor) {
			this.buttonGroup = buttonGroup;
			syncMenu(monitor);
			monitor.addMonitorListener(this);
		}

		@Override
		public void amstradMonitorSizeChanged(AmstradMonitor monitor) {
			syncMenu(monitor);
		}

		@Override
		protected void syncMenu(AmstradMonitor monitor) {
			for (Enumeration<AbstractButton> en = getButtonGroup().getElements(); en.hasMoreElements();) {
				AbstractButton button = en.nextElement();
				int sizeFactor = ((MonitorSizeAction) button.getAction()).getSizeFactor();
				if (sizeFactor == 1 && monitor.isSingleSize()) {
					button.setSelected(true);
				} else if (sizeFactor == 2 && monitor.isDoubleSize()) {
					button.setSelected(true);
				} else if (sizeFactor == 3 && monitor.isTripleSize()) {
					button.setSelected(true);
				}
			}
		}

		private ButtonGroup getButtonGroup() {
			return buttonGroup;
		}

	}

	private static abstract class MonitorCheckboxMenuHelper extends MonitorMenuHelper {

		private JCheckBoxMenuItem checkbox;

		protected MonitorCheckboxMenuHelper(JCheckBoxMenuItem checkbox, AmstradMonitor monitor) {
			this.checkbox = checkbox;
			syncMenu(monitor);
			monitor.addMonitorListener(this);
		}

		@Override
		protected final void syncMenu(AmstradMonitor monitor) {
			getCheckbox().setSelected(getState(monitor));
		}

		protected abstract boolean getState(AmstradMonitor monitor);

		public JCheckBoxMenuItem getCheckbox() {
			return checkbox;
		}

	}

	private static class MonitorEffectMenuHelper extends MonitorCheckboxMenuHelper {

		public MonitorEffectMenuHelper(JCheckBoxMenuItem checkbox, AmstradMonitor monitor) {
			super(checkbox, monitor);
		}

		@Override
		public void amstradMonitorEffectChanged(AmstradMonitor monitor) {
			syncMenu(monitor);
		}

		@Override
		protected boolean getState(AmstradMonitor monitor) {
			return monitor.isMonitorEffectOn();
		}

	}

	private static class MonitorScanLinesEffectMenuHelper extends MonitorCheckboxMenuHelper {

		public MonitorScanLinesEffectMenuHelper(JCheckBoxMenuItem checkbox, AmstradMonitor monitor) {
			super(checkbox, monitor);
		}

		@Override
		public void amstradMonitorScanLinesEffectChanged(AmstradMonitor monitor) {
			syncMenu(monitor);
		}

		@Override
		protected boolean getState(AmstradMonitor monitor) {
			return monitor.isScanLinesEffectOn();
		}

	}

	private static class MonitorBilinearEffectMenuHelper extends MonitorCheckboxMenuHelper {

		public MonitorBilinearEffectMenuHelper(JCheckBoxMenuItem checkbox, AmstradMonitor monitor) {
			super(checkbox, monitor);
		}

		@Override
		public void amstradMonitorBilinearEffectChanged(AmstradMonitor monitor) {
			syncMenu(monitor);
		}

		@Override
		protected boolean getState(AmstradMonitor monitor) {
			return monitor.isBilinearEffectOn();
		}

	}

	private static class MonitorGateArrayMenuHelper extends MonitorCheckboxMenuHelper {

		public MonitorGateArrayMenuHelper(JCheckBoxMenuItem checkbox, AmstradMonitor monitor) {
			super(checkbox, monitor);
		}

		@Override
		public void amstradMonitorGateArraySizeChanged(AmstradMonitor monitor) {
			syncMenu(monitor);
		}

		@Override
		protected boolean getState(AmstradMonitor monitor) {
			return monitor.isFullGateArray();
		}

	}

	private static class MonitorShowSystemStatsMenuHelper extends MonitorCheckboxMenuHelper {

		public MonitorShowSystemStatsMenuHelper(JCheckBoxMenuItem checkbox, AmstradMonitor monitor) {
			super(checkbox, monitor);
		}

		@Override
		public void amstradShowSystemStatsChanged(AmstradMonitor monitor) {
			syncMenu(monitor);
		}

		@Override
		protected boolean getState(AmstradMonitor monitor) {
			return monitor.isShowSystemStats();
		}

	}

	private static class WindowAlwaysOnTopMenuHelper extends MonitorCheckboxMenuHelper {

		public WindowAlwaysOnTopMenuHelper(JCheckBoxMenuItem checkbox, AmstradMonitor monitor) {
			super(checkbox, monitor);
		}

		@Override
		public void amstradWindowAlwaysOnTopChanged(AmstradMonitor monitor) {
			syncMenu(monitor);
		}

		@Override
		protected boolean getState(AmstradMonitor monitor) {
			return monitor.isWindowAlwaysOnTop();
		}

	}

}