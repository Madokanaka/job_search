package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDao {

    private final JdbcTemplate jdbcTemplate;

    public CategoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Category getCategoryById(Integer id) {
        String sql = "SELECT id, name, PARENT_ID FROM categories WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            if (rs.next()) {
                return new Category(rs.getInt("id"), rs.getString("name"), rs.getInt("parentId"));
            }
            return null;
        });
    }

    public List<Category> getAllCategories() {
        String sql = "SELECT id, name, parent_id FROM categories";

        return jdbcTemplate.query(sql, new RowMapper<Category>() {
            @Override
            public Category mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Integer parentId = rs.getInt("parent_id");
                return new Category(id, name, parentId);
            }
        });
    }
}
