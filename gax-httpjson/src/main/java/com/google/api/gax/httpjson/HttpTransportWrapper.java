package com.google.api.gax.httpjson;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.io.IOException;

/** Wrapper class around a {@link com.google.api.client.http.HttpTransport}. */
public class HttpTransportWrapper {
  private HttpTransport httpTransportImpl;

  public HttpTransportWrapper(HttpTransport transport) {
    this.httpTransportImpl = transport == null ? new NetHttpTransport() : transport;
  }

  public HttpTransportWrapper() {
    this.httpTransportImpl = new NetHttpTransport();
  }

  public HttpRequestFactory createRequestFactory(HttpRequestInitializer initializer) {
    return httpTransportImpl.createRequestFactory(initializer);
  }

  public HttpRequestFactory createRequestFactory() {
    return httpTransportImpl.createRequestFactory();
  }

  public HttpTransport getHttpTransportImpl() {
    return httpTransportImpl;
  }

  public void shutdown() throws IOException {
    httpTransportImpl.shutdown();
  }
}
