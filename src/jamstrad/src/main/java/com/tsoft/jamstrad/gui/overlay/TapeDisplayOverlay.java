package com.tsoft.jamstrad.gui.overlay;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import com.tsoft.jamstrad.gui.UIResources;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitorMode;
import com.tsoft.jamstrad.pc.monitor.display.AmstradDisplayView;
import com.tsoft.jamstrad.pc.monitor.display.AmstradGraphicsContext;
import com.tsoft.jamstrad.pc.monitor.display.AmstradSystemColors;
import com.tsoft.jamstrad.pc.tape.AmstradTape;

public class TapeDisplayOverlay extends AbstractDisplayOverlay {

	private static Color colorRead = AmstradSystemColors.getSystemColors(AmstradMonitorMode.COLOR).getColor(22);

	private static Color colorWrite = AmstradSystemColors.getSystemColors(AmstradMonitorMode.COLOR).getColor(16);

	private Font labelFont;

	public TapeDisplayOverlay(AmstradPc amstracPc) {
		super(amstracPc);
	}

	@Override
	public void renderOntoDisplay(AmstradDisplayView displayView, Rectangle displayBounds, Insets monitorInsets,
			boolean offscreenImage, AmstradGraphicsContext graphicsContext) {
		AmstradTape tape = getAmstracPc().getTape();
		if (tape.isActive() && !tape.isSuppressMessages() && getMode().isTapeActivityShown() && !offscreenImage) {
			String filename = tape.getFilenameAtTapeHead();
			if (filename != null) {
				// tape icon
				ImageIcon icon = isLargeDisplay(displayBounds) ? UIResources.tapeOverlayIcon
						: UIResources.tapeSmallOverlayIcon;
				Rectangle iconBounds = drawIconTopLeft(icon, displayView, displayBounds, monitorInsets);
				int x0 = iconBounds.x + iconBounds.width + 3;
				int yc = iconBounds.y + iconBounds.height / 2;
				// activity icon & label
				String label = filename;
				Font labelFont = getLabelFont(graphicsContext);
				FontMetrics fm = displayView.getFontMetrics(labelFont);
				int labelWidth = fm.stringWidth(label);
				int labelOffset = 24;
				int labelBaseline = 10 + (fm.getAscent() - fm.getDescent()) / 2 + 1;
				Graphics2D g = displayView.createDisplayViewport(x0, yc - 10, labelOffset + labelWidth, 22);
				if (tape.isWriting()) {
					drawIcon(UIResources.tapeWriteOverlayIcon, 0, 0, g);
					g.setColor(colorWrite);
				} else {
					drawIcon(UIResources.tapeReadOverlayIcon, 0, 0, g);
					g.setColor(colorRead);
				}
				g.setFont(labelFont);
				g.drawString(label, labelOffset, labelBaseline);
				g.dispose();
			}
		}
	}

	private Font getLabelFont(AmstradGraphicsContext graphicsContext) {
		if (labelFont == null) {
			labelFont = graphicsContext.getSystemFont().deriveFont(8f);
		}
		return labelFont;
	}

}