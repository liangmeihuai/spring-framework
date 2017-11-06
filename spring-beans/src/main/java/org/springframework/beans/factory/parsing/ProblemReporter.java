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

/**
 * SPI interface allowing tools and other external processes to handle errors
 * and warnings reported during bean definition parsing.
 *SPI接口，在bean definition解析过程中允许工具和其它外部的处理来解决报告的错误和警告
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see Problem
 */
public interface ProblemReporter {

	/**
	 * Called when a fatal error is encountered during the parsing process.
	 * 当在解析过程中遇到一个致命性错误时候被调用
	 * <p>Implementations must treat the given problem as fatal,
	 * i.e. they have to eventually raise an exception.
	 * 实现必须处理致命问题，也就是说，它们不得不最后抛出一个异常
	 * @param problem the source of the error (never {@code null})
	 * 错误源(永不为null)
	 */
	void fatal(Problem problem);

	/**
	 * Called when an error is encountered during the parsing process.
	 * 当在解析过程中遇到一个错误时候被调用。
	 * <p>Implementations may choose to treat errors as fatal.
	 * 实现可以选择把这个错误当做致命性错误
	 * @param problem the source of the error (never {@code null})
	 *  错误源(永不为null)
	 */
	void error(Problem problem);

	/**
	 * Called when a warning is raised during the parsing process.
	 * 当在解析过程中抛出一个警告时被调用
	 * <p>Warnings are <strong>never</strong> considered to be fatal.
	 * 警告永远不会被当做fatal
	 * @param problem the source of the warning (never {@code null})
	 * 警告源(永不为null)
	 */
	void warning(Problem problem);

}
