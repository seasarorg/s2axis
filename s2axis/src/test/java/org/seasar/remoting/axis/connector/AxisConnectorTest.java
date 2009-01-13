/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import java.util.Map;

import javax.xml.rpc.encoding.DeserializerFactory;
import javax.xml.rpc.encoding.SerializerFactory;
import javax.xml.rpc.encoding.TypeMapping;

import junit.framework.TestCase;

import org.apache.axis.client.Service;
import org.apache.axis.constants.Use;
import org.apache.axis.encoding.AutoRegisterableTypeMappingDelegate;
import org.apache.axis.encoding.TypeMappingDelegate;
import org.apache.axis.encoding.TypeMappingImpl;
import org.apache.axis.encoding.ser.MapDeserializerFactory;
import org.apache.axis.encoding.ser.MapSerializerFactory;
import org.seasar.remoting.axis.TypeMappingDef;

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

        TypeMappingDelegate delegate = (TypeMappingDelegate) connector.tmr
                .getTypeMapping(Use.DEFAULT.getEncoding());
        assertTrue(delegate instanceof AutoRegisterableTypeMappingDelegate);

        Field f = TypeMappingDelegate.class.getDeclaredField("delegate");
        f.setAccessible(true);
        Object tm = f.get(delegate);
        assertTrue(tm instanceof TypeMappingImpl);

        f = TypeMappingImpl.class.getDeclaredField("doAutoTypes");
        f.setAccessible(true);
        Boolean doAtuoTypes = (Boolean) f.get(tm);
        assertTrue(doAtuoTypes.booleanValue());

        delegate = delegate.getNext();
    }

    public void testAddTypeMapping() throws Exception {
        Service service = new Service();

        AxisConnector connector = new AxisConnector();
        connector.setService(service);

        TypeMappingDef tmd = new TypeMappingDef();
        tmd.setType(Map.class);
        tmd.setSerializer(MapSerializerFactory.class);
        tmd.setDeserializer(MapDeserializerFactory.class);

        connector.addTypeMapping(tmd);

        TypeMapping tm = connector.tmr.getTypeMapping(tmd.getEncodingStyle());
        SerializerFactory ser = tm.getSerializer(Map.class, tmd.getQName());
        assertNotNull(ser);
        assertTrue(ser instanceof MapSerializerFactory);
        DeserializerFactory deser = tm.getDeserializer(Map.class, tmd.getQName());
        assertNotNull(deser);
        assertTrue(deser instanceof MapDeserializerFactory);
    }
}
