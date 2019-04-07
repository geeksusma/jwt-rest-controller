package es.geeksusma.jwtrestcontroller.security;

import es.geeksusma.jwtrestcontroller.config.EncryptionPropertiesConfiguration;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static javax.crypto.Cipher.getInstance;
import static org.springframework.util.Assert.isTrue;

@Component
public class PasswordDecryptor {
    private static final String AES = "AES";
    private static final String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    private final EncryptionPropertiesConfiguration encryptionPropertiesConfiguration;

    PasswordDecryptor(EncryptionPropertiesConfiguration encryptionPropertiesConfiguration) {
        this.encryptionPropertiesConfiguration = encryptionPropertiesConfiguration;
    }

    public String decrypt(byte[] encodedPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        isTrue(encodedPassword != null && encodedPassword.length != 0, "Binary to decrypt is mandatory");
        final byte[] encryptionKeyBytes = encryptionPropertiesConfiguration.getKey().getBytes();
        Cipher cipher = getInstance(AES_ECB_PKCS5_PADDING);
        final SecretKey secretKey = new SecretKeySpec(encryptionKeyBytes, AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodePassword = cipher.doFinal(encodedPassword);
        return new String(decodePassword);
    }
}
