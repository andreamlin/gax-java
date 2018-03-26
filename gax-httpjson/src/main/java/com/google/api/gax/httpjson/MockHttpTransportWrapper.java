package com.google.api.gax.httpjson;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MockHttpTransportWrapper extends HttpTransportWrapper {
  private List<Objects> requestObjects = new LinkedList<>();



  public HttpRequestFactory createRequestFactory(HttpRequestInitializer initializer) {
    return getHttpTransportImpl().createRequestFactory(initializer);
  }

  public HttpRequestFactory createRequestFactory() {
    return getHttpTransportImpl().createRequestFactory();
  }
}
