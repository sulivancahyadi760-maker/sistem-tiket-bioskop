package sistem.tiket.bioskop.dto;

import sistem.tiket.bioskop.model.User;

public class UserResponseDTO {
    public String username;
    public String role;
    public int saldo;

    public UserResponseDTO(User user) {
        this.username = user.getUsername();
        this.role = user.getRole();
        this.saldo = user.getSaldo();
    }
}
