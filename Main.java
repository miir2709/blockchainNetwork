import java.util.ArrayList;

public class Main {

    public static block genesisBlock;
    public static ArrayList<block> blockchain = new ArrayList<block>();

    public static peer Bob = new peer("Bob", 100);
    // peer.infoPeer(Bob);
    public static peer Alice = new peer("Alice", 30);
    // peer.infoPeer(Alice);
    public static peer Tom = new peer("Tom", 10);
    // peer.infoPeer(Tom);

    public static block createGenesisBlock() {
        peer satoshi = new peer("Satioshi Nakamoto", 0);
        block genesisBlock = new block(0, satoshi, "");
        genesisBlock.currentBlockHash = "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f";
        genesisBlock.previousBlockHash = null;
        genesisBlock.nonce = 0;
        genesisBlock.transactionsList = null;
        System.out.println("Genesis Block created");
        return genesisBlock;
    }

    public static void mineBlock(peer Miner){
        block previous = blockchain.get(blockchain.size() - 1);
        block b = new block(previous.blockNumber+1, Miner, previous.currentBlockHash);
        makeTransaction(b);
        blockchain.add(b);
    }
    
    private static void makeTransaction(block b){
        transaction t = new transaction(1, b, Bob, Alice);
        if(t.validate == false) return;
        ArrayList<transaction> arrList = new ArrayList<transaction>();
        arrList.add(t);
        b.transactionsList = arrList;
    }

    public static void main(String args[]) throws java.lang.Exception {

        peer Miner = new peer("Miner", 0);
        peer.infoPeer(Bob);
        peer.infoPeer(Alice);
        // peer.infoPeer(Tom);
        genesisBlock = createGenesisBlock();
        blockchain.add(genesisBlock);

        mineBlock(Miner);
        // block.blockInfo(blockchain);

    }
}
