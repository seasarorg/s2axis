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
package org.seasar.remoting.axis.deployment.provider;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.deployment.wsdd.WSDDProvider;
import org.apache.axis.deployment.wsdd.WSDDService;
import org.seasar.remoting.axis.S2AxisConstants;
import org.seasar.remoting.axis.deployment.WSDDS2Service;
import org.seasar.remoting.axis.providers.S2RPCProvider;

/**
 * S2�R���e�i����RPC�^�̃T�[�r�X�������擾����v���o�C�_�̃t�@�N�g���ł��B <br>
 * ���̃N���X�́A <code>s2-axis.jar</code> �t�@�C������
 * <code>META-INF/services/org.apache.axis.deployment.wsdd.Provider</code>
 * �ɋL�q����邱�Ƃɂ��A <code>org.apache.axis.wsdd.WSDDProvider</code> �ɓo�^����܂��B
 * 
 * @author koichik
 */
public class WSDDS2RPCProvider extends WSDDProvider {

    /**
     * ���̃v���o�C�_�����ʂ��郍�[�J������Ԃ��܂��B
     * 
     * @return ���̃v���o�C�_�����ʂ��郍�[�J����
     */
    public String getName() {
        return S2AxisConstants.PROVIDER_S2RPC;
    }

    /**
     * �V�����v���o�C�_�̃C���X�^���X�𐶐����ĕԂ��܂��B
     * 
     * @param service
     *            WSDDS2Service�̃C���X�^���X
     * @param registry
     *            �n���h���̃��W�X�g��
     */
    public Handler newProviderInstance(final WSDDService service, final EngineConfiguration registry)
            throws Exception {
        return new S2RPCProvider(((WSDDS2Service) service).getComponentDef());
    }
}
