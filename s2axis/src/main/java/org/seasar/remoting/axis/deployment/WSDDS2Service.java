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
import javax.xml.rpc.encoding.TypeMapping;

import org.apache.axis.constants.Use;
import org.apache.axis.deployment.wsdd.WSDDConstants;
import org.apache.axis.deployment.wsdd.WSDDException;
import org.apache.axis.deployment.wsdd.WSDDService;
import org.apache.axis.deployment.wsdd.WSDDTypeMapping;
import org.apache.axis.description.JavaServiceDesc;
import org.apache.axis.encoding.TypeMappingDelegate;
import org.apache.axis.encoding.TypeMappingRegistry;
import org.apache.axis.providers.java.JavaProvider;
import org.apache.axis.wsdl.fromJava.Namespaces;
import org.apache.axis.wsdl.fromJava.Types;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.message.MessageFormatter;
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis.S2AxisConstants;
import org.seasar.remoting.axis.ServiceDef;
import org.seasar.remoting.axis.TypeMappingDef;
import org.w3c.dom.Element;

/**
 * S2�ɂ��C���X�^���X�Ǘ������T�[�r�X��WSDD�ɂ��\���ł��B
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
     * �R���|�[�l���g��`����C���X�^���X���\�z���܂��B
     * 
     * @param componentDef
     *            �R���|�[�l���g��`
     * @throws WSDDException
     *             �C���X�^���X�̍\�z�Ɏ��s�����ꍇ�ɃX���[����܂��B
     */
    public WSDDS2Service(final ComponentDef componentDef) throws WSDDException {
        this(componentDef, new ServiceDef());
    }

    /**
     * �R���|�[�l���g��`�ƃT�[�r�X��`����C���X�^���X���\�z���܂��B
     * 
     * @param componentDef
     *            �R���|�[�l���g��`
     * @param serviceDef
     *            �T�[�r�X��`
     * @throws WSDDException
     *             �C���X�^���X�̍\�z�Ɏ��s�����ꍇ�ɃX���[����܂��B
     */
    public WSDDS2Service(final ComponentDef componentDef, final ServiceDef serviceDef)
            throws WSDDException {
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
        TypeMapping tm = tmr.getTypeMapping(Use.DEFAULT.getEncoding());
        ((TypeMappingDelegate) tm).setDoAutoTypes(true);
    }

    /**
     * �R���|�[�l���g��`��WSDD�t�@�C��(XML)��DOM�c���[����C���X�^���X���\�z���܂��B <br>
     * WSDD���� <code>&lt;service&gt;</code> �v�f�� <code>provider</code>
     * �����Ŏw�肳�ꂽ�v���o�C�_��Ή�����S2Axis�̃v���o�C�_�ɕύX���܂��B
     * 
     * @param componentDef
     *            �R���|�[�l���g��`
     * @param serviceElement
     *            WSDD�t�@�C��(XML)��DOM�c���[
     * @throws WSDDException
     *             �C���X�^���X�̍\�z�Ɏ��s�����ꍇ�ɃX���[����܂��B
     */
    public WSDDS2Service(final ComponentDef componentDef, final Element serviceElement)
            throws WSDDException {
        super(serviceElement);
        this.componentDef = componentDef;
        final String provider = getProviderQName().getLocalPart();
        setProviderQName(new QName(WSDDConstants.URI_WSDD_JAVA, getS2Provider(provider)));
    }

    /**
     * �R���|�[�l���g��`��Ԃ��܂��B
     * 
     * @return �R���|�[�l���g��`
     */
    public ComponentDef getComponentDef() {
        return componentDef;
    }

    /**
     * �T�[�r�X����Ԃ��܂��B <br>
     * �T�[�r�X���́A�R���|�[�l���g��`�ɖ��O��Ԃ��w�肳��Ă����"���O���/�R���|�[�l���g��"�A�����łȂ����"�R���|�[�l���g��"�ɂȂ�܂��B
     * 
     * @return �T�[�r�X��
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
     * �T�[�r�X�̌^��Ԃ��܂��B <br>
     * �T�[�r�X�̌^�́A�T�[�r�X��`�Ɍ^���w�肳��Ă���΂��̌^�A�����łȂ���΃R���|�[�l���g�̌^�ɂȂ�܂��B
     * <p>
     * �R���|�[�l���g�^��(�C���^�t�F�[�X�ł͂Ȃ�)�����N���X�̏ꍇ�ŁA���\�b�h���`���ꂽ�C���^�t�F�[�X��
     * ������������Ă���ꍇ�ɂ́A���̃C���^�t�F�[�X���T�[�r�X�̌^�Ƃ��܂��B <br>
     * ���\�b�h����`���ꂽ�C���^�t�F�[�X�𕡐��������Ă���ꍇ�ł��A�T�[�r�X��`��
     * ���J���郁�\�b�h���w�肳��Ă��Ȃ��ꍇ�ɂ́A�C���^�t�F�[�X�ɒ�`���ꂽ���\�b�h�̖��O���T�[�r�X��`�̌��J���郁�\�b�h�ɐݒ肵�܂��B
     * </p>
     * 
     * @param serviceDef
     *            �T�[�r�X��`
     * @return �T�[�r�X�̌^
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
     * Axis�̃v���o�C�_���ɑΉ�����S2Axis�p�̃v���o�C�_����Ԃ��܂��B
     * 
     * @param provider
     *            Axis�W���̃v���o�C�_��
     * @return S2Axis�p�̃v���o�C�_��
     * @throws WSDDException
     *             Axis�̃v���o�C�_���Ή�����S2Axis�̃v���o�C�_���Ȃ��ꍇ�ɃX���[����܂�
     */
    protected String getS2Provider(final String provider) throws WSDDException {
        final String s2Provider = (String) providerMapping.get(provider);
        if (s2Provider == null) {
            throw new WSDDException(provider);
        }
        return s2Provider;
    }

    /**
     * WSDD�^�C�v�}�b�s���O���쐬���ĕԂ��܂��B
     * 
     * @param typeMappingDef
     *            �^�C�v�}�b�s���O��`
     * @return WSDD�^�C�v�}�b�s���O
     */
    protected WSDDTypeMapping createWSDDTypeMapping(final TypeMappingDef typeMappingDef) {
        final WSDDTypeMapping wsddTypeMapping = new WSDDTypeMapping();

        final Class type = typeMappingDef.getType();
        wsddTypeMapping.setLanguageSpecificType(typeMappingDef.getType());

        wsddTypeMapping.setQName(createQNameOfType(type, typeMappingDef.getNamespaceURI(),
                typeMappingDef.getLocalPart(), typeMappingDef.getNamespacePrefix()));

        wsddTypeMapping.setSerializer(typeMappingDef.getSerializer());
        wsddTypeMapping.setDeserializer(typeMappingDef.getDeserializer());

        wsddTypeMapping.setEncodingStyle(typeMappingDef.getEncodingStyle());

        return wsddTypeMapping;
    }

    /**
     * XML�^��QName���쐬���ĕԂ��܂��B
     * 
     * @param type
     *            Java�^
     * @param namespaceURI
     *            XML�^�̖��O���URI�B�ȗ������Java�^�̃p�b�P�[�W�����瓱�o����܂�
     * @param localPart
     *            XML�^�̃��[�J�����B�ȗ������Java�^�̃N���X�����g���܂�
     * @param namespacePrefix
     *            XML�^�̖��O��Ԑړ����B�ȗ�����ƃf�t�H���g���O��ԂɂȂ�܂�
     * @return XML�^��QName
     */
    protected QName createQNameOfType(final Class type, String namespaceURI, String localPart,
            final String namespacePrefix) {
        if (StringUtil.isEmpty(namespaceURI)) {
            namespaceURI = Namespaces.makeNamespace(type.getName());
        }

        if (StringUtil.isEmpty(localPart)) {
            localPart = Types.getLocalNameFromFullName(type.getName());
        }

        return new QName(namespaceURI, localPart, namespacePrefix);
    }
}
