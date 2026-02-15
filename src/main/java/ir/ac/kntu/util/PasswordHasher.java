package ir.ac.kntu.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * PBKDF2 password hashing helper.
 * <p>
 * Stored format: pbkdf2$<iterations>$<saltB64>$<hashB64>
 */
public final class PasswordHasher {
    private static final String PREFIX = "pbkdf2";
    private static final int SALT_LEN = 16;
    private static final int KEY_LEN_BITS = 256;
    private static final int DEFAULT_ITER = 65_536;

    private PasswordHasher() {
    }

    public static String hash(String plain) {
        byte[] salt = new byte[SALT_LEN];
        new SecureRandom().nextBytes(salt);
        byte[] dk = pbkdf2(plain.toCharArray(), salt, DEFAULT_ITER, KEY_LEN_BITS);
        return PREFIX + "$" + DEFAULT_ITER + "$" + b64(salt) + "$" + b64(dk);
    }

    public static boolean matches(String stored, String plain) {
        if (stored == null || plain == null) {
            return false;
        }
        // Backward compatibility: old data might store plain text.
        if (!stored.startsWith(PREFIX + "$")) {
            return stored.equals(plain);
        }
        String[] parts = stored.split("\\$");
        if (parts.length != 4) {
            return false;
        }
        int iter;
        try {
            iter = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        byte[] salt = b64d(parts[2]);
        byte[] expected = b64d(parts[3]);
        byte[] actual = pbkdf2(plain.toCharArray(), salt, iter, expected.length * 8);
        return constantTimeEquals(expected, actual);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLenBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLenBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("PBKDF2 not available", e);
        }
    }

    private static String b64(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    private static byte[] b64d(String s) {
        return Base64.getDecoder().decode(s);
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int r = 0;
        for (int i = 0; i < a.length; i++) {
            r |= a[i] ^ b[i];
        }
        return r == 0;
    }
}
