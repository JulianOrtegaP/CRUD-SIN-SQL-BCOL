package com.example.demo.models;

import java.util.UUID;

import lombok.Data;

// Atributos: id (UUID), originAccount (UUID), destinationAccount (UUID), amount (Double).

@Data
public class Transfer {

    private UUID id;
    private UUID originAccount;
    private UUID destinationAccount;
    private Double amount;


    public Transfer(Double amount) {
        
        this.amount = amount;
        this.id= UUID.randomUUID();
        this.originAccount= UUID.randomUUID();
        this.destinationAccount=UUID.randomUUID();
    }


    

    
}
