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

import org.apache.axis.Constants;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

/**
 * dicon�t�@�C�����Ń^�C�v�}�b�s���O����ݒ肷�邽�߂Ɏg���܂��B
 * 
 * @see ServiceDef#addTypeMapping(TypeMappingDef)
 * @author koichik
 */
public class TypeMappingDef {

    // instance fields
    protected Class type;
    protected String localPart = "";
    protected String namespaceURI = "";
    protected String namespacePrefix = "";
    protected Class serializer = BeanSerializerFactory.class;
    protected Class deserializer = BeanDeserializerFactory.class;
    protected String encodingStyle = Constants.URI_DEFAULT_SOAP_ENC;

    /**
     * Java�^��Ԃ��܂��B
     * 
     * @return Returns the type.
     */
    public Class getType() {
        return type;
    }

    /**
     * Java�^��ݒ肵�܂��B
     * 
     * @param type
     *            The type to set.
     */
    public void setType(final Class type) {
        this.type = type;
    }

    /**
     * XML�^�̖��O���URI��Ԃ��܂��B
     * 
     * @return Returns the namespaceURI.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * XML�^�̖��O���URI��ݒ肵�܂��B
     * 
     * @param namespaceURI
     *            The namespaceURI to set.
     */
    public void setNamespaceURI(final String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    /**
     * XML�^�̃��[�J������Ԃ��܂��B
     * 
     * @return Returns the localPart.
     */
    public String getLocalPart() {
        return localPart;
    }

    /**
     * XML�^�̃��[�J������ݒ肵�܂��B
     * 
     * @param localPart
     *            The localPart to set.
     */
    public void setLocalPart(final String localPart) {
        this.localPart = localPart;
    }

    /**
     * XML�^�̖��O��Ԑړ�����Ԃ��܂��B
     * 
     * @return Returns the namespacePrefix.
     */
    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    /**
     * XML�^�̖��O��Ԑړ�����ݒ肵�܂��B
     * 
     * @param namespacePrefix
     *            The namespacePrefix to set.
     */
    public void setNamespacePrefix(final String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    /**
     * Java�^����XML�^�ւ̃V���A���C�U��Ԃ��܂��B
     * 
     * @return Returns the serializer.
     */
    public Class getSerializer() {
        return serializer;
    }

    /**
     * Java�^����XML�^�ւ̃V���A���C�U��ݒ肵�܂��B
     * 
     * @param serializer
     *            The serializer to set.
     */
    public void setSerializer(final Class serializer) {
        this.serializer = serializer;
    }

    /**
     * XML�^����Java�^�ւ̃f�V���A���C�U��Ԃ��܂��B
     * 
     * @return Returns the deserializer.
     */
    public Class getDeserializer() {
        return deserializer;
    }

    /**
     * XML�^����Java�^�ւ̃f�V���A���C�U��ݒ肵�܂��B
     * 
     * @param deserializer
     *            The deserializer to set.
     */
    public void setDeserializer(final Class deserializer) {
        this.deserializer = deserializer;
    }

    /**
     * �G���R�[�f�B���O�X�^�C����Ԃ��܂��B
     * 
     * @return Returns the encodingStyle.
     */
    public String getEncodingStyle() {
        return encodingStyle;
    }

    /**
     * �G���R�[�f�B���O�X�^�C����ݒ肵�܂��B
     * 
     * @param encodingStyle
     *            The encodingStyle to set.
     */
    public void setEncodingStyle(final String encodingStyle) {
        this.encodingStyle = encodingStyle;
    }
}
