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
package org.seasar.remoting.axis.providers;

import org.apache.axis.MessageContext;
import org.apache.axis.providers.java.MsgProvider;
import org.seasar.framework.container.ComponentDef;

/**
 * RPC�T�[�r�X�̎�����S2�R���e�i����擾����v���o�C�_�ł��B
 * 
 * @author koichik
 */
public class S2MsgProvider extends MsgProvider {

    // constants
    private static final long serialVersionUID = 1L;

    // instance fields
    protected final ComponentDef componentDef;

    /**
     * �C���X�^���X���\�z���܂��B
     * 
     * @param componentDef
     *            �R���|�[�l���g��`
     */
    public S2MsgProvider(final ComponentDef componentDef) {
        this.componentDef = componentDef;
    }

    /**
     * �T�[�r�X�̃C���X�^���X��Ԃ��܂��B
     * 
     * @param msgContext
     *            ���b�Z�[�W�R���e�L�X�g
     * @param className
     *            �N���X��
     */
    protected Object makeNewServiceObject(final MessageContext msgContext, final String className) {
        return componentDef.getComponent();
    }
}
