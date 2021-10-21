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
    ArrayList<transaction> transactionsList = new ArrayList<transaction>();
    StringBuilder difficulty = new StringBuilder("00");
    long timestamp;
    
    block(long blockNumber, peer peer, String previousBlockHash){
        this.blockNumber = blockNumber;
        this.miner = peer.name;
        
        long nonce = 1;
        String hashTest = null;
        String block_data = String.valueOf(blockNumber) + previousBlockHash;
        String message =   block_data + nonce; 

        long startTime = System.nanoTime();
        while (true) {
            try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(digest.digest(message.concat(Long.toString(nonce)).getBytes(StandardCharsets.UTF_8)));
            hashTest = bytesToHex(reverseBytes(hash));
            // System.out.println("Hash: " + hashTest);
            
            if(hashTest.substring(0, difficulty.length()).equals(difficulty.toString()))
                break;
            nonce++;
            }catch(NoSuchAlgorithmException e){
                e.getStackTrace();
            }
        }
        long endTime = System.nanoTime();
        this.timestamp = (endTime - startTime) / 1000000;
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

    public static block createGenesisBlock() {
        peer satoshi = new peer("Satioshi Nakamoto", 0);
        block genesisBlock = new block(0, satoshi, "");
        genesisBlock.currentBlockHash = "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f";
        genesisBlock.previousBlockHash = null;
        genesisBlock.nonce = 0;
        genesisBlock.transactionsList = null;
        System.out.println("======================");
        System.out.println("\nGenesis Block created");
        return genesisBlock;
    }

    public static block mineBlock(peer Miner){
        block previous = Main.blockchain.get(Main.blockchain.size() - 1);
        block b = new block(previous.blockNumber+1, Miner, previous.currentBlockHash);
        Main.blockchain.add(b);
        return b;
    }

    public static void blockInfo(ArrayList<block> blockchain){
        System.out.println("===========================");
        System.out.println("Blockchain:");
        for(int i = 0; i < blockchain.size(); i++){
            block b = blockchain.get(i);
            System.out.println("=======================");
            System.out.println("Block Number: " + b.blockNumber);
            System.out.println("Block Miner: " + b.miner);
            System.out.println("Timestamp: " + b.timestamp);
            System.out.println("Previous Block Hash: " + b.previousBlockHash);
            System.out.println("Current Block Hash: " + b.currentBlockHash);
            System.out.println("Block nonce: " + b.nonce);
            if(b.transactionsList != null){
                for(int j = 0; j < b.transactionsList.size(); j++){
                    transaction t = b.transactionsList.get(j);
                    transaction.transactionInfo(t);
                }
            }
        }
    }    
}
