package com.nttdata.bank.auth.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Login account stored only by the authentication service.
 */
@Getter
@Setter
@Document("users")
public class UserDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String passwordHash;

    private List<String> roles = new ArrayList<>();
}
