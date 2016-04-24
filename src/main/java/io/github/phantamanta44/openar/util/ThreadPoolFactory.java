package io.github.phantamanta44.openar.util;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;


public class ThreadPoolFactory {

	private QueueType underlyingPoolType = QueueType.CACHED;
	private PoolType poolExecutionType = PoolType.EXECUTOR;
	private int poolSize = 4;
	
	public ThreadPoolFactory withQueue(QueueType type) {
		this.underlyingPoolType = type;
		return this;
	}
	
	public ThreadPoolFactory withPool(PoolType type) {
		this.poolExecutionType = type;
		return this;
	}
	
	public ThreadPoolFactory withPoolSize(int size) {
		this.poolSize = size;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ExecutorService> T construct() {
		return (T)poolExecutionType.builder.apply(underlyingPoolType.builder.apply(poolSize));
	}
	
	public enum QueueType {
		
		FIXED(Executors::newFixedThreadPool),
		CACHED(s -> Executors.newCachedThreadPool()),
		SINGLE(s -> Executors.newSingleThreadExecutor());
		
		private final Function<Integer, ExecutorService> builder;
		
		QueueType(Function<Integer, ExecutorService> builder) {
			this.builder = builder;
		}
		
	}
	
	public enum PoolType {
		
		EXECUTOR(e -> e),
		SCHEDULED(DelegatedScheduler::new);
		
		private final Function<ExecutorService, ? extends ExecutorService> builder;
		
		PoolType(Function<ExecutorService, ExecutorService> builder) {
			this.builder = builder;
		}
		
	}
	
	private static class DelegatedScheduler extends ScheduledThreadPoolExecutor {

		private final ExecutorService del;
		
		private DelegatedScheduler(ExecutorService executor) {
			super(1);
			this.del = executor;
		}

		@Override
		public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
			return super.schedule(() -> del.submit(command), delay, unit);
		}

		@Override
		public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
			return new ChildFuture<>(super.schedule(() -> del.submit(callable), delay, unit));
		}

		@Override
		public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
			return super.scheduleAtFixedRate(() -> del.submit(command), initialDelay, period, unit);
		}

		@Override
		public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
			return super.scheduleWithFixedDelay(() -> del.submit(command), initialDelay, delay, unit);
		}

		@Override
		public void execute(Runnable command) {
			del.execute(command);
		}

		@Override
		public Future<?> submit(Runnable task) {
			return del.submit(task);
		}

		@Override
		public <T> Future<T> submit(Runnable task, T result) {
			return del.submit(task, result);
		}

		@Override
		public <T> Future<T> submit(Callable<T> task) {
			return del.submit(task);
		}

		@Override
		public void shutdown() {
			super.shutdown();
			del.shutdown();
		}

		@Override
		public List<Runnable> shutdownNow() {
			List<Runnable> remainingTasks = super.shutdownNow();
			remainingTasks.addAll(del.shutdownNow());
			return remainingTasks;
		}
		
		private static class ChildFuture<T> implements ScheduledFuture<T> {
			
			private ScheduledFuture<Future<T>> del;
			
			private ChildFuture(ScheduledFuture<Future<T>> parent) {
				this.del = parent;
			}

			@Override
			public long getDelay(TimeUnit unit) {
				return del.getDelay(unit);
			}

			@Override
			public int compareTo(Delayed o) {
				return del.compareTo(o);
			}

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				try {
					del.get().cancel(mayInterruptIfRunning);
				} catch (Exception ex) { }
				return del.cancel(mayInterruptIfRunning);
			}

			@Override
			public boolean isCancelled() {
				try {
					if (del.isDone())
						return del.get().isCancelled();
				} catch (Exception ex) { }
				return del.isCancelled();
			}

			@Override
			public boolean isDone() {
				try {
					return del.get().isDone();
				} catch (Exception ex) { 
					return false;
				}
			}

			@Override
			public T get() throws InterruptedException, ExecutionException {
				return del.get().get();
			}

			@Override
			public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
				return del.get(timeout, unit).get(timeout, unit);
			}
			
		}
		
	}
	
}