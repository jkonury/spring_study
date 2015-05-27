package springbook.learningtest.spring.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:springbook/learningtest/spring/jdbc/jdbctest-context.xml"
})
public class JdbcTest {
    @Autowired
    DataSource dataSource;

    SimpleJdbcInsert simpleJdbcInsert;
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void jdbcTemplate() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(SimpleDao.class);
        SimpleDao dao = context.getBean(SimpleDao.class);
        dao.deleteAll();

        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "Spring");
        map.put("point", 3.5);
        dao.insert(map);
        dao.insert(new MapSqlParameterSource()
            .addValue("id", 2)
            .addValue("name", "Book")
            .addValue("point", 10.1)
        );

        assertThat(dao.rowCount(), is(2));
        assertThat(dao.rowCount(5), is(1));

        assertThat(dao.name(1), is("Spring"));
        assertThat(dao.point(1), is(3.5));

        Member member = dao.get(1);
        assertThat(member.id, is(1));
        assertThat(member.name, is("Spring"));
        assertThat(member.point, is(3.5));

        assertThat(dao.find(1).size(), is(2));
    }
}