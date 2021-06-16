package com.example.springpractice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

//@Component
public class H2Runner implements ApplicationRunner {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try(Connection connection = dataSource.getConnection()) {
            connection.getMetaData().getURL();
            connection.getMetaData().getUserName();

            System.out.println(connection.getMetaData().getURL());
            System.out.println(connection.getMetaData().getUserName());

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE USER(ID INTEGER NOT NULL, name VARCHAR(255), PRIMARY KEY (id));";
            statement.executeUpdate(sql);
        }

        // JDBCtemplete를 사용하면 가독성 높은 에러 메세지, 간결한 sql문을 사용할 수 있어서 여러모로 편리함.
        jdbcTemplate.execute("INSERT INTO USER VALUES (1, 'jieun')");
    }
}
