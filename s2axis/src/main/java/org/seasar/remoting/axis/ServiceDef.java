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
package org.seasar.remoting.axis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.deployment.wsdd.WSDDConstants;

/**
 * diconファイル中でAxisサービスの情報を設定するために使われます。
 * 
 * @author koichik
 */
public class ServiceDef {

    // instance fields
    protected String provider = WSDDConstants.PROVIDER_RPC;
    protected Class serviceType;
    protected String allowedMethods;
    protected final List typeMappingDefs = new ArrayList();

    /**
     * プロバイダを返します。
     * 
     * @return Returns the providerType.
     */
    public String getProvider() {
        return provider;
    }

    /**
     * プロバイダを指定します。
     * 
     * @param providerType
     *            The providerType to set.
     */
    public void setProvider(final String providerType) {
        this.provider = providerType;
    }

    /**
     * サービスの型を返します。
     * 
     * @return Returns the serviceType.
     */
    public Class getServiceType() {
        return serviceType;
    }

    /**
     * サービスの型を設定します。
     * 
     * @param serviceType
     *            The serviceType to set.
     */
    public void setServiceType(final Class serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * サービスとして公開するメソッドを返します。
     * 
     * @return Returns the allowedMethods.
     */
    public String getAllowedMethods() {
        return allowedMethods;
    }

    /**
     * サービスとして公開するメソッドを設定します。
     * 
     * @param allowedMethods
     *            The allowedMethods to set.
     */
    public void setAllowedMethods(final String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    /**
     * タイプマッピングを追加します。
     * 
     * @param typeMappingDef
     */
    public void addTypeMapping(final TypeMappingDef typeMappingDef) {
        typeMappingDefs.add(typeMappingDef);
    }

    /**
     * タイプマッピングのイテレータを返します。
     * 
     * @return Returns the TypeMappings.
     */
    public Iterator getTypeMappings() {
        return typeMappingDefs.iterator();
    }
}
