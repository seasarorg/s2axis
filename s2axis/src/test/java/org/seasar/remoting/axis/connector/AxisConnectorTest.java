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
package org.seasar.remoting.axis.connector;

import java.lang.reflect.Field;

import javax.xml.rpc.encoding.TypeMapping;

import junit.framework.TestCase;

import org.apache.axis.client.Service;
import org.apache.axis.constants.Use;
import org.apache.axis.encoding.TypeMappingDelegate;
import org.apache.axis.encoding.TypeMappingImpl;

/**
 * @author koichik
 */
public class AxisConnectorTest extends TestCase {

    public AxisConnectorTest() {
    }

    public AxisConnectorTest(String name) {
        super(name);
    }

    public void testSetService() throws Exception {
        Service service = new Service();

        AxisConnector connector = new AxisConnector();
        connector.setService(service);

        TypeMapping delegate = service.getTypeMappingRegistry().getTypeMapping(
                Use.DEFAULT.getEncoding());
        assertTrue("1", delegate instanceof TypeMappingDelegate);

        Field f = TypeMappingDelegate.class.getDeclaredField("delegate");
        f.setAccessible(true);
        Object tm = f.get(delegate);
        assertTrue("2", tm instanceof TypeMappingImpl);

        f = TypeMappingImpl.class.getDeclaredField("doAutoTypes");
        f.setAccessible(true);
        Boolean doAtuoTypes = (Boolean) f.get(tm);
        assertTrue("3", doAtuoTypes.booleanValue());
    }
}
