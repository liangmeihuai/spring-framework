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

package org.springframework.beans.factory.parsing;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;

/**
 * Interface that describes the logical view of a set of {@link BeanDefinition BeanDefinitions}
 * and {@link BeanReference BeanReferences} as presented in some configuration context.
 * Interface在一些配置上下文中描述了一组@{link BeanDefinition BeanDefinitions}和{@link BeanReference BeanReferences}的逻辑视图。
 * <p>With the introduction of {@link org.springframework.beans.factory.xml.NamespaceHandler pluggable custom XML tags},
 * it is now possible for a single logical configuration entity, in this case an XML tag, to
 * 现在，单个逻辑配置实体(在本例中是一个XML标记)成为可能
 * create multiple {@link BeanDefinition BeanDefinitions} and {@link BeanReference RuntimeBeanReferences}
 * in order to provide more succinct configuration and greater convenience to end users.
 *为了提供更简洁的配置和更方便的终端用户
 * As such, it can no longer be assumed that each configuration entity (e.g. XML tag) maps to one {@link BeanDefinition}.
 * 因此，不能再假设每个配置实体(eg,XML标记)映射到一个{@link BeanDefinition}.
 * For tool vendors and other users who wish to present visualization or support for configuring Spring
 * applications it is important that there is some mechanism in place to tie the {@link BeanDefinition BeanDefinitions}
 * 对于希望提供可视化或支持配置Spring应用程序的工具供应商和其他用户来说，有一些机制来约束{@link BeanDefinition BeanDefinitions}这些应用程序是很重要的
 * in the {@link org.springframework.beans.factory.BeanFactory} back to the configuration data in a way that has concrete meaning to the end user.
 * in the {@link org.springframework.beans.factory.BeanFactory} 回到配置数据的方式这对最终用户有具体的意义
 * As such, {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * implementations are able to publish events in the form of a {@code ComponentDefinition} for each
 * logical entity being configured.
 * 实现能够以一个{@代码组件定义}的形式发布事件，为每个被配置的逻辑实体
 * Third parties can then {@link ReaderEventListener subscribe to these events},allowing for a user-centric view of the bean metadata.
 * <p>Each {@code ComponentDefinition} has a {@link #getSource source object} which is configuration-specific.
 * In the case of XML-based configuration this is typically the {@link org.w3c.dom.Node} which contains the user
 * supplied configuration information. In addition to this, each {@link BeanDefinition} enclosed in a
 * {@code ComponentDefinition} has its own {@link BeanDefinition#getSource() source object} which may point
 * to a different, more specific, set of configuration data.
 * Beyond this, individual pieces of bean metadata such as the {@link org.springframework.beans.PropertyValue PropertyValues} may also have a source object giving an
 * even greater level of detail. Source object extraction is handled through the
 * {@link SourceExtractor} which can be customized as required.
 *
 * <p>Whilst direct access to important {@link BeanReference BeanReferences} is provided through
 * {@link #getBeanReferences}, tools may wish to inspect all {@link BeanDefinition BeanDefinitions} to gather
 * the full set of {@link BeanReference BeanReferences}. Implementations are required to provide
 * all {@link BeanReference BeanReferences} that are required to validate the configuration of the
 * overall logical entity as well as those required to provide full user visualisation of the configuration.
 * It is expected that certain {@link BeanReference BeanReferences} will not be important to
 * validation or to the user view of the configuration and as such these may be ommitted. A tool may wish to
 * display any additional {@link BeanReference BeanReferences} sourced through the supplied
 * {@link BeanDefinition BeanDefinitions} but this is not considered to be a typical case.
 *
 * <p>Tools can determine the important of contained {@link BeanDefinition BeanDefinitions} by checking the
 * {@link BeanDefinition#getRole role identifier}. The role is essentially a hint to the tool as to how
 * important the configuration provider believes a {@link BeanDefinition} is to the end user. It is expected
 * that tools will <strong>not</strong> display all {@link BeanDefinition BeanDefinitions} for a given
 * {@code ComponentDefinition} choosing instead to filter based on the role. Tools may choose to make
 * this filtering user configurable. Particular notice should be given to the
 * {@link BeanDefinition#ROLE_INFRASTRUCTURE INFRASTRUCTURE role identifier}. {@link BeanDefinition BeanDefinitions}
 * classified with this role are completely unimportant to the end user and are required only for
 * internal implementation reasons.
 *描述在同一配置的Context中一组BeanDefinition和BeanReference的逻辑视图的接口。
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see AbstractComponentDefinition
 * @see CompositeComponentDefinition
 * @see BeanComponentDefinition
 * @see ReaderEventListener#componentRegistered(ComponentDefinition)
 */
public interface ComponentDefinition extends BeanMetadataElement {

	/**
	 * Get the user-visible name of this {@code ComponentDefinition}.
	 * <p>This should link back directly to the corresponding configuration data
	 * for this component in a given context.
	 * 这应该直接链接到给定上下文中的该组件的相应配置数据
	 */
	String getName();

	/**
	 * Return a friendly description of the described component.
	 * <p>Implementations are encouraged to return the same value from
	 * {@code toString()}.
	 */
	String getDescription();

	/**
	 * Return the {@link BeanDefinition BeanDefinitions} that were registered
	 * to form this {@code ComponentDefinition}.
	 * <p>It should be noted that a {@code ComponentDefinition} may well be related with
	 * other {@link BeanDefinition BeanDefinitions} via {@link BeanReference references},
	 * however these are <strong>not</strong> included as they may be not available immediately.
	 * Important {@link BeanReference BeanReferences} are available from {@link #getBeanReferences()}.
	 * @return the array of BeanDefinitions, or an empty array if none
	 */
	BeanDefinition[] getBeanDefinitions();

	/**
	 * Return the {@link BeanDefinition BeanDefinitions} that represent all relevant
	 * inner beans within this component.
	 * <p>Other inner beans may exist within the associated {@link BeanDefinition BeanDefinitions},
	 * however these are not considered to be needed for validation or for user visualization.
	 * @return the array of BeanDefinitions, or an empty array if none
	 */
	BeanDefinition[] getInnerBeanDefinitions();

	/**
	 * Return the set of {@link BeanReference BeanReferences} that are considered
	 * to be important to this {@code ComponentDefinition}.
	 * <p>Other {@link BeanReference BeanReferences} may exist within the associated
	 * {@link BeanDefinition BeanDefinitions}, however these are not considered
	 * to be needed for validation or for user visualization.
	 * @return the array of BeanReferences, or an empty array if none
	 */
	BeanReference[] getBeanReferences();

}
