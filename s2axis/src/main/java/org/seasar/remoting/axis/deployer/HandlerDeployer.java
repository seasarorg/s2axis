/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.remoting.axis.deployment.WSDDS2Handler;

/**
 * diconファイル中に記述されたコンポーネントをハンドラとしてAxisにデプロイします。
 * 
 * @author koichik
 */
public class HandlerDeployer implements ItemDeployer {

    // class fields
    private static final Logger logger = Logger.getLogger(HandlerDeployer.class);

    // instance fields
    protected AxisDeployer deployer;

    /**
     * インスタンスを構築します。
     * 
     * @param deployer
     *            デプロイヤー
     */
    public HandlerDeployer(final AxisDeployer deployer) {
        this.deployer = deployer;
    }

    /**
     * コンポーネントをハンドラとしてデプロイします。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param metaDef
     *            メタ定義
     */
    public void deploy(final ComponentDef componentDef, final MetaDef metaDef) {
        final WSDDS2Handler handler = new WSDDS2Handler(componentDef);
        deployer.getDeployment(componentDef.getContainer()).deployHandler(handler);

        if (logger.isDebugEnabled()) {
            logger.log("DAXS0004", new Object[] { handler.getQName() });
        }
    }
}
