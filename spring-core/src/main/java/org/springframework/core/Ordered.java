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
 * Interface that can be implemented by objects that should be
 * orderable, for example in a Collection.
 *
 * <p>The actual order can be interpreted as prioritization, with
 * the first object (with the lowest order value) having the highest
 * priority.
 *
 * <p>Note that there is a 'priority' marker for this interface:
 * {@link PriorityOrdered}. Order values expressed by PriorityOrdered
 * objects always apply before order values of 'plain' Ordered values.
 *可以通过按顺序的对象来实现的接口，例如在一个集合中。实际的顺序可以被解释为优先级，
 * 第一个对象（具有最低的顺序值）具有最高的优先级。注意对于这个接口来说有一个优先级的标记：
 * {@ link PriorityOrdered}。 PriorityOrdered对象表示的顺序值始终应用于“普通”有序值的顺序值之前。
 * @author Juergen Hoeller
 * @since 07.04.2003
 * @see OrderComparator
 * @see org.springframework.core.annotation.Order
 */
public interface Ordered {

	/**
	 * Useful constant for the highest precedence value.
	 * 表示最高优先值，使用的常量
	 * @see java.lang.Integer#MIN_VALUE
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * Useful constant for the lowest precedence value.
	 * 表示最低优先值，使用的常量
	 * @see java.lang.Integer#MAX_VALUE
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


	/**
	 * Return the order value of this object, with a
	 * higher value meaning greater in terms of sorting.
	 * <p>Normally starting with 0, with {@code Integer.MAX_VALUE}
	 * indicating the greatest value. Same order values will result
	 * in arbitrary positions for the affected objects.
	 * <p>Higher values can be interpreted as lower priority. As a
	 * consequence, the object with the lowest value has highest priority
	 * (somewhat analogous to Servlet "load-on-startup" values).
	 *返回这个对象的顺序值，[值越大]在排序方面意味着更高的值。
	 *通常从0开始，以{@Code Integer.MAX_VALUE}指示最大值。
	 *相同的顺序值将导致受影响对象的任意位置。较高的值可被解释为较低的优先级。
	 *因此，具有最低值的对象具有最高的优先级（有点类似于Servlet“加载启动”值）。
	 * @return the order value
	 */
	int getOrder();

}
