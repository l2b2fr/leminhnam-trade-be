package library.leminhnam_library.repository;

import jakarta.persistence.*;
import library.leminhnam_library.dto.UserDTO;
import library.leminhnam_library.dto.UserInfoDTO;
import library.leminhnam_library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private UserRepository userRepository;

    private SimpleJdbcCall searchUsersCall;

    @Override
    public List<UserInfoDTO> dynamicSearchNative(UserDTO userDTO) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sql = new StringBuilder("SELECT u.id,u.name,u.username,u.password,u.age,u.sex,u.address,u.phone,u.email,u.image_url,r.id,r.name_role,r.role_description" +
                " FROM TB_LMN_USERS u, TB_LMN_ROLES r" +
                " WHERE 1=1 AND u.role_id = r.id");

        if (userDTO.getUsername() != null && !userDTO.getUsername().trim().isEmpty()) {
            sql.append(" AND u.username LIKE :username");
            params.put("username", "%" + userDTO.getUsername().trim() + "%");
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            sql.append(" AND u.password = :password");
            params.put("password", userDTO.getPassword().trim());
        }
        if (userDTO.getName() != null && !userDTO.getName().trim().isEmpty()) {
            sql.append(" AND u.name LIKE :name");
            params.put("name", "%" + userDTO.getName().trim() + "%");
        }
        if (userDTO.getAge() != null) {
            sql.append(" AND u.age = :age");
            params.put("age", userDTO.getAge());
        }
        if (userDTO.getSex() != null && !userDTO.getSex().trim().isEmpty()) {
            sql.append(" AND u.sex = :sex");
            params.put("sex", userDTO.getSex().trim());
        }
        if (userDTO.getAddress() != null && !userDTO.getAddress().trim().isEmpty()) {
            sql.append(" AND u.address LIKE :address");
            params.put("address", "%" + userDTO.getAddress().trim() + "%");
        }
        if (userDTO.getPhone() != null && !userDTO.getPhone().trim().isEmpty()) {
            sql.append(" AND u.phone LIKE :phone");
            params.put("phone", "%" + userDTO.getPhone().trim() + "%");
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().trim().isEmpty()) {
            sql.append(" AND u.email LIKE :email");
            params.put("email", "%" + userDTO.getEmail().trim() + "%");
        }
        if (userDTO.getImageUrl() != null && !userDTO.getImageUrl().trim().isEmpty()) {
            sql.append(" AND u.imageUrl LIKE :imageUrl");
            params.put("imageUrl", "%" + userDTO.getImageUrl().trim() + "%");
        }
        if (userDTO.getRole() != null) {
            sql.append(" AND u.role_id = :roleId");
            params.put("roleId", userDTO.getRole());
        }

        Query query = entityManager.createNativeQuery(sql.toString(), UserInfoDTO.class);
        params.forEach(query::setParameter);

        return query.getResultList();
    }

    @Override
    public List<UserInfoDTO> dynamicSearchProc(UserDTO userDTO) {
        // Giả sử entityManager đã được tiêm (injected)
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SEARCH_USERS_DYNAMIC", User.class);

        // Đăng ký các tham số theo thứ tự
        query.registerStoredProcedureParameter("p_username", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_password", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_age", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_sex", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_role_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Thiết lập giá trị cho các tham số IN từ UserDTO
        query.setParameter("p_username", userDTO.getUsername());
        query.setParameter("p_password", userDTO.getPassword());
        query.setParameter("p_name", userDTO.getName());
        query.setParameter("p_age", userDTO.getAge());
        query.setParameter("p_sex", userDTO.getSex());
        query.setParameter("p_address", userDTO.getAddress());
        query.setParameter("p_phone", userDTO.getPhone());
        query.setParameter("p_email", userDTO.getEmail());
        query.setParameter("p_role_id", userDTO.getRole());

        // Thực thi procedure
        query.execute();

        // Lấy kết quả từ cursor
        List<UserInfoDTO> users = query.getResultList();

        return users;
    }
}
