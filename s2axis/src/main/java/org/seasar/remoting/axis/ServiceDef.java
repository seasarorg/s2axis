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
 * dicon�t�@�C������Axis�T�[�r�X�̏���ݒ肷�邽�߂Ɏg���܂��B
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
     * �v���o�C�_��Ԃ��܂��B
     * 
     * @return Returns the providerType.
     */
    public String getProvider() {
        return provider;
    }

    /**
     * �v���o�C�_���w�肵�܂��B
     * 
     * @param providerType
     *            The providerType to set.
     */
    public void setProvider(final String providerType) {
        this.provider = providerType;
    }

    /**
     * �T�[�r�X�̌^��Ԃ��܂��B
     * 
     * @return Returns the serviceType.
     */
    public Class getServiceType() {
        return serviceType;
    }

    /**
     * �T�[�r�X�̌^��ݒ肵�܂��B
     * 
     * @param serviceType
     *            The serviceType to set.
     */
    public void setServiceType(final Class serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * �T�[�r�X�Ƃ��Č��J���郁�\�b�h��Ԃ��܂��B
     * 
     * @return Returns the allowedMethods.
     */
    public String getAllowedMethods() {
        return allowedMethods;
    }

    /**
     * �T�[�r�X�Ƃ��Č��J���郁�\�b�h��ݒ肵�܂��B
     * 
     * @param allowedMethods
     *            The allowedMethods to set.
     */
    public void setAllowedMethods(final String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    /**
     * �^�C�v�}�b�s���O��ǉ����܂��B
     * 
     * @param typeMappingDef
     */
    public void addTypeMapping(final TypeMappingDef typeMappingDef) {
        typeMappingDefs.add(typeMappingDef);
    }

    /**
     * �^�C�v�}�b�s���O�̃C�e���[�^��Ԃ��܂��B
     * 
     * @return Returns the TypeMappings.
     */
    public Iterator getTypeMappings() {
        return typeMappingDefs.iterator();
    }
}
