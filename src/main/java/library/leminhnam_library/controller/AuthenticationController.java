package library.leminhnam_library.controller;

import library.leminhnam_library.dto.AuthenticationRequest;
import library.leminhnam_library.dto.AuthenticationResponse;
import library.leminhnam_library.model.User;
import library.leminhnam_library.repository.UserRepository;
import library.leminhnam_library.security.JwtTokenUtil;
import library.leminhnam_library.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            // Xác thực username và password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username hoặc password không đúng");
        }

        // Lấy thông tin người dùng từ database
        User user = userRepository.findByUsername(authenticationRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User không tồn tại");
        }

        // Kiểm tra role được gửi lên có khớp với role của người dùng trong database hay không
        if (user.getRole() == null || !user.getRole().getId().equals(authenticationRequest.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role không khớp");
        }

        // Tạo JWT token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        // Trả về token và thông tin người dùng
        return ResponseEntity.ok(new AuthenticationResponse(jwt, user));
    }


    // Với JWT, đăng xuất thường được xử lý ở phía client bằng cách loại bỏ token.
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Nếu muốn triển khai cơ chế logout server-side, bạn có thể lưu token vào blacklist.
        return ResponseEntity.ok("Đăng xuất thành công. Vui lòng loại bỏ token phía client.");
    }
}
