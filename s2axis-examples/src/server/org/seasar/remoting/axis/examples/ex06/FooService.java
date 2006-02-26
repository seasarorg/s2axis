/**
 * FooService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.)
 */

package org.seasar.remoting.axis.examples.ex06;

public interface FooService extends javax.xml.rpc.Service {
    public java.lang.String getFooAddress();

    public org.seasar.remoting.axis.examples.ex06.Foo getFoo() throws javax.xml.rpc.ServiceException;

    public org.seasar.remoting.axis.examples.ex06.Foo getFoo(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
