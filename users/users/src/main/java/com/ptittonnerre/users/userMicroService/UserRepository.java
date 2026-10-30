package com.ptittonnerre.users.userMicroService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByMail(String mail);          // pour la connexion
    Optional<User> findById(Integer id);             // déjà fourni par JpaRepository
}
