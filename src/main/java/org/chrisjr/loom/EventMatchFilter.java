package org.chrisjr.loom;

import java.util.Collection;
import java.util.Collections;

import org.chrisjr.loom.time.Interval;

/**
 * Match events from the parent EventQueryable according to a specified value.
 * 
 * @author chrisjr
 */
public class EventMatchFilter extends EventTransformer {
	double matchValue;
	static final double EPSILON = 1e-4;

	public EventMatchFilter(EventQueryable parentEvents, double matchValue) {
		super(parentEvents);
		this.matchValue = matchValue;
	}

	@Override
	public Collection<Event> apply(Interval interval, Event e) {
		if (Math.abs(e.getValue() - matchValue) < EPSILON)
			return Collections.singletonList(new Event(e.getInterval(), 1.0, e
					.getParentEvent()));
		else
			return Collections.singletonList(new Event(e.getInterval(), 0.0, e
					.getParentEvent()));
	}
}
