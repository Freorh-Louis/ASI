package com.ptittonnerre.users.userMicroService;

import com.ptittonnerre.users.client.CardClient;
import com.ptittonnerre.users.globalLibraries.Card;
import com.ptittonnerre.users.globalLibraries.CardDTO;
import com.ptittonnerre.users.globalLibraries.HttpGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardClient cardClient;

    private final WebClient cardWebClient;
    private final HttpGetService httpGetService;

    public UserService() {
        this.cardWebClient = WebClient.create("http://localhost:8082/card");
        this.httpGetService = new HttpGetService();
    }

    public User register(User u) {
        Optional<User> existing = userRepo.findByMail(u.getMail());
        if (existing.isPresent()) {
            throw new RuntimeException("Cet utilisateur existe déjà");
        }

        u.setWallet(100);
        Set<Card> cards = new HashSet<>(cardClient.getRandomCards(5));
        u.setCards(cards);

        for (Card card : cards) {
            card.setUser(u);
        }

        return userRepo.save(u);
    }

    public User login(String mail, String password) {
        return userRepo.findByMail(mail)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Login invalide"));
    }

    public User getUser(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public Set<CardDTO> getCards(int id) {
        return getUser(id).getCards().stream()
                .map(this::convertCardToDTO)
                .collect(Collectors.toSet());
    }

    private CardDTO convertCardToDTO(Card card) {
        return new CardDTO(
                card.getId(),
                card.getName(),
                card.getFamilyName(),
                card.getDescription(),
                card.getImgSrc(),
                card.getHp(),
                card.getEnergy(),
                card.getAttack(),
                card.getDefense(),
                card.getPrice()
        );
    }

    public void buyCard(User user, Card card) {
        user.addCard(card);
        card.setUser(user);
        userRepo.save(user);
    }

    public void sellCard(User user, Card card) {
        user.removeCard(card);
        card.setUser(null);
        userRepo.save(user);
    }

    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getMail(),
                user.getWallet()
        );
    }

    public List<UserDTO> getAllUserDTOs() {
        return userRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public CardDTO getCardByIdFromMicroservice(String cardId) {
        try {
            Mono<Card> cardMono = httpGetService.request(cardWebClient, "/" + cardId, Card.class);
            Card card = cardMono.block(); // bloquant ici
            return convertCardToDTO(card);
        } catch (Exception e) {
            throw new RuntimeException("Carte introuvable dans le microservice", e);
        }
    }
    //  Supprimer une carte de l'inventaire d'un utilisateur
    public boolean removeCardFromUser(int userId, String cardId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        Optional<Card> cardToRemove = user.getCards().stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst();

        if (cardToRemove.isPresent()) {
            Card card = cardToRemove.get();
            user.removeCard(card);
            card.setUser(null);
            userRepo.save(user);
            return true;
        }

        return false;
    }

    //  Ajouter une carte complète (objet) à un utilisateur
    public boolean addCardToUser(int userId, Card card) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();

        boolean alreadyExists = user.getCards().stream()
                .anyMatch(c -> c.getId().equals(card.getId()));
        if (alreadyExists) return false;

        user.addCard(card);
        card.setUser(user);
        userRepo.save(user);

        return true;
    }

    //  Ajouter une carte à partir de son ID via appel HTTP
    public boolean addCardToUserById(int userId, String cardId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) return false;

        Optional<Card> cardOpt = cardClient.getCardById(cardId);
        if (cardOpt.isEmpty()) return false;

        User user = userOpt.get();
        Card card = cardOpt.get();

        boolean alreadyExists = user.getCards().stream()
                .anyMatch(c -> c.getId().equals(card.getId()));
        if (alreadyExists) return false;

        user.addCard(card);
        card.setUser(user);
        userRepo.save(user);

        return true;
    }
}
