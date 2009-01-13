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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.axis.deployment.wsdd.WSDDConstants;
import org.apache.axis.deployment.wsdd.WSDDException;
import org.apache.axis.utils.XMLUtils;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.framework.message.MessageFormatter;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.remoting.axis.DeployFailedException;
import org.seasar.remoting.axis.ServiceDef;
import org.seasar.remoting.axis.deployment.WSDDS2Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * diconファイル中に記述されたコンポーネントをサービスとしてAxisにデプロイします。
 * 
 * @author koichik
 */
public class ServiceDeployer implements ItemDeployer {

    // class fields
    private static final Logger logger = Logger.getLogger(ServiceDeployer.class);

    // instance fields
    protected final AxisDeployer deployer;

    /**
     * インスタンスを構築します。
     * 
     * @param deployer
     *            デプロイヤー
     */
    public ServiceDeployer(final AxisDeployer deployer) {
        this.deployer = deployer;
    }

    /**
     * コンポーネントをサービスとしてデプロイします。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param metaDef
     *            メタ定義
     */
    public void deploy(final ComponentDef componentDef, final MetaDef metaDef) {
        final WSDDS2Service service = createWSDDS2Service(componentDef, metaDef);
        deployer.getDeployment(componentDef.getContainer()).deployService(service);

        if (logger.isDebugEnabled()) {
            logger.log("DAXS0003", new Object[] { service.getQName() });
        }
    }

    /**
     * <code>WSDDS2Service</code> をインスタンス化して返します。 <br>
     * メタデータの指定に従い、 <code>ServiceDef</code> またはWSDDファイルから
     * <code>WSDDS2Service</code> をインスタンス化します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param metaDef
     *            メタデータ定義
     * @return <code>WSDDS2Service</code>
     */
    protected WSDDS2Service createWSDDS2Service(final ComponentDef componentDef,
            final MetaDef metaDef) {
        try {
            final Map options = deployer.getEngine().getOptions();
            final Object metadata = metaDef.getValue();
            if (metadata == null) {
                return new WSDDS2Service(componentDef, options);
            }
            else if (metadata instanceof ServiceDef) {
                return new WSDDS2Service(componentDef, (ServiceDef) metadata, options);
            }
            else if (metadata instanceof String) {
                return new WSDDS2Service(componentDef, getServiceElement((String) metadata));
            }
            throw new DeployFailedException();
        }
        catch (final WSDDException e) {
            throw new DeployFailedException(e);
        }
    }

    /**
     * WSDDファイルをクラスパスから読み込み、 <code>&lt;service&gt;</code> 要素を返します。
     * 
     * @param wsddFileName
     *            WSDDファイルのパス名
     * @return <code>&lt;service&gt;</code> 要素
     */
    protected Element getServiceElement(final String wsddFileName) {
        try {
            final InputStream is = ResourceUtil.getResourceAsStream(wsddFileName);
            final Element documentElement = XMLUtils.newDocument(is).getDocumentElement();
            final Element[] serviceElements = getChildElements(documentElement,
                    WSDDConstants.ELEM_WSDD_SERVICE);
            if (serviceElements.length != 1) {
                throw new DeployFailedException(MessageFormatter.getSimpleMessage("EAXS0005",
                        new Object[] { wsddFileName }));
            }
            return serviceElements[0];
        }
        catch (final DeployFailedException e) {
            throw e;
        }
        catch (final Exception e) {
            throw new DeployFailedException(e);
        }
    }

    /**
     * 指定されたローカル名を持つ子要素の配列を返します。
     * 
     * @param parent
     *            親要素
     * @param name
     *            子要素のローカル名
     * @return 指定されたローカル名を持つ子要素の配列。該当する子要素が存在しない場合は空の配列。
     */
    protected Element[] getChildElements(final Element parent, final String name) {
        final List result = new ArrayList();
        final NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node thisNode = children.item(i);
            if (thisNode instanceof Element) {
                final Element element = (Element) thisNode;
                if (element.getLocalName().equals(name)) {
                    result.add(element);
                }
            }
        }
        return (Element[]) result.toArray(new Element[result.size()]);
    }
}
