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
package org.seasar.remoting.axis.connector;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.encoding.DeserializerFactory;
import javax.xml.rpc.encoding.SerializerFactory;

import org.apache.axis.encoding.AutoRegisterableTypeMappingDelegate;
import org.apache.axis.encoding.TypeMapping;
import org.apache.axis.encoding.TypeMappingRegistry;
import org.apache.axis.encoding.TypeMappingRegistryImpl;
import org.apache.axis.encoding.ser.BaseDeserializerFactory;
import org.apache.axis.encoding.ser.BaseSerializerFactory;
import org.seasar.remoting.axis.S2AxisConstants;
import org.seasar.remoting.axis.TypeMappingDef;
import org.seasar.remoting.common.connector.impl.TargetSpecificURLBasedConnector;

/**
 * Webサービスを呼び出すS2Remotingコネクタの実装クラスです。
 * 
 * @author koichik
 */
public class AxisConnector extends TargetSpecificURLBasedConnector {

    // instance fields
    protected Service service;
    protected TypeMappingRegistry tmr;

    /** タイムアウト値 */
    private int timeout = 0;

    /**
     * Axisサービスを設定します。
     * 
     * @param service
     *            Axisサービス
     */
    public void setService(final Service service) {
        this.service = service;
        tmr = new TypeMappingRegistryImpl();

        final Map options = ((org.apache.axis.client.Service) service).getEngine().getOptions();
        final String typeMappingVersion = (String) options
                .get(S2AxisConstants.TYPE_MAPPING_VERSION);
        ((TypeMappingRegistryImpl) tmr).doRegisterFromVersion(typeMappingVersion);

        final String[] encodings = tmr.getRegisteredEncodingStyleURIs();
        for (int i = 0; i < encodings.length; ++i) {
            tmr.register(encodings[i], new AutoRegisterableTypeMappingDelegate());
        }
    }

    /**
     * タイムアウト時間をミリ秒単位で設定します。
     * 
     * @param timeout
     *            タイムアウト時間
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * タイプマッピング定義を追加します。
     * 
     * @param typeMappingDef
     *            タイプマッピング定義
     */
    public void addTypeMapping(final TypeMappingDef typeMappingDef) {
        final TypeMapping tm = (TypeMapping) tmr.getOrMakeTypeMapping(typeMappingDef
                .getEncodingStyle());
        final QName qName = typeMappingDef.getQName();
        final SerializerFactory ser = BaseSerializerFactory.createFactory(typeMappingDef
                .getSerializer(), typeMappingDef.getType(), qName);
        final DeserializerFactory deser = BaseDeserializerFactory.createFactory(typeMappingDef
                .getDeserializer(), typeMappingDef.getType(), qName);
        tm.register(typeMappingDef.getType(), qName, ser, deser);
    }

    /**
     * Axisサービスを使用してリモートメソッドの呼び出しを実行し、その結果を返します。
     * 
     * @param targetURL
     *            リモートオブジェクトのURL
     * @param method
     *            呼び出すメソッド
     * @param args
     *            リモートオブジェクトのメソッド呼び出しに渡される引数値を格納するオブジェクト配列
     * @return リモートオブジェクトに対するメソッド呼び出しからの戻り値
     * @throws Throwable
     *             リモートオブジェクトに対するメソッド呼び出しからスローされる例外
     */
    protected Object invoke(final URL targetURL, final Method method, final Object[] args)
            throws Throwable {
        final Call call = service.createCall();
        call.setTargetEndpointAddress(targetURL.toString());
        call.setOperationName(new QName(S2AxisConstants.OPERATION_NAMESPACE_URI, method.getName()));

        if (call instanceof org.apache.axis.client.Call) {
            org.apache.axis.client.Call axisCall = (org.apache.axis.client.Call) call;
            axisCall.getMessageContext().setTypeMappingRegistry(tmr);
            if (timeout > 0) {
                axisCall.setTimeout(new Integer(timeout));
            }
        }

        return call.invoke(args);
    }

}
