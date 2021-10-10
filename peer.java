import java.util.*;
import java.security.*;
import java.nio.charset.StandardCharsets;

public class peer {
    String name;
    String publicKey;
    String privateKey;
    String blockAdd; // blockchainAddress of a node. hash of public key
    long UTXO;

    peer(String name, long UTXO) {
        this.name = name;
        this.UTXO = UTXO;
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
            keyPairGen.initialize(512);
            KeyPair pair = keyPairGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            byte[] byte_pubkey = publicKey.getEncoded();
            String str_key = Base64.getEncoder().encodeToString(byte_pubkey);
            // System.out.println(str_key);
            this.publicKey = str_key;
            
            byte[] byte_privkey = privateKey.getEncoded();
            String priv_str_key = Base64.getEncoder().encodeToString(byte_privkey);
            this.privateKey = priv_str_key;
            // System.out.println(priv_str_key);
            this.blockAdd = publicKey_to_blockAdd(this.publicKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private static String publicKey_to_SHA(String publicKey){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(publicKey.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getEncoder().encodeToString(hash); // string after SHA-256 hash
            // System.out.println("publicKey_to_SHA returns: " + encoded);
            return encoded;
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    private static String publicKey_to_blockAdd (String publicKey){
        String blockAdd = publicKey_to_SHA(publicKey);
        return blockAdd;
    }

    public static void infoPeer(peer p){
        System.out.println("==========================================");
        System.out.println("\nName: \t" + p.name);
        System.out.println("\nPrivate Key: " + p.publicKey);
        System.out.println("\nPublic Key: " + p.privateKey);
        System.out.println("\nBlock Address: " + p.blockAdd);
    }

    
}