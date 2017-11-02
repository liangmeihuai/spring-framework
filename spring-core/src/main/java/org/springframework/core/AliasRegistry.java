/*
 * Copyright 2002-2008 the original author or authors.
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
 * Common interface for managing aliases. Serves as super-interface for
 * {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}.
 *管理别名的公共接口，作为BeanDefinitionRegistry的父类接口
 * @author Juergen Hoeller
 * @since 2.5.2
 */
public interface AliasRegistry {

	/**
	 * Given a name, register an alias for it.
	 * 为一个name注册一个别名
	 * @param name the canonical name 标准名称
	 * @param alias the alias to be registered 被注册的别名
	 * @throws IllegalStateException if the alias is already in use
	 * and may not be overridden
	 * 抛出IllegalStateException 如果这个别名已经被使用且不能够被覆盖
	 */
	void registerAlias(String name, String alias);

	/**
	 * Remove the specified alias from this registry.
	 * 从注册的地方移除掉特定的别名
	 * @param alias the alias to remove 被移除的别名
	 * @throws IllegalStateException if no such alias was found
	 * 抛出IllegalStateException异常，如果查找不大这个别名
	 */
	void removeAlias(String alias);

	/**
	 * Determine whether this given name is defines as an alias
	 * (as opposed to the name of an actually registered component).
	 * @param beanName the bean name to check
	 * @return whether the given name is an alias
	 * 确定这个给定的名称是否定义为别名
	 * (与实际上注册的组件的名称相反)
	 * 要检查的bean名称
	 * 返回：给定的名称是否为别名
	 */
	boolean isAlias(String beanName);

	/**
	 * Return the aliases for the given name, if defined.
	 * @param name the name to check for aliases
	 * @return the aliases, or an empty array if none
	 * 返回给定名称的别名，如果这个别名已经被定义
	 * 检查别名的名称
	 * 返回:别名，或空数组，如果没有
	 */
	String[] getAliases(String name);

}
