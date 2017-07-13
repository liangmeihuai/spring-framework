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

package org.springframework.beans.factory.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

/**
 * Base interface used by the {@link DefaultBeanDefinitionDocumentReader}
 * for handling custom namespaces in a Spring XML configuration file.
 *
 * <p>Implementations are expected to return implementations of the
 * {@link BeanDefinitionParser} interface for custom top-level tags and
 * implementations of the {@link BeanDefinitionDecorator} interface for
 * custom nested tags.
 *
 * <p>The parser will call {@link #parse} when it encounters a custom tag
 * directly under the {@code &lt;beans&gt;} tags and {@link #decorate} when
 * it encounters a custom tag directly under a {@code &lt;bean&gt;} tag.
 *
 * <p>Developers writing their own custom element extensions typically will
 * not implement this interface directly, but rather make use of the provided
 * {@link NamespaceHandlerSupport} class.
 *
 * @author Rob Harrop
 * @author Erik Wiersma
 * @since 2.0
 * @see DefaultBeanDefinitionDocumentReader
 * @see NamespaceHandlerResolver
 *
 *
 * 基于Spring可扩展Schema提供自定义配置支持
标签： schemaspring扩展stringbean工作
2010-09-05 14:02 24909人阅读 评论(7) 收藏 举报
分类： 技术积累（62）
版权声明：本文为博主原创文章，未经博主允许不得转载。

在很多情况下，我们需要为系统提供可配置化支持，简单的做法可以直接基于spring的标准Bean来配置，但配置较为复杂或者需要更多丰富控制的时候，会显得非常笨拙。一般的做法会用原生态的方式去解析定义好的xml文件，然后转化为配置对象，这种方式当然可以解决所有问题，但实现起来比较繁琐，特别是是在配置非常复杂的时候，解析工作是一个不得不考虑的负担。Spring提供了可扩展Schema的支持，这是一个不错的折中方案，完成一个自定义配置一般需要以下步骤：

设计配置属性和JavaBean
编写XSD文件
编写NamespaceHandler和BeanDefinitionParser完成解析工作
编写spring.handlers和spring.schemas串联起所有部件
在Bean文件中应用
下面结合一个小例子来实战以上过程

1）设计配置属性和JavaBean

首先当然得设计好配置项，并通过JavaBean来建模，本例中需要配置People实体，配置属性name和age（id是默认需要的）

[java] view plain copy
public class People {
private String id;
private String name;
private Integer age;
}
2）编写XSD文件

为上一步设计好的配置项编写XSD文件，XSD是schema的定义文件，配置的输入和解析输出都是以XSD为契约，本例中XSD如下：

[xhtml] view plain copy
<?xml version="1.0" encoding="UTF-8"?>
<xsd:schema
xmlns="http://blog.csdn<a href="http://lib.csdn.net/base/dotnet" class='replace_word' title=".NET知识库" target='_blank' style='color:#df3434; font-weight:bold;'>.NET</a>/cutesource/schema/people"
xmlns:xsd="http://www.w3.org/2001/XMLSchema"
xmlns:beans="http://www.springframework.org/schema/beans"
targetNamespace="http://blog.csdn<a href="http://lib.csdn.net/base/dotnet" class='replace_word' title=".NET知识库" target='_blank' style='color:#df3434; font-weight:bold;'>.net</a>/cutesource/schema/people"
elementFormDefault="qualified"
attributeFormDefault="unqualified">
<xsd:import namespace="http://www.springframework.org/schema/beans" />
<xsd:element name="people">
<xsd:complexType>
<xsd:complexContent>
<xsd:extension base="beans:identifiedType">
<xsd:attribute name="name" type="xsd:string" />
<xsd:attribute name="age" type="xsd:int" />
</xsd:extension>
</xsd:complexContent>
</xsd:complexType>
</xsd:element>
</xsd:schema>
关于xsd:schema的各个属性具体含义就不作过多解释，可以参见http://www.w3school.com.cn/schema/schema_schema.asp

<xsd:element name="people">对应着配置项节点的名称，因此在应用中会用people作为节点名来引用这个配置

<xsd:attribute name="name" type="xsd:string" />和<xsd:attribute name="age" type="xsd:int" />对应着配置项people的两个属性名，因此在应用中可以配置name和age两个属性，分别是string和int类型

完成后需把xsd存放在classpath下，一般都放在META-INF目录下（本例就放在这个目录下）

3）编写NamespaceHandler和BeanDefinitionParser完成解析工作

下面需要完成解析工作，会用到NamespaceHandler和BeanDefinitionParser这两个概念。具体说来NamespaceHandler会根据schema和节点名找到某个BeanDefinitionParser，然后由BeanDefinitionParser完成具体的解析工作。因此需要分别完成NamespaceHandler和BeanDefinitionParser的实现类，Spring提供了默认实现类NamespaceHandlerSupport和AbstractSingleBeanDefinitionParser，简单的方式就是去继承这两个类。本例就是采取这种方式：

[java] view plain copy
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
public class MyNamespaceHandler extends NamespaceHandlerSupport {
public void init() {
registerBeanDefinitionParser("people", new PeopleBeanDefinitionParser());
}
}
其中registerBeanDefinitionParser("people", new PeopleBeanDefinitionParser());就是用来把节点名和解析类联系起来，在配置中引用people配置项时，就会用PeopleBeanDefinitionParser来解析配置。PeopleBeanDefinitionParser就是本例中的解析类：

[java] view plain copy
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
public class PeopleBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
protected Class getBeanClass(Element element) {
return People.class;
}
protected void doParse(Element element, BeanDefinitionBuilder bean) {
String name = element.getAttribute("name");
String age = element.getAttribute("age");
String id = element.getAttribute("id");
if (StringUtils.hasText(id)) {
bean.addPropertyValue("id", id);
}
if (StringUtils.hasText(name)) {
bean.addPropertyValue("name", name);
}
if (StringUtils.hasText(age)) {
bean.addPropertyValue("age", Integer.valueOf(age));
}
}
}
其中element.getAttribute就是用配置中取得属性值，bean.addPropertyValue就是把属性值放到bean中。

4）编写spring.handlers和spring.schemas串联起所有部件

上面几个步骤走下来会发现开发好的handler与xsd还没法让应用感知到，就这样放上去是没法把前面做的工作纳入体系中的，spring提供了spring.handlers和spring.schemas这两个配置文件来完成这项工作，这两个文件需要我们自己编写并放入META-INF文件夹中，这两个文件的地址必须是META-INF/spring.handlers和META-INF/spring.schemas，spring会默认去载入它们，本例中spring.handlers如下所示：

http/://blog.csdn.Net/cutesource/schema/people=study.schemaExt.MyNamespaceHandler

以上表示当使用到名为"http://blog.csdn.net/cutesource/schema/people"的schema引用时，会通过study.schemaExt.MyNamespaceHandler来完成解析

spring.schemas如下所示：
http/://blog.csdn.net/cutesource/schema/people.xsd=META-INF/people.xsd

以上就是载入xsd文件

5）在Bean文件中应用

到此为止一个简单的自定义配置以完成，可以在具体应用中使用了。使用方法很简单，和配置一个普通的spring bean类似，只不过需要基于我们自定义schema，本例中引用方式如下所示：

[xhtml] view plain copy
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:cutesource="http://blog.csdn.net/cutesource/schema/people"
xsi:schemaLocation="
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
http://blog.csdn.net/cutesource/schema/people http://blog.csdn.net/cutesource/schema/people.xsd">
<cutesource:people id="cutesource" name="袁志俊" age="27"/>
</beans>
其中xmlns:cutesource="http://blog.csdn.net/cutesource/schema/people"是用来指定自定义schema，xsi:schemaLocation用来指定xsd文件。<cutesource:people id="cutesource" name="zhijun.yuanzj" age="27"/>是一个具体的自定义配置使用实例。

最后就可以在具体程序中使用基本的bean载入方式来载入我们的自定义配置对象了，如：

[java] view plain copy
ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
People p = (People)ctx.getBean("cutesource");
System.out.println(p.getId());
System.out.println(p.getName());
System.out.println(p.getAge());
会输出：

cutesource
袁志俊
27



以上就是一个基于Spring可扩展Schema提供自定义配置支持实战过程，一些复杂应用和技巧还有待挖掘



顶
9
踩
2


上一篇分布式设计与开发（六）------让memcached分布式
下一篇基于Unitils和Spring解决一些单元测试的常见问题
相关文章推荐
• Spring Boot 部署与服务配置
• 对Spring的一些理解
• Spring注解@Component、@Repository、@Service、@Controller区别
• Spring 整合 Redis
• Spring注解@Component、@Repository、@Service、@Controller区别
• Spring读书笔记-----Spring核心机制：依赖注入
• Spring Boot 快速入门
• SpringMVC+Spring3+Hibernate4开发环境搭建
• 基于Spring可扩展Schema提供自定义配置支持
• 基于Spring可扩展Schema提供自定义配置支持（转载）
猜你在找
机器学习之概率与统计推断 机器学习之数学基础 机器学习之凸优化 机器学习之矩阵 响应式布局全新探索 探究Linux的总线、设备、驱动模型 深度学习基础与TensorFlow实践 深度学习之神经网络原理与实战技巧 前端开发在线峰会 TensorFlow实战进阶：手把手教你做图像识别应用
查看评论
7楼 Oliver949 2017-05-30 17:30发表 [回复]

可以转载吗？
6楼 joseph_cool 2016-05-18 19:02发表 [回复]

Parser的子类需要重写getBeanClass方法，不然运行报错。


protected Class getBeanClass(Element element) {
return People.class;
}

血的教训...
5楼 Shldreamfly 2016-04-23 19:39发表 [回复]

将的还可以。
4楼 程序员A 2014-04-03 16:42发表 [回复]

请问一下，怎样将spring.handler和spring.schema这两个文件放到META-INF中去，我在打包时，这个文件是自动生成的啊，先谢谢了
3楼 sunny3super 2012-07-16 10:47发表 [回复]

其实博主还是说清楚了的，只是可能我们理解有问题。我参考博主的东西，自己实现了一个。
我在这里写了一篇文章，参考博主的，并提供了代码下载
http://www.3822.net/topicshow/5802
2楼 ykdsg 2012-01-30 17:18发表 [回复]

spring.handlers和spring.schemas 的文件写错了
http/://blog.csdn.net/cutesource/schema/people=study.schemaExt.MyNamespaceHandler

http/://blog.csdn.net/cutesource/schema/people.xsd=META-INF/people.xsd

http后面应该用“\”

http\://blog.csdn.net/cutesource/schema/people=study.schemaExt.MyNamespaceHandler

http\://blog.csdn.net/cutesource/schema/people.xsd=META-INF/people.xsd
这个太让人无语了，折腾很久才发现是这个问题引起的。楼主要认真负责啊
1楼 txbhcml 2010-11-16 17:47发表 [回复]

可以发一个工程源码给我马 我就在搞这个玩意 集成一个自己定义的包 怎么处理都不行 按照你的步骤一步步来还是报错 我想拿你的工程试试 谢谢 txbhcml@163.com
您还没有登录,请[登录]或[注册]
 *
 *
 */
