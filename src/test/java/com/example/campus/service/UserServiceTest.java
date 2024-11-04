package com.example.campus.service;

import com.example.campus.entity.NationalIdInfo;
import com.example.campus.entity.User;
import com.example.campus.exception.UserNotFoundException;
import com.example.campus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserService(new DummyNormalizer(), userRepository);
    }

    @Test
    public void testFindAllUsers() {
        User user1 = getUser();
        User user2 = getUser2();

        List<User> users = userService.findAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testCreateUser() {
        List<Arguments> usersAndUsernames = new ArrayList<>();
        usersAndUsernames.add(Arguments.of(createUser("Josep", "123450", "josep@example.com"), "JDoe"));
        usersAndUsernames.add(Arguments.of(createUser("Jane", "123451", "jane@example.com"), "JDoeP"));
        usersAndUsernames.add(Arguments.of(createUser("Jeremy", "123452", "jeremy@example.com"), "JDoePo"));
        usersAndUsernames.add(Arguments.of(createUser("Jana", "123453", "jana@example.com"), "JDoePou"));
        usersAndUsernames.add(Arguments.of(createUser("John", "123454", "john@example.com"), "JoDoe"));
        usersAndUsernames.add(Arguments.of(createUser("Joana", "123455", "joana@example.com"), "JoDoeP"));
        usersAndUsernames.add(Arguments.of(createUser("Joan", "123456", "joan@example.com"), "JoDoePo"));
        usersAndUsernames.add(Arguments.of(createUser("Jordi", "123457", "jordi@example.com"), "JoDoePou"));
        usersAndUsernames.add(Arguments.of(createUser("Joel", "123458", "joel@example.com"), "JoeDoe"));
        usersAndUsernames.add(Arguments.of(createUser("Joel Joan", "123459", "joeljoan@example.com"), "JoeDoeP"));
        usersAndUsernames.add(Arguments.of(createUser("Joey", "123460", "joey@example.com"), "JoeDoePo"));
        usersAndUsernames.add(Arguments.of(createUser("Joey Joan", "123461", "joeyjoan@example.com"), "JoeDoePou"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123462", "joe@example.com"), "JDoe1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123463", "joedoe@example.com"), "JDoeP1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123464", "joedoepou@example.com"), "JDoePo1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123465", "jdp@example.com"), "JDoePou1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123466", "joedp@example.com"), "JoDoe1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123467", "jdoe@example.com"), "JoDoeP1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123468", "jdoep@example.com"), "JoDoePo1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123469", "jdoepou@example.com"), "JoDoePou1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123470", "joe@example.com"), "JoeDoe1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123471", "joedoe@example.com"), "JoeDoeP1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123472", "joedoepou@example.com"), "JoeDoePo1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123473", "jdp@example.com"), "JoeDoePou1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123474", "joedp@example.com"), "JDoe2"));

        for (Arguments userAndUsername : usersAndUsernames) {
            User user = (User) userAndUsername.get()[0];
            String expectedUsername = (String) userAndUsername.get()[1];

            User createdUser = userService.createUser(user);

            assertEquals(expectedUsername, createdUser.getUsername());
        }
    }

    @Test
    public void testFindUserById() {
        User user = getUser();

        User foundUser = userService.findUserById(user.getId());

        assertEquals(user, foundUser);

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(999L));
    }

    @Test
    public void testFindUserByUsername() {
        User user = getUser();

        User foundUser = userService.findUserByUsername(user.getUsername());

        assertEquals(user, foundUser);

        assertThrows(UserNotFoundException.class, () -> userService.findUserByUsername("nonexistent"));
    }

    @Test
    public void testUpdateUser() {
        User user = getUser();

        User userDetails = new User();
        userDetails.setName("Alice Updated");

        User updatedUser = userService.updateUser(user.getId(), userDetails);

        assertEquals("Alice Updated", updatedUser.getName());
    }

    @Test
    public void testDeleteUser() {
        User user = getUser();

        userService.deleteUser(user.getId());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void testActivateUser() {
        User user = getUser();
        user.setIsActive(false);
        userRepository.save(user);

        userService.activateUser(user.getId());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertTrue(updatedUser.getIsActive());
    }

    @Test
    public void testDeactivateUser() {
        User user = getUser();
        user.setIsActive(true);
        userRepository.save(user);

        userService.deactivateUser(user.getId());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertFalse(updatedUser.getIsActive());
    }

    @Test
    public void testSaveUser() {
        User user = getUser();
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals("Alice", savedUser.getName());
    }

    private User getUser() {
        User user = createUser("Alice", "123450", "alice@example.com");
        return userService.createUser(user);
    }

    private User getUser2() {
        User user = createUser("Bob", "123451", "bob@example.com");
        return userService.createUser(user);
    }

    private User createUser(String name, String nationalId, String email) {
        User user = new User();
        user.setName(name);
        user.setFirstSurname("Doe");
        user.setSecondSurname("Pou");
        user.setNationalIdInfo(createNationalIdInfo(nationalId));
        user.setEmail(email);
        user.setIsActive(true);
        return user;
    }

    private NationalIdInfo createNationalIdInfo(String nationalId) {
        NationalIdInfo nationalIdInfo = new NationalIdInfo();
        nationalIdInfo.setNationalId(nationalId);
        nationalIdInfo.setCountry("Canada");
        return nationalIdInfo;
    }
}
