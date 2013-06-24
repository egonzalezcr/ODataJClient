/*
 * Copyright 2013 MS OpenTech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msopentech.odatajclient.engine.communication.request.cud;

import com.msopentech.odatajclient.engine.communication.request.ODataRequestImpl;
import com.msopentech.odatajclient.engine.communication.request.ODataStreamer;
import com.msopentech.odatajclient.engine.communication.request.ODataStreamingManagement;
import com.msopentech.odatajclient.engine.communication.request.batch.ODataBatchRequest;
import com.msopentech.odatajclient.engine.communication.response.ODataResponse;
import com.msopentech.odatajclient.engine.types.ODataFormat;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Streamed OData request abstract class.
 *
 * @param <V> OData response type corresponding to the request implementation.
 * @param <T> OData request payload type corresponding to the request implementation.
 */
public abstract class ODataStreamedRequestImpl<V extends ODataResponse, T extends ODataStreamingManagement<V>>
        extends ODataRequestImpl
        implements ODataStreamedRequest<V, T> {

    /**
     * OData request payload.
     */
    protected ODataStreamingManagement<V> payload = null;

    private ODataFormat format;

    /**
     * Constructor.
     *
     * @param method OData request HTTP method.
     * @param uri OData request URI.
     */
    public ODataStreamedRequestImpl(final Method method, final URI uri) {
        super(method, uri);
    }

    /**
     * Gets OData request payload management object.
     *
     * @return OData request payload management object.
     */
    protected abstract T getPayload();

    /**
     * {@inheritDoc}
     */
    @Override
    public ODataFormat getFormat() {
        return format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFormat(final ODataFormat format) {
        this.format = format;
        setAccept(format.toString());
        setContentType(format.toString());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public T execute() {
        // execute the request

        // return the payload object
        return getPayload();
    }

    /**
     * Writes (and consume) the request onto the given batch stream.
     * <p>
     * Please note that this method will consume the request (execution won't be possible anymore).
     *
     * @param req destination batch request.
     */
    public void batch(final ODataBatchRequest req) {
        final InputStream is = getPayload().getBody();

        try {

            // finalize the body
            getPayload().finalizeBody();

            req.rowAppend(toByteArray());
            req.rowAppend(ODataStreamer.CRLF);

            final byte[] buff = new byte[1024];

            int len;

            while ((len = is.read(buff)) >= 0) {
                req.rowAppend(buff, 0, len);
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
