package es.geeksusma.jwtrestcontroller.login;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
class UserDAO {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    UserDAO(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Transactional(readOnly = true)
    Optional<User> getByEmail(final String email) {

        final Optional<UserModel> foundUser = userRepository.findByEmail(email);
        return foundUser.map(userMapper::mapToDomain);
    }

}
