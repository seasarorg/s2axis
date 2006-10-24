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
package org.seasar.remoting.axis.deployment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.deployment.wsdd.WSDDConstants;
import org.apache.axis.deployment.wsdd.WSDDException;
import org.apache.axis.deployment.wsdd.WSDDService;
import org.apache.axis.deployment.wsdd.WSDDTypeMapping;
import org.apache.axis.description.JavaServiceDesc;
import org.apache.axis.encoding.AutoRegisterableTypeMappingDelegate;
import org.apache.axis.encoding.TypeMappingRegistry;
import org.apache.axis.providers.java.JavaProvider;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.message.MessageFormatter;
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis.S2AxisConstants;
import org.seasar.remoting.axis.ServiceDef;
import org.seasar.remoting.axis.TypeMappingDef;
import org.w3c.dom.Element;

/**
 * S2によりインスタンス管理されるサービスのWSDDによる表現です。
 * 
 * @author koichik
 */
public class WSDDS2Service extends WSDDService {

    // constants
    private static final long serialVersionUID = 1L;

    // static fields
    protected static final Map providerMapping = new HashMap();
    static {
        providerMapping.put(PROVIDER_RPC, S2AxisConstants.PROVIDER_S2RPC);
        providerMapping.put(PROVIDER_MSG, S2AxisConstants.PROVIDER_S2MSG);
    }

    // instance fields
    protected final ComponentDef componentDef;

    /**
     * コンポーネント定義からインスタンスを構築します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param options
     *            Axisエンジンに設定されたオプション情報
     * @throws WSDDException
     *             インスタンスの構築に失敗した場合にスローされます。
     */
    public WSDDS2Service(final ComponentDef componentDef, final Map options) throws WSDDException {
        this(componentDef, new ServiceDef(), options);
    }

    /**
     * コンポーネント定義とサービス定義からインスタンスを構築します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param serviceDef
     *            サービス定義
     * @param options
     *            Axisエンジンに設定されたオプション情報
     * @throws WSDDException
     *             インスタンスの構築に失敗した場合にスローされます。
     */
    public WSDDS2Service(final ComponentDef componentDef, final ServiceDef serviceDef,
            final Map options) throws WSDDException {
        this.componentDef = componentDef;

        final String name = getName();
        setQName(new QName(name));

        final JavaServiceDesc serviceDesc = (JavaServiceDesc) getServiceDesc();
        serviceDesc.setName(name);

        Class serviceType = getServiceClass(serviceDef);
        if (serviceType == null) {
            serviceType = componentDef.getComponentClass();
        }
        if (serviceType == null) {
            throw new WSDDException(MessageFormatter.getSimpleMessage("EAXS0006",
                    new Object[] { name }));
        }
        setParameter(JavaProvider.OPTION_CLASSNAME, serviceType.getName());

        final String typeMappingVersion = (String) options
                .get(S2AxisConstants.TYPE_MAPPING_VERSION);
        if (!StringUtil.isEmpty(typeMappingVersion)) {
            setParameter(S2AxisConstants.TYPE_MAPPING_VERSION, typeMappingVersion);
        }

        setProviderQName(new QName(WSDDConstants.URI_WSDD_JAVA, getS2Provider(serviceDef
                .getProvider())));

        final String allowedMethods = serviceDef.getAllowedMethods();
        if (!StringUtil.isEmpty(allowedMethods)) {
            setParameter(JavaProvider.OPTION_ALLOWEDMETHODS, allowedMethods);
        }

        final Iterator typeMappingDefs = serviceDef.getTypeMappings();
        while (typeMappingDefs.hasNext()) {
            final TypeMappingDef typeMappingDef = (TypeMappingDef) typeMappingDefs.next();
            addTypeMapping(createWSDDTypeMapping(typeMappingDef));
        }

        initTMR();
        validateDescriptors();

        final TypeMappingRegistry tmr = serviceDesc.getTypeMappingRegistry();
        final String[] encodings = tmr.getRegisteredEncodingStyleURIs();
        for (int i = 0; i < encodings.length; ++i) {
            tmr.register(encodings[i], new AutoRegisterableTypeMappingDelegate());
        }
    }

