package org.chrisjr.loom;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.concurrent.atomic.AtomicInteger;

import netP5.NetAddress;

import org.chrisjr.loom.time.NonRealTimeScheduler;
import org.chrisjr.loom.wrappers.OscP5Impl;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import oscP5.OscMessage;
import oscP5.OscP5;

public class AsOscMessageTest {
	private OscP5 oscP5;
	private final NetAddress myRemoteLocation = new NetAddress("127.0.0.1",
			12001);

	private Loom loom;
	private NonRealTimeScheduler scheduler;
	private Pattern pattern;

	private final AtomicInteger eventsCounter = new AtomicInteger();

	@Before
	public void setUp() throws Exception {
		oscP5 = new OscP5(this, 12001);

		scheduler = new NonRealTimeScheduler();
		loom = new Loom(null, scheduler);
		pattern = new Pattern(loom);

		OscP5Impl oscP52 = new OscP5Impl(loom, new OscP5(loom, 12000));
		loom.oscP5Wrapper.set(oscP52);
		loom.play();
	}

	@After
	public void tearDown() throws Exception {
		loom.dispose();
		oscP5.dispose();
	}

	void oscEvent(OscMessage theOscMessage) {
		eventsCounter.incrementAndGet();
		System.out.print(theOscMessage.addrPattern());
		System.out.print(" ");
		System.out.println(theOscMessage.get(0).intValue());
	}

	void waitForEvents(int expected, int timeout) {
		int millisPassed = 0;
		while (eventsCounter.get() < expected && millisPassed < timeout) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			millisPassed++;
		}
		assertThat(eventsCounter.get(), is(equalTo(expected)));
	}

	@Ignore
	public void localReceiver() {
		OscMessage myMessage = new OscMessage("/test");
		myMessage.add(123);
		oscP5.send(myMessage, myRemoteLocation);

		waitForEvents(1, 100);
	}

	@Test
	public void receive() {
		pattern.extend("1101");

		Pattern messagePat = new Pattern(loom);
		messagePat.asOscMessage("/test", 123);

		pattern.asOscBundle(myRemoteLocation, messagePat);

		scheduler.setElapsedMillis(1001);
		waitForEvents(3, 200);
	}
}
