package cc.dhandho.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SemaphoreFuture<T> implements Future<T> {

	private Semaphore ready = new Semaphore(0);

	private boolean isDone;

	private T result;

	public void done(T result) {
		this.result = result;
		this.isDone = true;
		this.ready.release();
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		while (!this.isDone) {
			try {
				get(1, TimeUnit.DAYS);
			} catch (TimeoutException e) {
				continue;
			}
		}
		return this.result;
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if (!this.isDone) {
			this.ready.tryAcquire(timeout, unit);
		}
		return this.result;
	}

}