public interface NamespaceHandler {

	/**
	 * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after
	 * construction but before any custom elements are parsed.
	 * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
	 */
	void init();

	/**
	 * Parse the specified {@link Element} and register any resulting
	 * {@link BeanDefinition BeanDefinitions} with the
	 * {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}
	 * that is embedded in the supplied {@link ParserContext}.
	 * <p>Implementations should return the primary {@code BeanDefinition}
	 * that results from the parse phase if they wish to be used nested
	 * inside (for example) a {@code &lt;property&gt;} tag.
	 * <p>Implementations may return {@code null} if they will
	 * <strong>not</strong> be used in a nested scenario.
	 * @param element the element that is to be parsed into one or more {@code BeanDefinitions}
	 * @param parserContext the object encapsulating the current state of the parsing process
	 * @return the primary {@code BeanDefinition} (can be {@code null} as explained above)
	 */
	BeanDefinition parse(Element element, ParserContext parserContext);

	/**
	 * Parse the specified {@link Node} and decorate the supplied
	 * {@link BeanDefinitionHolder}, returning the decorated definition.
	 * <p>The {@link Node} may be either an {@link org.w3c.dom.Attr} or an
	 * {@link Element}, depending on whether a custom attribute or element
	 * is being parsed.
	 * <p>Implementations may choose to return a completely new definition,
	 * which will replace the original definition in the resulting
	 * {@link org.springframework.beans.factory.BeanFactory}.
	 * <p>The supplied {@link ParserContext} can be used to register any
	 * additional beans needed to support the main definition.
	 * @param source the source element or attribute that is to be parsed
	 * @param definition the current bean definition
	 * @param parserContext the object encapsulating the current state of the parsing process
	 * @return the decorated definition (to be registered in the BeanFactory),
	 * or simply the original bean definition if no decoration is required.
	 * A {@code null} value is strictly speaking invalid, but will be leniently
	 * treated like the case where the original bean definition gets returned.
	 */
	BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext);

}