    /**
     * コンポーネント定義とWSDDファイル(XML)のDOMツリーからインスタンスを構築します。 <br>
     * WSDD中の <code>&lt;service&gt;</code> 要素の <code>provider</code>
     * 属性で指定されたプロバイダを対応するS2Axisのプロバイダに変更します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param serviceElement
     *            WSDDファイル(XML)のDOMツリー
     * @throws WSDDException
     *             インスタンスの構築に失敗した場合にスローされます。
     */
    public WSDDS2Service(final ComponentDef componentDef, final Element serviceElement)
            throws WSDDException {
        super(serviceElement);
        this.componentDef = componentDef;
        final String provider = getProviderQName().getLocalPart();
        setProviderQName(new QName(WSDDConstants.URI_WSDD_JAVA, getS2Provider(provider)));
    }

    /**
     * コンポーネント定義を返します。
     * 
     * @return コンポーネント定義
     */
    public ComponentDef getComponentDef() {
        return componentDef;
    }

    /**
     * サービス名を返します。 <br>
     * サービス名は、コンポーネント定義に名前空間が指定されていれば"名前空間/コンポーネント名"、そうでなければ"コンポーネント名"になります。
     * 
     * @return サービス名
     */
    protected String getName() {
        final String namespace = componentDef.getContainer().getNamespace();
        final String componentName = componentDef.getComponentName();
        if (StringUtil.isEmpty(namespace)) {
            return componentName;
        }
        return namespace + "/" + componentName;
    }

    /**
     * サービスの型を返します。 <br>
     * サービスの型は、サービス定義に型が指定されていればその型、そうでなければコンポーネントの型になります。
     * <p>
     * コンポーネント型が(インタフェースではなく)実装クラスの場合で、メソッドを定義されたインタフェースを
     * 一つだけ実装している場合には、そのインタフェースをサービスの型とします。 <br>
     * メソッドが定義されたインタフェースを複数実装している場合でかつ、サービス定義に
     * 公開するメソッドが指定されていない場合には、インタフェースに定義されたメソッドの名前をサービス定義の公開するメソッドに設定します。
     * </p>
     * 
     * @param serviceDef
     *            サービス定義
     * @return サービスの型
     */
    protected Class getServiceClass(final ServiceDef serviceDef) {
        Class serviceType = null;
        serviceType = serviceDef.getServiceType();
        if (serviceType != null) {
            return serviceType;
        }

        serviceType = componentDef.getComponentClass();
        if (serviceType == null) {
            componentDef.getComponent().getClass();
        }

        if (!serviceType.isInterface()) {
            final Class[] interfaces = serviceType.getInterfaces();
            final List interfaceList = new ArrayList();
            final StringBuffer buf = new StringBuffer(200);
            for (int i = 0; i < interfaces.length; ++i) {
                final Method[] methods = interfaces[i].getMethods();
                if (methods.length > 0) {
                    interfaceList.add(interfaces[i]);
                    for (int j = 0; j < methods.length; ++j) {
                        buf.append(methods[j].getName()).append(" ");
                    }
                }
            }

            switch (interfaceList.size()) {
            case 0:
                break;
            case 1:
                serviceType = (Class) interfaceList.get(0);
                break;
            default:
                if (serviceDef.getAllowedMethods() == null) {
                    serviceDef.setAllowedMethods(new String(buf));
                }
            }
        }
        return serviceType;
    }

    /**
     * Axisのプロバイダ名に対応するS2Axis用のプロバイダ名を返します。
     * 
     * @param provider
     *            Axis標準のプロバイダ名
     * @return S2Axis用のプロバイダ名
     * @throws WSDDException
     *             Axisのプロバイダ名対応するS2Axisのプロバイダがない場合にスローされます
     */
    protected String getS2Provider(final String provider) throws WSDDException {
        final String s2Provider = (String) providerMapping.get(provider);
        if (s2Provider == null) {
            throw new WSDDException(provider);
        }
        return s2Provider;
    }

    /**
     * WSDDタイプマッピングを作成して返します。
     * 
     * @param typeMappingDef
     *            タイプマッピング定義
     * @return WSDDタイプマッピング
     */
    protected WSDDTypeMapping createWSDDTypeMapping(final TypeMappingDef typeMappingDef) {
        final WSDDTypeMapping wsddTypeMapping = new WSDDTypeMapping();

        final Class type = typeMappingDef.getType();
        wsddTypeMapping.setLanguageSpecificType(type);
        wsddTypeMapping.setQName(typeMappingDef.getQName());
        wsddTypeMapping.setSerializer(typeMappingDef.getSerializer());
        wsddTypeMapping.setDeserializer(typeMappingDef.getDeserializer());

        wsddTypeMapping.setEncodingStyle(typeMappingDef.getEncodingStyle());

        return wsddTypeMapping;
    }

}
