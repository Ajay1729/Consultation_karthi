package com.bytatech.ayoos.client.digitalsigning;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bytatech.ayoos.client.ExcludeFromComponentScan;

import feign.auth.BasicAuthRequestInterceptor;

@Configuration
@ExcludeFromComponentScan
@EnableConfigurationProperties
public class ClientConfiguration {

	@Value("${alfrescoRestApi.security.basicAuth.username:}")
	private String basicAuthUsername;

	@Value("${alfrescoRestApi.security.basicAuth.password:}")
	private String basicAuthPassword;

	@Bean
	@ConditionalOnProperty(name = "alfrescoRestApi.security.basicAuth.username")
	public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		System.out.println("auth bean ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
				+ basicAuthUsername + "    " + basicAuthPassword);
		return new BasicAuthRequestInterceptor(this.basicAuthUsername, this.basicAuthPassword);
	}

}
