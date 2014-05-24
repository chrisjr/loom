package org.chrisjr.loom.time;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IntervalTest {
	private Interval interval;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		interval = new Interval(0, 1);
	}

	@After
	public void tearDown() throws Exception {
		interval = null;
	}

	@Test
	public void mustStartBeforeEnd() {
		thrown.expect(IllegalArgumentException.class);
		new Interval(0.5, 0.25);
	}

	@Test
	public void equivalentIntervalsAreEqual() {
		Interval decimal = new Interval(0.25, 0.5);
		Interval decimal2 = new Interval(0.25, 0.5);
		assertThat(decimal, is(equalTo(decimal2)));
	}

	@Test
	public void canAddToInterval() {
		Interval newInterval = interval.add(1);
		assertThat(newInterval.getStart().intValue(), is(equalTo(1)));
	}

	@Test
	public void moduloOtherInterval() {
		Interval quarterToHalf = new Interval(0.25, 0.5);
		Interval plusOne = quarterToHalf.add(1);

		assertThat(plusOne.modulo(interval), is(equalTo(quarterToHalf)));
	}

	@Test
	public void moduloOtherSmallerInterval() {
		Interval zeroToOneAndAHalf = new Interval(0, 1.5);
		
		thrown.expect(IllegalArgumentException.class);
		zeroToOneAndAHalf.modulo(interval);
	}

	
}
