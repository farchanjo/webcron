package br.eti.archanjo.webcron.utils;

import br.eti.archanjo.webcron.enums.HashType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/*
 * Created by fabricio on 25/06/17.
 */
public class HashUtils {
    private static final Logger logger = LoggerFactory.getLogger(HashUtils.class);

    /**
     * @param file {@link File}
     * @return {@link String}
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String computeContentMD5Header(File file) throws NoSuchAlgorithmException, IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        DigestInputStream digestInputStream = new DigestInputStream(inputStream,
                MessageDigest.getInstance("MD5"));
        String md5Hex = null;
        while (digestInputStream.read(buffer) > 0) {
            md5Hex = new String(Base64.encodeBase64(digestInputStream.getMessageDigest().digest()), StandardCharsets.UTF_8);
        }
        return md5Hex;
    }

    /**
     * @return {@link String}
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * @param file {@link File}
     * @return {@link String}
     */
    public static String getMD5Base64(File file) throws IOException {
        return Base64.encodeBase64String(DigestUtils.md5(new FileInputStream(file)));
    }

    /**
     * @param message {@link String}
     * @return {@link String}
     */
    public static String sha256(String message) {
        return sha(message, HashType.SHA256);
    }

    /**
     * @param message {@link String}
     * @return {@link String}
     */
    public static String sha512(String message) {
        return sha(message, HashType.SHA512);
    }

    /**
     * @param message {@link String}
     * @return {@link String}
     */
    public static String sha1(String message) {
        return sha(message, HashType.SHA1);
    }

    /**
     * @param message {@link String}
     * @param type    {@link HashType}
     * @return {@link String}
     */
    private static String sha(String message, HashType type) {
        switch (type) {
            case SHA1:
                return DigestUtils.sha1Hex(message);
            case SHA256:
                return DigestUtils.sha256Hex(message);
            case SHA512:
                return DigestUtils.sha512Hex(message);
            default:
                return DigestUtils.sha256Hex(message);
        }
    }

}