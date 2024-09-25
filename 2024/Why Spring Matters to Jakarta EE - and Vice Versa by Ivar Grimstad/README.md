# 相辅相成的 Jakarta EE 与 Spring


## Java 的发展历程

Java 语言自 1995 年诞生以来，最初被设计为一种客户端技术，旨在应用于设备和浏览器中。然而，随着时间的推移，Java 在服务器端的表现优异，成为开发企业应用程序的首选。1999 年，J2EE（Java 2 Platform， Enterprise Edition）的推出，进一步巩固了 Java 在企业应用开发中的地位。

J2EE 虽然成功，但其复杂性也引发了一些批评。为了简化 J2EE 的繁杂配置，Spring 框架于 2004 年推出，通过简化配置和增强灵活性，迅速获得了开发者的青睐。

## 从 J2EE 到 Jakarta EE

Java EE 是 J2EE 的继任者，提供了依赖注入（Dependency Injection）和基于注解的配置，减少了对 XML 配置的依赖。到 2013 年，Java EE 7 的发布标志着其功能的完善，与 Spring 的差距逐渐缩小。

2017 年，Java EE 项目转移至 Eclipse 基金会，并更名为 Jakarta EE，标志着其完全开源化。2020 年，Jakarta EE 9 发布，进行了一次重要的命名空间变更，这对整个 Java 生态系统产生了深远影响。

## Spring 与 Jakarta EE 的相互关系

Spring 框架建立在 Jakarta EE 之上，利用其提供的 API 和实现。在 Spring Boot 2 到 Spring Boot 3 的迁移过程中，许多开发者首次感受到 Jakarta EE 的影响，特别是在命名空间变更方面（从 `javax.*` 到 `jakarta.*`）

Jakarta EE 作为规范提供者，其兼容性测试套件（TCK）确保了实现的标准化。Spring 在构建企业 Java 应用时，广泛使用了 Jakarta EE 的规范，如 Servlet、持久化和验证等。因此，Spring 的发展离不开 Jakarta EE 提供的基础设施:

 - Jakarta Servlet 是 Spring 的核心组件。Spring Boot 以云友好的方式嵌入了 Servlet 容器。使用 Spring Initializr 生成的应用程序默认嵌入的容器是 Apache Tomcat。这可以很容易地更改为 Eclipse Jetty、OpenLiberty、Undertow 或其他 Jakarta Servlet 实现。 
 - Spring WebSocket API 扩展了 Jakarta WebSocket 提供的标准 API。如果 Spring WebSocket 应用程序运行在支持 Jakarta EE 10 或更高版本的容器中，如 Tomcat 10+ 和 Jetty 12+，则会使用最新的 Jakarta WebSocket 规范提供的标准请求升级策略。 
 - 当存在 Jakarta Concurrency 实现时，Spring 会检测到并能够持续自动适应。 
 - 当 Spring 检测到 Jakarta JSON Binding API 的存在时，自动配置将查找一个实现并为 Spring 应用程序开发人员提供。Spring 中 Jakarta JSON Binding 的建议实现是 Eclipse Yasson。 
 - 对于 Jakarta Validation 也是同样的工作方式。当它存在时，Spring 会通过引入 Hibernate Validator 作为实现来自动配置。然后应用程序开发人员就能够验证数据输入是否符合规则和限制。 
 - 大多数应用程序以某种方式存储和/或检索数据，而绝大多数都使用关系数据库这样做。这需要一种将对象映射到关系的方法。在 Spring 应用程序中，这主要是通过 Hibernate ORM 完成的，它是 Jakarta Persistence 的一个实现。在 Spring 中持久化数据的最流行方法是使用 Spring Data JPA。这种技术会自动生成大部分样板 Jakarta Persistence 代码，包括所有最相关的 CRUD 方法。 
 - Spring Data 是 Jakarta EE 从 Spring 中获取灵感的一个优秀例子。Jakarta Data 是一个新的规范，大量借鉴了 Spring Data 的理念。这个新规范将作为即将发布的 Jakarta EE 11 的一部分。以像 Spring Data 这样成熟的技术作为基础制定 Jakarta Data 规范，体现了在 Eclipse 基金会下制定 Jakarta EE 规范时所采用的"代码先行"方法。 
 - Spring Messaging 通过使用 JmsTemplate 的模板方法，简化了对 Jakarta Messaging API 的使用。Spring 还提供了更高层次的消息传递抽象，因此开发人员不需要直接使用 Jakarta Messaging API。 
 - Spring 在整个框架中以一致的方式提供事务管理，支持不同的事务 API，包括声明式事务管理以及用于处理复杂事务的简单编程 API，例如与 Jakarta Transactions 一起使用。 
 - Spring 还支持最初在 JSR 330 中定义的 Jakarta 依赖注入规范。这个 JSR 是由 Spring 框架开发人员等人提出和开发的。结合对 Jakarta 注解的支持，这允许应用程序开发人员可以互换地使用这些注解和 Spring 本身提供的特定于 Spring 的依赖注入机制。

## Jakarta EE 迁移工具

如果源代码在你手里控制，那么迁移很简单，基本就是修改升级依赖，修改 namespace（从 `javax.*` 到 `jakarta.*`）。

如果源代码不在你手里，比如你使用的某些依赖库还是用的 `javax.*`，这时候就需要使用 Jakarta EE 迁移工具。这个工具可以帮助你将依赖中的 `javax.*` 的依赖转换为 `jakarta.*`，通过修改字节码的方式。这里以 [Eclipse Transformer](https://github.com/eclipse/transformer) 为例，介绍一下使用方法：

```
java -jar transformer.jar input.jar
```
这个命令会将 `input.jar` 中的 `javax.*` 转换为 `jakarta.*`，并生成一个新的 jar 包。




## Jakarta EE 的未来

目前，Jakarta EE 正在开发其第 11 个版本，计划在 Java 21 之后的 6-9 个月内发布。这个版本将包括一个新的 Jakarta Data 规范，用于标准化数据访问的存储库模式。

Jakarta EE 11 将基于 Java 17，尽管最初计划基于 Java 21，但为了兼顾广泛的用户基础，选择了更为稳定的 Java 17。对于 Spring 社区来说，Jakarta EE 的更新意味着需要及时跟进，以确保兼容性和功能的完整性。

## 结语

通过此次演讲，我们可以清晰地看到，Spring 和 Jakarta EE 在 Java 开发生态中如何互相推动和影响。Spring 的简化和创新，使其成为企业 Java 应用的行业标准，而 Jakarta EE 的规范化和标准化，为 Spring 提供了坚实的基础支持。未来，随着 Jakarta EE 的不断演进，Spring 将继续依赖并受益于 Jakarta EE 提供的最新技术和规范。

这种相互依存和合作的关系，使得 Java 生态系统更加丰富和多样，开发者在选择技术栈时也拥有了更多的灵活性和可能性。对于所有 Java 开发者来说，关注和理解这两者之间的关系，不仅有助于提升技术水平，也有助于更好地应对未来的技术挑战。

