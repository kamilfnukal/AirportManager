package cz.fi.muni.pa165.airport_manager.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Kamil Fňukal
 * This class represents DTO used for BaseUser entity.
 */

@Getter
@Setter
public abstract class BaseUserDto {

    protected Long id;

    protected String password;

    @Email
    @NotBlank
    protected String email;

    @Size(min=3, max=25)
    @NotBlank
    @Pattern(regexp = "^[a-zA-z ]*$",
            message = "Firstname must contain only letters. Length must be in interval [3-25].")
    protected String firstName;

    @Size(min=3, max=25)
    @NotBlank
    @Pattern(regexp = "^[a-zA-z ]*$",
            message = "Lastname must contain only letters. Length must be in interval [3-25].")
    protected String lastName;

    @NotBlank
    @Pattern(regexp = "^[\\d+]*$",
            message = "Phone must contain only digits or '+'.")
    protected String phone;

    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
