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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.axis.AxisEngine;
import org.apache.axis.WSDDEngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.deployment.wsdd.WSDDDeployment;
import org.apache.axis.encoding.TypeMappingImpl;
import org.apache.axis.utils.JavaUtils;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.MetaDefAware;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.message.MessageFormatter;
import org.seasar.remoting.axis.DeployFailedException;
import org.seasar.remoting.axis.S2AxisConstants;
import org.seasar.remoting.common.deployer.Deployer;

/**
 * diconファイル中に記述されたコンポーネントをAxisにデプロイします。
 * 
 * @author koichik
 */
public class AxisDeployer implements Deployer {

    // class fields
    /**
     * <code>&lt;meta&gt;</code> 要素の <code>name</code>
     * 属性に指定される名前を取得するための正規表現です。 <br>
     * S2Axis-V1.0.0-RC2以降は接頭辞 <code>axis-</code> の後にローカル名が続きます。 <br>
     * S2Axis-V1.0.0-RC1以前との互換性のため、接頭辞 <code>s2-axis:</code> も使えるようにしています。
     */
    protected static final Pattern META_NAME_PATTERN = Pattern.compile("(?:s2-axis:|axis-)(.+)");

    // instance fields
    protected S2Container container;
    protected ServletContext servletContext;
    protected ItemDeployer serviceDeployer = new ServiceDeployer(this);
    protected ItemDeployer handlerDeployer = new HandlerDeployer(this);
    protected ItemDeployer wsddDeployer = new WSDDDeployer(this);
    protected ThreadLocal context = new ThreadLocal() {

        protected Object initialValue() {
            return new HashSet();
        }
    };

    /**
     * S2コンテナを設定します。
     * 
     * @param container
     *            S2コンテナ
     */
    public void setContainer(final S2Container container) {
        this.container = container.getRoot();
    }

    /**
     * サーブレットコンテキストを設定します。
     * 
     * @param servletContext
     *            サーブレットコンテキスト
     */
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * コンテナに登録されているサービスやハンドラをデプロイします。
     */
    public void deploy() {
        ((Set) context.get()).clear();
        forEach(container);

        final AxisEngine engine = getEngine(container);
        final Object dotnet = engine.getOption(AxisEngine.PROP_DOTNET_SOAPENC_FIX);
        if (JavaUtils.isTrue(dotnet)) {
            TypeMappingImpl.dotnet_soapenc_bugfix = true;
        }
    }

    /**
     * コンテナの階層をたどって全てのコンテナとコンポーネント定義を走査します。 <br>
     * 走査する順序は次の通りです。
     * <ol>
     * <li>子のコンテナを再起的に</li>
     * <li>コンテナ自身(&lt;components&gt;直下の&lt;meta&gt;要素)</li>
     * <li>コンポーネント(&lt;component&gt;の下の&lt;meta&gt;要素)</li>
     * </ol>
     * 
     * @param container
     *            起点となるコンテナ
     */
    protected void forEach(final S2Container container) {
        Set set = (Set) context.get();
        if (set.contains(container)) {
            return;
        }
        set.add(container);

        final int childContainerSize = container.getChildSize();
        for (int i = 0; i < childContainerSize; ++i) {
            forEach(container.getChild(i));
        }

        process(container);

        final int componentDefSize = container.getComponentDefSize();
        for (int i = 0; i < componentDefSize; ++i) {
            process(container.getComponentDef(i));
        }
    }

    /**
     * S2コンテナにS2Axisのメタデータ <code>&lt;meta name="axis-deploy"&gt;</code>
     * が指定されていれば、そのWSDDをAxisにデプロイします。
     * 
     * @param container
     *            S2コンテナ
     */
    protected void process(final S2Container container) {
        final MetaDef[] metaDefs = getMetaDefs(container, S2AxisConstants.META_DEPLOY);
        for (int i = 0; metaDefs != null && i < metaDefs.length; ++i) {
            wsddDeployer.deploy(null, metaDefs[i]);
        }
    }

