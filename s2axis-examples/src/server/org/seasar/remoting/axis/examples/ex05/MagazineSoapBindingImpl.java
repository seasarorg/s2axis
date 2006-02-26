/**
 * MagazineSoapBindingImpl.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.)
 */

package org.seasar.remoting.axis.examples.ex05;

import java.util.HashSet;
import java.util.Set;

public class MagazineSoapBindingImpl implements org.seasar.remoting.axis.examples.ex05.Magazine{
    private String title;
    private Set models = new HashSet();

    public java.lang.String getTitle() throws java.rmi.RemoteException {
        return title;
    }

    public org.seasar.remoting.axis.examples.ex05.Model[] getModels() throws java.rmi.RemoteException {
        return (Model[]) models.toArray(new Model[models.size()]);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addModel(Model model) {
        models.add(model);
    }

}
