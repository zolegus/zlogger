package com.algodefu.zlogger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author oleg.zherebkin
 */
public final class ZLogFactory {

	private final Object lock = new Object();

	private final AtomicReference<LoggerService> loggerService;
	private final Map<String, ZLogView> namedLogger;
	private final Map<Class, ZLogView> classedLogger;

	private ZLogFactory(){
		loggerService = new AtomicReference<LoggerService>();
		namedLogger = new HashMap<String, ZLogView>();
		classedLogger = new HashMap<Class, ZLogView>();
	}

	private ZLog get(final String name){
		return get0(name, name, namedLogger);
	}

	private ZLog get(final Class clazz){
		return get0(clazz, clazz.getName(), classedLogger);
	}

	private <T> ZLog get0(final T key, final String name, final Map<T, ZLogView> map){
		ZLogView logger = map.get(key);
		if (logger != null) return logger;
		synchronized (lock) {
			logger = map.get(name);
			if (logger != null) return logger;

			logger = new ZLogView(name);
			final LoggerService service = getService();
			logger.setLoggerService(service);
			map.put(key, logger);
			return logger;
		}
	}

	public static LoggerService lookupService(final String name) {
		return Helper.FACTORY.getService();
	}

	private LoggerService getService(){
		synchronized (lock) {
			return loggerService.get();
		}
	}

	public static ZLog getLog(final String name){
		return Helper.FACTORY.get(name);
	}

	public static ZLog getLog(final Class clazz){
		return Helper.FACTORY.get(clazz);
	}

	public static void stop(){
		synchronized (Helper.FACTORY.lock) {
			final LoggerService service = Helper.FACTORY.loggerService.getAndSet(null);
			if (service == null) return;

			service.stop();

			for(final ZLogView loggerView : Helper.FACTORY.namedLogger.values()){
				loggerView.invalidate();
			}
			for(final ZLogView loggerView : Helper.FACTORY.classedLogger.values()){
				loggerView.invalidate();
			}
		}
	}

	public static ZLogFactory init(LoggerService service){
		synchronized (Helper.FACTORY.lock) {
			stop();
			if (service == null)
				throw new IllegalArgumentException("Not a null logger service is expected");
			Helper.FACTORY.loggerService.set(service);
		}
		return Helper.FACTORY;
	}

	private final static class Helper {
		final static ZLogFactory FACTORY = new ZLogFactory();
	}
}
