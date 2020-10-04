/*
 * Copyright 2020 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.core;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.reactivex.Flowable;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.generated.RemoteCall1;

public class Request<S, T extends Response<?>> {
    private static final AtomicLong nextId = new AtomicLong(0);

    private String jsonrpc = "2.0";
    private String method;
    private List<S> params;
    private long id;

    private Web3jService web3jService;

    // Unfortunately require an instance of the type too, see
    // http://stackoverflow.com/a/3437930/3211687
    private Class<T> responseType;

    public Request() {}

    public Request(
            final String method,
            final List<S> params,
            final Web3jService web3jService,
            final Class<T> type) {
        this.method = method;
        this.params = params;
        this.id = nextId.getAndIncrement();
        this.web3jService = web3jService;
        this.responseType = type;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(final String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public List<S> getParams() {
        return params;
    }

    public void setParams(final List<S> params) {
        this.params = params;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @JsonIgnore
    public Class<T> getResponseType() {
        return responseType;
    }

    public T send() throws IOException {
        return web3jService.send(this, responseType);
    }

    public CompletableFuture<T> sendAsync() {
        return web3jService.sendAsync(this, responseType);
    }

    public Flowable<T> flowable() {
        // FIXME
        return new RemoteCall1<T>(null, null, true, null, null).toFlowable();
    }
}
