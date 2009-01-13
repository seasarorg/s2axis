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

import org.apache.axis.ConfigurationException;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.deployment.wsdd.WSDDHandler;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.util.StringUtil;

/**
 * S2によりインスタンス管理されるハンドラのWSDDによる表現です。
 * 
 * @author koichik
 */
public class WSDDS2Handler extends WSDDHandler {

    // constants
    private static final long serialVersionUID = 1L;

    // instance fields
    protected ComponentDef componentDef;

    /**
     * コンポーネント定義からインスタンスを構築します。
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    public WSDDS2Handler(final ComponentDef componentDef) {
        this.componentDef = componentDef;

        final String namespace = componentDef.getContainer().getNamespace();
        final String componentName = componentDef.getComponentName();
        if (StringUtil.isEmpty(namespace)) {
            qname = new QName(componentName);
        }
        else {
            qname = new QName(namespace + ContainerConstants.NS_SEP + componentName);
        }
    }

    /**
     * ハンドラのインスタンスを返します。
     * 
     * @param registry
     *            レジストリ
     * @return ハンドラ
     */
    protected Handler makeNewInstance(final EngineConfiguration registry)
            throws ConfigurationException {
        return (Handler) componentDef.getComponent();
    }
}
