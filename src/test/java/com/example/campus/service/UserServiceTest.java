package com.example.campus.service;

import com.example.campus.entity.User;
import com.example.campus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository, new DummyNormalizer());
    }
    @Test
    public void testCreateUser() {
        List<Arguments> usersAndUsernames = new ArrayList<>();
        usersAndUsernames.add(Arguments.of(new User("Josep", "Doe", "Pou", "123450", "Chile", "josep@example.com"), "JDoe"));
        usersAndUsernames.add(Arguments.of(new User("Jane", "Doe", "Pou", "123451", "Chile", "jane@example.com"), "JDoeP"));
        usersAndUsernames.add(Arguments.of(new User("Jeremy", "Doe", "Pou", "123452", "Chile", "jeremy@example.com"), "JDoePo"));
        usersAndUsernames.add(Arguments.of(new User("Jana", "Doe", "Pou", "123453", "Chile", "jana@example.com"), "JDoePou"));
        usersAndUsernames.add(Arguments.of(new User("John", "Doe", "Pou", "123454", "Chile", "john@example.com"), "JoDoe"));
        usersAndUsernames.add(Arguments.of(new User("Joana", "Doe", "Pou", "123455", "Chile", "joana@example.com"), "JoDoeP"));
        usersAndUsernames.add(Arguments.of(new User("Joan", "Doe", "Pou", "123456", "Chile", "joan@example.com"), "JoDoePo"));
        usersAndUsernames.add(Arguments.of(new User("Jordi", "Doe", "Pou", "123457", "Chile", "jordi@example.com"), "JoDoePou"));
        usersAndUsernames.add(Arguments.of(new User("Joel", "Doe", "Pou", "123458", "Chile", "joel@example.com"), "JoeDoe"));
        usersAndUsernames.add(Arguments.of(new User("Joel Joan", "Doe", "Pou", "123459", "Chile", "joeljoan@example.com"), "JoeDoeP"));
        usersAndUsernames.add(Arguments.of(new User("Joey", "Doe", "Pou", "123460", "Chile", "joey@example.com"), "JoeDoePo"));
        usersAndUsernames.add(Arguments.of(new User("Joey Joan", "Doe", "Pou", "123461", "Chile", "joeyjoan@example.com"), "JoeDoePou"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123462", "Chile", "joe@example.com"), "JDoe1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123463", "Chile", "joedoe@example.com"), "JDoeP1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123464", "Chile", "joedoepou@example.com"), "JDoePo1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123465", "Chile", "jdp@example.com"), "JDoePou1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123466", "Chile", "joedp@example.com"), "JoDoe1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123467", "Chile", "jdoe@example.com"), "JoDoeP1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123468", "Chile", "jdoep@example.com"), "JoDoePo1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123469", "Chile", "jdoepou@example.com"), "JoDoePou1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123470", "Chile", "joe@example.com"), "JoeDoe1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123471", "Chile", "joedoe@example.com"), "JoeDoeP1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123472", "Chile", "joedoepou@example.com"), "JoeDoePo1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123473", "Chile", "jdp@example.com"), "JoeDoePou1"));
        usersAndUsernames.add(Arguments.of(new User("Joe", "Doe", "Pou", "123474", "Chile", "joedp@example.com"), "JDoe2"));

        for (Arguments userAndUsername : usersAndUsernames) {
            User user = (User) userAndUsername.get()[0];
            String expectedUsername = (String) userAndUsername.get()[1];

            User createdUser = userService.createUser(user);

            assertEquals(expectedUsername, createdUser.getUsername());
        }
    }
}
