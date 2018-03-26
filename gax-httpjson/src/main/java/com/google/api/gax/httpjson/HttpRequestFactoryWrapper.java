package com.google.api.gax.httpjson;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import java.io.IOException;

public class HttpRequestFactoryWrapper {
  private HttpRequestFactory requestFactoryImpl;

  public HttpRequestFactoryWrapper(HttpRequestFactory requestFactoryImpl) {
    this.requestFactoryImpl = requestFactoryImpl;
  }

  public HttpRequestFactory getRequestFactoryImpl() {
    return requestFactoryImpl;
  }

  public HttpRequest buildRequest(String requestMethod, GenericUrl url, HttpContent content)
      throws IOException {
    return requestFactoryImpl.buildRequest(requestMethod, url, content);
  }
}
