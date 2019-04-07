package es.geeksusma.jwtrestcontroller.security;

import es.geeksusma.jwtrestcontroller.config.EncryptionPropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class PasswordDecryptorTest {

    private PasswordDecryptor passwordDecryptor;

    @Mock
    private EncryptionPropertiesConfiguration encryptionPropertiesConfiguration;

    @Before
    public void setUp() {
        passwordDecryptor = new PasswordDecryptor(encryptionPropertiesConfiguration);
        given(encryptionPropertiesConfiguration.getKey()).willReturn("thisisa128bitkey");
    }

    @Test
    public void should_throwIllegalArgument_when_passwordIsNull() {
        // given
        final byte[] encodedPassword = null;
        // when
        final Throwable expectedException = catchThrowable(() -> passwordDecryptor.decrypt(encodedPassword));
        // then
        assertThat(expectedException).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Binary to decrypt is mandatory");

    }

    @Test
    public void should_throwIllegalArgument_when_passwordIsEmpty() {
        // given
        final byte[] encodedPassword = new byte[0];
        // when
        final Throwable expectedException = catchThrowable(() -> passwordDecryptor.decrypt(encodedPassword));
        // then
        assertThat(expectedException).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Binary to decrypt is mandatory");
    }

    @Test
    public void should_returnStringPaswordDecript_when_decrypter() throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // given
        final String expected = "textToCipher";
        final byte[] encodedPassword = new byte[]{66, 90, -48, 58, -110, 55, -8, 87, 104, -112, -1, -11, -34, 32, 3,
                4};
        // when
        final String passwordDecoded = passwordDecryptor.decrypt(encodedPassword);
        // then
        assertThat(passwordDecoded).isEqualTo(expected);

    }

    @Test
    public void should_callToGetKey_when_decrypt() throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // given
        final byte[] encodedPassword = new byte[]{66, 90, -48, 58, -110, 55, -8, 87, 104, -112, -1, -11, -34, 32, 3,
                4};
        // when
        passwordDecryptor.decrypt(encodedPassword);
        // then
        then(encryptionPropertiesConfiguration).should().getKey();

    }

}
