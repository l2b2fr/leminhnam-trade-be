package library.leminhnam_library.service;

import library.leminhnam_library.model.Role;
import library.leminhnam_library.model.User;
import library.leminhnam_library.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> geRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role với id: " + id));

        role.setNameRole(roleDetails.getNameRole());

        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy role với id: " + id);
        }
        roleRepository.deleteById(id);
    }
}
