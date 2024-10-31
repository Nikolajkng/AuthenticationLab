import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

public class keygen {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        byte[] privKey = pair.getPrivate().getEncoded();
        byte[] pubKey = pair.getPublic().getEncoded();
        System.out.println(Arrays.toString(privKey));
        System.out.println(Arrays.toString(pubKey));
    }
}
