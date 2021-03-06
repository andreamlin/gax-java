/*
 * Copyright 2017 Google LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google LLC nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.google.api.gax.rpc.testing;

import com.google.api.core.InternalApi;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.TransportChannel;
import com.google.auth.Credentials;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.threeten.bp.Duration;

@InternalApi("for testing")
public class FakeCallContext implements ApiCallContext {
  private final Credentials credentials;
  private final FakeChannel channel;
  private final Duration timeout;
  private final Duration streamWaitTimeout;
  private final Duration streamIdleTimeout;

  private FakeCallContext(
      Credentials credentials,
      FakeChannel channel,
      Duration timeout,
      Duration streamWaitTimeout,
      Duration streamIdleTimeout) {
    this.credentials = credentials;
    this.channel = channel;
    this.timeout = timeout;
    this.streamWaitTimeout = streamWaitTimeout;
    this.streamIdleTimeout = streamIdleTimeout;
  }

  public static FakeCallContext createDefault() {
    return new FakeCallContext(null, null, null, null, null);
  }

  @Override
  public FakeCallContext nullToSelf(ApiCallContext inputContext) {
    FakeCallContext fakeCallContext;
    if (inputContext == null) {
      fakeCallContext = this;
    } else {
      if (!(inputContext instanceof FakeCallContext)) {
        throw new IllegalArgumentException(
            "context must be an instance of FakeCallContext, but found "
                + inputContext.getClass().getName());
      }
      fakeCallContext = (FakeCallContext) inputContext;
    }
    return fakeCallContext;
  }

  @Override
  public ApiCallContext merge(ApiCallContext inputCallContext) {
    if (inputCallContext == null) {
      return this;
    }
    if (!(inputCallContext instanceof FakeCallContext)) {
      throw new IllegalArgumentException(
          "context must be an instance of FakeCallContext, but found "
              + inputCallContext.getClass().getName());
    }
    FakeCallContext fakeCallContext = (FakeCallContext) inputCallContext;

    FakeChannel newChannel = fakeCallContext.channel;
    if (newChannel == null) {
      newChannel = channel;
    }

    Credentials newCallCredentials = fakeCallContext.credentials;
    if (newCallCredentials == null) {
      newCallCredentials = credentials;
    }

    Duration newTimeout = fakeCallContext.timeout;
    if (newTimeout == null) {
      newTimeout = timeout;
    }

    Duration newStreamWaitTimeout = fakeCallContext.streamWaitTimeout;
    if (newStreamWaitTimeout == null) {
      newStreamWaitTimeout = streamWaitTimeout;
    }

    Duration newStreamIdleTimeout = fakeCallContext.streamIdleTimeout;
    if (newStreamIdleTimeout == null) {
      newStreamIdleTimeout = streamIdleTimeout;
    }

    return new FakeCallContext(
        newCallCredentials, newChannel, newTimeout, newStreamWaitTimeout, newStreamIdleTimeout);
  }

  public Credentials getCredentials() {
    return credentials;
  }

  public FakeChannel getChannel() {
    return channel;
  }

  public Duration getTimeout() {
    return timeout;
  }

  @Nullable
  @Override
  public Duration getStreamWaitTimeout() {
    return streamWaitTimeout;
  }

  @Nullable
  @Override
  public Duration getStreamIdleTimeout() {
    return streamIdleTimeout;
  }

  @Override
  public FakeCallContext withCredentials(Credentials credentials) {
    return new FakeCallContext(
        credentials, this.channel, this.timeout, this.streamWaitTimeout, this.streamIdleTimeout);
  }

  @Override
  public FakeCallContext withTransportChannel(TransportChannel inputChannel) {
    Preconditions.checkNotNull(inputChannel);
    if (!(inputChannel instanceof FakeTransportChannel)) {
      throw new IllegalArgumentException(
          "Expected FakeTransportChannel, got " + inputChannel.getClass().getName());
    }
    FakeTransportChannel transportChannel = (FakeTransportChannel) inputChannel;
    return withChannel(transportChannel.getChannel());
  }

  public FakeCallContext withChannel(FakeChannel channel) {
    return new FakeCallContext(
        this.credentials, channel, this.timeout, this.streamWaitTimeout, this.streamIdleTimeout);
  }

  @Override
  public FakeCallContext withTimeout(Duration timeout) {
    return new FakeCallContext(
        this.credentials, this.channel, timeout, this.streamWaitTimeout, this.streamIdleTimeout);
  }

  @Override
  public ApiCallContext withStreamWaitTimeout(@Nonnull Duration streamWaitTimeout) {
    Preconditions.checkNotNull(streamWaitTimeout);
    return new FakeCallContext(
        this.credentials, this.channel, this.timeout, streamWaitTimeout, this.streamIdleTimeout);
  }

  @Override
  public ApiCallContext withStreamIdleTimeout(@Nonnull Duration streamIdleTimeout) {
    Preconditions.checkNotNull(streamIdleTimeout);
    return new FakeCallContext(
        this.credentials, this.channel, this.timeout, this.streamWaitTimeout, streamIdleTimeout);
  }

  public static FakeCallContext create(ClientContext clientContext) {
    return FakeCallContext.createDefault()
        .withTransportChannel(clientContext.getTransportChannel())
        .withCredentials(clientContext.getCredentials());
  }
}
