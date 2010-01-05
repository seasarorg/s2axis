/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import org.apache.axis.deployment.wsdd.WSDDDocument;
import org.apache.axis.utils.XMLUtils;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.framework.message.MessageFormatter;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.remoting.axis.DeployFailedException;

/**
 * diconファイル中に記述されたメタ情報で指定されたWSDDファイルをAxisにデプロイします。
 * 
 * @author koichik
 */
public class WSDDDeployer implements ItemDeployer {

    // class fields
    private static final Logger logger = Logger.getLogger(WSDDDeployer.class);

    // instance fields
    protected final AxisDeployer deployer;

    /**
     * インスタンスを構築します。
     * 
     * @param deployer
     *            デプロイヤー
     */
    public WSDDDeployer(final AxisDeployer deployer) {
        this.deployer = deployer;
    }

    /**
     * メタ情報で指定されたWSDDファイルをデプロイします。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param metaDef
     *            メタ定義
     */
    public void deploy(final ComponentDef componentDef, final MetaDef metaDef) {
        try {
            final Object value = metaDef.getValue();
            if (!(value instanceof String)) {
                throw new DeployFailedException(MessageFormatter.getSimpleMessage("EAXS0002",
                        new Object[] { value }));
            }

            final String wsddFileName = (String) value;
            final InputStream is = ResourceUtil.getResourceAsStream(wsddFileName);
            final WSDDDocument wsddDocument = new WSDDDocument(XMLUtils.newDocument(is));
            wsddDocument.deploy(deployer.getDeployment(metaDef.getContainer()));

            if (logger.isDebugEnabled()) {
                logger.log("DAXS0001", new Object[] { wsddFileName });
            }
        }
        catch (final Exception e) {
            throw new DeployFailedException(e);
        }
    }
}
