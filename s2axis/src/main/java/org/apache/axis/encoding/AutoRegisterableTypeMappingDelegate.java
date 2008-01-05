/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.apache.axis.encoding;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.encoding.SerializerFactory;

/**
 * タイプマッピングを自動登録するための <code>TypeMappingDelegate</code> 拡張クラス。
 * <p>
 * 少なくともバージョン 1.4 以前の Axis では、 <code>TypeMappingImpl</code> を自動登録モードで利用した場合に、
 * <code>getSerializer(Class, QName)</code> の 第 2 引数が <code>null</code>
 * で呼び出されると、タイプマッピングのチェーンに登録されているマッピングをチェックしないで <code>BeanSerializer</code>
 * が自動登録されてしまう問題があるようです。<br>
 * 本クラスは、 上記の問題を回避するため <code>getSerializer(Class, QName)</code> の第 2 引数が
 * <code>null</code> で呼び出された場合に、 あらかじめタイプマッピングチェーンから <code>QName</code>
 * を解決することを目的とします。
 * </p>
 * <p>
 * なお，<code>TypeMappingDelegate</code> クラスのコンストラクタがデフォルト (package)
 * の可視性となっているため， Axis のパッケージ名をそのまま利用しています。
 * 
 * @author koichik
 * 
 */
public class AutoRegisterableTypeMappingDelegate extends TypeMappingDelegate {

    private static final long serialVersionUID = 1L;

    /**
     * 自動登録モードで空のマッピングを持つ <code>TypeMappingImpl</code> を移譲先とするインスタンスを構築します。
     * 
     */
    public AutoRegisterableTypeMappingDelegate() {
        super(new TypeMappingImpl());
        setDoAutoTypes(true);
    }

    /**
     * シリアライザを返します。
     * <p>
     * <code>xmlType</code> が <code>null</code> で呼び出された場合は、 タイプマッピングに委譲する前に
     * タイプマッピングチェーンから <code>xmlType</code> を解決します。
     * </p>
     * 
     * @param javaType
     *            Java 型
     * @param xmlType
     *            XML 型
     * @return シリアライザ
     */
    public SerializerFactory getSerializer(Class javaType, QName xmlType) throws JAXRPCException {
        if (xmlType == null) {
            xmlType = getTypeQName(javaType);
        }
        return super.getSerializer(javaType, xmlType);
    }

}
