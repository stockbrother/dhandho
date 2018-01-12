package cc.dhandho.test.jsweet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsweet.transpiler.JSweetProblem;
import org.jsweet.transpiler.SourcePosition;
import org.jsweet.transpiler.util.ConsoleTranspilationHandler;

public class TestTranspilationHandler extends ConsoleTranspilationHandler {

	List<JSweetProblem> reportedProblems = new ArrayList<>();
	List<SourcePosition> reportedSourcePositions = new ArrayList<>();

	@Override
	public void report(JSweetProblem problem, SourcePosition sourcePosition, String message) {
		super.report(problem, sourcePosition, message);

		if (problem == JSweetProblem.CANDY_VERSION_DISCREPANCY) {
			return;
		}
		if (problem == JSweetProblem.NODE_OBSOLETE_VERSION) {
			return;
		}

		reportedProblems.add(problem);
		reportedSourcePositions.add(sourcePosition);
	}

	public void assertNoProblems() {
		// assert 0 problems = empty problem list
		assertReportedProblems();
	}

	public void assertReportedProblems(JSweetProblem... expectedProblems) {
		List<JSweetProblem> expectedProblemsList = Arrays.asList(expectedProblems);
		assertEquals(expectedProblemsList, reportedProblems);
	}

	public List<JSweetProblem> getReportedProblems() {
		return reportedProblems;
	}

	public List<SourcePosition> getReportedSourcePositions() {
		return reportedSourcePositions;
	}

}
