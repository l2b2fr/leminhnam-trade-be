package library.leminhnam_library.specification;

import jakarta.persistence.criteria.Predicate;
import library.leminhnam_library.dto.UserDTO;
import library.leminhnam_library.model.User;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> dynamicSearch(UserDTO userDTO) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userDTO.getName() != null && !userDTO.getName().isEmpty())
                predicates.add(cb.like(root.get("name"), "%" + userDTO.getName() + "%"));

            if (userDTO.getAge() != null)
                predicates.add(cb.equal(root.get("age"), userDTO.getAge()));

            if (userDTO.getSex() != null && !userDTO.getSex().isEmpty())
                predicates.add(cb.equal(root.get("sex"), userDTO.getSex()));

            if (userDTO.getAddress() != null && !userDTO.getAddress().isEmpty())
                predicates.add(cb.like(root.get("address"), "%" + userDTO.getAddress() + "%"));

            if (userDTO.getPhone() != null && !userDTO.getPhone().isEmpty())
                predicates.add(cb.like(root.get("phone"), "%" + userDTO.getPhone() + "%"));

            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty())
                predicates.add(cb.like(root.get("email"), "%" + userDTO.getEmail() + "%"));

            if (userDTO.getRole() != null)
                predicates.add(cb.equal(root.get("role").get("id"), userDTO.getRole()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
