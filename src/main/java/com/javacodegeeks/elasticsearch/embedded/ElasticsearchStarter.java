package com.javacodegeeks.elasticsearch.embedded;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * This will run ElasticSearch inside the JVM process; such technique is commonly referred as embedding.
 * Embedded version of the Elasticsearch is not officially supported and not recommended.
 */
@SpringBootApplication
public class ElasticsearchStarter {
	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchEmbeddedConfiguration.class);
	}
}
