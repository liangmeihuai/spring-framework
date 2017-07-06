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

package org.springframework.core.env;

/**
 * Interface representing the environment in which the current application is running.
 * Models two key aspects of the application environment: <em>profiles</em> and
 * <em>properties</em>. Methods related to property access are exposed via the
 * {@link PropertyResolver} superinterface.
 *
 * <p>A <em>profile</em> is a named, logical group of bean definitions to be registered
 * with the container only if the given profile is <em>active</em>. Beans may be assigned
 * to a profile whether defined in XML or via annotations; see the spring-beans 3.1 schema
 * or the {@link org.springframework.context.annotation.Profile @Profile} annotation for
 * syntax details. The role of the {@code Environment} object with relation to profiles is
 * in determining which profiles (if any) are currently {@linkplain #getActiveProfiles
 * active}, and which profiles (if any) should be {@linkplain #getDefaultProfiles active
 * by default}.
 *
 * <p><em>Properties</em> play an important role in almost all applications, and may
 * originate from a variety of sources: properties files, JVM system properties, system
 * environment variables, JNDI, servlet context parameters, ad-hoc Properties objects,
 * Maps, and so on. The role of the environment object with relation to properties is to
 * provide the user with a convenient service interface for configuring property sources
 * and resolving properties from them.
 *
 * <p>Beans managed within an {@code ApplicationContext} may register to be {@link
 * org.springframework.context.EnvironmentAware EnvironmentAware} or {@code @Inject} the
 * {@code Environment} in order to query profile state or resolve properties directly.
 *
 * <p>In most cases, however, application-level beans should not need to interact with the
 * {@code Environment} directly but instead may have to have {@code ${...}} property
 * values replaced by a property placeholder configurer such as
 * {@link org.springframework.context.support.PropertySourcesPlaceholderConfigurer
 * PropertySourcesPlaceholderConfigurer}, which itself is {@code EnvironmentAware} and
 * as of Spring 3.1 is registered by default when using
 * {@code <context:property-placeholder/>}.
 *
 * <p>Configuration of the environment object must be done through the
 * {@code ConfigurableEnvironment} interface, returned from all
 * {@code AbstractApplicationContext} subclass {@code getEnvironment()} methods. See
 * {@link ConfigurableEnvironment} Javadoc for usage examples demonstrating manipulation
 * of property sources prior to application context {@code refresh()}.
 *
 * @author Chris Beams
 * @since 3.1
 * @see PropertyResolver
 * @see EnvironmentCapable
 * @see ConfigurableEnvironment
 * @see AbstractEnvironment
 * @see StandardEnvironment
 * @see org.springframework.context.EnvironmentAware
 * @see org.springframework.context.ConfigurableApplicationContext#getEnvironment
 * @see org.springframework.context.ConfigurableApplicationContext#setEnvironment
 * @see org.springframework.context.support.AbstractApplicationContext#createEnvironment
 *
 * 环境代表当前应用运行时所处的环境。

整个应用环境模型包括2个关键方面：

profiles配置组（以下简称组）：
一个profile组，是一个以name名称命名的、逻辑上的、要被注册到容器中的BeanDefinition的集合。简单一点说，
一个profile就代表一组BeanDefinition，这个对应配置文件中<beans profile="">。当加载解析xml配置文件的时候，
只有active=true激活的BeanDefinition才会被加载进容器。

properties环境变量：
在几乎所有的应用中，Properties环境变量都扮演着非常重要的角色，
且这些变量值可以来自于各种PropertySource属性源，如：properties文件、
jvm虚拟机环境变量、操作系统环境变量、
JNDI、Servlet上下文参数、自定义的属性对象、Map对象，等等。
Environment环境对象为用户提供了方便的接口，用于配置和使用属性源。

刚才提到环境模型具有2个关键方面：profiles和properties，从体系图中可以看出，properties方面的所有功能由PropertyResolver
属性解决器来实现，环境模型只是通过装饰模式，在PropertyResolver功能的基础上，额外扩展出了profiles方面的功能。
因此在接口方面，Environment继承自PropertyResolver，从实现类方面，AbstractEnvironment类内部持有一个
PropertySourcesPropertyResolver类型对象的引用。

关于PropertyResolver，我前边的文章已经进行了详细的解释，因此在本文中，我们重点关注环境模型在profiles方面的实现原理，体系图如下：
 */
public interface Environment extends PropertyResolver {

	/**
	 * Return the set of profiles explicitly made active for this environment. Profiles
	 * are used for creating logical groupings of bean definitions to be registered
	 * conditionally, for example based on deployment environment.  Profiles can be
	 * activated by setting {@linkplain AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 * "spring.profiles.active"} as a system property or by calling
	 * {@link ConfigurableEnvironment#setActiveProfiles(String...)}.
	 * <p>If no profiles have explicitly been specified as active, then any {@linkplain
	 * #getDefaultProfiles() default profiles} will automatically be activated.
	 * @see #getDefaultProfiles
	 * @see ConfigurableEnvironment#setActiveProfiles
	 * @see AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 * 获取当前环境对象激活的所有profile组。
	 */
	String[] getActiveProfiles();

	/**
	 * Return the set of profiles to be active by default when no active profiles have
	 * been set explicitly.
	 * @see #getActiveProfiles
	 * @see ConfigurableEnvironment#setDefaultProfiles
	 * @see AbstractEnvironment#DEFAULT_PROFILES_PROPERTY_NAME
	 * 获取默认的profile组。
	 * 如果当前环境对象中激活的组为空（getActiveProfiles()返回空数组）的话，
	 * 则会启用默认profile组。
	 */
	String[] getDefaultProfiles();

	/**
	 * Return whether one or more of the given profiles is active or, in the case of no
	 * explicit active profiles, whether one or more of the given profiles is included in
	 * the set of default profiles. If a profile begins with '!' the logic is inverted,
	 * i.e. the method will return true if the given profile is <em>not</em> active.
	 * For example, <pre class="code">env.acceptsProfiles("p1", "!p2")</pre> will
	 * return {@code true} if profile 'p1' is active or 'p2' is not active.
	 * @throws IllegalArgumentException if called with zero arguments
	 * or if any profile is {@code null}, empty or whitespace-only
	 * @see #getActiveProfiles
	 * @see #getDefaultProfiles
	 *
	 * 刚才提到环境模型具有2个关键方面：profiles和properties，从体系图中可以看出，properties方面的所有功能由PropertyResolver
	 * 属性解决器来实现，环境模型只是通过装饰模式，在PropertyResolver功能的基础上，额外扩展出了profiles方面的功能。
	 * 因此在接口方面，Environment继承自PropertyResolver，从实现类方面，
	 * AbstractEnvironment类内部持有一个PropertySourcesPropertyResolver类型对象的引用。

	   关于PropertyResolver，我前边的文章已经进行了详细的解释，因此在本文中，我们重点关注环境模型在profiles方面的实现原理，体系图如下：
	 */
	boolean acceptsProfiles(String... profiles);

}
