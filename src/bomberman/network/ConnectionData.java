package bomberman.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.util.Base64.getDecoder;

public class ConnectionData {

    private final InetAddress ip;
    private final int port;
    private final PublicKey publicKey;

    public ConnectionData(InetAddress ip, int port, PublicKey publicKey) {
        this.ip = ip;
        this.port = port;
        this.publicKey = publicKey;
    }

    public ConnectionData(InetAddress ip, int port, String json){
        this.ip = ip;
        this.port = port;

        Gson gson = new Gson();

        Type type = new TypeToken<Map<String, byte[]>>(){}.getType();


        Map<String, byte[]> connectionData = gson.fromJson(json, type);

        PublicKey tempKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(connectionData.get("modulus")), new BigInteger(connectionData.get("exponent")));

            tempKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        publicKey = tempKey;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String toJson(){

        Map<String, byte[]> connectionData = new HashMap<>();

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

            connectionData.put("modulus", publicKeySpec.getModulus().toByteArray());
            connectionData.put("exponent", publicKeySpec.getPublicExponent().toByteArray());

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        return gson.toJson(connectionData);
    }

    public String encrypt(String message){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decrpyt(String message, PrivateKey privateKey){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] bytes = Base64.getDecoder().decode(message);

            return new String(cipher.doFinal(bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }
}
