package es.geeksusma.jwtrestcontroller.login;

import es.geeksusma.jwtrestcontroller.security.PasswordDecryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserMapperTest {


    private static final String MYEMAIL_EMAIL_COM = "myemail@email.com";
    private static final String MY_NICK = "mynick";
    private static final Long USER_ID = 123L;
    private static final Long TERMS_CONDITIONS_ID = 12342L;
    private static final String PASSWORD = "1234556777888";
    private static final String MY_AVATAR = "my avatar";

    private UserMapper userMapper;
    @Mock
    private PasswordDecryptor passwordDecrypter;

    @Before
    public void setUp() {
        userMapper = new UserMapper(passwordDecrypter);

    }


    @Test
    public void should_mapToDomain_when_fromModel() {
        //given
        final UserModel user = UserModel.builder()
                .email(MYEMAIL_EMAIL_COM)
                .nickName(MY_NICK)
                .userId(USER_ID)
                .build();

        final User expected = User.builder()
                .email(user.getEmail())
                .nickName(user.getNickName())
                .userId(user.getUserId())
                .build();

        //when
        final User returned = userMapper.mapToDomain(user);

        //then
        assertThat(returned).isEqualToComparingFieldByField(expected);
    }


}