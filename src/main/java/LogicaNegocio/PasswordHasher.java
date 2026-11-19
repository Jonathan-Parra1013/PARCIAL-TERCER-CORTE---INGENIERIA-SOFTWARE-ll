package LogicaNegocio;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACIONES = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {}

    public static String hash(char[] password) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] hash = derivar(password, salt, ITERACIONES);
        return ITERACIONES + ":"
                + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(char[] password, String almacenado) {
        try {
            String[] partes = almacenado.split(":");
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            byte[] calculado = derivar(password, salt, iteraciones);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static byte[] derivar(char[] password, byte[] salt, int iteraciones) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iteraciones, HASH_BITS);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("No se pudo calcular el hash", e);
        } finally {
            spec.clearPassword();
        }
    }
}
