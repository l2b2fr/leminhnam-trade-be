package library.leminhnam_library.dto;

public class AuthenticationRequest {
    private String username;
    private String password;
    private Long role; // hoặc roleId

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password, Long role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters và Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }
}
