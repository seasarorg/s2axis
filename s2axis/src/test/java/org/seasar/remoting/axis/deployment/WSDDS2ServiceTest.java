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
package org.seasar.remoting.axis.deployment;

import java.awt.Color;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.axis.deployment.wsdd.WSDDConstants;
import org.apache.axis.deployment.wsdd.WSDDTypeMapping;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.providers.java.JavaProvider;
import org.apache.axis.utils.XMLUtils;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.remoting.axis.ServiceDef;
import org.seasar.remoting.axis.TypeMappingDef;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author koichik
 */
public class WSDDS2ServiceTest extends S2TestCase {

    public WSDDS2ServiceTest() {
    }

    public WSDDS2ServiceTest(String name) {
        super(name);
    }

    public void setUp() {
        include("WSDDS2ServiceTest.dicon");
    }

    public void testNoInterface() throws Exception {
        ComponentDef cd = getComponentDef("no");
        WSDDS2Service s2Service = new WSDDS2Service(cd, new HashMap());

        assertEquals("1", new QName("no"), s2Service.getQName());
        assertEquals("2", "no", s2Service.getServiceDesc().getName());
        assertEquals("3", "org.seasar.remoting.axis.deployment.WSDDS2ServiceTest$NoInterface",
                s2Service.getParameter(JavaProvider.OPTION_CLASSNAME));
        assertEquals("4", new QName(WSDDConstants.URI_WSDD_JAVA, "S2RPC"), s2Service
                .getProviderQName());
        assertNull("5", s2Service.getParameter(JavaProvider.OPTION_ALLOWEDMETHODS));
    }

    public void testIgnoreInterface() throws Exception {
        ComponentDef cd = getComponentDef("ignore");
        WSDDS2Service s2Service = new WSDDS2Service(cd, new HashMap());

        assertEquals("1", new QName("ignore"), s2Service.getQName());
        assertEquals("2", "ignore", s2Service.getServiceDesc().getName());
        assertEquals("3", "org.seasar.remoting.axis.deployment.WSDDS2ServiceTest$IgnoreInterface",
                s2Service.getParameter(JavaProvider.OPTION_CLASSNAME));
        assertEquals("4", new QName(WSDDConstants.URI_WSDD_JAVA, "S2RPC"), s2Service
                .getProviderQName());
        assertNull("5", s2Service.getParameter(JavaProvider.OPTION_ALLOWEDMETHODS));
    }

    public void testOneInterface() throws Exception {
        ComponentDef cd = getComponentDef("one");
        WSDDS2Service s2Service = new WSDDS2Service(cd, new HashMap());

        assertEquals("1", new QName("one"), s2Service.getQName());
        assertEquals("2", "one", s2Service.getServiceDesc().getName());
        assertEquals("3", "java.lang.Runnable", s2Service
                .getParameter(JavaProvider.OPTION_CLASSNAME));
        assertEquals("4", new QName(WSDDConstants.URI_WSDD_JAVA, "S2RPC"), s2Service
                .getProviderQName());
        assertNull("5", s2Service.getParameter(JavaProvider.OPTION_ALLOWEDMETHODS));
    }

    public void testTwoInterface() throws Exception {
        ComponentDef cd = getComponentDef("two");
        WSDDS2Service s2Service = new WSDDS2Service(cd, new HashMap());

        assertEquals("1", new QName("two"), s2Service.getQName());
        assertEquals("2", "two", s2Service.getServiceDesc().getName());
        assertEquals("3", "org.seasar.remoting.axis.deployment.WSDDS2ServiceTest$TwoInterface",
                s2Service.getParameter(JavaProvider.OPTION_CLASSNAME));
        assertEquals("4", new QName(WSDDConstants.URI_WSDD_JAVA, "S2RPC"), s2Service
                .getProviderQName());

        Set allowMethods = new HashSet(Arrays.asList(s2Service.getParameter(
                JavaProvider.OPTION_ALLOWEDMETHODS).split(" ")));
        assertEquals("5", 3, allowMethods.size());
        assertTrue("6", allowMethods.contains("run"));
        assertTrue("7", allowMethods.contains("hasMoreElements"));
        assertTrue("8", allowMethods.contains("nextElement"));
    }

