package io.ccjmne.endpoints.users;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

@ApplicationScoped
public class UsersRepository implements PanacheMongoRepository<User> {}
