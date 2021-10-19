import java.util.ArrayList;
import java.util.*;
public class Main {

    public static block genesisBlock;
    public static ArrayList<block> blockchain = new ArrayList<block>();

    public static peer Bob = new peer("Bob", 100);
    // peer.infoPeer(Bob);
    public static peer Alice = new peer("Alice", 30);
    // peer.infoPeer(Alice);
    public static peer Tom = new peer("Tom", 10);
    // peer.infoPeer(Tom);

    public static void main(String args[]) throws java.lang.Exception {

        Scanner sc = new Scanner(System.in);
        peer Miner = new peer("Miner", 0);
        peer.infoPeer(Bob);
        peer.infoPeer(Alice);
        // peer.infoPeer(Tom);
        genesisBlock = block.createGenesisBlock();
        blockchain.add(genesisBlock);
        while(true){
            System.out.println("\nPress: \n (1) mine a block \n (2) add a transaction to current block \n (3) print the blockchain");
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                    block.mineBlock(Miner);
                    System.out.println("\n============================\nSuccessfully mined a block.");
                    break;
                case 2: 
                    if(blockchain.size() == 1) {
                        System.out.println("Please mine a block to make a transaction.");
                        break;
                    }
                    else{
                        transaction.makeTransaction(blockchain.get(blockchain.size() - 1), Bob, Alice);
                        break;
                    }
                case 3: 
                    System.out.println("\n Displaying the blockchain:\n ");
                    block.blockInfo(blockchain);
                    break;
            }
            System.out.println("\nDo you want to proceed? Press Y to contine. N to exit");
            String s = sc.next();
            if(s.equals("n") || s.equals("N")) break; 
        }
        sc.close();
    }
}
