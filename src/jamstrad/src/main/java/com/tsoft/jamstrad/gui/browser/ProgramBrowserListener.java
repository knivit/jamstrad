package com.tsoft.jamstrad.gui.browser;

import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.util.GenericListener;

public interface ProgramBrowserListener extends GenericListener {

	void programLoadedFromBrowser(ProgramBrowserDisplaySource displaySource, AmstradProgram program);

	void programRunFromBrowser(ProgramBrowserDisplaySource displaySource, AmstradProgram program);

}