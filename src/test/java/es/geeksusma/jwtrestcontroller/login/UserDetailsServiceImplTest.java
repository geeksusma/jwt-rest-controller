package es.geeksusma.jwtrestcontroller.login;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    private static final String NOT_FOUND_GEEKSUSMA_ES = "NOT_FOUND@GEEKSUSMA.ES";

    private static final String FOUND_GEEKSUSMA_ES = "FOUND@GEEKSUAMA.ES";

    private static final long USER_ID = 1L;
    private static final String PASSWORD = "1234";

    @Mock
    private UserDAO userDAO;


    private UserDetailsService userDetailsService;

    @Before
    public void setUp() {

        userDetailsService = new UserDetailsServiceImpl(userDAO);
    }

    @Test
    public void should_throwException_when_userNotFound() {

        //when
        final Throwable throwable = catchThrowable(() -> userDetailsService.loadUserByUsername(NOT_FOUND_GEEKSUSMA_ES));

        //then
        assertThat(throwable).isInstanceOf(UsernameNotFoundException.class);

    }

    @Test
    public void should_returnUser_when_userIsFound() {
        //given
        given(userDAO.getByEmail(FOUND_GEEKSUSMA_ES)).willReturn(
                Optional.of(
                        User.builder()
                                .userId(USER_ID)
                                .password(PASSWORD)
                                .email(FOUND_GEEKSUSMA_ES)
                                .build()
                )
        );

        final UserDetails expected = new org.springframework.security.core.userdetails.User(FOUND_GEEKSUSMA_ES, PASSWORD, Collections.emptyList());
        //when
        final UserDetails userDetails = userDetailsService.loadUserByUsername(FOUND_GEEKSUSMA_ES);

        //then
        assertThat(userDetails).isEqualToComparingFieldByFieldRecursively(expected);
    }
}