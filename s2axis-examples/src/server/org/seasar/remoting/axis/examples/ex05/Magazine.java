/**
 * Magazine.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.)
 */

package org.seasar.remoting.axis.examples.ex05;

public interface Magazine extends java.rmi.Remote {
    public java.lang.String getTitle() throws java.rmi.RemoteException;
    public org.seasar.remoting.axis.examples.ex05.Model[] getModels() throws java.rmi.RemoteException;
}
