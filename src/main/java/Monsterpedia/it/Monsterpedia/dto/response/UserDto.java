package Monsterpedia.it.Monsterpedia.dto.response;

import Monsterpedia.it.Monsterpedia.enumerated.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    private long id;

    @NotBlank(message = "Inserire uno username")
    private String username;

    @NotBlank(message = "l'email non può essere vuota")
    @Email(message = "formato email non valido")
    private String email;

    @NotBlank(message = "la password non può essere vuota")
    @Size(min = 8, message = "la password deve essere di almeno 8 caratteri")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$£%^&+*=_!?~/|.,:;§]).*$",
            message = "La password deve contenere almeno una lettera maiuscola, una minuscola, " +
                    "un numero e un carattere speciale (@ # $ £ % ^ & + * = _ ! ? ~ / | . , : ; §)"
    )
    private String password;

    private String avatarUrl;

    private Role role;
}
