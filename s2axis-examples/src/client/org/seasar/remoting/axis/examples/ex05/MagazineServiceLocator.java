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
package org.seasar.remoting.axis.examples.ex05;

public class MagazineServiceLocator extends org.apache.axis.client.Service implements org.seasar.remoting.axis.examples.ex05.MagazineService {

    public MagazineServiceLocator() {
    }


    public MagazineServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MagazineServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Magazineのプロキシクラスの取得に使用します / [en]-(Use to get a proxy class for Magazine)
    private java.lang.String Magazine_address = "http://localhost:8080/s2axis-examples/services/Magazine";

    public java.lang.String getMagazineAddress() {
        return Magazine_address;
    }

    // WSDDサービス名のデフォルトはポート名です / [en]-(The WSDD service name defaults to the port name.)
    private java.lang.String MagazineWSDDServiceName = "Magazine";

    public java.lang.String getMagazineWSDDServiceName() {
        return MagazineWSDDServiceName;
    }

    public void setMagazineWSDDServiceName(java.lang.String name) {
        MagazineWSDDServiceName = name;
    }

    public org.seasar.remoting.axis.examples.ex05.Magazine getMagazine() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Magazine_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMagazine(endpoint);
    }

    public org.seasar.remoting.axis.examples.ex05.Magazine getMagazine(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.seasar.remoting.axis.examples.ex05.MagazineSoapBindingStub _stub = new org.seasar.remoting.axis.examples.ex05.MagazineSoapBindingStub(portAddress, this);
            _stub.setPortName(getMagazineWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMagazineEndpointAddress(java.lang.String address) {
        Magazine_address = address;
    }

    /**
     * 与えられたインターフェースに対して、スタブの実装を取得します。 / [en]-(For the given interface, get the stub implementation.)
     * このサービスが与えられたインターフェースに対してポートを持たない場合、 / [en]-(If this service has no port for the given interface,)
     * ServiceExceptionが投げられます。 / [en]-(then ServiceException is thrown.)
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.seasar.remoting.axis.examples.ex05.Magazine.class.isAssignableFrom(serviceEndpointInterface)) {
                org.seasar.remoting.axis.examples.ex05.MagazineSoapBindingStub _stub = new org.seasar.remoting.axis.examples.ex05.MagazineSoapBindingStub(new java.net.URL(Magazine_address), this);
                _stub.setPortName(getMagazineWSDDServiceName());
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
        if ("Magazine".equals(inputPortName)) {
            return getMagazine();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.seasar.org/axis/examples/ex05", "MagazineService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.seasar.org/axis/examples/ex05", "Magazine"));
        }
        return ports.iterator();
    }

    /**
    * 指定したポート名に対するエンドポイントのアドレスをセットします / [en]-(Set the endpoint address for the specified port name.)
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Magazine".equals(portName)) {
            setMagazineEndpointAddress(address);
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
