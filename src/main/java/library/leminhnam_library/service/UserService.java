package library.leminhnam_library.service;

import library.leminhnam_library.dto.UserDTO;
import library.leminhnam_library.dto.UserInfoDTO;
import library.leminhnam_library.model.Role;
import library.leminhnam_library.model.User;
import library.leminhnam_library.repository.RoleRepository;
import library.leminhnam_library.repository.UserRepository;
import library.leminhnam_library.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private UserService userService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User addUser(UserDTO userDTO) {
        Role role = roleRepository.getReferenceById(userDTO.getRole());

        User user = new User();
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setSex(userDTO.getSex());
        user.setAddress(userDTO.getAddress());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setImageUrl(userDTO.getImageUrl());
        user.setRole(role);

        return userRepository.save(user);
    }

    public User updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại với id: " + id));

        Role role = roleRepository.getReferenceById(userDTO.getRole());
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setAddress(userDTO.getAddress());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setImageUrl(userDTO.getImageUrl());
        user.setRole(role);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User không tồn tại với id: " + id);
        }
    }

    // Phương thức tìm kiếm động sử dụng JpaSpecificationExecutor, bao gồm cả tìm theo roleId
    public List<User> searchUsers(UserDTO userDTO) {
        return userRepository.findAll(UserSpecification.dynamicSearch(userDTO));
    }

    // Phương thức tìm kiếm động sử dụng Native Query
    public List<UserInfoDTO> searchUsersNative(UserDTO userDTO) {
        return userRepository.dynamicSearchNative(userDTO);
    }


    public List<UserInfoDTO> searchUsersPro(UserDTO userDTO) {
        return userRepository.dynamicSearchProc(userDTO);
    }
}