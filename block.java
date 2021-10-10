import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class block {
    long blockNumber;
    String miner;
    String currentBlockHash;
    String previousBlockHash;
    long nonce;
    ArrayList<transaction> transactionsList;
    String difficulty = "0000"; 

    block(long blockNumber, peer peer, String previousBlockHash){
        // block b = new block();
        this.blockNumber = blockNumber;
        this.miner = peer.name;
        
        long nonce = 1;
        String hashTest = null;
        String block_data = String.valueOf(blockNumber) + previousBlockHash;
        String message =   block_data + nonce; 

        while (true) {
            try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(digest.digest(message.concat(Long.toString(nonce)).getBytes(StandardCharsets.UTF_8)));
            hashTest = bytesToHex(reverseBytes(hash));
            // System.out.println("Hash: " + hashTest);
            
            if(hashTest.substring(0, difficulty.length()).equals(difficulty))
                break;
            nonce++;
            }catch(NoSuchAlgorithmException e){
                e.getStackTrace();
            }
        }

        this.currentBlockHash = hashTest;
        this.previousBlockHash = previousBlockHash;
        this.nonce = nonce;
    }

    
    private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
        // System.out.println(hex);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}

    private static byte[] reverseBytes(byte[] bytes) { 
        byte[] buffer = new byte[bytes.length]; 
        for (int i = 0; i < bytes.length; i++) 
        	buffer[i] = bytes[bytes.length - 1 - i]; 
        // for(int i : buffer) System.out.println(i);
        return buffer; 
    }

    public static void blockInfo(ArrayList<block> blockchain){
        for(int i = 0; i < blockchain.size(); i++){
            block b = blockchain.get(i);
            System.out.println("=======================");
            System.out.println("Block Number: " + b.blockNumber);
            System.out.println("Block Miner: " + b.miner);
            System.out.println("Previous Block Hash: " + b.previousBlockHash);
            System.out.println("Current Block Hash: " + b.currentBlockHash);
            System.out.println("Block nonce: " + b.nonce);
        }
    }    
}
