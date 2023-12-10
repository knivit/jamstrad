package com.tsoft.jamstrad.program.repo.cover;

import com.tsoft.jamstrad.AmstradFactory;
import com.tsoft.jamstrad.gui.ImageProxy;
import com.tsoft.jamstrad.program.repo.AmstradProgramRepository.Node;
import com.tsoft.jamstrad.util.KeyedCacheLRU;

public class CoverImageCache extends KeyedCacheLRU<Node, ImageProxy> {

	private static CoverImageCache instance;

	private static final String SETTING_CACHE_CAPACITY = "program_browser.cover_images.cache_capacity";

	private CoverImageCache() {
		super(Integer.parseInt(
				AmstradFactory.getInstance().getAmstradContext().getUserSettings().get(SETTING_CACHE_CAPACITY, "10")));
	}

	@Override
	protected void evicted(Node node, ImageProxy imageProxy) {
		super.evicted(node, imageProxy);
		imageProxy.dispose(); // disposes the memory in use by the image
		// System.out.println("CACHE-EVICTED cover image for " + node.getName());
	}

	public static CoverImageCache getInstance() {
		if (instance == null) {
			setInstance(new CoverImageCache());
		}
		return instance;
	}

	private static synchronized void setInstance(CoverImageCache cache) {
		if (instance == null) {
			instance = cache;
		}
	}

}