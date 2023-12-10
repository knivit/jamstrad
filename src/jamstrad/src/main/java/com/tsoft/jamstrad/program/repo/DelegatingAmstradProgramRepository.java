package com.tsoft.jamstrad.program.repo;

public abstract class DelegatingAmstradProgramRepository extends AmstradProgramRepository {

	private AmstradProgramRepository delegate;

	protected DelegatingAmstradProgramRepository(AmstradProgramRepository delegate) {
		this.delegate = delegate;
	}

	@Override
	public void refresh() {
		getDelegate().refresh();
		super.refresh();
	}

	protected AmstradProgramRepository getDelegate() {
		return delegate;
	}

}