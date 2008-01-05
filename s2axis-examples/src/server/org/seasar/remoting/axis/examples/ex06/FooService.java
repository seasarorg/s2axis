/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

public interface FooService extends javax.xml.rpc.Service {
    public java.lang.String getFooAddress();

    public org.seasar.remoting.axis.examples.ex06.Foo getFoo() throws javax.xml.rpc.ServiceException;

    public org.seasar.remoting.axis.examples.ex06.Foo getFoo(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