    /**
     * コンポーネント定義にS2Axisのメタデータ <code>&lt;meta name="axis-service"&gt;</code>
     * または <code>&lt;meta name="axis-handler"&gt;</code>
     * が指定されていれば、そのコンポーネントをサービスまたはハンドラとしてAxisにデプロイします。
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    protected void process(final ComponentDef componentDef) {
        final MetaDef serviceMetaDef = getMetaDef(componentDef, S2AxisConstants.META_SERVICE);
        if (serviceMetaDef != null) {
            serviceDeployer.deploy(componentDef, serviceMetaDef);
        }

        final MetaDef handlerMetaDef = getMetaDef(componentDef, S2AxisConstants.META_HANDLER);
        if (handlerMetaDef != null) {
            handlerDeployer.deploy(componentDef, handlerMetaDef);
        }
    }

    /**
     * WSDDデプロイメントを返します。
     * 
     * @param container
     *            コンテナ
     * @return WSDDデプロイメント
     */
    protected WSDDDeployment getDeployment(final S2Container container) {
        return ((WSDDEngineConfiguration) getEngine(container).getConfig()).getDeployment();
    }

    /**
     * Axisエンジンを返します。 <br>
     * デフォルトの S2 コンテナから Axis エンジンを探して返します。
     * 
     * @return Axisエンジン
     */
    protected AxisEngine getEngine() {
        return getEngine(container);
    }

    /**
     * Axisエンジンを返します。 <br>
     * Axisエンジンは、コンテナに名前 <code>axis-engine</code> を持つ
     * <code>&lt;meta&gt;</code> 要素が指定されていれば、その内容文字列から次のように決定されます。
     * <dl>
     * <dt>未定義の場合</dt>
     * <dd><code>"default"</code> が指定されたものとしてAxisエンジンを決定します。</dd>
     * <dt><code>"default"</code></dt>
     * <dd>コンテナにサーブレットコンテキストが設定されていれば <code>"default-server"</code> 、そうでなければ
     * <code>"default-client"</code> が指定されたものとしてAxisエンジンを決定します。</dd>
     * <dt><code>"default-client"</code></dt>
     * <dd>コンテナから <code>javax.xml.rpc.Service</code>
     * を実装したコンポーネントを取得し、そのエンジンを使用します。</dd>
     * <dt><code>"default-server"</code></dt>
     * <dd>サーブレットコンテキストに設定されているAxisエンジンを使用します。 <br>
     * 最初に {@link S2AxisConstants#AXIS_SERVLET}と
     * {@link S2AxisConstants#ATTR_AXIS_ENGINE}を連結した文字列をキーとして
     * サーブレットコンテキストからAxisエンジンを取得します。 <br>
     * 見つからなかった場合は{S2AxisConstants#ATTR_AXIS_ENGINE}を
     * キーとしてサーブレットコンテキストから取得したAxisエンジンを取得します。 <br>
     * </dd>
     * <dt><code>"servlet:"</code> で始まる文字列</dt>
     * <dd><code>"servlet:"</code> の後ろの文字列をキーとしてサーブレットコンテキストから
     * 取得したAxisエンジンを使用します。
     * <dd>
     * <dt><code>"s2:"</code> で始まる文字列</dt>
     * <dd><code>"s2:"</code> の後ろの文字列をキーとしてS2コンテナから
     * 取得したコンポーネントをAxisエンジンを使用します。</dd>
     * <dt>その他</dt>
     * <dd>キーとしてS2コンテナから取得したコンポーネントをAxisエンジンとして使用します。</dd>
     * </dl>
     * 
     * @param container
     *            コンテナ
     * @return Axisエンジン
     */
    protected AxisEngine getEngine(final S2Container container) {
        String engineName = S2AxisConstants.ENGINE_DEFAULT;

        final MetaDef metadata = getMetaDef(container, S2AxisConstants.META_ENGINE);
        if (metadata != null) {
            engineName = (String) metadata.getValue();
        }

        if (S2AxisConstants.ENGINE_DEFAULT.equals(engineName)) {
            if (servletContext == null) {
                engineName = S2AxisConstants.ENGINE_DEFAULT_CLIENT;
            }
            else {
                engineName = S2AxisConstants.ENGINE_DEFAULT_SERVER;
            }
        }

        AxisEngine engine = null;
        if (S2AxisConstants.ENGINE_DEFAULT_CLIENT.equals(engineName)) {
            final Service service = (Service) container.getComponent(javax.xml.rpc.Service.class);
            engine = service.getEngine();
        }
        else if (S2AxisConstants.ENGINE_DEFAULT_SERVER.equals(engineName)) {
            engine = (AxisEngine) servletContext.getAttribute(S2AxisConstants.AXIS_SERVLET
                    + S2AxisConstants.ATTR_AXIS_ENGINE);
            if (engine == null) {
                engine = (AxisEngine) servletContext.getAttribute(S2AxisConstants.ATTR_AXIS_ENGINE);
            }
        }
        else if (engineName.startsWith(S2AxisConstants.ENGINE_FROM_SERVLET)) {
            final String servletName = engineName.substring(S2AxisConstants.ENGINE_FROM_SERVLET
                    .length());
            engine = (AxisEngine) servletContext.getAttribute(servletName
                    + S2AxisConstants.ATTR_AXIS_ENGINE);
        }
        else if (engineName.startsWith(S2AxisConstants.ENGINE_FROM_S2CONTAINER)) {
            final String componentName = engineName
                    .substring(S2AxisConstants.ENGINE_FROM_S2CONTAINER.length());
            engine = (AxisEngine) container.getComponent(componentName);
        }
        else {
            engine = (AxisEngine) container.getComponent(engineName);
        }

        if (engine == null) {
            throw new DeployFailedException(MessageFormatter.getSimpleMessage("EAXS0007", null));
        }
        return engine;
    }

