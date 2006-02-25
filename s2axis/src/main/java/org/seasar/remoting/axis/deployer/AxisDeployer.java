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
import org.apache.axis.ConfigurationException;
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
 * dicon�t�@�C�����ɋL�q���ꂽ�R���|�[�l���g��Axis�Ƀf�v���C���܂��B
 * 
 * @author koichik
 */
public class AxisDeployer implements Deployer {

    // class fields
    /**
     * <code>&lt;meta&gt;</code> �v�f�� <code>name</code>
     * �����Ɏw�肳��閼�O���擾���邽�߂̐��K�\���ł��B <br>
     * S2Axis-V1.0.0-RC2�ȍ~�͐ړ��� <code>axis-</code> �̌�Ƀ��[�J�����������܂��B <br>
     * S2Axis-V1.0.0-RC1�ȑO�Ƃ̌݊����̂��߁A�ړ��� <code>s2-axis:</code> ���g����悤�ɂ��Ă��܂��B
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
     * S2�R���e�i��ݒ肵�܂��B
     * 
     * @param container
     *            S2�R���e�i
     */
    public void setContainer(final S2Container container) {
        this.container = container.getRoot();
    }

    /**
     * �T�[�u���b�g�R���e�L�X�g��ݒ肵�܂��B
     * 
     * @param servletContext
     *            �T�[�u���b�g�R���e�L�X�g
     */
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * �R���e�i�ɓo�^����Ă���T�[�r�X��n���h�����f�v���C���܂��B
     */
    public void deploy() {
        context.remove();
        forEach(container);

        final AxisEngine engine = getEngine(container);
        try {
            engine.refreshGlobalOptions();
        }
        catch (final ConfigurationException e) {
            throw new DeployFailedException(e);
        }

        final Object dotnet = engine.getOption(AxisEngine.PROP_DOTNET_SOAPENC_FIX);
        if (JavaUtils.isTrue(dotnet)) {
            TypeMappingImpl.dotnet_soapenc_bugfix = true;
        }
    }

    /**
     * �R���e�i�̊K�w�����ǂ��đS�ẴR���e�i�ƃR���|�[�l���g��`�𑖍����܂��B <br>
     * �������鏇���͎��̒ʂ�ł��B
     * <ol>
     * <li>�q�̃R���e�i���ċN�I��</li>
     * <li>�R���e�i���g(&lt;components&gt;������&lt;meta&gt;�v�f)</li>
     * <li>�R���|�[�l���g(&lt;component&gt;�̉���&lt;meta&gt;�v�f)</li>
     * </ol>
     * 
     * @param container
     *            �N�_�ƂȂ�R���e�i
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
     * S2�R���e�i��S2Axis�̃��^�f�[�^ <code>&lt;meta name="axis-deploy"&gt;</code>
     * ���w�肳��Ă���΁A����WSDD��Axis�Ƀf�v���C���܂��B
     * 
     * @param container
     *            S2�R���e�i
     */
    protected void process(final S2Container container) {
        final MetaDef[] metaDefs = getMetaDefs(container, S2AxisConstants.META_DEPLOY);
        for (int i = 0; metaDefs != null && i < metaDefs.length; ++i) {
            wsddDeployer.deploy(null, metaDefs[i]);
        }
    }

    /**
     * �R���|�[�l���g��`��S2Axis�̃��^�f�[�^ <code>&lt;meta name="axis-service"&gt;</code>
     * �܂��� <code>&lt;meta name="axis-handler"&gt;</code>
     * ���w�肳��Ă���΁A���̃R���|�[�l���g���T�[�r�X�܂��̓n���h���Ƃ���Axis�Ƀf�v���C���܂��B
     * 
     * @param componentDef
     *            �R���|�[�l���g��`
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
     * WSDD�f�v���C�����g��Ԃ��܂��B
     * 
     * @param container
     *            �R���e�i
     * @return WSDD�f�v���C�����g
     */
    protected WSDDDeployment getDeployment(final S2Container container) {
        return ((WSDDEngineConfiguration) getEngine(container).getConfig()).getDeployment();
    }

    /**
     * Axis�G���W����Ԃ��܂��B <br>
     * Axis�G���W���́A�R���e�i�ɖ��O <code>axis-engine</code> ������
     * <code>&lt;meta&gt;</code> �v�f���w�肳��Ă���΁A���̓��e�����񂩂玟�̂悤�Ɍ��肳��܂��B
     * <dl>
     * <dt>����`�̏ꍇ</dt>
     * <dd><code>"default"</code> ���w�肳�ꂽ���̂Ƃ���Axis�G���W�������肵�܂��B</dd>
     * <dt><code>"default"</code></dt>
     * <dd>�R���e�i�ɃT�[�u���b�g�R���e�L�X�g���ݒ肳��Ă���� <code>"default-server"</code> �A�����łȂ����
     * <code>"default-client"</code> ���w�肳�ꂽ���̂Ƃ���Axis�G���W�������肵�܂��B</dd>
     * <dt><code>"default-client"</code></dt>
     * <dd>�R���e�i���� <code>javax.xml.rpc.Service</code>
     * �����������R���|�[�l���g���擾���A���̃G���W�����g�p���܂��B</dd>
     * <dt><code>"default-server"</code></dt>
     * <dd>�T�[�u���b�g�R���e�L�X�g�ɐݒ肳��Ă���Axis�G���W�����g�p���܂��B <br>
     * �ŏ��� {@link S2AxisConstants#AXIS_SERVLET}��
     * {@link S2AxisConstants#ATTR_AXIS_ENGINE}��A��������������L�[�Ƃ���
     * �T�[�u���b�g�R���e�L�X�g����Axis�G���W�����擾���܂��B <br>
     * ������Ȃ������ꍇ��{S2AxisConstants#ATTR_AXIS_ENGINE}��
     * �L�[�Ƃ��ăT�[�u���b�g�R���e�L�X�g����擾����Axis�G���W�����擾���܂��B <br>
     * </dd>
     * <dt><code>"servlet:"</code> �Ŏn�܂镶����</dt>
     * <dd><code>"servlet:"</code> �̌��̕�������L�[�Ƃ��ăT�[�u���b�g�R���e�L�X�g����
     * �擾����Axis�G���W�����g�p���܂��B
     * <dd>
     * <dt><code>"s2:"</code> �Ŏn�܂镶����</dt>
     * <dd><code>"s2:"</code> �̌��̕�������L�[�Ƃ���S2�R���e�i����
     * �擾�����R���|�[�l���g��Axis�G���W�����g�p���܂��B</dd>
     * <dt>���̑�</dt>
     * <dd>�L�[�Ƃ���S2�R���e�i����擾�����R���|�[�l���g��Axis�G���W���Ƃ��Ďg�p���܂��B</dd>
     * </dl>
     * 
     * @param container
     *            �R���e�i
     * @return Axis�G���W��
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
     * <code>S2Container</code> �܂��� <code>ComponentDef</code> �����O
     * <code>"axis-<var>localName</var></code> �� <code>&lt;meta&gt;</code>
     * �v�f�������Ă���΁A���� <code>MetaDef</code> ��Ԃ��܂��B <br>
     * <code>S2Container</code> �܂��� <code>ComponentDef</code> �ɊY������
     * ���^�f�[�^��������`����Ă���ꍇ�͍ŏ��Ɍ����������^�f�[�^��Ԃ��܂��B
     * 
     * @param metaDefSupport
     *            <code>S2Container</code> �܂��� <code>ComponentDef</code>
     * @param localName
     *            �ړ��� <code>axis-</code> �ɑ������^�f�[�^�̖��O
     * @return �w�肳�ꂽ���O������ <code>MetaDef</code> �B���݂��Ȃ��ꍇ�� <code>null</code>
     */
    protected MetaDef getMetaDef(final MetaDefAware metaDefSupport, final String localName) {
        for (int i = 0; i < metaDefSupport.getMetaDefSize(); ++i) {
            final MetaDef metaDef = metaDefSupport.getMetaDef(i);
            if (localName.equals(getLocalName(metaDef))) {
                return metaDef;
            }
        }
        return null;
    }

    /**
     * <code>S2Container</code> �܂��� <code>ComponentDef</code> �����O
     * <code>"axis-<var>localName</var></code> �� <code>&lt;meta&gt;</code>
     * �v�f�������Ă���΁A���� <code>MetaDef</code> ��S�ĕԂ��܂��B <br>
     * 
     * @param metaDefSupport
     *            <code>S2Container</code> �܂��� <code>ComponentDef</code>
     * @param localName
     *            �ړ��� <code>axis-</code> �ɑ������^�f�[�^�̖��O
     * @return �w�肳�ꂽ���O������ <code>MetaDef</code> �̔z��
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
     * ���^�f�[�^�̖��O��S2Axis�Ŏg�p����ړ����Ŏn�܂��Ă���΁A�ړ����̌��̃��[�J������Ԃ��܂��B
     * 
     * @param metaDef
     *            ���^�f�[�^��`
     * @return ���[�J����
     */
    protected String getLocalName(final MetaDef metaDef) {
        final Matcher matcher = META_NAME_PATTERN.matcher(metaDef.getName());
        return matcher.matches() ? matcher.group(1) : null;
    }
}
