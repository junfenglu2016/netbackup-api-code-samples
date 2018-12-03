package com.leo.demo.common.util;


import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;
import org.springframework.http.MediaType;

/**
 * Remote api
 *
 * @author lujf2
 */
public class RemoteRestClient {

	private static final Logger logger = LoggerFactory.getLogger(RemoteRestClient.class);

	private static RemoteRestClient remoteRestClient = null;

	public static synchronized RemoteRestClient getInstance() // 
	{
		if (remoteRestClient == null) {
			remoteRestClient = new RemoteRestClient();
		}
		return remoteRestClient;
	}
	  /**
     *
     * @return
     */
    public  HttpHeaders getHeaders(){
        HttpHeaders requestHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        requestHeaders.setContentType(mediaType);
        requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return requestHeaders;
    }
	/**
	 * post
	 *
	 * @param jsonData
	 * @param url
	 * @param headers
	 * @return
	 */
	public String postRequest(String jsonData, String url, HttpHeaders headers) {
		// 鏋勫缓POST form琛ㄥ崟璇锋眰
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>(jsonData, headers);
		ResponseEntity<String> response = null;
		restTemplate.setRequestFactory(getRequestFactory());
		try {
			response = restTemplate.postForEntity(url, request, String.class);
		} catch (Exception exception) {
			//logger.error("An exception is found while accessing", exception);
			throw exception;
		}
		logger.debug("build form request:{}  send to :{} receive the response:{}", jsonData, url, response);
		return response.getBody();
	}
	
	public String postRequestnoverify(String jsonData, String url, HttpHeaders headers) {
		// 鏋勫缓POST form琛ㄥ崟璇锋眰
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>(jsonData, headers);
		ResponseEntity<String> response = null;
		restTemplate.setRequestFactory(getRequestFactorynotverifyhostname());
		try {
			response = restTemplate.postForEntity(url, request, String.class);
		} catch (Exception exception) {
			//logger.error("An exception is found while accessing", exception);
			throw exception;
		}
		logger.debug("build form request:{}  send to :{} receive the response:{}", jsonData, url, response);
		return response.getBody();

	}
	
	public String putRequest(String jsonData, String url, HttpHeaders headers) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>(jsonData, headers);
		ResponseEntity<String> response = null;
		restTemplate.setRequestFactory(getRequestFactory());
		try {
			response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		} catch (Exception exception) {
			//logger.error("An exception is found while accessing", exception);
			throw exception;
		}
		logger.debug("build form request:{}  send to :{} receive the response:{}", jsonData, url, response);
		return response.getBody();
	}
	
	public String putRequestnoverify(String jsonData, String url, HttpHeaders headers) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>(jsonData, headers);
		ResponseEntity<String> response = null;
		restTemplate.setRequestFactory(getRequestFactorynotverifyhostname());
		try {
			response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		} catch (Exception exception) {
			//logger.error("An exception is found while accessing", exception);
			throw exception;
		}
		logger.debug("build form request:{}  send to :{} receive the response:{}", jsonData, url, response);
		return response.getBody();

	}
	/**
	 * Get
	 * 
	 * @param map
	 * @param url
	 * @param headers
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getRequest(Map params, String url, HttpHeaders headers) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
		restTemplate.setRequestFactory(getRequestFactory());
		try {
			if (params != null) {
				response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, params);
			} else {
				response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

			}
		} catch (Exception exception) {
			//logger.error("An exception is found while accessing", exception);
			throw exception;
		}
		return response.getBody();
	}
	
	@SuppressWarnings("unchecked")
	public String getRequestnoverify(Map params, String url, HttpHeaders headers) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
		restTemplate.setRequestFactory(getRequestFactorynotverifyhostname());
		try {
			if (params != null) {
				response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, params);
			} else {
				response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

			}
		} catch (Exception exception) {
			//logger.error("An exception is found while accessing", exception);
			throw exception;
		}
		return response.getBody();
	}
	/**
	 * HttpComponentsClientHttpRequestFactory
	 *
	 * @return
	 */
	public HttpComponentsClientHttpRequestFactory getRequestFactory() {
		HttpComponentsClientHttpRequestFactory requestFactory = null;
		try {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
		} catch (Exception e) {
			logger.error("Ignore SSL Exception", e.getMessage());
		}
		return requestFactory;
	}
	
	public HttpComponentsClientHttpRequestFactory getRequestFactorynotverifyhostname() {
		HttpComponentsClientHttpRequestFactory requestFactory = null;
		
		X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
			public void verify(String arg0, SSLSocket arg1) throws IOException {}
			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
			public void verify(String arg0, X509Certificate arg1) throws SSLException {}
		};
		
		try {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			
			SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
			socketFactory.setHostnameVerifier(hostnameVerifier);
			//SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
			requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
		} catch (Exception e) {
			logger.error("Ignore SSL Exception", e.getMessage());
		}
		return requestFactory;
	}
}
