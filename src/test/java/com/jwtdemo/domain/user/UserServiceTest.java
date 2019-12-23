package com.jwtdemo.domain.user;

import com.jwtdemo.domain.user.exception.DataNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    private UserFactory userFactory = new UserFactory();
    private String userName = "user";

    @Test
    public void testGetUserByName() {
        User expectedUser = userFactory.createUser(userName);
        given(userRepository.findByName(eq(userName))).willReturn(Optional.of(expectedUser));
        User actualUser = userService.getUser(userName);

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test(expected = DataNotFoundException.class)
    public void testGetUserByNameFailed() {
        given(userRepository.findByName(eq(userName))).willReturn(Optional.empty());
        userService.getUser(userName);
    }

    @Test
    public void testGetUserByCredentials() {
        User expectedUser = userFactory.createUser(userName);
        given(userRepository.findByName(eq(userName))).willReturn(Optional.of(expectedUser));
        User actualUser = userService.getUser(userName, expectedUser.getSecret());

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test(expected = DataNotFoundException.class)
    public void testGetUserByCredentialsFailed() {
        User expectedUser = userFactory.createUser(userName);
        given(userRepository.findByName(eq(userName))).willReturn(Optional.of(expectedUser));
        userService.getUser(userName, expectedUser.getSecret() + "some wrong data");
    }

    @Test
    public void changeTokenSalt() {
        User user = userFactory.createUser(userName);
        String oldSalt = user.getTokenSalt();
        userService.changeTokenSalt(user);

        then(userRepository).should(times(1)).save(eq(user));
        assertThat(user.getTokenSalt()).isNotEqualTo(oldSalt);
    }
}
