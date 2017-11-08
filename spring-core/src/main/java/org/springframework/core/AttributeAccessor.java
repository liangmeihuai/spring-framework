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

package org.springframework.core;

/**
 * Interface defining a generic contract for attaching and accessing metadata
 * to/from arbitrary objects.
 * 此接口定义了一个通用的契约，用于附加和访问任意对象的元数据。
 * @author Rob Harrop
 * @since 2.0
 */
public interface AttributeAccessor {

	/**
	 * Set the attribute defined by {@code name} to the supplied	{@code value}.
	 * If {@code value} is {@code null}, the attribute is {@link #removeAttribute removed}.
	 * <p>In general, users should take care to prevent overlaps with other
	 * metadata attributes by using fully-qualified names, perhaps using
	 * class or package names as prefix.
	 * @param name the unique attribute key
	 * @param value the attribute value to be attached
	 *
	 *  将由{@code name}定义的属性设置为提供的{@code值}。 如果{code}的值为{null}，则该属性为{@link #removeAttribute removed}。
	 *  通常，用户应该注意通过使用完全限定的名称（可能使用类或包名称作为前缀）来防止与其他元数据属性重叠。
	 *              @参数 命名唯一的属性键
	 *              @参数 值要附加的属性值
	 */
	void setAttribute(String name, Object value);

	/**
	 * Get the value of the attribute identified by {@code name}.
	 * Return {@code null} if the attribute doesn't exist.
	 * 取到有name定义的属性值
	 * 如果这个属性不存在则返回null
	 * @param name the unique attribute key  唯一属性key
	 * @return the current value of the attribute, if any  如果有的话，返回属性的当前值
	 */
	Object getAttribute(String name);

	/**
	 * Remove the attribute identified by {@code name} and return its value.
	 * Return {@code null} if no attribute under {@code name} is found.
	 * 移除掉用name定义的属性，如果没有找到name下属性，则返回null
	 * @param name the unique attribute key  唯一属性key
	 * @return the last value of the attribute, if any 如果有的话，返回属性的最后一个值
	 */
	Object removeAttribute(String name);

	/**
	 * Return {@code true} if the attribute identified by {@code name} exists.
	 * Otherwise return {@code false}.
	 * 如果用name定义的属性存在，则返回true，否则返回false
	 * @param name the unique attribute key 唯一属性key
	 */
	boolean hasAttribute(String name);

	/**
	 * Return the names of all attributes.
	 * 返回所有属性的名称
	 */
	String[] attributeNames();

}
