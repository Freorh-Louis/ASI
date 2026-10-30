package com.ptittonnerre.users.userMicroService;

import com.ptittonnerre.users.globalLibraries.Card;
import com.ptittonnerre.users.globalLibraries.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

//pour les tests
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/users") // Pluriel pour respecter les conventions REST
public class UserRestCtrl {

    @Autowired
    private UserService uService;

    //  Inscription
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return uService.register(user);
    }

    //  Connexion
    @PostMapping("/login")
    public User loginUser(@RequestBody User user) {
        return uService.login(user.getMail(), user.getPassword());
    }

    //  Récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        User user = uService.getUser(id);
        return ResponseEntity.ok(user);
    }

    //  Récupérer les cartes d’un utilisateur
    @GetMapping("/{id}/cards")
    public Set<CardDTO> getUserCards(@PathVariable int id) {
        return uService.getCards(id);
    }

    //  Récupérer tous les utilisateurs (DTO)
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return uService.getAllUserDTOs();
    }

    //  Supprimer une carte de l'inventaire d'un utilisateur
    @DeleteMapping("/{userId}/cards/{cardId}")
    public ResponseEntity<Void> removeCardFromUser(@PathVariable int userId, @PathVariable String cardId) {
        boolean removed = uService.removeCardFromUser(userId, cardId);
        if (removed) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carte non trouvée ou utilisateur inexistant");
        }
    }

    //  Ajouter une carte à un utilisateur par son ID
    @PostMapping("/{userId}/cards/{cardId}")
    public ResponseEntity<Void> addCardToUserById(@PathVariable int userId, @PathVariable String cardId) {
        boolean added = uService.addCardToUserById(userId, cardId);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Carte déjà possédée ou introuvable");
        }
    }

    //  Test
    @GetMapping("/test")
    public String hello() {
        return "User controller is working!";
    }

    //endpoint pour test la récupération de carte par id
    @GetMapping("/card/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable String id) {
        try {
            CardDTO card = uService.getCardByIdFromMicroservice(id);
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //récupérer un user par son id
//    @GetMapping("/user/{id}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
//        UserDTO user = marketService.getUserById(id);
//        if (user == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
//        }
//        return ResponseEntity.ok(user);
//    }

}
