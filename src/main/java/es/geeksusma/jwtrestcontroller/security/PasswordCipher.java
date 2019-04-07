package es.geeksusma.jwtrestcontroller.security;

import es.geeksusma.jwtrestcontroller.config.EncryptionPropertiesConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.springframework.util.Assert.isTrue;

@Component
class PasswordCipher {

    private static final String AES = "AES";
    private static final String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    private final EncryptionPropertiesConfiguration encryptionPropertiesConfiguration;

    PasswordCipher(EncryptionPropertiesConfiguration encryptionPropertiesConfiguration) {
        this.encryptionPropertiesConfiguration = encryptionPropertiesConfiguration;
    }

    byte[] cipher(String textToCipher) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        isTrue(!StringUtils.isEmpty(textToCipher), "Text to cipher is mandatory");

        final byte[] encryptionKeyBytes = encryptionPropertiesConfiguration.getKey().getBytes();
        final Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);
        final SecretKey secretKey = new SecretKeySpec(encryptionKeyBytes, AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(textToCipher.getBytes());

    }
}
