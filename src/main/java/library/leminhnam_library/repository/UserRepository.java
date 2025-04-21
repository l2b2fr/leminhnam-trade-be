package library.leminhnam_library.repository;

import library.leminhnam_library.dto.UserInfoDTO;
import library.leminhnam_library.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository, JpaSpecificationExecutor<User> {
    User findByUsername(String username);

    @Query(value = "SELECT u.id,u.name,u.username,u.password,u.age,u.sex,u.address,u.phone,u.email,u.image_url,r.id,r.name_role,r.role_description " +
            "FROM TB_LMN_USERS u, TB_LMN_ROLES r " +
            "WHERE u.role_id = r.id",
            countQuery = "SELECT COUNT(*) FROM TB_LMN_USERS u, TB_LMN_ROLES r WHERE u.role_id = r.id",
            nativeQuery = true)
    Page<UserInfoDTO> fetchUserInfo(Pageable pageable);
}