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
public class PasswordCipherTest {

    private PasswordCipher passwordCipher;
    @Mock
    private EncryptionPropertiesConfiguration encryptionPropertiesConfiguration;

    @Before
    public void setUp() {
        given(encryptionPropertiesConfiguration.getKey()).willReturn("thisisa128bitkey");
        passwordCipher = new PasswordCipher(encryptionPropertiesConfiguration);
    }

    @Test
    public void should_throwIllegal_when_cipherButNullTextToCipher() {

        // when
        final Throwable expectedException = catchThrowable(() -> passwordCipher.cipher(null));

        //
        assertThat(expectedException).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Text to cipher is mandatory");

    }

    @Test
    public void should_transformTextToCipheredBinary_when_cipher() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        // given
        final String textToCipher = "textToCipher";
        final byte[] expected = new byte[]{66, 90, -48, 58, -110, 55, -8, 87, 104, -112, -1, -11, -34, 32, 3, 4};

        // when
        final byte[] result = passwordCipher.cipher(textToCipher);

        // then
        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void should_throwIllegal_when_cipherButBlankTextToCipher() {
        // given
        final String textToCipher = "";

        // when
        final Throwable expectedException = catchThrowable(() -> passwordCipher.cipher(textToCipher));
        // then
        assertThat(expectedException).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Text to cipher is mandatory");
    }

    @Test
    public void should_useKeyFromPropertiesConfiguration_when_cipher() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        // given
        final String textToCipher = "test";

        // when
        passwordCipher.cipher(textToCipher);

        // then
        then(encryptionPropertiesConfiguration).should().getKey();

    }
}