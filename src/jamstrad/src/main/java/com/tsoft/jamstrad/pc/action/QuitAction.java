package com.tsoft.jamstrad.pc.action;

import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

import com.tsoft.jamstrad.gui.terminate.AmstradTerminationDisplaySource;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.keyboard.AmstradKeyboardEvent;
import com.tsoft.jamstrad.pc.monitor.display.AmstradDisplayOverlay;
import com.tsoft.jamstrad.pc.monitor.display.AmstradDisplayView;
import com.tsoft.jamstrad.pc.monitor.display.AmstradGraphicsContext;
import com.tsoft.jamstrad.util.SystemUtils;

public class QuitAction extends AmstradPcAction {

	private AmstradTerminationDisplaySource terminationDisplaySource;

	private boolean quitting;

	private boolean cancelCommand;

	public QuitAction(AmstradPc amstradPc) {
		this(amstradPc, "Quit");
	}

	public QuitAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
		amstradPc.getKeyboard().addKeyboardListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		quit();
	}

	@Override
	public void amstradKeyboardEventDispatched(AmstradKeyboardEvent event) {
		super.amstradKeyboardEventDispatched(event);
		if (event.isKeyPressed()) {
			if (!isTriggeredByMenuKeyBindings()) {
				if (event.getKeyCode() == KeyEvent.VK_Q && event.isControlDown() && event.isShiftDown()) {
					quit();
				}
			}
			if (event.getKeyCode() == KeyEvent.VK_ESCAPE && isQuitting()) {
				setCancelCommand(event.isShiftDown());
				if (getTerminationDisplaySource() != null)
					getTerminationDisplaySource().forceQuit();
			}
		}
	}

	public synchronized void quit() {
		if (!isQuitting()) {
			setQuitting(true);
			runInSeparateThread(new Runnable() {
				@Override
				public void run() {
					doQuit();
				}
			});
		}
	}

	private void doQuit() {
		if (getAmstradContext().isAnimateOnTerminate()) {
			showTerminationAnimation();
		}
		executeSystemCommand();
		getAmstradPc().terminate();
	}

	private void showTerminationAnimation() {
		AmstradTerminationDisplaySource ds = new AmstradTerminationDisplaySource(getAmstradPc());
		setTerminationDisplaySource(ds);
		getAmstradPc().getMonitor().swapDisplaySource(ds);
		getAmstradPc().getMonitor().setCustomDisplayOverlay(new VoidDisplayOverlay());
		while (!ds.isAnimationCompleted()) {
			SystemUtils.sleep(50L);
		}
	}

	private void executeSystemCommand() {
		String command = getAmstradContext().getSystemCommandOnTerminate();
		if (command != null && !command.isEmpty()) {
			if (isCancelCommand()) {
				System.out.println("Cancelled quit command");
			} else {
				getAmstradContext().executeSystemCommand(command);
			}
		}
	}

	private AmstradTerminationDisplaySource getTerminationDisplaySource() {
		return terminationDisplaySource;
	}

	private void setTerminationDisplaySource(AmstradTerminationDisplaySource displaySource) {
		this.terminationDisplaySource = displaySource;
	}

	private boolean isQuitting() {
		return quitting;
	}

	private void setQuitting(boolean quitting) {
		this.quitting = quitting;
	}

	private boolean isCancelCommand() {
		return cancelCommand;
	}

	private void setCancelCommand(boolean cancelCommand) {
		this.cancelCommand = cancelCommand;
	}

	private static class VoidDisplayOverlay implements AmstradDisplayOverlay {

		public VoidDisplayOverlay() {
		}

		@Override
		public void init(JComponent displayComponent, AmstradGraphicsContext graphicsContext) {
		}

		@Override
		public void renderOntoDisplay(AmstradDisplayView displayView, Rectangle displayBounds, Insets monitorInsets,
				boolean offscreenImage, AmstradGraphicsContext graphicsContext) {
			// render nothing
		}

		@Override
		public void dispose(JComponent displayComponent) {
		}

	}

}