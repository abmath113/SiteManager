package com.eps.sitemanager.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/proxy") // This will be the base path for proxied requests
public class ProxyController {
 
    private final RestTemplate restTemplate;
//  private final String nodeJsTargetBaseUrl = "http://192.168.202.105:5000"; // Node.js backend URL
  private final String nodeJsTargetBaseUrl = "http://127.0.0.1:5000"; 
//    private final String nodeJsTargetBaseUrl = "http://192.168.7.42:5000";
    
    // Inject RestTemplateBuilder to build RestTemplate
    public ProxyController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
 
    @PostMapping("/query") // This matches the path after /proxy
    public ResponseEntity<String> proxyQueryToNodeJs(@RequestBody String requestBodyFromClient) {
 
        String targetUrl = nodeJsTargetBaseUrl + "/api/query";
 
        // Prepare headers for the request to Node.js backend
        // The client (React app) should send 'Content-Type: application/json'
        // We will forward this content type.
        HttpHeaders headersToNodeJs = new HttpHeaders();
        
        
        headersToNodeJs.setContentType(MediaType.APPLICATION_JSON);
        // If you needed to copy more headers from the original request:
        // originalRequest.getHeaderNames().asIterator().forEachRemaining(headerName -> {
        //     headersToNodeJs.set(headerName, originalRequest.getHeader(headerName));
        // });
 
        // Create the HTTP entity with the client's body and our headers
        HttpEntity<String> entityToNodeJs = new HttpEntity<>(requestBodyFromClient, headersToNodeJs);
 
        ResponseEntity<String> responseFromNodeJs;
        try {
            // Execute the POST request to the Node.js backend
            responseFromNodeJs = restTemplate.exchange(
                    targetUrl,
                    HttpMethod.POST,
                    entityToNodeJs,
                    String.class // We expect a String response (which will be JSON)
            );
        } catch (HttpStatusCodeException e) {
            // This catches HTTP errors from the Node.js backend (e.g., 4xx, 5xx)
            // We want to return the Node.js backend's error response directly to the client
            return ResponseEntity
                    .status(e.getStatusCode())
                    .headers(e.getResponseHeaders()) // Forward headers from Node.js error response
                    .body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            // This catches other client-side exceptions during the call to Node.js
            // (e.g., connection refused if Node.js is down)
            // Return a generic 503 Service Unavailable
            System.err.println("Error proxying request to Node.js: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\": \"The backend service is currently unavailable. Please try again later.\"}");
        }
 
        // If the call to Node.js was successful, forward its response to the client
        // Build a new ResponseEntity to explicitly control what's sent back
        HttpHeaders responseHeadersToClient = new HttpHeaders();
 
        // Copy Content-Type from Node.js response (should be application/json)
        MediaType contentTypeFromNodeJs = responseFromNodeJs.getHeaders().getContentType();
        if (contentTypeFromNodeJs != null) {
            responseHeadersToClient.setContentType(contentTypeFromNodeJs);
        }
        // You can copy other specific headers from responseFromNodeJs.getHeaders() if needed
 
        return new ResponseEntity<>(
                responseFromNodeJs.getBody(),
                responseHeadersToClient,
                responseFromNodeJs.getStatusCode()
        );
    }
}