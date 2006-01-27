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

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.encoding.TypeMapping;

import org.apache.axis.constants.Use;
import org.apache.axis.encoding.TypeMappingDelegate;
import org.apache.axis.encoding.TypeMappingRegistry;
import org.seasar.remoting.axis.S2AxisConstants;
import org.seasar.remoting.common.connector.impl.TargetSpecificURLBasedConnector;

/**
 * Web�T�[�r�X���Ăяo��S2Remoting�R�l�N�^�̎����N���X�ł��B
 * 
 * @author koichik
 */
public class AxisConnector extends TargetSpecificURLBasedConnector {

    // instance fields
    protected Service service;

    /**
     * Axis�T�[�r�X��ݒ肵�܂��B
     * 
     * @param service
     *            Axis�T�[�r�X
     */
    public void setService(final Service service) {
        this.service = service;

        final TypeMappingRegistry tmr = (TypeMappingRegistry) service.getTypeMappingRegistry();
        TypeMapping tm = tmr.getTypeMapping(Use.DEFAULT.getEncoding());
        ((TypeMappingDelegate) tm).setDoAutoTypes(true);
    }

    /**
     * Axis�T�[�r�X���g�p���ă����[�g���\�b�h�̌Ăяo�������s���A���̌��ʂ�Ԃ��܂��B
     * 
     * @param targetURL
     *            �����[�g�I�u�W�F�N�g��URL
     * @param method
     *            �Ăяo�����\�b�h
     * @param args
     *            �����[�g�I�u�W�F�N�g�̃��\�b�h�Ăяo���ɓn���������l���i�[����I�u�W�F�N�g�z��
     * @return �����[�g�I�u�W�F�N�g�ɑ΂��郁�\�b�h�Ăяo������̖߂�l
     * @throws Throwable
     *             �����[�g�I�u�W�F�N�g�ɑ΂��郁�\�b�h�Ăяo������X���[������O
     */
    protected Object invoke(final URL targetURL, final Method method, final Object[] args)
            throws Throwable {
        final Call call = service.createCall();
        call.setTargetEndpointAddress(targetURL.toString());
        call.setOperationName(new QName(S2AxisConstants.OPERATION_NAMESPACE_URI, method.getName()));
        return call.invoke(args);
    }
}
