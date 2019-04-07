package es.geeksusma.jwtrestcontroller.login;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserDAOTest {


    private UserDAO userDAO;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;


    @Before
    public void setUp() {

        userDAO = new UserDAO(userRepository, userMapper);

    }


    @Test
    public void should_returnDomain_when_getByEmail() {
        //given
        final String userMail = "mymail@gmail.com";
        final UserModel foundUser = UserModel.builder()
                .email(userMail)
                .build();
        when(userRepository.findByEmail(userMail)).thenReturn(Optional.of(foundUser));

        final User expectedDomain = User.builder().email(userMail).build();
        when(userMapper.mapToDomain(foundUser)).thenReturn(expectedDomain);

        //when
        final Optional<User> result = userDAO.getByEmail(userMail);

        //then
        assertThat(result.orElse(User.builder().build())).isEqualTo(expectedDomain);
    }

    @Test
    public void should_returnNull_when_getByEmailButNotFound() {
        //given
        final String userMail = "mymail@gmail.com";

        //when
        final Optional<User> result = userDAO.getByEmail(userMail);

        //then
        assertThat(result.isPresent()).isFalse();
    }

}