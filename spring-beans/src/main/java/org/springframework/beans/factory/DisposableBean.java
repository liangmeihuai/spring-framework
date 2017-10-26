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

package org.springframework.beans.factory;

/**
 * Interface to be implemented by beans that want to release resources
 * on destruction. A BeanFactory is supposed to invoke the destroy
 * method if it disposes a cached singleton. An application context
 * is supposed to dispose all of its singletons on close.
 *此接口由想要在销毁对象的时候释放资源的bean来实现
 * 一个BeanFactory如果处理了一个缓存的单例，应该要调用destroy方法
 * 一个application上下文在它关闭的时候应该要销毁它所有的单例
 * [
 * google翻译:要由销毁资源释放的bean实现的接口。 如果BeanFactory处理缓存的单例，则应该调用destroy方法。 应用程序的上下文应该是将其所有的单例处理完成。
 * ]
 * <p>An alternative to implementing DisposableBean is specifying a custom
 * destroy-method, for example in an XML bean definition.
 * For a list of all bean lifecycle methods, see the BeanFactory javadocs.
 *另外一种选择去实现DisposableBean[一次性的]就是通过指定一个定制的destroy-method,例如
 * 在xml的bean定义中指定destroy-method方法
 * 对于要看到bean的所有生命周期的方法，具体参考哦beanFactory文档
 * @author Juergen Hoeller
 * @since 12.08.2003
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 * @see org.springframework.context.ConfigurableApplicationContext#close
 */
public interface DisposableBean {

	/**
	 * Invoked by a BeanFactory on destruction of a singleton.
	 * 由一个beanfactory在销毁一个单例时候调用
	 * @throws Exception in case of shutdown errors.
	 * 抛出异常以防关闭时候的错误
	 * Exceptions will get logged but not rethrown to allow
	 * 异常将会被记录，但是不会重新抛出来允许其它beans也能够释放它们的资源。
	 * other beans to release their resources too.
	 */
	void destroy() throws Exception;

}
