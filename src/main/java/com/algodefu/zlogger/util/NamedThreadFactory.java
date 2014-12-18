package com.algodefu.zlogger.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author oleg.zherebkin
 */
public class NamedThreadFactory implements ThreadFactory {
	private static final AtomicInteger poolNumber = new AtomicInteger(1);

	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(0);
	private final String namePrefix;
	private final String[] names;

	public NamedThreadFactory(final String prefix, final String... names) {
		this.names = names;
		final SecurityManager s = System.getSecurityManager();
		this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.namePrefix = prefix + "-" + poolNumber.getAndIncrement() + "-";
	}

	@Override
	public Thread newThread(final Runnable r) {
		final int idx = this.threadNumber.getAndIncrement();
		final String threadName = this.namePrefix + (idx < names.length ? names[idx] + "-" : "") + idx;
		final Thread t = new Thread(this.group, r, threadName, 0);
		t.setDaemon(true);
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}
}
