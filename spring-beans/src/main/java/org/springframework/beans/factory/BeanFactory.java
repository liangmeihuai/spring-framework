/*
 * Copyright 2002-2014 the original author or authors.
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

import org.springframework.beans.BeansException;

/**
 * The root interface for accessing a Spring bean container.
 * This is the basic client view of a bean container;
 * further interfaces such as {@link ListableBeanFactory} and
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * are available for specific purposes.
 *
 * <p>This interface is implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. Depending on the bean definition,
 * the factory will return either an independent instance of a contained object
 * (the Prototype design pattern), or a single shared instance (a superior
 * alternative to the Singleton design pattern, in which the instance is a
 * singleton in the scope of the factory). Which type of instance will be returned
 * depends on the bean factory configuration: the API is the same. Since Spring
 * 2.0, further scopes are available depending on the concrete application
 * context (e.g. "request" and "session" scopes in a web environment).
 *
 * <p>The point of this approach is that the BeanFactory is a central registry
 * of application components, and centralizes configuration of application
 * components (no more do individual objects need to read properties files,
 * for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
 * Development" for a discussion of the benefits of this approach.
 *
 * <p>Note that it is generally better to rely on Dependency Injection
 * ("push" configuration) to configure application objects through setters
 * or constructors, rather than use any form of "pull" configuration like a
 * BeanFactory lookup. Spring's Dependency Injection functionality is
 * implemented using this BeanFactory interface and its subinterfaces.
 *
 * <p>Normally a BeanFactory will load bean definitions stored in a configuration
 * source (such as an XML document), and use the {@code org.springframework.beans}
 * package to configure the beans. However, an implementation could simply return
 * Java objects it creates as necessary directly in Java code. There are no
 * constraints on how the definitions could be stored: LDAP, RDBMS, XML,
 * properties file, etc. Implementations are encouraged to support references
 * amongst beans (Dependency Injection).
 *
 * <p>In contrast to the methods in {@link ListableBeanFactory}, all of the
 * operations in this interface will also check parent factories if this is a
 * {@link HierarchicalBeanFactory}. If a bean is not found in this factory instance,
 * the immediate parent factory will be asked. Beans in this factory instance
 * are supposed to override beans of the same name in any parent factory.
 *
 * <p>Bean factory implementations should support the standard bean lifecycle interfaces
 * as far as possible. The full set of initialization methods and their standard order is:<br>
 * 1. BeanNameAware's {@code setBeanName}<br>
 * 2. BeanClassLoaderAware's {@code setBeanClassLoader}<br>
 * 3. BeanFactoryAware's {@code setBeanFactory}<br>
 * 4. ResourceLoaderAware's {@code setResourceLoader}
 * (only applicable when running in an application context)<br>
 * 5. ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
 * (only applicable when running in an application context)<br>
 * 6. MessageSourceAware's {@code setMessageSource}
 * (only applicable when running in an application context)<br>
 * 7. ApplicationContextAware's {@code setApplicationContext}
 * (only applicable when running in an application context)<br>
 * 8. ServletContextAware's {@code setServletContext}
 * (only applicable when running in a web application context)<br>
 * 9. {@code postProcessBeforeInitialization} methods of BeanPostProcessors<br>
 * 10. InitializingBean's {@code afterPropertiesSet}<br>
 * 11. a custom init-method definition<br>
 * 12. {@code postProcessAfterInitialization} methods of BeanPostProcessors
 *
 * <p>On shutdown of a bean factory, the following lifecycle methods apply:<br>
 * 1. DisposableBean's {@code destroy}<br>
 * 2. a custom destroy-method definition
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 13 April 2001
 * @see BeanNameAware#setBeanName
 * @see BeanClassLoaderAware#setBeanClassLoader
 * @see BeanFactoryAware#setBeanFactory
 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware#setMessageSource
 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
 * @see org.springframework.web.context.ServletContextAware#setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization
 * @see InitializingBean#afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization
 * @see DisposableBean#destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 *
 *
Spring源码解析 - BeanFactory接口体系解读

不知道为什么看着Spring的源码,感触最深的是Spring对概念的抽象,所以我就先学接口了.



BeanFactory是Spring IOC实现的基础,这边定义了一系列的接口,我们通过这些接口的学习,可以大致了解BeanFactory体系各接口如何分工合作.

为学习具体实现打下基础.毕竟这边逻辑复杂,涉及的概念很多.



BeanFactory 是Spring bean容器的根接口.提供获取bean,是否包含bean,是否单例与原型,获取bean类型,bean 别名的api.

-- AutowireCapableBeanFactory 添加集成其他框架功能.如果集成WebWork则可以使用Spring对Actions等进行管理.

-- HierarchicalBeanFactory 提供父容器的访问功能

-- -- ConfigurableBeanFactory 如名,提供factory的配置功能,眼花缭乱好多api

-- -- -- ConfigurableListableBeanFactory 集大成者,提供解析,修改bean定义,并与初始化单例.

-- ListableBeanFactory 提供容器内bean实例的枚举功能.这边不会考虑父容器内的实例.



看到这边,我们是不是想起了设计模式原则里的接口隔离原则

Interface Segregation Principle(ISP)：客户端不应该依赖它不需要的接口；类间的依赖关系应该建立在最小的接口上

对这个有兴趣的话,找度娘或者看看这个设计模式六大原则（4）：接口隔离原则



这边清晰地定义了如下的体系:

　　根接口BeanFactory(基础容器)

　　第二层: 第三方集成,继承体系,遍历bean

　　第三层: 配置功能

　　第四层: 配置+迭代



接下来具体分析下各个接口吧(顺便做目录):

1. BeanFactory

2. AutowireCapableBeanFactory

3. HierarchicalBeanFactory

4. ListableBeanFactory

5. ConfigurableBeanFactory

6. ConfigurableListableBeanFactory




1. BeanFactory

BeanFactory是Spring bean容器的根接口.

每个bean都是通过string类型bean name进行标识.这边提供了设计模式单例,原型的替代实现.

　　如果bean name配置为单例,应用内只会获取到一个实例.如果配置为原型,那么可以实例化好后填充属性(基于用户的配置).

BeanFactory作为应用集中配置管理的地方,极大简便应用开发,这样开发人员可以集中与业务.



BeanFactory需要管理bean的生命周期,比如初始化时需要按顺序实现如下接口:

　　1. BeanNameAware's {@code setBeanName}
　　2. BeanClassLoaderAware's {@code setBeanClassLoader}
　　3. BeanFactoryAware's {@code setBeanFactory}
　　4. ResourceLoaderAware's {@code setResourceLoader}仅对application context有效
　　5. ApplicationEventPublisherAware's {@code setApplicationEventPublisher}仅对application context有效
　　6. MessageSourceAware's {@code setMessageSource}仅对application context有效
　　7. ApplicationContextAware's {@code setApplicationContext}仅对application context有效
　　8. ServletContextAware's {@code setServletContext}仅对application context有效
　　9. {@code postProcessBeforeInitialization} methods of BeanPostProcessors
　　10. InitializingBean's {@code afterPropertiesSet}
　　11. a custom init-method definition xml中配置的init-method
　　12. {@code postProcessAfterInitialization} methods of BeanPostProcessors

还有关闭容器的接口:

　　1. DisposableBean's {@code destroy}
　　2. a custom destroy-method definition xml配置中的destroy-method



接口里定义了一个变量String FACTORY_BEAN_PREFIX = "&";

　　这是用来区分是获取FactoryBean还是FactoryBean的createBean创建的实例.如果&开始则获取FactoryBean;否则获取createBean创建的实例.

我们来看下定义的方法:

　　a, 获取bean,这边可以实现单例,原型

　　　　Object getBean(String name) throws BeansException; 可以用别名查找哦

　　　　<T> T getBean(String name, Class<T> requiredType) throws BeansException;

　　　　<T> T getBean(Class<T> requiredType) throws BeansException; 这边的类型可以是接口或者子类,但不能是null

　　　　Object getBean(String name, Object... args) throws BeansException;

　　b, 判断是否包含bean.陷阱出现:这边不管类是否抽象类,懒加载,是否在容器范围内,只要符合都返回true,所以这边true,不一定能从getBean获取实例

　　　　boolean containsBean(String name);

　　c, 单例,原型,bean类型的判断

　　　　boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

　　　　boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

　　　　boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;

　　d, 获取bean 的类型,别名

　　　　Class<?> getType(String name) throws NoSuchBeanDefinitionException;

　　　　String[] getAliases(String name);



2. AutowireCapableBeanFactory

在BeanFactory基础上实现对已存在实例的管理.

可以使用这个接口集成其它框架,捆绑并填充并不由Spring管理生命周期并已存在的实例.像集成WebWork的Actions 和Tapestry Page就很实用.

一般应用开发者不会使用这个接口,所以像ApplicationContext这样的外观实现类不会实现这个接口,如果真手痒痒可以通过ApplicationContext的getAutowireCapableBeanFactory接口获取.



这边定义了5种自动装配策略:不注入AUTOWIRE_NO,使用bean name策略装配AUTOWIRE_BY_NAME,使用类型装配策略AUTOWIRE_BY_TYPE,使用构造器装配策略AUTOWIRE_CONSTRUCTOR,自动装配策略AUTOWIRE_AUTODETECT

　　这边的自动策略是先尝试构造器,然后才是byType.这边应该是跟xml配置文件中的装配策略对应.

继续看定义的api:

　　a, 创建和填充外部bean实例的典型方法

　　　　<T> T createBean(Class<T> beanClass) throws BeansException;

　　　　void autowireBean(Object existingBean) throws BeansException; // 使用autowireBeanProperties装配属性

　　　　Object configureBean(Object existingBean, String beanName) throws BeansException; // 自动装配属性,填充属性值,使用诸如setBeanName,setBeanFactory这样的工厂回调填充属性,最好还要调用post processor

　　　　Object resolveDependency(DependencyDescriptor descriptor, String beanName) throws BeansException;

　　b, 在bean的生命周期进行细粒度控制的专门方法

　　　　Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException; // 会执行bean完整的初始化,包括BeanPostProcessors和initializeBean

　　　　Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

　　　　void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws BeansException;

　　　　void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException;

　　　　Object initializeBean(Object existingBean, String beanName) throws BeansException;

　　　　Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

　　　　Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;

　　　　Object resolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException;



3. HierarchicalBeanFactory

提供父容器的访问功能.至于父容器的设置,需要找ConfigurableBeanFactory的setParentBeanFactory(接口把设置跟获取给拆开了!).

这边可说的不多,直接上api:

　　a, 获取父容器 bean factory

　　　　BeanFactory getParentBeanFactory();

　　b, 判断当前容器是否保护bean

　　　　boolean containsLocalBean(String name);



4. ListableBeanFactory

获取bean时,Spring 鼓励使用这个接口定义的api. 还有个Beanfactory方便使用.其他的4个接口都是不鼓励使用的.

提供容器中bean迭代的功能,不再需要一个个bean地查找.比如可以一次获取全部的bean(太暴力了),根据类型获取bean.在看SpringMVC时,扫描包路径下的具体实现策略就是使用的这种方式(那边使用的是BeanFactoryUtils封装的api).

如果同时实现了HierarchicalBeanFactory,返回值不会考虑父类BeanFactory,只考虑当前factory定义的类.当然也可以使用BeanFactoryUtils辅助类来查找祖先工厂中的类.

这个接口中的方法只会考虑本factory定义的bean.这些方法会忽略ConfigurableBeanFactory的registerSingleton注册的单例bean(getBeanNamesOfType和getBeansOfType是例外,一样会考虑手动注册的单例).当然BeanFactory的getBean一样可以透明访问这些特殊bean.当然在典型情况下,所有的bean都是由external bean定义,所以应用不需要顾虑这些差别.

注意:getBeanDefinitionCount和containsBeanDefinition的实现方法因为效率比较低,还是少用为好.

继续上api吧

　　a, 暴力获取全部bean的属性:

　　　　boolean containsBeanDefinition(String beanName);  //是否包含bean

　　　　int getBeanDefinitionCount();　// 当前factory中定义的bean数量

　　　　String[] getBeanDefinitionNames(); // 获取当前工厂中定义的所有bean 的name

　　b, 根据bean 的类型获取bean

　　　　这边的方法仅检查顶级bean.它不会检查嵌套的bean.FactoryBean创建的bean会匹配为FactoryBean而不是原始类型.

　　　　一样不会考虑父factory中的bean,非要用可以通过BeanFactoryUtils中的beanNamesForTypeIncludingAncestors.
　　　　其他方式注册的单例这边会纳入判断.
　　　　这个版本的getBeanNamesForType会匹配所有类型的bean,包括单例,原型,FactoryBean.返回的bean names会根据backend 配置的进行排序.

　　　　String[] getBeanNamesForType(Class<?> type); // 获取给定类型的bean names(包括子类),通过bean 定义或者FactoryBean的getObjectType判断.

　　　　String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);

　　　　<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException; // 如果保护懒加载的类,FactoryBean初始化的类和工厂方法初始化的类会被初始化.就是说执行这个方法会执行对应的初始化.

　　　　<T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException;

　　c, 查找使用注解的类

　　　　Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException;

　　d, 查找一个类上的注解,如果找不到,父类,接口使用注解也算.

　　　　<A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType);



5. ConfigurableBeanFactory

定义BeanFactory的配置.

这边定义了太多太多的api,比如类加载器,类型转化,属性编辑器,BeanPostProcessor,作用域,bean定义,处理bean依赖关系,合并其他ConfigurableBeanFactory,bean如何销毁.



定义了两个作用域: 单例和原型.可以通过registerScope来添加.

　　SCOPE_SINGLETON,SCOPE_PROTOTYPE

这边定义了好多好多的api,所以我们这边只讲业务,具体的api看文末的附录吧:

　　a, 父容器设置.而且一旦设置了就不让修改

　　b, 类加载器设置与获取.默认使用当前线程中的类加载器

　　c, 为了类型匹配,搞个临时类加载器.好在一般情况为null,使用上面定义的标准加载器　　

　　d, 是否需要缓存bean metadata,比如bean difinition 和 解析好的classes.默认开启缓存

　　e, 定义用于解析bean definition的表达式解析器

　　f, 类型转化器

　　g, 属性编辑器

　　h, BeanFactory用来转换bean属性值或者参数值的自定义转换器

　　i,string值解析器(想起mvc中的ArgumentResolver了)

　　j,大boss BeanPostProcessor用于增强bean初始化功能　

　　k,作用域定义

　　l,访问权限控制

　　m, 合并其他ConfigurableBeanFactory的配置,包括上面说到的BeanPostProcessor,作用域等

　　n, bean定义处理

　　o, bean创建状态控制.在解决循环依赖时有使用

　　p, 处理bean依赖问题

　　q, bean生命周期管理-- 销毁bean







6. ConfigurableListableBeanFactory

提供bean definition的解析,注册功能,再对单例来个预加载(解决循环依赖问题).

貌似我们一般开发就会直接定义这么个接口了事.而不是像Spring这样先根据使用情况细分那么多,到这边再合并



　　a, 设置忽略的依赖关系,注册找到的特殊依赖

　　　　void ignoreDependencyType(Class<?> type); // 忽略类型

　　　　void ignoreDependencyInterface(Class<?> ifc); // 忽略接口

　　　　void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue);

　　　　boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException;

　　b, 获取bean定义 (可以访问属性值跟构造方法的参数值)

　　　　BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

　　c, 锁定配置信息.在调用refresh时会使用到.

　　　　void freezeConfiguration();

　　　　boolean isConfigurationFrozen();

　　d, 预加载不是懒加载的单例.用于解决循环依赖问题

　　　　void preInstantiateSingletons() throws BeansException;





附录--ConfigureableBeanFactory中定义的api:

　　a, 父容器设置.而且一旦设置了就不让修改

　　　　void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

　　b, 类加载器设置与获取.默认使用当前线程中的类加载器

　　　　void setBeanClassLoader(ClassLoader beanClassLoader);

　　　　ClassLoader getBeanClassLoader();

　　c, 为了类型匹配,搞个临时类加载器.好在一般情况为null,使用上面定义的标准加载器　　

　　　　void setTempClassLoader(ClassLoader tempClassLoader);

　　　　ClassLoader getTempClassLoader();

　　d, 是否需要缓存bean metadata,比如bean difinition 和 解析好的classes.默认开启缓存

　　　　void setCacheBeanMetadata(boolean cacheBeanMetadata);

　　　　boolean isCacheBeanMetadata();

　　e, 定义用于解析bean definition的表达式解析器

　　　　void setBeanExpressionResolver(BeanExpressionResolver resolver);

　　　　BeanExpressionResolver getBeanExpressionResolver();

　　f, 类型转化器

　　　　void setConversionService(ConversionService conversionService);

　　　　ConversionService getConversionService();

　　g, 属性编辑器

　　　　void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

　　　　void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

　　　　void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

　　h, BeanFactory用来转换bean属性值或者参数值的自定义转换器

　　　　void setTypeConverter(TypeConverter typeConverter);

　　　　TypeConverter getTypeConverter();

　　i,string值解析器(想起mvc中的ArgumentResolver了)

　　　　void addEmbeddedValueResolver(StringValueResolver valueResolver);

　　　　String resolveEmbeddedValue(String value);

　　j,大boss BeanPostProcessor用于增强bean初始化功能

　　　　void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

　　　　int getBeanPostProcessorCount();　　　　

　　k,作用域定义

　　　　void registerScope(String scopeName, Scope scope);

　　　　String[] getRegisteredScopeNames();

　　　　Scope getRegisteredScope(String scopeName);

　　l,访问权限控制

　　　　AccessControlContext getAccessControlContext();

　　m, 合并其他ConfigurableBeanFactory的配置,包括上面说到的BeanPostProcessor,作用域等

　　　　void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

　　n, bean定义处理

　　　　void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException; // 注册别名

　　　　void resolveAliases(StringValueResolver valueResolver);

　　　　BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException; // 合并bean定义,包括父容器的

　　　　boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException; // 是否是FactoryBean类型

　　o, bean创建状态控制.在解决循环依赖时有使用

　　　　void setCurrentlyInCreation(String beanName, boolean inCreation);

　　　　boolean isCurrentlyInCreation(String beanName);

　　p, 处理bean依赖问题

　　　　void registerDependentBean(String beanName, String dependentBeanName);

　　　　String[] getDependentBeans(String beanName);

　　　　String[] getDependenciesForBean(String beanName);

　　q, bean生命周期管理-- 销毁bean

　　　　void destroyBean(String beanName, Object beanInstance);

　　　　void destroyScopedBean(String beanName);

　　　　void destroySingletons();



分类: spring
 */
