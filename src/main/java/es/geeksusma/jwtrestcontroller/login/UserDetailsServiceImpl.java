package es.geeksusma.jwtrestcontroller.login;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDAO;

    UserDetailsServiceImpl(final UserDAO userDAO) {

        this.userDAO = userDAO;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {

        final Optional<User> user = userDAO.getByEmail(s);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException(String.format("The user %s doesn't exist", s));
        }

        final User fetchedUser = user.get();
        return new org.springframework.security.core.userdetails.
                User(fetchedUser.getEmail(), fetchedUser.getPassword(), Collections.emptyList());

    }


}
