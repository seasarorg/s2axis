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

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.wsdl.fromJava.Namespaces;
import org.apache.axis.wsdl.fromJava.Types;
import org.seasar.framework.util.StringUtil;

/**
 * diconファイル中でタイプマッピング情報を設定するために使われます。
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
     * Java型を返します。
     * 
     * @return Returns the type.
     */
    public Class getType() {
        return type;
    }

    /**
     * Java型を設定します。
     * 
     * @param type
     *            The type to set.
     */
    public void setType(final Class type) {
        this.type = type;
    }

    /**
     * XML型の名前空間URIを返します。
     * 
     * @return Returns the namespaceURI.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * XML型の名前空間URIを設定します。
     * 
     * @param namespaceURI
     *            The namespaceURI to set.
     */
    public void setNamespaceURI(final String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    /**
     * XML型のローカル名を返します。
     * 
     * @return Returns the localPart.
     */
    public String getLocalPart() {
        return localPart;
    }

    /**
     * XML型のローカル名を設定します。
     * 
     * @param localPart
     *            The localPart to set.
     */
    public void setLocalPart(final String localPart) {
        this.localPart = localPart;
    }

    /**
     * XML型の名前空間接頭辞を返します。
     * 
     * @return Returns the namespacePrefix.
     */
    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    /**
     * XML型の名前空間接頭辞を設定します。
     * 
     * @param namespacePrefix
     *            The namespacePrefix to set.
     */
    public void setNamespacePrefix(final String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    /**
     * Java型からXML型へのシリアライザを返します。
     * 
     * @return Returns the serializer.
     */
    public Class getSerializer() {
        return serializer;
    }

    /**
     * Java型からXML型へのシリアライザを設定します。
     * 
     * @param serializer
     *            The serializer to set.
     */
    public void setSerializer(final Class serializer) {
        this.serializer = serializer;
    }

    /**
     * XML型からJava型へのデシリアライザを返します。
     * 
     * @return Returns the deserializer.
     */
    public Class getDeserializer() {
        return deserializer;
    }

    /**
     * XML型からJava型へのデシリアライザを設定します。
     * 
     * @param deserializer
     *            The deserializer to set.
     */
    public void setDeserializer(final Class deserializer) {
        this.deserializer = deserializer;
    }

    /**
     * エンコーディングスタイルを返します。
     * 
     * @return Returns the encodingStyle.
     */
    public String getEncodingStyle() {
        return encodingStyle;
    }

    /**
     * エンコーディングスタイルを設定します。
     * 
     * @param encodingStyle
     *            The encodingStyle to set.
     */
    public void setEncodingStyle(final String encodingStyle) {
        this.encodingStyle = encodingStyle;
    }

    /**
     * XML型のQNameを作成して返します。
     * 
     * @return XML型のQName
     */
    public QName getQName() {
        if (StringUtil.isEmpty(namespaceURI)) {
            namespaceURI = Namespaces.makeNamespace(type.getName());
        }

        if (StringUtil.isEmpty(localPart)) {
            localPart = Types.getLocalNameFromFullName(type.getName());
        }

        return new QName(namespaceURI, localPart, namespacePrefix);
    }

}