    public void testServiceType() throws Exception {
        ComponentDef cd = getComponentDef("two");
        ServiceDef sd = new ServiceDef();
        sd.setServiceType(Comparable.class);
        WSDDS2Service s2Service = new WSDDS2Service(cd, sd, new HashMap());

        assertEquals("1", "java.lang.Comparable", s2Service
                .getParameter(JavaProvider.OPTION_CLASSNAME));
        assertNull("2", s2Service.getParameter(JavaProvider.OPTION_ALLOWEDMETHODS));
    }

    public void testAlloeMethods() throws Exception {
        ComponentDef cd = getComponentDef("two");
        ServiceDef sd = new ServiceDef();
        sd.setAllowedMethods("run hasMoreElements");
        WSDDS2Service s2Service = new WSDDS2Service(cd, sd, new HashMap());

        assertEquals("1", "org.seasar.remoting.axis.deployment.WSDDS2ServiceTest$TwoInterface",
                s2Service.getParameter(JavaProvider.OPTION_CLASSNAME));

        Set allowMethods = new HashSet(Arrays.asList(s2Service.getParameter(
                JavaProvider.OPTION_ALLOWEDMETHODS).split(" ")));
        assertEquals("2", 2, allowMethods.size());
        assertTrue("3", allowMethods.contains("run"));
        assertTrue("4", allowMethods.contains("hasMoreElements"));
    }

    public void testProvider() throws Exception {
        ComponentDef cd = getComponentDef("no");
        ServiceDef sd = new ServiceDef();
        sd.setProvider("MSG");
        WSDDS2Service s2Service = new WSDDS2Service(cd, sd, new HashMap());

        assertEquals("1", new QName(WSDDConstants.URI_WSDD_JAVA, "S2MSG"), s2Service
                .getProviderQName());
    }

    public void testTypeMapping() throws Exception {
        ComponentDef cd = getComponentDef("no");
        TypeMappingDef tmd = new TypeMappingDef();
        tmd.setType(Color.class);
        tmd.setLocalPart("COLOR");
        tmd.setNamespaceURI("http://www.seasar.org/");
        tmd.setNamespacePrefix("cc");
        ServiceDef sd = new ServiceDef();
        sd.addTypeMapping(tmd);
        WSDDS2Service s2Service = new WSDDS2Service(cd, sd, new HashMap());

        Vector typeMappings = s2Service.getTypeMappings();
        assertEquals("1", 1, typeMappings.size());
        WSDDTypeMapping tm = (WSDDTypeMapping) typeMappings.get(0);
        assertEquals("2", new QName("http://www.seasar.org/", "COLOR", "cc"), tm.getQName());
        assertEquals("3", BeanSerializerFactory.class, tm.getSerializer());
        assertEquals("4", BeanDeserializerFactory.class, tm.getDeserializer());
    }

    public void testWSDD() throws Exception {
        InputStream is = ResourceUtil
                .getResourceAsStream("org/seasar/remoting/axis/deployment/WSDDS2ServiceTest.wsdd");
        Element documentElement = XMLUtils.newDocument(is).getDocumentElement();
        Element serviceElement = null;
        NodeList children = documentElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node thisNode = children.item(i);
            if (thisNode instanceof Element) {
                serviceElement = (Element) thisNode;
                break;
            }
        }

        ComponentDef cd = getComponentDef("one");
        WSDDS2Service s2Service = new WSDDS2Service(cd, serviceElement);
        assertEquals("0", new QName(WSDDConstants.URI_WSDD_JAVA, "S2RPC"), s2Service
                .getProviderQName());
    }

    public static class NoInterface {

        public void foo() {
        }
    }

    public static class IgnoreInterface implements Serializable {

        private static final long serialVersionUID = 1L;

        public void foo() {
        }
    }

    public static class OneInterface implements Runnable, Serializable {

        private static final long serialVersionUID = 1L;

        public void run() {
        }
    }

    public static class TwoInterface implements Runnable, Enumeration, Serializable {

        private static final long serialVersionUID = 1L;

        public void run() {
        }

        public boolean hasMoreElements() {
            return false;
        }

        public Object nextElement() {
            return null;
        }
    }
}
