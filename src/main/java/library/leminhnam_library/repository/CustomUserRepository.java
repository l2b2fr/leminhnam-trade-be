package library.leminhnam_library.repository;

import library.leminhnam_library.dto.UserDTO;
import library.leminhnam_library.dto.UserInfoDTO;

import java.util.List;

public interface CustomUserRepository {
    List<UserInfoDTO> dynamicSearchNative(UserDTO userDTO);
    List<UserInfoDTO> dynamicSearchProc(UserDTO userDTO);
}
