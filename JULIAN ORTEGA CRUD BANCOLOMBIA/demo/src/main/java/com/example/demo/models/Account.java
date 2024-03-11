package com.example.demo.models;

import java.util.UUID;

import lombok.Data;


// Atributos: id (UUID), number (String único), type (String - "ahorro" o "corriente"), idUser (UUID),
// balance (Double), isActivated (Boolean, por defecto, al crear una account, este campo debe estar
// en "true"; se establece como "true" en el constructor).



@Data
public class Account {

  private UUID id;
  private String number; //Unico
  private String type;
  private UUID idUser;
  private Double balance;
  private Boolean isActivated;


public Account(String number, String type, Double balance) {
    this.number = number;
    this.type = type;
    this.balance = balance;
    this.id = UUID.randomUUID();
    this.idUser = UUID.randomUUID();
    this.isActivated = true;
}


  

    
    
}
