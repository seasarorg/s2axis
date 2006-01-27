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

/**
 * S2Axis�Ŏg�p����萔�ł��B
 * 
 * @author koichik
 */
public interface S2AxisConstants {

    /**
     * �I�y���[�V�������̖��O���URI
     */
    String OPERATION_NAMESPACE_URI = "http://soapinterop.org/";

    /**
     * dicon�t�@�C���ŁA�g�p����Axis�G���W�����w�����邽�߂� <code>&lt;meta&gt;</code> �v�f�Ɏw�肷��
     * <code>name</code> �����l�̃��[�J����(�ړ��� <code>"axis-"</code> �̌��)�ł��B
     */
    String META_ENGINE = "engine";

    /**
     * dicon�t�@�C���ŁAWSDD�t�@�C�����f�v���C������w�����邽�߂� <code>&lt;meta&gt;</code> �v�f�Ɏw�肷��
     * <code>name</code> �����l�̃��[�J����(�ړ��� <code>"axis-"</code> �̌��)�ł��B
     */
    String META_DEPLOY = "deploy";

    /**
     * dicon�t�@�C���ŁA�R���|�[�l���g��Axis�T�[�r�X�ł��邱�Ƃ��������߂� <code>&lt;meta&gt;</code>
     * �v�f�Ɏw�肷�� <code>name</code> �����l�̃��[�J����(�ړ��� <code>"axis-"</code> �̌��)�ł��B
     */
    String META_SERVICE = "service";

    /**
     * dicon�t�@�C���ŁA�R���|�[�l���g��Axis�n���h���ł��邱�Ƃ��������߂� <code>&lt;meta&gt;</code>
     * �v�f�Ɏw�肷�� <code>name</code> �����l�̃��[�J����(�ړ��� <code>"axis-"</code> �̌��)�ł��B
     */
    String META_HANDLER = "handler";

    /**
     * S2Axis���C���X�^���X�Ǘ����s��RPC�v���o�C�_�̃��[�J�����ł��B
     */
    String PROVIDER_S2RPC = "S2RPC";

    /**
     * S2Axis���C���X�^���X�Ǘ����s��MSG�v���o�C�_�̃��[�J�����ł��B
     */
    String PROVIDER_S2MSG = "S2MSG";

    /**
     * �f�t�H���g��Axis�G���W�����g�p���邱�Ƃ������܂��B
     */
    String ENGINE_DEFAULT = "default";

    /**
     * �f�t�H���g��Axis�N���C�A���g�G���W�����g�p���邱�Ƃ������܂��B
     */
    String ENGINE_DEFAULT_CLIENT = "default-client";

    /**
     * �f�t�H���g��Axis�T�[�o�G���W�����g�p���邱�Ƃ������܂��B
     */
    String ENGINE_DEFAULT_SERVER = "default-server";

    /**
     * �T�[�u���b�g�R���e�L�X�g����Axis�G���W�����擾���邱�Ƃ������܂��B
     */
    String ENGINE_FROM_SERVLET = "servlet:";

    /**
     * S2�R���e�i����Axis�G���W�����擾���邱�Ƃ������܂��B
     */
    String ENGINE_FROM_S2CONTAINER = "s2:";

    /**
     * AxisServlet�̃T�[�u���b�g���ł��B
     */
    String AXIS_SERVLET = "AxisServlet";

    /**
     * �T�[�u���b�g�R���e�L�X�g����Axis�G���W�����擾���邽�߂̃L�[�ł��B
     */
    String ATTR_AXIS_ENGINE = "AxisEngine";
}
