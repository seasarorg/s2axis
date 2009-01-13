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

import javax.xml.namespace.QName;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class WSDDS2HandlerTest extends S2TestCase {

    public WSDDS2HandlerTest() {
    }

    public WSDDS2HandlerTest(String name) {
        super(name);
    }

    public void setUp() {
        include("WSDDS2HandlerTest.dicon");
    }

    public void test() {
        WSDDS2Handler handler1 = new WSDDS2Handler(getComponentDef("hoge"));
        assertEquals(new QName("hoge"), handler1.getQName());

        WSDDS2Handler handler2 = new WSDDS2Handler(getComponentDef("foo.bar"));
        assertEquals(new QName("foo.bar"), handler2.getQName());
    }
}
