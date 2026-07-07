package org.example.tacocloud.tutorial.oop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User类单元测试")
class UserTest {

    @Test
    @DisplayName("测试构造函数是否正确初始化对象")
    void testConstructor() {
        User user = new User("testUser", 25);
        assertNotNull(user);
    }

    @Test
    @DisplayName("测试sayHello方法不抛出异常")
    void testSayHello() {
        User user = new User("testUser", 25);
        assertDoesNotThrow(() -> user.sayHello());
    }

    @Test
    @DisplayName("测试使用不同用户名和年龄创建多个用户")
    void testMultipleUsers() {
        User user1 = new User("user1", 20);
        User user2 = new User("user2", 30);
        User user3 = new User("user3", 40);

        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);
    }

    @Test
    @DisplayName("测试边界值：年龄为0")
    void testZeroAge() {
        User user = new User("testUser", 0);
        assertNotNull(user);
        assertDoesNotThrow(() -> user.sayHello());
    }

    @Test
    @DisplayName("测试边界值：负数年龄")
    void testNegativeAge() {
        User user = new User("testUser", -5);
        assertNotNull(user);
        assertDoesNotThrow(() -> user.sayHello());
    }

    @Test
    @DisplayName("测试边界值：大年龄值")
    void testLargeAge() {
        User user = new User("testUser", 200);
        assertNotNull(user);
        assertDoesNotThrow(() -> user.sayHello());
    }

    @Test
    @DisplayName("测试空用户名")
    void testEmptyUsername() {
        User user = new User("", 25);
        assertNotNull(user);
        assertDoesNotThrow(() -> user.sayHello());
    }

    @Test
    @DisplayName("测试null用户名")
    void testNullUsername() {
        User user = new User(null, 25);
        assertNotNull(user);
        assertDoesNotThrow(() -> user.sayHello());
    }

    @Test
    @DisplayName("测试sayHello方法可以被多次调用")
    void testSayHelloMultipleTimes() {
        User user = new User("testUser", 25);
        assertDoesNotThrow(() -> {
            user.sayHello();
            user.sayHello();
            user.sayHello();
        });
    }
}
