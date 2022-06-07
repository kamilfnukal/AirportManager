package cz.fi.muni.pa165.airport_manager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Pavel Sklenar
 * Base entity for managing User roles.
 * Manages Id, password, email, firstName, lastName, phone, createdAt and updatedAt fields.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    protected String password;

    @NotNull
    @Column(unique=true)
    protected String email;

    @NotNull
    protected String firstName;

    @NotNull
    protected String lastName;

    protected String phone;

    protected LocalDateTime createdAt;

    protected LocalDateTime updatedAt;

}
