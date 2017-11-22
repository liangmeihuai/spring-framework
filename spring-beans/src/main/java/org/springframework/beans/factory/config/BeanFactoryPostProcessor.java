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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * Allows for custom modification of an application context's bean definitions,
 * adapting the bean property values of the context's underlying bean factory.
 *
 * <p>Application contexts can auto-detect BeanFactoryPostProcessor beans in
 * their bean definitions and apply them before any other beans get created.
 *
 * <p>Useful for custom config files targeted at system administrators that
 * override bean properties configured in the application context.
 *
 * <p>See PropertyResourceConfigurer and its concrete implementations
 * for out-of-the-box solutions that address such configuration needs.
 *
 * <p>A BeanFactoryPostProcessor may interact with and modify bean
 * definitions, but never bean instances. Doing so may cause premature bean
 * instantiation, violating the container and causing unintended side-effects.
 * If bean instance interaction is required, consider implementing
 * {@link BeanPostProcessor} instead.
 *
 * 允许自定义修改应用程序上下文的bean定义，以适应上下文的基础bean工厂的bean属性值。
 * 应用程序上下文可以在其bean定义中自动检测BeanFactoryPostProcessor bean，并在任何其他bean被创建之前应用它们。
 * 有用的定制配置文件 系统管理员重写在应用程序上下文中配置的Bean属性。参见PropertyResourceConfigurer及其具体实现
 *用于解决这种配置需求的开箱即用的解决方案。BeanFactoryPostProcessor可以与bean定义进行交互和修改，
 *但不能使用bean实例。 这样做可能会导致bean过早实例化，违反容器并导致意想不到的副作用。
 *如果需要bean实例交互，请考虑实现BeanPostProcessor。
 * @author Juergen Hoeller
 * @since 06.07.2003
 * @see BeanPostProcessor
 * @see PropertyResourceConfigurer
 */
public interface BeanFactoryPostProcessor {

	/**
	 * Modify the application context's internal bean factory after its standard
	 * initialization. All bean definitions will have been loaded, but no beans
	 * will have been instantiated yet. This allows for overriding or adding
	 * properties even to eager-initializing beans.
	 * 标准初始化后，修改应用程序上下文的内部bean工厂。 所有的bean定义都会被加载，
	 * 但是没有bean会被实例化。 这允许重写或添加属性，即使对于初始化bean也是如此。
	 * @param beanFactory the bean factory used by the application context
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
