package com.ironhack.ironbank.model.entities.users;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Admin extends User {

    private String firstName;

    private String lastName;

    private String email;
}
