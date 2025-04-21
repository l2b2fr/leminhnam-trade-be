package library.leminhnam_library.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long id;
    private String name;
    private String username;
    private String password;
    private Integer age;
    private String sex;
    private String address;
    private String phone;
    private String email;
    private String imageUrl;
    private Long roleId;
    private String nameRole;
    private String roleDescription;

    public UserInfoDTO(Long id, String name, String username, String password, Integer age, String sex, String address, String phone, String email, String imageUrl, Long roleId, String nameRole, String roleDescription) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.imageUrl = imageUrl;
        this.roleId = roleId;
        this.nameRole = nameRole;
        this.roleDescription = roleDescription;
    }
}