public interface BeanFactory {

	/**
	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
	 * will return the factory, not the instance returned by the factory.
	 */
	String FACTORY_BEAN_PREFIX = "&";

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>This method allows a Spring BeanFactory to be used as a replacement for the
	 * Singleton or Prototype design pattern. Callers may retain references to
	 * returned objects in the case of Singleton beans.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to retrieve
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no bean definition
	 * with the specified name
	 * @throws BeansException if the bean could not be obtained
	 */
	Object getBean(String name) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Behaves the same as {@link #getBean(String)}, but provides a measure of type
	 * safety by throwing a BeanNotOfRequiredTypeException if the bean is not of the
	 * required type. This means that ClassCastException can't be thrown on casting
	 * the result correctly, as can happen with {@link #getBean(String)}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to retrieve
	 * @param requiredType type the bean must match. Can be an interface or superclass
	 * of the actual class, or {@code null} for any match. For example, if the value
	 * is {@code Object.class}, this method will succeed whatever the class of the
	 * returned instance.
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanNotOfRequiredTypeException if the bean is not of the required type
	 * @throws BeansException if the bean could not be created
	 */
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;

	/**
	 * Return the bean instance that uniquely matches the given object type, if any.
	 * @param requiredType type the bean must match; can be an interface or superclass.
	 * {@code null} is disallowed.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 * @return an instance of the single bean matching the required type
	 * @throws NoSuchBeanDefinitionException if no bean of the given type was found
	 * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
	 * @since 3.0
	 * @see ListableBeanFactory
	 */
	<T> T getBean(Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 * @param name the name of the bean to retrieve
	 * @param args arguments to use if creating a prototype using explicit arguments
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype
	 * @throws BeansException if the bean could not be created
	 * @since 2.5
	 */
	Object getBean(String name, Object... args) throws BeansException;

	/**
	 * Does this bean factory contain a bean definition or externally registered singleton
	 * instance with the given name?
	 * <p>If the given name is an alias, it will be translated back to the corresponding
	 * canonical bean name.
	 * <p>If this factory is hierarchical, will ask any parent factory if the bean cannot
	 * be found in this factory instance.
	 * <p>If a bean definition or singleton instance matching the given name is found,
	 * this method will return {@code true} whether the named bean definition is concrete
	 * or abstract, lazy or eager, in scope or not. Therefore, note that a {@code true}
	 * return value from this method does not necessarily indicate that {@link #getBean}
	 * will be able to obtain an instance for the same name.
	 * @param name the name of the bean to query
	 * @return whether a bean with the given name is present
	 */
	boolean containsBean(String name);

	/**
	 * Is this bean a shared singleton? That is, will {@link #getBean} always
	 * return the same instance?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * independent instances. It indicates non-singleton instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isPrototype} operation to explicitly
	 * check for independent instances.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return whether this bean corresponds to a singleton instance
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @see #getBean
	 * @see #isPrototype
	 */
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Is this bean a prototype? That is, will {@link #getBean} always return
	 * independent instances?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * a singleton object. It indicates non-independent instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isSingleton} operation to explicitly
	 * check for a shared singleton instance.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return whether this bean will always deliver independent instances
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.3
	 * @see #getBean
	 * @see #isSingleton
	 */
	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @param targetType the type to match against
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.1
	 * @see #getBean
	 * @see #getType
	 */
	boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;

	/**
	 * Determine the type of the bean with the given name. More specifically,
	 * determine the type of object that {@link #getBean} would return for the given name.
	 * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
	 * as exposed by {@link FactoryBean#getObjectType()}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return the type of the bean, or {@code null} if not determinable
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 1.1.2
	 * @see #getBean
	 * @see #isTypeMatch
	 */
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Return the aliases for the given bean name, if any.
	 * All of those aliases point to the same bean when used in a {@link #getBean} call.
	 * <p>If the given name is an alias, the corresponding original bean name
	 * and other aliases (if any) will be returned, with the original bean name
	 * being the first element in the array.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the bean name to check for aliases
	 * @return the aliases, or an empty array if none
	 * @see #getBean
	 */
	String[] getAliases(String name);

}
