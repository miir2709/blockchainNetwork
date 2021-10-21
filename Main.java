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
    public static peer Miner = new peer("Miner", 0);
    public static void main(String args[]) throws java.lang.Exception {
        Scanner sc = new Scanner(System.in);
        
        peer.infoPeer(Bob);
        peer.infoPeer(Alice);
        // peer.infoPeer(Tom);
        genesisBlock = block.createGenesisBlock();
        blockchain.add(genesisBlock);
        while (true) {
            System.out.println(
                    "\nPress: \n (1) mine a block \n (2) add a transaction to current block \n (3) print the blockchain");
            int choice = sc.nextInt();
            switch (choice) {
            case 1:
                block b = block.mineBlock(Miner);
                System.out.println(
                        "\n============================\nSuccessfully mined a block in " + b.timestamp + " miliseconds.");
                break;
            case 2:
                if (blockchain.size() == 1) {
                    System.out.println("Please mine a block to make a transaction.");
                    break;
                } else {
                    System.out.println("To make a transaction, kidly select Sender: \n1 for Alice\n2 for Bob");
                    int inputSender = sc.nextInt();
                    peer sender = null;
                    peer receiver = null;
                    switch (inputSender) {
                    case 1:
                        sender = Alice;
                        receiver = Bob;
                        break;
                    case 2:
                        sender = Bob;
                        receiver = Alice;
                        break;
                    }
                    transaction.makeTransaction(blockchain.get(blockchain.size() - 1), sender, receiver);
                    break;
                }
            case 3:
                System.out.println("\n Displaying the blockchain:\n ");
                block.blockInfo(blockchain);
                break;
            }
            System.out.println("\nDo you want to proceed? Press Y to contine. N to exit");
            String s = sc.next();
            if (s.equals("n") || s.equals("N"))
                break;
        }
        sc.close();
    }
}
