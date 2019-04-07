package es.geeksusma.jwtrestcontroller.login;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "user")
class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "pwd", columnDefinition = "blob")
    private byte[] password;

}
