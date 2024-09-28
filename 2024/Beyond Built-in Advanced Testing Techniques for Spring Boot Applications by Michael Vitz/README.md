# Spring Boot 应用高级单测技巧

## JUnit Parameterized Test

一个使用 `@MethodSource` 的 ParameterizedTest，可以用来测试多个参数组合的情况。

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MathUtilsTest {

    @ParameterizedTest
    @MethodSource("provideNumbersForAddition")
    @DisplayName("Test addition with multiple numbers")
    void testAddition(int a, int b, int expectedSum) {
        assertEquals(expectedSum, MathUtils.add(a, b));
    }

    private Stream<org.junit.jupiter.params.provider.Arguments> provideNumbersForAddition() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(1, 1, 2),
            org.junit.jupiter.params.provider.Arguments.of(2, 3, 5),
            org.junit.jupiter.params.provider.Arguments.of(3, 7, 10)
        );
    }
}

class MathUtils {
    static int add(int a, int b) {
        return a + b;
    }
}
```

## 基于 @ExtendWith 的自定义扩展

比如我们要单元测试某个方法的日志输出，可以通过自定义扩展来实现。有现成的库可以用：

```xml
 <dependency>
    <groupId>dk.bitcraft</groupId>
    <artifactId>LogCollector</artifactId>
    <version>0.9.0</version>
    <scope>test</scope>
</dependency>
```

```java
//这里的 @LogCollectorExtension 其实就是基于 @ExtendWith 的自定义扩展
@Log4j2
@LogCollectorExtension
public class LogTest {
    //必须要 public，必须要这个字段，否则无法收集日志
    public JUnit5LogCollector collector = new JUnit5LogCollector(log);
    @Test
    public void testLog() {
        log.info("Test passed for values: 1 and 100");
        System.out.println(collector.getLogs());
    }
}

```

## 初始化数据

可以使用 javafaker：

```xml
<dependency>
    <groupId>com.github.javafaker</groupId>
    <artifactId>javafaker</artifactId>
    <version>1.0.2</version>
</dependency>
```

```java
public class PersonTest {
    @Data
    @AllArgsConstructor
    public static class Person {
        private String name;
        private int age;
        private String email;
    }

    @Test
    void testPersonCreationWithFaker() {
        Faker faker = new Faker();
        Person person = new Person(
                faker.name().fullName(),
                faker.number().numberBetween(18, 80),
                faker.internet().emailAddress()
        );
        System.out.println(person);
    }
}
```

示例输出：

```
PersonTest.Person(name=Mr. Cletus Prosacco, age=54, email=venita.fay@yahoo.com)
```

## 断言判断

使用 AssertJ

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetPerson() {
        String url = "http://localhost:" + port + "/person";

        ResponseEntity<Person> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            Person.class
        );

        assertThat(responseEntity).satisfies(response -> {
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            Person person = response.getBody();
            assertThat(person).satisfies(p -> {
                assertThat(p.getName()).isEqualTo("John Doe");
                assertThat(p.getAge()).isEqualTo(30);
            });
        });
    }
}
```
## 如何判断 equals 和 hashcode 的实现合理

- equals 方法的实现必须满足以下条件：
  - 自反性：对于任何非空引用值 x，x.equals(x) 必须返回 true。
  - 对称性：对于任何非空引用值 x 和 y，当且仅当 y.equals(x) 返回 true 时，x.equals(y) 必须返回 true。
  - 传递性：对于任何非空引用值 x、y 和 z，如果 x.equals(y) 返回 true，并且 y.equals(z) 返回 true，那么 x.equals(z) 必须返回 true。
  - 一致性：对于任何非空引用值 x 和 y，只要 equals 的比较操作在对象上的信息没有被修改，多次调用 x.equals(y) 就会一致地返回 true 或者 false。
  - 对于任何非空引用值 x，x.equals(null) 必须返回 false。
- hashCode 方法的实现必须满足以下条件：
  - 在应用程序的执行期间，只要对象的 equals 方法的比较操作在对象上的信息没有被修改，那么对这同一个对象调用多次，hashCode 方法都必顫返回同一个整数。
  - 如果根据 equals(Object) 方法，两个对象是相等的，那么这两个对象调用 hashCode 方法必须返回相同的整数结果。
  - 如果根据 equals(Object) 方法，两个对象不相等，那么这两个对象调用 hashCode 方法不一定要返回不同的整数结果。但是，程序员应该意识到，为不相等的对象生成不同的整数结果可以提高哈希表的性能。

有工具类可以验证 equals 和 hashcode 的实现是否合理，比如 [EqualsVerifier](https://jqno.nl/equalsverifier/)。

添加依赖：
```xml

<!-- https://mvnrepository.com/artifact/nl.jqno.equalsverifier/equalsverifier-nodep -->
<dependency>
    <groupId>nl.jqno.equalsverifier</groupId>
    <artifactId>equalsverifier-nodep</artifactId>
    <version>3.17</version>
    <type>pom</type>
    <scope>test</scope>
</dependency>
```

```java
EqualsVerifier.forClass(Person.class).verify();
```
