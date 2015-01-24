package de.taimos.daemon;

import org.junit.Assert;
import org.junit.Test;

public class SpringLockTest {
	
	@Test
	public void startContext() throws Exception {
		TestAdapter adapter = new TestAdapter("good");
		adapter.doStart();
	}
	
	@Test
	public void testCtx() throws Exception {
		TestAdapter adapter = new TestAdapter("good");
		adapter.doStart();
		Assert.assertNotNull(adapter.getContext());
		Assert.assertNotNull(adapter.getContext().getId());
		adapter.doStop();
	}
	
	@Test
	public void testStopBad() {
		TestAdapter adapter = new TestAdapter("bad");
		try {
			adapter.doStart();
			Assert.fail();
		} catch (Exception e) {
			// should happen
		}
		Assert.assertNull(adapter.getContext());
		try {
			adapter.doStop();
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(RuntimeException.class, e.getClass());
		}
	}
	
	@Test
	public void testDoubleStart() throws Exception {
		TestAdapter adapter = new TestAdapter("good");
		adapter.doStart();
		Assert.assertNotNull(adapter.getContext());
		try {
			adapter.doStart();
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(RuntimeException.class, e.getClass());
		}
		adapter.doStop();
	}
	
	@Test
	public void testDoubleStop() throws Exception {
		TestAdapter adapter = new TestAdapter("good");
		adapter.doStart();
		Assert.assertNotNull(adapter.getContext());
		adapter.doStop();
		try {
			adapter.doStop();
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(RuntimeException.class, e.getClass());
		}
	}
}
