/*
 * Copyright 2002-2013 the original author or authors.
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

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 *beanDefinition描述了一个bean实例,该实例具有属性值，构造器参数值，以及具体实现中的
 * 更多信息。sss
 * [BeanDefinition描述了一个bean实例，该实例具有属性值，构造函数参数值以及由具体实现提供的更多信息。]
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} such as {@link PropertyPlaceholderConfigurer}
 * to introspect and modify property values and other bean metadata.
 *这是一个最小限度的接口:这种做的主要意图是为了允许BeanFactoryPostProcessor，比如PropertyPlaceholderConfigurer
 * 来内省和修改属性值以及其他的bean元数据
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * 对于单例范围用"singleton"作为标准的scope标识符
	 * <p>Note that extended bean factories might support further scopes.
	 * 注意扩展的bean工厂有可能支持更大的范围
	 * @see #setScope
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * 对于多例范围用"prototype"作为标准的scope标识符
	 * <p>Note that extended bean factories might support further scopes.
	 * 注意扩展的bean工厂有可能支持更大的范围
	 * @see #setScope
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


	/**
	 * Role hint indicating that a {@code BeanDefinition} is a major part
	 * of the application. Typically corresponds to a user-defined bean.
	 * 角色暗示表明一个beanDefinition是applicationde主要部分。
	 * 特定对应于用户自定义的bean
	 */
	int ROLE_APPLICATION = 0;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is a supporting
	 * part of some larger configuration, typically an outer
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * {@code SUPPORT} beans are considered important enough to be aware
	 * of when looking more closely at a particular
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
	 * but not when looking at the overall configuration of an application.
	 * 角色暗示表明一个beanDefinition对于一些更大的配置的一个支持部分，
	 * bean被认为足够重要，以便在更仔细地查看特定的ComponentDefinition时注意到，
	 但是在查看应用程序的整体配置时却不行。
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is providing an
	 * entirely background role and has no relevance to the end-user. This hint is
	 * used when registering beans that are completely part of the internal workings
	 * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * 角色提示表明一个BeanDefinition是提供一个完全背景的角色，并且与最终用户没有关系。
	 * 这个提示用于注册完全是ComponentDefinition内部工作的一部分的bean。
	 */
	int ROLE_INFRASTRUCTURE = 2;


	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 * 如果有的话，返回这个bean定义的父定义的名字。
	 */
	String getParentName();

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 * 如果有的话，设置这个bean definition的父bean definition的名字。
	 */
	void setParentName(String parentName);

	/**
	 * Return the current bean class name of this bean definition.
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 * 返回这个bean definition的当前bean class的名称。
	 * 之一这个名称不一定是在运行时使用的实际类名，以防child bedefinition覆盖(继承)它父类的类名.
	 *因此，不要认为这是在运行时定义的bean类型，而只是在单个bean定义级别将其用于解析目的。
	 */
	String getBeanClassName();

	/**
	 * Override the bean class name of this bean definition.
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 * 覆盖此bean定义的bean类名称。在bean factory后处理期间，可以修改类名称，通常用原始类名称的解析变体替换原始类名称
	 */
	void setBeanClassName(String beanClassName);

	/**
	 * Return the factory bean name, if any.
	 * 如果有的话，返回这个bedefinition的工厂bean的名称
	 */
	String getFactoryBeanName();

	/**
	 * Specify the factory bean to use, if any.
	 * 指定要使用的工厂bean（如果有的话）。
	 */
	void setFactoryBeanName(String factoryBeanName);

	/**
	 * Return a factory method, if any.
	 * 如果有的话，返回一个工厂的方法名称
	 */
	String getFactoryMethodName();

	/**
	 * Specify a factory method, if any. This method will be invoked with
	 * constructor arguments, or with no arguments if none are specified.
	 * The method will be invoked on the specified factory bean, if any,
	 * or otherwise as a static method on the local bean class.
	 * 指定工厂方法（如果有的话）。 这个方法将会被构造器函数参数调用，或者如果没有指定任何参数，将调用该方法。
	 * 方法将在指定的工厂bean（如果有的话）上被调用，或者作为本地bean类的静态方法被调用
	 * @param factoryMethodName static factory method name,
	 * or {@code null} if normal constructor creation should be used
	 * @see #getBeanClassName()
	 * factoryMethodName静态工厂方法名称，或者{@code null}如果应该使用正常的构造函数创建
	 */
	void setFactoryMethodName(String factoryMethodName);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 * 返回此bean的当前目标作用域的名称，如果还不知道，则返回{@code null}。
	 */
	String getScope();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * 覆盖这个bean的目标scope,指定一个新的scope name
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 */
	void setScope(String scope);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 * 返回这个bean是否应该被懒加载标志，也就是说，不着急在启动时候就马上加载。
	 * 这个仅仅适用于单例类
	 */
	boolean isLazyInit();

	/**
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 * 设置这个bean是否应该被懒记载。
	 * 如果为false，这个bean将会在执行初始化singletons的bean工厂启动的时候被实例化。
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return the bean names that this bean depends on.
	 * 返回这个bean依赖的bean名称
	 */
	String[] getDependsOn();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 * 在这个bean正在初始化时候，设置这个bean所依赖的bean的名称。
	 * bean工厂将会保证这些依赖的bean会被首先初始化。
	 */
	void setDependsOn(String[] dependsOn);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 * 返回这个bean是否是作为自动装配到其它bean的候选bean
	 * [返回这个bean是否是自动装配到其他bean的候选者.]
	 */
	boolean isAutowireCandidate();

	/**
	 * Set whether this bean is a candidate for getting autowired into some other bean.
	 * 设置这个bean是否是获得自动装配到其他bean的候选人
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 * If this value is true for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 * 返回这个bean是否是主要的autowire候选者。
	 * 如果这个值在多个匹配的候选者中只有一个bean是真的，
	 * 那么它将作为一个平局。
	 */
	boolean isPrimary();

	/**
	 * Set whether this bean is a primary autowire candidate.
	 * <p>If this value is true for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 */
	void setPrimary(boolean primary);


	/**
	 * Return the constructor argument values for this bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the ConstructorArgumentValues object (never {@code null})
	 * 返回这个bean的构造函数参数值。 在bean factory后处理期间可以修改返回的实例。
	 * @return ConstructorArgumentValues对象（从不{code null}）
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * Return the property values to be applied to a new instance of the bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the MutablePropertyValues object (never {@code null})
	 * 返回要应用到bean的新实例的属性值。返回的实例可以在bean factory后期处理期间修改。
	 * @返回MutablePropertyValues对象（从不{code null}）
	 */
	MutablePropertyValues getPropertyValues();


	/**
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 * 返回这是一个Singleton，在所有调用中返回一个共享实例。
	 * @see #SCOPE_SINGLETON
	 */
	boolean isSingleton();

	/**
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * 返回是否这是一个Prototype，为每个调用返回一个独立的实例。
	 * @see #SCOPE_PROTOTYPE
	 */
	boolean isPrototype();

	/**
	 * Return whether this bean is "abstract", that is, not meant to be instantiated.
	 * 返回这个bean是否是“抽象的”，也就是说，不打算被实例化。
	 */
	boolean isAbstract();

	/**
	 * Get the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools with an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * 获取这个{@Code BeanDefinition}的角色提示。
	 * 角色提示提供框架以及工具，指出特定BeanDefinition的角色和重要性。
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	int getRole();

	/**
	 * Return a human-readable description of this bean definition.
	 * 返回这个bean定义的可读描述。
	 */
	String getDescription();

	/**
	 * Return a description of the resource that this bean definition
	 * came from (for the purpose of showing context in case of errors).
	 * 返回这个bean定义来自的资源的描述（为了在错误的情况下显示上下文）。
	 *
	 */
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * Allows for retrieving the decorated bean definition, if any.
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 * 返回原始BeanDefinition，如果没有，则返回{null}。 允许检索装饰的bean定义，如果有的话。
	 * 请注意，此方法返回直接发件人。 迭代原始链，找到用户定义的原始BeanDefinition。
	 */
	BeanDefinition getOriginatingBeanDefinition();

}
