/*
 * Copyright 2002-2011 the original author or authors.
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

package org.springframework.beans.factory;

/**
 * Interface to be implemented by beans that want to be aware of their
 * bean name in a bean factory. Note that it is not usually recommended
 * that an object depend on its bean name, as this represents a potentially
 * brittle dependence on external configuration, as well as a possibly
 * unnecessary dependence on a Spring API.
 *
 * <p>For a list of all bean lifecycle methods, see the
 * {@link BeanFactory BeanFactory javadocs}.
 * 接口由要在bean工厂中知道其bean名称的bean实现。
 * 请注意，通常不建议对象依赖于其bean名称，因为这表示对外部配置的潜在脆弱依赖性，
 * 以及对Spring API的可能不必要的依赖性。有关所有bean生命周期方法的列表，请参阅 {@link BeanFactory BeanFactory javadocs}。
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 01.11.2003
 * @see BeanClassLoaderAware
 * @see BeanFactoryAware
 * @see InitializingBean
 */
public interface BeanNameAware extends Aware {

	/**
	 * Set the name of the bean in the bean factory that created this bean.
	 * <p>Invoked after population of normal bean properties but before an
	 * init callback such as {@link InitializingBean#afterPropertiesSet()}
	 * or a custom init-method.
	 * @param name the name of the bean in the factory.
	 * Note that this name is the actual bean name used in the factory, which may
	 * differ from the originally specified name: in particular for inner bean
	 * names, the actual bean name might have been made unique through appending
	 * "#..." suffixes. Use the {@link BeanFactoryUtils#originalBeanName(String)}
	 * method to extract the original bean name (without suffix), if desired.
	 *  在创建此bean的bean工厂中设置bean的名称。
	 * 在正常bean属性的填充之后但在init回调（如InitializingBean＃afterPropertiesSet（））或
	 *自定义init方法之前调用@
	 * param命名该名称 请注意，这个名称是工厂中使用的实际bean名称，
	 * 可能与原来指定的名称不同：特别是对于内部bean名称，实际的bean名称可能已经通过附加“＃” ...“后缀。
	 *使用{@ linkBeanFactoryUtils＃originalBeanName（String）}
      *方法提取原始的bean名称（不带后缀），如果需要的话。
	 */
	void setBeanName(String name);

}
