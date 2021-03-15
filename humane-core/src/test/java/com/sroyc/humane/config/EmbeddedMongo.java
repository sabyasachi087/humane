package com.sroyc.humane.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@Component
public class EmbeddedMongo implements DisposableBean {
	private static final String CONNECTION_STRING = "mongodb://%s:%d";

	private final MongodExecutable MONGO_EXECUTABLE = startMongoExecutable();
	private final MongodProcess MONGO_PROCESS = startProcess();
	private static final String MONGO_IP = "localhost";
	private static final int MONGO_PORT = 50513;

	private final MongodExecutable startMongoExecutable() {
		try {
			ImmutableMongodConfig mongodConfig = ImmutableMongodConfig.builder().version(Version.Main.PRODUCTION)
					.net(new Net(MONGO_IP, MONGO_PORT, Network.localhostIsIPv6())).build();
			MongodStarter starter = MongodStarter.getDefaultInstance();
			return starter.prepare(mongodConfig);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private final MongodProcess startProcess() {
		try {
			return MONGO_EXECUTABLE.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void stop() throws Exception {
		if (isRunning()) {
			MONGO_PROCESS.stop();
			MONGO_EXECUTABLE.stop();
			while (MONGO_PROCESS.isProcessRunning()) {
				TimeUnit.SECONDS.sleep(1);
			}
		}
	}

	public boolean isRunning() {
		return MONGO_PROCESS.isProcessRunning();
	}

	public String getConnectionUrl() {
		return String.format(CONNECTION_STRING, MONGO_IP, MONGO_PORT);
	}

	@Override
	public void destroy() throws Exception {
		stop();
	}

}
