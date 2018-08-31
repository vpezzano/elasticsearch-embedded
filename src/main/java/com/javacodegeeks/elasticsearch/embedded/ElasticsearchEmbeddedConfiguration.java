package com.javacodegeeks.elasticsearch.embedded;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import javax.annotation.PreDestroy;

import org.elasticsearch.common.network.NetworkModule;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.transport.Netty4Plugin;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileSystemUtils;

/**
 * http://stackoverflow.com/questions/41298467/how-to-start-elasticsearch-5-1-embedded-in-my-java-application
 */
@Configuration
public class ElasticsearchEmbeddedConfiguration {
	private static class EmbeddedNode extends Node {
		public EmbeddedNode(Settings preparedSettings) {
			super(InternalSettingsPreparer.prepareEnvironment(preparedSettings, null),
					Collections.singletonList(Netty4Plugin.class));
		}
	}

	/*
	 * On startup, after the Node bean is created, the method start is invoked.
	 * Before the Node bean is destroyed, the method stop is invoked.
	 */
	@Bean(initMethod = "start", destroyMethod = "stop")
	Node elasticSearchTestNode() throws NodeValidationException, IOException {
		return new EmbeddedNode(Settings.builder().put(NetworkModule.TRANSPORT_TYPE_KEY, "netty4")
				.put(NetworkModule.HTTP_TYPE_KEY, "netty4").put(NetworkModule.HTTP_ENABLED.getKey(), "true")
				.put(Environment.PATH_HOME_SETTING.getKey(), home().getAbsolutePath())
				.put(Environment.PATH_DATA_SETTING.getKey(), data().getAbsolutePath()).build());
	}

	@Bean
	File home() throws IOException {
		return Files.createTempDirectory("elasticsearch-home-").toFile();
	}

	@Bean
	File data() throws IOException {
		return Files.createTempDirectory("elasticsearch-data-").toFile();
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		return factory;
	}

	@PreDestroy
	void destroy() throws IOException {
		FileSystemUtils.deleteRecursively(home());
		FileSystemUtils.deleteRecursively(data());
	}
}
