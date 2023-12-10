package com.tsoft.jamstrad.program.load;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.util.GenericListenerList;

public abstract class AmstradProgramRuntime {

	private AmstradProgram program;

	private AmstradPc amstradPc;

	private boolean run;

	private boolean disposed;

	private int exitCode;

	private GenericListenerList<AmstradProgramRuntimeListener> listeners;

	protected AmstradProgramRuntime(AmstradProgram program, AmstradPc amstradPc) {
		this.program = program;
		this.amstradPc = amstradPc;
		this.listeners = new GenericListenerList<AmstradProgramRuntimeListener>();
	}

	public void addListener(AmstradProgramRuntimeListener listener) {
		getListeners().addListener(listener);
	}

	public void removeListener(AmstradProgramRuntimeListener listener) {
		getListeners().removeListener(listener);
	}

	public final synchronized void run(String... args) throws AmstradProgramException {
		checkNotDisposed();
		for (AmstradProgramRuntimeListener listener : getListeners()) {
			listener.amstradProgramIsAboutToRun(this);
		}
		doRun(args);
		setRun(true);
		for (AmstradProgramRuntimeListener listener : getListeners()) {
			listener.amstradProgramIsRun(this);
		}
	}

	protected abstract void doRun(String... args) throws AmstradProgramException;

	public synchronized void dispose(boolean programRemainsLoaded, int exitCode) {
		if (!isDisposed()) {
			setDisposed(true);
			setExitCode(exitCode);
			for (AmstradProgramRuntimeListener listener : getListeners()) {
				listener.amstradProgramIsDisposed(this, programRemainsLoaded);
			}
		}
	}

	protected void checkNotDisposed() {
		if (isDisposed())
			throw new IllegalStateException("This program runtime is disposed");
	}

	public AmstradProgram getProgram() {
		return program;
	}

	public AmstradPc getAmstradPc() {
		return amstradPc;
	}

	public boolean isRun() {
		return run;
	}

	private void setRun(boolean run) {
		this.run = run;
	}

	public boolean isDisposed() {
		return disposed;
	}

	private void setDisposed(boolean disposed) {
		this.disposed = disposed;
	}

	public int getExitCode() {
		return exitCode;
	}

	private void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}

	protected GenericListenerList<AmstradProgramRuntimeListener> getListeners() {
		return listeners;
	}

}