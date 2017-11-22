/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.support;

import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.util.Assert;

/**
 * Qualifier for resolving autowire candidates. A bean definition that
 * includes one or more such qualifiers enables fine-grained matching
 * against annotations on a field or parameter to be autowired.
 *用于解析自动装配候选者的限定符。
 * 包含一个或多个这样的限定符的bean定义可以实现对字段或参数上的注释进行细粒度匹配的自动装配。
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.5
 * @see org.springframework.beans.factory.annotation.Qualifier
 */
@SuppressWarnings("serial")
public class AutowireCandidateQualifier extends BeanMetadataAttributeAccessor {

	public static String VALUE_KEY = "value";

	private final String typeName;


	/**
	 * Construct a qualifier to match against an annotation of the
	 * given type.
	 *构造一个限定符以匹配给定类型的注解。
	 * @param type the annotation type 注解的类型
	 */
	public AutowireCandidateQualifier(Class<?> type) {
		this(type.getName());
	}

	/**
	 * Construct a qualifier to match against an annotation of the
	 * given type name.
	 * <p>The type name may match the fully-qualified class name of
	 * the annotation or the short class name (without the package).
	 * @param typeName the name of the annotation type 注解类型名称
	 */
	public AutowireCandidateQualifier(String typeName) {
		Assert.notNull(typeName, "Type name must not be null");
		this.typeName = typeName;
	}

	/**
	 * Construct a qualifier to match against an annotation of the
	 * given type whose {@code value} attribute also matches
	 * the specified value.
	 * @param type the annotation type
	 * @param value the annotation value to match
	 */
	public AutowireCandidateQualifier(Class<?> type, Object value) {
		this(type.getName(), value);
	}

	/**
	 * Construct a qualifier to match against an annotation of the
	 * given type name whose {@code value} attribute also matches
	 * the specified value.
	 * <p>The type name may match the fully-qualified class name of
	 * the annotation or the short class name (without the package).
	 * 构造一个限定符以匹配给定类型名称的注释，该类型名称的{@code value}属性也匹配指定的值。
	 * 类型名称可以匹配注释的完全限定类名称或简短类名称（不带包）。
	 * @param typeName the name of the annotation type
	 * @param value the annotation value to match
	 */
	public AutowireCandidateQualifier(String typeName, Object value) {
		Assert.notNull(typeName, "Type name must not be null");
		this.typeName = typeName;
		setAttribute(VALUE_KEY, value);
	}


	/**
	 * Retrieve the type name. This value will be the same as the
	 * type name provided to the constructor or the fully-qualified
	 * class name if a Class instance was provided to the constructor.
	 * 检索类型名称。 如果向构造函数提供了一个Class实例，则该值将与提供给构造函数的类型名称或完全限定的类名称相同。
	 */
	public String getTypeName() {
		return this.typeName;
	}

}
