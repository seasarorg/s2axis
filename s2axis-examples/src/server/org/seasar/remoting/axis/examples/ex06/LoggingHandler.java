/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis.examples.ex06;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author koichik
 */
public class LoggingHandler extends BasicHandler {
    private static final long serialVersionUID = 1L;
    private static final Log logger = LogFactory.getLog(LoggingHandler.class);

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void invoke(MessageContext context) throws AxisFault {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}
