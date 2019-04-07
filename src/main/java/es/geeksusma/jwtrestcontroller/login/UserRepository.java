package es.geeksusma.jwtrestcontroller.login;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByEmail(final String email);

}

