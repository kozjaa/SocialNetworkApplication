package com.example.market.repository;

import com.example.market.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepositoryManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveUser(User user)
    {
        entityManager.merge(user);
    }
}
