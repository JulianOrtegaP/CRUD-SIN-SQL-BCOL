package com.example.demo.models;

import java.util.UUID;

import lombok.Data;


// Atributos: id (UUID), name (String), lastname (String), email (String único), cc (String único),
// isActivated (Boolean, por defecto, al crear un usuario, este campo debe estar en "true"; se
// establece como "true" en el constructor)

@Data

public class User {

    private UUID id;
    private String name;
    private String lastname;
    private String email;
    private String cc;
    private Boolean isActivated;

    public User(String name, String lastname, String email, String cc) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.cc = cc;
        this.id = UUID.randomUUID();
        this.isActivated = true;
    }

    
}