    /**
     * <code>S2Container</code> または <code>ComponentDef</code> が名前
     * <code>"axis-<var>localName</var></code> の <code>&lt;meta&gt;</code>
     * 要素を持っていれば、その <code>MetaDef</code> を返します。 <br>
     * <code>S2Container</code> または <code>ComponentDef</code> に該当する
     * メタデータが複数定義されている場合は最初に見つかったメタデータを返します。
     * 
     * @param metaDefSupport
     *            <code>S2Container</code> または <code>ComponentDef</code>
     * @param localName
     *            接頭辞 <code>axis-</code> に続くメタデータの名前
     * @return 指定された名前を持つ <code>MetaDef</code> 。存在しない場合は <code>null</code>
     */
    protected MetaDef getMetaDef(final MetaDefAware metaDefSupport, final String localName) {
        if (metaDefSupport == null) {
            return null;
        }
        for (int i = 0; i < metaDefSupport.getMetaDefSize(); ++i) {
            final MetaDef metaDef = metaDefSupport.getMetaDef(i);
            if (localName.equals(getLocalName(metaDef))) {
                return metaDef;
            }
        }
        return null;
    }

    /**
     * <code>S2Container</code> または <code>ComponentDef</code> が名前
     * <code>"axis-<var>localName</var></code> の <code>&lt;meta&gt;</code>
     * 要素を持っていれば、その <code>MetaDef</code> を全て返します。 <br>
     * 
     * @param metaDefSupport
     *            <code>S2Container</code> または <code>ComponentDef</code>
     * @param localName
     *            接頭辞 <code>axis-</code> に続くメタデータの名前
     * @return 指定された名前を持つ <code>MetaDef</code> の配列
     */
    protected MetaDef[] getMetaDefs(final MetaDefAware metaDefSupport, final String localName) {
        final List result = new ArrayList();
        for (int i = 0; i < metaDefSupport.getMetaDefSize(); ++i) {
            final MetaDef metaDef = metaDefSupport.getMetaDef(i);
            if (localName.equals(getLocalName(metaDef))) {
                result.add(metaDef);
            }
        }
        return (MetaDef[]) result.toArray(new MetaDef[result.size()]);
    }

    /**
     * メタデータの名前がS2Axisで使用する接頭辞で始まっていれば、接頭辞の後ろのローカル名を返します。
     * 
     * @param metaDef
     *            メタデータ定義
     * @return ローカル名
     */
    protected String getLocalName(final MetaDef metaDef) {
        final Matcher matcher = META_NAME_PATTERN.matcher(metaDef.getName());
        return matcher.matches() ? matcher.group(1) : null;
    }
}
