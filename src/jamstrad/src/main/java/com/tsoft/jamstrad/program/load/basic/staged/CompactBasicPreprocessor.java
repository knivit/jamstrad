package com.tsoft.jamstrad.program.load.basic.staged;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tsoft.jamstrad.AmstradFactory;
import com.tsoft.jamstrad.AmstradSettings;
import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicMinifier;
import com.tsoft.jamstrad.basic.BasicMinifierBatch;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.BasicSyntaxException;
import com.tsoft.jamstrad.basic.locomotive.minify.LocomotiveBasicLinesMinifier;
import com.tsoft.jamstrad.basic.locomotive.minify.LocomotiveBasicRemarksMinifier;
import com.tsoft.jamstrad.basic.locomotive.minify.LocomotiveBasicVariableNameMinifier;
import com.tsoft.jamstrad.basic.locomotive.minify.LocomotiveBasicWhitespaceMinifier;
import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;
import com.tsoft.jamstrad.program.load.basic.BasicPreprocessor;

public class CompactBasicPreprocessor extends StagedBasicPreprocessor {

	public static final int LEVEL_NONE = 0;

	public static final int LEVEL_NON_INVASIVE = 2;

	public static final int LEVEL_ULTRA = 10;

	public static final int LEVEL_DEFAULT = LEVEL_NON_INVASIVE;

	public CompactBasicPreprocessor() {
	}

	@Override
	public int getDesiredPreambleLineCount() {
		return 0;
	}

	@Override
	public boolean isApplicableToMergedCode() {
		return false; // is applied to the chained code separately (before merging)
	}

	@Override
	public Collection<BasicKeywordToken> getKeywordsActedOn() {
		return Collections.emptyList();
	}

	@Override
	protected void stage(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session) throws BasicException {
		boolean printStats = shouldPrintStatistics();
		int bytesBefore = printStats ? session.getBasicRuntime().getCompiler().compile(sourceCode).getByteCount() : 0;
		int level = getMinificationLevel();
		BasicMinifier minifier = createMinifier(level, session);
		minifier.minify(sourceCode);
		if (printStats) {
			int bytesAfter = session.getBasicRuntime().getCompiler().compile(sourceCode).getByteCount();
			int bytesReduction = bytesBefore - bytesAfter;
			System.out.println("Compacted source code on level " + level + " with " + bytesReduction + " bytes (from "
					+ bytesBefore + " to " + bytesAfter + " bytes)");
		}
	}

	private boolean shouldPrintStatistics() {
		return getAmstradSettings().getBool("basic_staging.minify.printStats", false);
	}

	private int getMinificationLevel() {
		int level = LEVEL_DEFAULT;
		String value = getAmstradSettings().get("basic_staging.minify.level", String.valueOf(level));
		try {
			level = Math.max(Math.min(Integer.parseInt(value), LEVEL_ULTRA), LEVEL_NONE);
		} catch (NumberFormatException e) {
			System.err.println(e);
		}
		return level;
	}

	private BasicMinifier createMinifier(int level, StagedBasicProgramLoaderSession session) {
		BasicMinifierBatch batch = new BasicMinifierBatch();
		if (level > LEVEL_NONE) {
			batch.add(new LocomotiveBasicRemarksMinifier());
		}
		if (level > LEVEL_NONE + 1) {
			batch.add(new LocomotiveBasicWhitespaceMinifier());
		}
		if (level > LEVEL_NONE + 2) {
			batch.add(new LocomotiveBasicVariableNameMinifier(session.getOriginalToStagedVariableMapping()));
		}
		if (level > LEVEL_NONE + 3) {
			double intensity = (level - LEVEL_NONE - 4) / (double) (LEVEL_ULTRA - LEVEL_NONE - 4);
			batch.add(new ReservedStagingLinesMinifier(intensity, session));
		}
		return batch;
	}

	private AmstradSettings getAmstradSettings() {
		return AmstradFactory.getInstance().getAmstradContext().getUserSettings();
	}

	private static class ReservedStagingLinesMinifier extends LocomotiveBasicLinesMinifier {

		private static final int LINE_LENGTH_MARGIN = 32;

		private StagedBasicProgramLoaderSession session;

		private Set<BasicKeywordToken> allKeywordsActedOnWhenStaging;

		public ReservedStagingLinesMinifier(double intensity, StagedBasicProgramLoaderSession session) {
			super(intensity);
			this.session = session;
		}

		@Override
		protected int getMaximumJoinedLineLength(BasicSourceCodeLine line, BasicSourceCodeLine nextLine) {
			int max = super.getMaximumJoinedLineLength(line, nextLine);
			try {
				max -= LINE_LENGTH_MARGIN * getCountOfKeywordsActedOnWhenStaging(line);
				max -= LINE_LENGTH_MARGIN * getCountOfKeywordsActedOnWhenStaging(nextLine);
			} catch (BasicSyntaxException e) {
				e.printStackTrace();
			}
			return max;
		}

		@Override
		protected int getLineLengthUpperBound() {
			return super.getLineLengthUpperBound() - LINE_LENGTH_MARGIN;
		}

		private int getCountOfKeywordsActedOnWhenStaging(BasicSourceCodeLine line) throws BasicSyntaxException {
			int count = 0;
			Set<BasicKeywordToken> keywords = getAllKeywordsActedOnWhenStaging();
			BasicSourceTokenSequence sequence = line.parse();
			for (int i = 0; i < sequence.size(); i++) {
				if (keywords.contains(sequence.get(i)))
					count++;
			}
			return count;
		}

		private Set<BasicKeywordToken> deriveAllKeywordsActedOnWhenStaging() {
			Set<BasicKeywordToken> keywords = new HashSet<BasicKeywordToken>();
			Iterator<BasicPreprocessor> it = getSession().getLoader().getPreprocessors();
			while (it.hasNext()) {
				BasicPreprocessor preprocessor = it.next();
				if (preprocessor instanceof StagedBasicPreprocessor) {
					keywords.addAll(((StagedBasicPreprocessor) preprocessor).getKeywordsActedOn());
				}
			}
			return keywords;
		}

		private Set<BasicKeywordToken> getAllKeywordsActedOnWhenStaging() {
			if (allKeywordsActedOnWhenStaging == null) {
				allKeywordsActedOnWhenStaging = deriveAllKeywordsActedOnWhenStaging();
			}
			return allKeywordsActedOnWhenStaging;
		}

		private StagedBasicProgramLoaderSession getSession() {
			return session;
		}

	}

}