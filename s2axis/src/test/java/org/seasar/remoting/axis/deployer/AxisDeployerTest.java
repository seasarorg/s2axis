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
package org.seasar.remoting.axis.deployer;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.MetaDefImpl;

/**
 * @author koichik
 */
public class AxisDeployerTest extends S2TestCase {

    private S2Container container;
    private int containerCount;
    private int componentDefCount;
    private List serviceComponents = new ArrayList();
    private List handlerComponents = new ArrayList();
    private List wsddFileNames = new ArrayList();

    public AxisDeployerTest() {
    }

    public AxisDeployerTest(String name) {
        super(name);
    }

    public void setUp() {
        containerCount = 0;
        componentDefCount = 0;
        serviceComponents.clear();
        handlerComponents.clear();
        wsddFileNames.clear();
    }

    public void testForEach0() {
        AxisDeployer deployer = new DeployCounter();
        deployer.forEach(container);
        assertEquals(1, containerCount);
        assertEquals(0, componentDefCount);
    }

    public void testForEach1() {
        include("AxisDeployerTest.forEach1.dicon");
        AxisDeployer deployer = new DeployCounter();
        deployer.forEach(container);
        assertEquals(2, containerCount);
        assertEquals(1, componentDefCount);
    }

    public void testForEach2() {
        include("AxisDeployerTest.forEach1.dicon");
        include("AxisDeployerTest.forEach2.dicon");
        AxisDeployer deployer = new DeployCounter();
        deployer.forEach(container);
        assertEquals(4, containerCount);
        assertEquals(4, componentDefCount);
    }

    public void testForEach3() {
        include("AxisDeployerTest.forEach1.dicon");
        include("AxisDeployerTest.forEach2.dicon");
        include("AxisDeployerTest.forEach3.dicon");
        AxisDeployer deployer = new DeployCounter();
        deployer.forEach(container);
        assertEquals(4, containerCount);
        assertEquals(4, componentDefCount);
    }

    public void testProcessContainer0() {
        createTestDeployer().forEach(container);
        assertEquals(0, wsddFileNames.size());
    }

    public void testProcessContainer1() {
        include("AxisDeployerTest.processContainer1.dicon");
        createTestDeployer().forEach(container);
        assertEquals(2, wsddFileNames.size());
        assertEquals("foo.wsdd", wsddFileNames.get(0));
        assertEquals("bar.wsdd", wsddFileNames.get(1));
    }

    public void testProcessComponent0() {
        createTestDeployer().forEach(container);
        assertEquals(0, serviceComponents.size());
        assertEquals(0, handlerComponents.size());
    }

    public void testProcessComponent1() {
        include("AxisDeployerTest.processComponent1.dicon");
        createTestDeployer().forEach(container);
        assertEquals(2, serviceComponents.size());
        assertEquals(1, handlerComponents.size());
    }

    public void testGetLocalName() {
        AxisDeployer deployer = new AxisDeployer();
        assertEquals("1", "service", deployer.getLocalName(new MetaDefImpl("axis-service")));
        assertEquals("2", "handler", deployer.getLocalName(new MetaDefImpl("s2-axis:handler")));
        assertNull("3", deployer.getLocalName(new MetaDefImpl("axis-")));
        assertNull("4", deployer.getLocalName(new MetaDefImpl("s2-axis:")));
        assertNull("5", deployer.getLocalName(new MetaDefImpl("hoge")));
    }

    private class DeployCounter extends AxisDeployer {

        public void process(S2Container container) {
            ++containerCount;
        }

        public void process(ComponentDef compoenentDef) {
            ++componentDefCount;
        }
    };

    private AxisDeployer createTestDeployer() {
        AxisDeployer deployer = new AxisDeployer();
        deployer.serviceDeployer = new ItemDeployer() {

            public void deploy(ComponentDef componentDef, MetaDef metaDef) {
                serviceComponents.add(componentDef);
            }
        };
        deployer.handlerDeployer = new ItemDeployer() {

            public void deploy(ComponentDef componentDef, MetaDef metaDef) {
                handlerComponents.add(componentDef);
            }
        };
        deployer.wsddDeployer = new ItemDeployer() {

            public void deploy(ComponentDef componentDef, MetaDef metaDef) {
                wsddFileNames.add(metaDef.getValue());
            }
        };
        return deployer;
    }
}
