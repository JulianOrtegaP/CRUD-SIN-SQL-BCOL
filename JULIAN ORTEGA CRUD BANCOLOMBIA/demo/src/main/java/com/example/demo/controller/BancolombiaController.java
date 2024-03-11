package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Account;
import com.example.demo.models.Transfer;
import com.example.demo.models.User;

@RestController
@CrossOrigin(origins = "*")


public class BancolombiaController {

    //------------------------FAKE DB----------------------------------------------------

    List<User> usuariosDb = new ArrayList<>();
    List<Account> cuentasDb = new ArrayList<>();
    List<Transfer> transferenciasDb = new ArrayList<>();


    // --------------------------USER--------------------USER-----------------------USER------------

    // 1)  Crear un usuario. Se realiza una validación para asegurarse de que los campos 'email' y'cc' sean únicos. /users

    @PostMapping("/users")

    public ResponseEntity<?> createUser(@RequestBody User newUser){

        Map<String,Object> response = new HashMap<>();

        if(newUser.getName()==null||newUser.getName().isEmpty()||newUser.getLastname()==null||newUser.getLastname().isEmpty()||newUser.getEmail()==null||newUser.getEmail().isEmpty()||newUser.getCc()==null||newUser.getCc().isEmpty()){

             response.put("ok", false);
            response.put("data", "");
            response.put("message", "los campos son requeridos");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User cedulaDuplicada = usuariosDb.stream().filter(item->item.getCc().equals(newUser.getCc())).findAny().orElse(null);

        
        if (cedulaDuplicada != null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "la cedula ya existe en otro registro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User emailDuplicado = usuariosDb.stream().filter(item->item.getEmail().equals(newUser.getEmail())).findAny().orElse(null);

        if (emailDuplicado != null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "el email ya existe en otro registro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        usuariosDb.add(newUser);
        response.put("ok", true);
        response.put("data", newUser);
        response.put("message", "Usuario creado");

        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }


    // 2 Listar todos los usuarios cuyo campo 'isActivated' esté configurado como 'true', lo quesignifica que los usuarios están activos en el banco
    

    @GetMapping("/users")

    public ResponseEntity<?> listActivatedUsers(){

    Map<String,Object> response = new HashMap<>();

    List<User> usuariosActivados = usuariosDb.stream().filter(item ->item.getIsActivated().equals(true)).collect(Collectors.toList());

        response.put("ok", true);
        response.put("data", usuariosActivados);
        response.put("message", "Lista de usuarions Activados");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    

// 3) Obtener un usuario por ID cuyo campo 'isActivated' esté configurado como 'true', lo que
// significa que el usuario está activo en el banco. En caso de que 'isActivated' esté
// configurado como 'false', se enviará un mensaje de error indicando que el usuario no se
// ha encontrado. /users/123

@GetMapping("/users/{id}")

public ResponseEntity<?> getUsersById(@PathVariable UUID id){

    Map<String,Object> response = new HashMap<>();

    User userInDb = usuariosDb.stream().filter(item->item.getId().equals(id)&&item.getIsActivated().equals(true)).findFirst().orElse(null);

    if (userInDb == null) {
        response.put("ok", false);
        response.put("data", "");
        response.put("message", "Usuario no encontrado");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    response.put("ok", true);
    response.put("data", userInDb);
    response.put("message", "Usuario encontrado");
    return new ResponseEntity<>(response, HttpStatus.OK);


}


// 4) Listar todos los usuarios sin excepción. /users/allusers

@GetMapping("/users/allusers")

public ResponseEntity<?> listAllUsersWithoutException(){

Map<String,Object> response = new HashMap<>();

        response.put("ok", true);
        response.put("data", usuariosDb);
        response.put("message", "Lista de usuarios");
        return new ResponseEntity<>(response, HttpStatus.OK);
    
}


// 5) Actualizar los datos de un usuario por ID, primero se verifica que el usuario exista y esté
// activo en el banco. 

//Esto implica que el atributo 'isActivated' debe estar configurado como
// 'true'. En caso de que 'isActivated' esté configurado como 'false', se enviará un mensaje
// de error indicando que el usuario no se ha encontrado. 

//Además, se realiza una verificación
// para determinar si se desean cambiar el correo electrónico (email) o el número de
// identificación (cc) del usuario. Es importante asegurarse de que estos nuevos datos no
// existan ya en otros registros. /users/123

@PutMapping("/users/{id}")

public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User updateUser){

    Map<String,Object> response = new HashMap<>();

     User userInDb = usuariosDb.stream().filter(item->item.getId().equals(id)&&item.getIsActivated().equals(true)).findFirst().orElse(null);

    if (userInDb == null) {
        response.put("ok", false);
        response.put("data", "");
        response.put("message", "Usuario no encontrado");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


        User emailDuplicado = usuariosDb.stream().filter(item->item.getEmail().equals(updateUser.getEmail())).findAny().orElse(null);

        if(emailDuplicado!=null){

            response.put("ok", false);
            response.put("data", "");
            response.put("message", "el email ya existe en otro registro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }

        User identificacionDuplicada = usuariosDb.stream().filter(item->item.getCc().equals(updateUser.getCc())).findAny().orElse(null);

        if( identificacionDuplicada!=null){

            response.put("ok", false);
            response.put("data", "");
            response.put("message", "la identificacion ya existe en otro registro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }


        userInDb.setName(updateUser.getName());
        userInDb.setLastname(updateUser.getLastname());
        userInDb.setEmail(updateUser.getEmail());
        userInDb.setCc(updateUser.getCc());

        response.put("ok", true);
        response.put("data", updateUser);
        response.put("message", "usuario actualizado");

        return new ResponseEntity<>(response, HttpStatus.OK);

   
 
    }


// 6) Eliminar un usuario por ID, no se elimina el registro de la base de datos temporal. En su
// lugar, se cambia el valor del atributo 'isActivated' a 'false'. /users/123

@DeleteMapping("/users/{idUser}")

public ResponseEntity<?> DeleteById(@PathVariable UUID idUser){

    Map<String,Object> response = new HashMap<>();

    User userInDb = usuariosDb.stream().filter(item->item.getId().equals(idUser)).findFirst().orElse(null);

    if (userInDb== null) {
        response.put("ok", false);
        response.put("data", "");
        response.put("message", "User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    userInDb.setIsActivated(false);
    
        response.put("ok", true);
        response.put("data", "");
        response.put("message", "User has been deactivated ");

    return new ResponseEntity<>(response, HttpStatus.OK);

}

//-------------------- CUENTA BANCARIA -----------------------------------------------------

// 7)Crear una cuenta bancaria, se asegura de que se cumplan varias condiciones. Primero, se
// verifica que el campo 'number' sea único, lo que significa que no esté asociado a ninguna
// otra cuenta existente. 

// Segundo, se confirma que el campo 'type' sea 'ahorro' o 'corriente'.

// Por último, se valida que el campo 'idUser' esté relacionado con un usuario que esté
// activado en la base de datos del banco, es decir, que tenga el atributo 'isActivated'
// configurado como 'true. /account

@PostMapping("/account")

public ResponseEntity<?> createAccount(@RequestBody Account newAccount){

    Map<String,Object> response = new HashMap<>();

    if(newAccount.getType()==null||newAccount.getType().isEmpty()||newAccount.getNumber().equals(null)||newAccount.getBalance().equals(null)){

            response.put("ok", false);
            response.put("data", "");
            response.put("message", "All fields are mandatory");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    if(newAccount.getType()!="ahorro"|| newAccount.getType()!="corriente"){

            response.put("ok", false);
            response.put("data", "");
            response.put("message", "El campo ingresado no es valido");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // Por último, se valida que el campo 'idUser' esté relacionado con un usuario que esté
    // activado en la base de datos del banco, es decir, que tenga el atributo 'isActivated'
    // configurado como 'true. /account


        cuentasDb.add(newAccount);
        response.put("ok", true);
        response.put("data", newAccount);
        response.put("message", "cuenta creada");

        return new ResponseEntity<>(response, HttpStatus.CREATED);

 

    }


        // 8)Listar todas las cuentas bancarias cuyo campo 'isActivated' esté configurado como 'true',
        // lo que significa que las cuentas están activas en el banco. /accounts

        @GetMapping("/accounts")
        public ResponseEntity<?> listAccountsActivated(){

            Map<String,Object> response = new HashMap<>();

            List<Account> activatedAccounts = cuentasDb.stream().filter(item ->item.getIsActivated().equals(true)).collect(Collectors.toList());

             response.put("ok", true);
             response.put("data", activatedAccounts);
             response.put("message", "Activated Accounts Lists");
             return new ResponseEntity<>(response, HttpStatus.OK);


        }


        // 9) Obtener una cuenta bancaria por ID cuyo campo 'isActivated' esté configurado como
        // 'true', lo que significa que la cuenta está activa en el banco. En caso de que 'isActivated'
        // esté configurado como 'false', se enviará un mensaje de error indicando que la cuenta no
        // se ha encontrado. /accounts/456

        @GetMapping("/accounts/ 456")

        public ResponseEntity<?> getAccountById(@PathVariable UUID id){

            Map<String,Object> response = new HashMap<>();

            Account accountById = cuentasDb.stream().filter(item->item.getId().equals(id)&&item.getIsActivated().equals(true)).findFirst().orElse(null);

        if (accountById == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Cuenta no se ha encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        
            response.put("ok", true);
            response.put("data", accountById);
            response.put("message", "Cuenta Encontrada");
            return new ResponseEntity<>(response, HttpStatus.OK);
        

    }


    //10)Listar todas las cuentas bancarias sin excepción. /accounts/allaccounts

    @GetMapping("/accounts/allaccounts")

    public ResponseEntity<?> listAllaccountsWithoutException(){
        Map<String,Object> response = new HashMap<>();

         response.put("ok", true);
         response.put("data", cuentasDb);
         response.put("message", "Lista de cuentas");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }



    // 11) Actualizar los datos de una cuenta bancaria por ID, se deben considerar algunas
    // restricciones. Los atributos 'idUser' y 'number' no pueden ser actualizados. Si por alguna
    // razón, los valores de 'idUser' o 'number' que se envían desde el cliente (por ejemplo,
    // Postman) son diferentes de los asociados a la cuenta que se desea actualizar, se generará
    // un error 400. Además, si se desea cambiar el atributo 'type', este debe ser 'ahorro' o
    // 'corriente'. /accounts/456


    @PutMapping("/accounts/{id}")

    public ResponseEntity<?> updateAccountById (@RequestBody Account updateAccount, @PathVariable UUID id){

         Map<String,Object> response = new HashMap<>();

         Account accountFound = cuentasDb.stream().filter(p -> p.getId().equals(id)).findAny().orElse(null);

         if (accountFound == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "cuenta no encontrada");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }


        if(updateAccount.getType()!="ahorro"||updateAccount.getType()!="corriente"){

            response.put("ok", true);
            response.put("data", "");
            response.put("message", "Los campos no son validos");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }

        if(!accountFound.getIdUser().equals(updateAccount.getIdUser())||!accountFound.getNumber().equals(updateAccount.getNumber())){

            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Los campos no se pueden actualizar");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }

            accountFound.setBalance(updateAccount.getBalance());
            accountFound.setType(updateAccount.getType());

            response.put("ok", true);
            response.put("data", updateAccount);
            response.put("message", "Cuenta actualizada");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);




    

    }







    // 12 // Eliminar una cuenta bancaria por ID, no se elimina el registro de la base de datos temporal.
    // // En su lugar, se cambia el valor del atributo 'isActivated' a 'false'. /accounts/456


    @DeleteMapping("/accounts/{id}")

    public ResponseEntity<?> DeleteAccountById(@PathVariable UUID id){

    Map<String,Object> response = new HashMap<>();

    Account accountInDb = cuentasDb.stream().filter(item->item.getId().equals(id)).findFirst().orElse(null);

    if (accountInDb== null) {
        response.put("ok", false);
        response.put("data", "");
        response.put("message", " account not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    accountInDb.setIsActivated(false);
    
        response.put("ok", true);
        response.put("data", "");
        response.put("message", "Account has been deactivated ");
    return new ResponseEntity<>(response, HttpStatus.OK);

}

//-------------------------TRANSFERENCIAS---------------------------

// 13) Listar todas las transferencias del banco. /transfers

@GetMapping("/transfers")

public ResponseEntity<?> listAllTransfer(){

     Map<String,Object> response = new HashMap<>();

        response.put("ok", true);
        response.put("data", transferenciasDb);
        response.put("message", "Lista de transferencias");
        return new ResponseEntity<>(response, HttpStatus.OK);
     

}


// 14)  Listar transferencia por ID. /transfers/789

@GetMapping("/transfers/{id}")

public ResponseEntity<?> listTransfersById(@PathVariable UUID id){

    Map<String,Object> response = new HashMap<>();

    Transfer listById = transferenciasDb.stream().filter(item->item.getId().equals(id)).findFirst().orElse(null);

    if(listById==null){
        response.put("ok", false);
        response.put("data", "");
        response.put("message", "transferencia no encontrada");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

        response.put("ok", true);
        response.put("data", listById);
        response.put("message", "transferencia encontrada");
        return new ResponseEntity<>(response, HttpStatus.OK);
}


}


   
    






