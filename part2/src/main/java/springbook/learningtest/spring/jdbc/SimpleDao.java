package springbook.learningtest.spring.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleDao {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Bean
    public DataSource dataSource() {
        return new SimpleDriverDataSource(new org.hsqldb.jdbcDriver(), "jdbc:hsqldb:hsql://localhost/spring");
    }

    @Autowired
    public void init(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void updates(Map<String, Object>[] maps) {
        this.namedParameterJdbcTemplate.batchUpdate("update member set name = :name where id = :id", maps);
    }

    public Map<String, Object> getMap(int id) {
        return this.namedParameterJdbcTemplate.queryForMap("select * from member where id > :id",
            new MapSqlParameterSource().addValue("id", id));
    }

    public List<Member> find(double point) {
        return this.namedParameterJdbcTemplate.query("select * from member where point > :point",
            new MapSqlParameterSource().addValue("point", point),
            new BeanPropertyRowMapper<>(Member.class));
    }

    public Member get(int id) {
        return this.namedParameterJdbcTemplate.queryForObject("select * from member where id = :id",
            new MapSqlParameterSource().addValue("id", id),
            new BeanPropertyRowMapper<>(Member.class));
    }

    public String name(int id) {
        return this.namedParameterJdbcTemplate.queryForObject("select name from member where id = :id",
            new MapSqlParameterSource().addValue("id", id),
            String.class
        );
    }

    public double point(int id) {
        return this.namedParameterJdbcTemplate.queryForObject("select point from member where id = :id",
            new MapSqlParameterSource().addValue("id", id),
            Double.class
        );
    }

    public int rowCount() {
        return this.namedParameterJdbcTemplate.queryForInt("select count(*) from member", new HashMap<>());
    }

    public int rowCount(double min) {
        return this.namedParameterJdbcTemplate.queryForInt("select count(*) from member where point > :min",
            new MapSqlParameterSource().addValue("min", min));
    }

    public void deleteAll() {
        this.namedParameterJdbcTemplate.update("delete from member", new HashMap<>());
    }

    public void insert(Map<String, Object> map) {
        this.namedParameterJdbcTemplate.update("insert into member(id, name, point) values(:id, :name, :point)", map);
    }

    public void insert(SqlParameterSource sqlParameterSource) {
        this.namedParameterJdbcTemplate.update("insert into member(id, name, point) values(:id, :name, :point)", sqlParameterSource);
    }
}
