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
package org.seasar.remoting.axis.deployer;

import javax.xml.namespace.QName;

import org.apache.axis.client.Service;
import org.apache.axis.providers.java.JavaProvider;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.remoting.axis.DeployFailedException;
import org.seasar.remoting.axis.deployment.WSDDS2Service;
import org.w3c.dom.Element;

/**
 * @author koichik
 */
public class ServiceDeployerTest extends S2TestCase {

    public ServiceDeployerTest() {
    }

    public ServiceDeployerTest(String name) {
        super(name);
    }

    public void setUpCreateWSDDS2Service() {
        include("ServiceDeployerTest.createWSDDS2Service.dicon");
        register(new Service());
    }

    public void testCreateWSDDS2Service() {
        AxisDeployer deployer = new AxisDeployer();
        deployer.container = getContainer();
        ServiceDeployer serviceDeployer = (ServiceDeployer) deployer.serviceDeployer;

        ComponentDef def1 = getComponentDef("null");
        MetaDef meta1 = def1.getMetaDef("axis-service");
        WSDDS2Service service1 = serviceDeployer.createWSDDS2Service(def1, meta1);
        assertNotNull(service1);
        assertEquals(new QName("null"), service1.getQName());

        ComponentDef def2 = getComponentDef("serviceDef");
        MetaDef meta2 = def2.getMetaDef("axis-service");
        WSDDS2Service service2 = serviceDeployer.createWSDDS2Service(def2, meta2);
        assertNotNull(service2);
        assertEquals(new QName("serviceDef"), service2.getQName());
        assertEquals("java.lang.Boolean", service2.getParameter(JavaProvider.OPTION_CLASSNAME));

        ComponentDef def3 = getComponentDef("wsdd");
        MetaDef meta3 = def3.getMetaDef("axis-service");
        WSDDS2Service service3 = serviceDeployer.createWSDDS2Service(def3, meta3);
        assertNotNull(service3);
        assertEquals(new QName("FromWSDD"), service3.getQName());
        assertEquals("java.lang.Double", service3.getParameter(JavaProvider.OPTION_CLASSNAME));

        try {
            ComponentDef def4 = getComponentDef("int");
            MetaDef meta4 = def4.getMetaDef("s2-axis:service");
            serviceDeployer.createWSDDS2Service(def4, meta4);
            fail();
        }
        catch (DeployFailedException expected) {
        }
    }

    public void testGetServiceElement() {
        AxisDeployer deployer = new AxisDeployer();
        ServiceDeployer serviceDeployer = (ServiceDeployer) deployer.serviceDeployer;

        Element e1 = serviceDeployer
                .getServiceElement("org/seasar/remoting/axis/deployer/ServiceDeployerTest.getServiceElement1.wsdd");
        assertNotNull(e1);
        assertEquals("service", e1.getNodeName());
        assertEquals("one", e1.getAttribute("name"));

        try {
            serviceDeployer
                    .getServiceElement("org/seasar/remoting/axis/deployer/ServiceDeployerTest.getServiceElement0.wsdd");
            fail();
        }
        catch (DeployFailedException expected) {
        }

        try {
            serviceDeployer
                    .getServiceElement("org/seasar/remoting/axis/deployer/ServiceDeployerTest.getServiceElement2.wsdd");
            fail();
        }
        catch (DeployFailedException expected) {
        }
    }
}
