package kg.attractor.job_search.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactTypeDao {
    private final JdbcTemplate jdbcTemplate;

    public Integer getContactTypeId(String value) {
        String sql = "select id from contact_types where UPPER(type) = UPPER(?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, value);
    }
}
