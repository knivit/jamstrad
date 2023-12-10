package com.tsoft.jamstrad.gui.overlay;

import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import com.tsoft.jamstrad.gui.UIResources;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.monitor.display.AmstradDisplayView;
import com.tsoft.jamstrad.pc.monitor.display.AmstradGraphicsContext;

public class PauseDisplayOverlay extends AbstractDisplayOverlay {

	public PauseDisplayOverlay(AmstradPc amstracPc) {
		super(amstracPc);
	}

	@Override
	public void renderOntoDisplay(AmstradDisplayView displayView, Rectangle displayBounds, Insets monitorInsets,
			boolean offscreenImage, AmstradGraphicsContext graphicsContext) {
		if (getAmstracPc().isPaused() && !getAmstracPc().getTape().isActive() && !offscreenImage) {
			ImageIcon icon = isLargeDisplay(displayBounds) ? UIResources.pauseOverlayIcon
					: UIResources.pauseSmallOverlayIcon;
			drawIconTopRight(icon, displayView, displayBounds, monitorInsets);
		}
	}

}