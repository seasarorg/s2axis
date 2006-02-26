/**
 * FooServiceLocator.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.)
 */

package org.seasar.remoting.axis.examples.ex06;

public class FooServiceLocator extends org.apache.axis.client.Service implements org.seasar.remoting.axis.examples.ex06.FooService {

    public FooServiceLocator() {
    }


    public FooServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FooServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Fooのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for Foo)
    private java.lang.String Foo_address = "http://localhost:8080/s2axis-examples/services/Foo";

    public java.lang.String getFooAddress() {
        return Foo_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String FooWSDDServiceName = "Foo";

    public java.lang.String getFooWSDDServiceName() {
        return FooWSDDServiceName;
    }

    public void setFooWSDDServiceName(java.lang.String name) {
        FooWSDDServiceName = name;
    }

    public org.seasar.remoting.axis.examples.ex06.Foo getFoo() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Foo_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFoo(endpoint);
    }

    public org.seasar.remoting.axis.examples.ex06.Foo getFoo(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.seasar.remoting.axis.examples.ex06.FooSoapBindingStub _stub = new org.seasar.remoting.axis.examples.ex06.FooSoapBindingStub(portAddress, this);
            _stub.setPortName(getFooWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFooEndpointAddress(java.lang.String address) {
        Foo_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.seasar.remoting.axis.examples.ex06.Foo.class.isAssignableFrom(serviceEndpointInterface)) {
                org.seasar.remoting.axis.examples.ex06.FooSoapBindingStub _stub = new org.seasar.remoting.axis.examples.ex06.FooSoapBindingStub(new java.net.URL(Foo_address), this);
                _stub.setPortName(getFooWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("インターフェースに対するスタブの実装がありません: / [en]-(There is no stub implementation for the interface:)  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("Foo".equals(inputPortName)) {
            return getFoo();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.seasar.org/axis/examples/ex06", "FooService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.seasar.org/axis/examples/ex06", "Foo"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Foo".equals(portName)) {
            setFooEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" 未知のポートに対してはエンドポイントのアドレスをセットできません / [en]-(Cannot set Endpoint Address for Unknown Port)" + portName);
        }
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
