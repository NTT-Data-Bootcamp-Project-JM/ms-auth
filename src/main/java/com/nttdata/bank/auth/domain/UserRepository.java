package com.nttdata.bank.auth.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Persistence port for login accounts.
 */
public interface UserRepository extends MongoRepository<UserDocument, String> {

    /**
     * Finds a login by its unique username.
     *
     * @param username login name
     * @return the account when it exists
     */
    Optional<UserDocument> findByUsername(String username);
}
