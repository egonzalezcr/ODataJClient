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
package com.msopentech.odatajclient.engine.types;

import javax.ws.rs.core.MediaType;

/**
 * Exchanged data format.
 */
public enum ODataValueFormat {

    /**
     * Application octet stream.
     */
    STREAM(MediaType.APPLICATION_OCTET_STREAM),
    /**
     * Plain text format.
     */
    TEXT(MediaType.TEXT_PLAIN);

    private final String format;

    ODataValueFormat(final String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return format;
    }
}
