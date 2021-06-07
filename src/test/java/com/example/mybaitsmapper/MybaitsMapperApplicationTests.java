package com.example.mybaitsmapper;

import com.example.mybaitsmapper.demo.User;
import com.example.mybaitsmapper.demo.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.mybatis.mapper.example.Example;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MybaitsMapperApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MybaitsMapperApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void before() {
    }

    @AfterEach
    public void after() {
    }

    @Test
    public void test_query() {
        User user = new User();
        user.setUserName("测试");
        userMapper.insert(user);
        //保存后自增id回写，不为空
        assert user.getId() != null;
        //删除
        assert user.delete() == 1;
        final Page<User> users = userMapper.listPage(1, 10);
        System.out.println(users.getTotal());
        System.out.println(users.getResult());

        userMapper.updateByPrimaryKey(user);
        Example<User> example = new Example<>();
        Example.Criteria<User> criteria = example.createCriteria();
        criteria.andEqualTo(User::getUserName, "Afghanistan");
        final Page<User> objects = PageHelper.startPage(1, 10).doSelectPage(() -> userMapper.selectByExample(example));
        System.out.println(objects.getTotal());
        System.out.println(objects.getResult());

    }

}
