package com.tsoft.jamstrad.pc.monitor.display;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import com.tsoft.jamstrad.pc.monitor.AmstradMonitorMode;

public interface AmstradGraphicsContext {

	Font getSystemFont();

	AmstradSystemColors getSystemColors();

	AmstradMonitorMode getMonitorMode();

	Insets getBorderInsetsForDisplaySize(Dimension size);

	Dimension getPrimaryDisplaySourceResolution();

	Dimension getDisplayCanvasSize();

	int getTextRows();

	int getTextColumns();

	int getDefaultBorderColorIndex();

	int getDefaultPaperColorIndex();

	int getDefaultPenColorIndex();

}