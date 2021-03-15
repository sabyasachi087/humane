package com.sroyc.humane.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sroyc.humane.data.model.HumaneEvent;

@Component
public class HumaneEventPublisher implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {

	private static final Logger LOGGER = LogManager.getLogger(HumaneEventPublisher.class);

	private static final BlockingQueue<HumaneEvent> EVENT_QUEUE = new ArrayBlockingQueue<>(1024, true);
	private static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
	private static final List<HumaneEventListener> LISTENERS = new ArrayList<>();
	private static final AtomicBoolean LOOP = new AtomicBoolean(true);
	private static final ReentrantLock LOCK = new ReentrantLock(true);
	private static final Condition COND = LOCK.newCondition();

	private ApplicationContext context;

	@Autowired
	public HumaneEventPublisher(ApplicationContext context) {
		this.context = context;
	}

	public boolean publish(HumaneEvent event) {
		boolean result = false;
		try {
			result = EVENT_QUEUE.offer(event, 1, TimeUnit.MINUTES);
			if (LOCK.tryLock()) {
				COND.signal();
				LOCK.unlock();
			}
		} catch (Exception e) {
			LOGGER.error("Unable to publish event ", e);
		}
		return result;
	}

	protected void broadcast() {
		HumaneEvent event = null;
		try {
			int waitCount = 0;
			LOCK.lock();
			while (LOOP.get()) {
				if ((event = EVENT_QUEUE.poll(5, TimeUnit.SECONDS)) != null) {
					for (HumaneEventListener listener : LISTENERS) {
						if (listener.isEventSupported(event.getEvent())) {
							listener.listen(event);
							break;
						}
					}
				} else if (!COND.await(++waitCount, TimeUnit.MINUTES)) {
					waitCount = 0;
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Error broadcasting event.", ex);
		} finally {
			LOCK.unlock();
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			Map<String, HumaneEventListener> beans = this.context.getBeansOfType(HumaneEventListener.class);
			if (!CollectionUtils.isEmpty(beans)) {
				beans.values().forEach(LISTENERS::add);
				LOGGER.info("Humane Event listener(s) is/are initialized.");
			}
		} catch (BeansException be) {
			LOGGER.warn("No beans found of type [{}]", HumaneEventListener.class.getCanonicalName());
		}

	}

	@Override
	public void destroy() throws Exception {
		LOOP.set(false);
		if (LOCK.tryLock()) {
			COND.signal();
			LOCK.unlock();
		}
		EXECUTOR.shutdown();
		LOGGER.info("Shutting down event publisher ... ");
		EXECUTOR.awaitTermination(100, TimeUnit.SECONDS);
		LOGGER.info("Event publisher has been stopped.");
	}

}
