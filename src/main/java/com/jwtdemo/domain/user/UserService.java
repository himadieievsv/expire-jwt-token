package com.jwtdemo.domain.user;

import com.jwtdemo.domain.user.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @NonNull
    public User getUser(@NonNull String name, @NonNull String secret) {
        User user = getUser(name);
        if (user.getSecret().equals(secret)) {
            return user;
        } else {
            throw new DataNotFoundException("No user matching username/password exists!");
        }
    }

    public User getUser(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
    }

    public void changeTokenSalt(@NonNull User user) {
        user.changeTokenSalt();
        userRepository.save(user);
    }
}
