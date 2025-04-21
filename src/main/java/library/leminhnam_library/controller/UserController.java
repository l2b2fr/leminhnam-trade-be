package library.leminhnam_library.controller;

import library.leminhnam_library.dto.UserDTO;
import library.leminhnam_library.dto.UserInfoDTO;
import library.leminhnam_library.model.User;
import library.leminhnam_library.repository.UserRepository;
import library.leminhnam_library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
//    @GetMapping("/{id}")
//    public Optional<User> getUserById(String id) {
//        return userRepository.findUserById(id);
//    }

    @GetMapping
    public Page<UserInfoDTO> getAllUserInfo(Pageable pageable) {
        return userRepository.fetchUserInfo(pageable);
    }

//    @GetMapping
//    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
//                               @RequestParam(defaultValue = "10") int size) {
//        return userRepository.findAll(PageRequest.of(page, size));
//    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
            User createdUser = userService.addUser(userDTO);
            return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            User updateUser = userService.updateUser(id,userDTO);
            return ResponseEntity.ok(updateUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/search")
    public List<User> searchUsers(@RequestBody UserDTO userDTO) {
        return userService.searchUsers(userDTO);
    }

    @PostMapping("/search_native")
    public ResponseEntity<List<UserInfoDTO>> searchUsersNative(@RequestBody UserDTO userDTO) {
        List<UserInfoDTO> users = userService.searchUsersNative(userDTO);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/search_pro")
    public ResponseEntity<List<UserInfoDTO>> searchUsersPro(@RequestBody UserDTO userDTO) {
        List<UserInfoDTO> users = userService.searchUsersPro(userDTO);
        return ResponseEntity.ok(users);
    }
}
