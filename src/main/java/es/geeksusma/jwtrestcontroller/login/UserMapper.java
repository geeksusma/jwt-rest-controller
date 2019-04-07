package es.geeksusma.jwtrestcontroller.login;

import es.geeksusma.jwtrestcontroller.arch.exception.EncodingException;
import es.geeksusma.jwtrestcontroller.security.PasswordDecryptor;
import org.springframework.stereotype.Component;

@Component
class UserMapper {

    private final PasswordDecryptor passwordDecryptor;

    UserMapper(final PasswordDecryptor passwordDecryptor) {

        this.passwordDecryptor = passwordDecryptor;
    }


    User mapToDomain(UserModel userModel) {

        try {
            return User.builder()
                    .userId(userModel.getUserId())
                    .email(userModel.getEmail())
                    .nickName(userModel.getNickName())
                    .password(passwordDecryptor.decrypt(userModel.getPassword()))
                    .build();
        } catch (Exception e) {
            throw new EncodingException("Could not decode the password");
        }
    }


}
