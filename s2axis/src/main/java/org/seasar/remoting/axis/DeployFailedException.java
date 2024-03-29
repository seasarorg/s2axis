/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.remoting.axis;

/**
 * Axisへのデプロイが失敗した場合にスローされます。
 * 
 * @author koichik
 */
public class DeployFailedException extends RuntimeException {

    // constants
    private static final long serialVersionUID = 1L;

    /**
     * インスタンスを構築します。
     */
    public DeployFailedException() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param message
     */
    public DeployFailedException(final String message) {
        super(message);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     */
    public DeployFailedException(final Throwable cause) {
        super(cause);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param message
     * @param cause
     */
    public DeployFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
