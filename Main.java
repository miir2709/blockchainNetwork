import java.util.ArrayList;

public class Main {

    public static block genesisBlock;
    public static ArrayList <block> blockchain = new ArrayList<block>();

    // private static void createUsers(){
    //     peer Bob = new peer("Bob", 110);
    //     peer.infoPeer(Bob);
    //     peer Alice = new peer("Alice", 0);
    //     peer.infoPeer(Alice);
    // }

    public static block createGenesisBlock(){
        peer satoshi = new peer("Satioshi Nakamoto", 0);
        block genesisBlock = new block(0,satoshi, "");
        genesisBlock.timeStamp = 0;
        genesisBlock.currentBlockHash = "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f";
        genesisBlock.previousBlockHash = null;
        genesisBlock.nonce = 0;
        genesisBlock.transactionsList = null;
        System.out.println("Genesis Block created");
        return genesisBlock;
    }

    private static void mineBlock(peer Miner){
        block previous = blockchain.get(blockchain.size() - 1);
        block b = new block(previous.blockNumber+1, Miner, previous.currentBlockHash);
        blockchain.add(b);
    }

    public static void main(String args[]) throws java.lang.Exception {

        peer Miner = new peer("Miner", 0);
        // createUsers();
        
        genesisBlock = createGenesisBlock();
        blockchain.add(genesisBlock);

        mineBlock(Miner);
        mineBlock(Miner);
        mineBlock(Miner);

        block.blockInfo(blockchain);
    }
}
 