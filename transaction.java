import java.security.*;

public class transaction {

    public static int transactionLimit = 3;
    long transaction_id;
    peer sender;
    peer receiver;
    block transactionblock;
    long amount;
    boolean validate = true;

    transaction(long id, block block, peer sender, peer receiver) {
        this.transaction_id = id;
        this.transactionblock = block;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = generateRandomAmount();

        beforeTransactionInfo(sender, receiver, amount);
        this.validate = initiateTransaction(sender, receiver, amount);
    }

    private static long generateRandomAmount() {
        long min = 0;
        long max = 15;
        long a = (long) Math.round(Math.random() * (max - min + 1) + min);

        return a;
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    private static byte[] signTransaction(long amount, peer sender) {
        byte data[] = intToByteArray((int)amount);
        try{
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(sender.pk);
            dsa.update(data);
            byte[] realSig = dsa.sign();
            // for(byte i : realSig) System.out.print(i + "");
            return realSig;
        }catch(NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e){
            e.getStackTrace();
        }
        return new byte[0];
    }

    private static void verifyTransaction(long amount, peer sender, peer receiver, byte[] realSig){
        byte data[] = intToByteArray((int)amount);
        try{
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");  
            sig.initVerify(sender.pubk);
            sig.update(data);
            
            boolean verifies = sig.verify(realSig);  
            System.out.println("Is the transaction verified among sender and receiver? " + verifies);
            if(verifies){
                // System.out.println("The transaction amount is: " + amount);
                sender.UTXO = sender.UTXO - amount;
                receiver.UTXO = receiver.UTXO + amount;
            }
        }catch(NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            e.getStackTrace();
        }
    }

    public static boolean initiateTransaction(peer sender, peer receiver, long amount){
        System.out.println("During the transcation: ");
        if (amount > sender.UTXO) {
            // System.out.println("The random amount generated is: " + amount);
            System.out.println("Since the random amount generated is greater than the UTXO of sender, \nDouble spending detected. Aborting!!!");
            return false;
        }else{
            byte realSig [] = signTransaction(amount, sender);
            verifyTransaction(amount, sender, receiver, realSig);
            return true;
        }
    }

    public static void makeTransaction(block b, peer sender, peer receiver){
        long temp_transaction_id = 0;
        block temp_block;
        block newBlock = null;
        if(b.transactionsList == null || b.transactionsList.size() == 0) 
            temp_transaction_id = 1;
        else
            temp_transaction_id = b.transactionsList.get(b.transactionsList.size() - 1).transaction_id + 1;

        if(b.transactionsList.size() == 3){
            newBlock = block.mineBlock(Main.Miner);
            System.out.println("===================");
            System.out.println("Transaction limit reached! Mining New Block.\nThis transaction will get alloted to new block");
            System.out.println("Difficulty of the blockmined: " + newBlock.difficulty);
            System.out.println("Time taken to mine new block: " + newBlock.timestamp); 
            temp_block = newBlock;
            temp_transaction_id = 1;
        }else 
            temp_block = b;

        transaction t = new transaction(temp_transaction_id, temp_block, sender, receiver);
        if(t.validate == false) return;
        
        if(b.transactionsList == null || b.transactionsList.size() < 3){
            b.transactionsList.add(t);
        }else{
            newBlock.transactionsList.add(t);
        }
        transactionInfo(t);
    }

    private static void beforeTransactionInfo(peer sender, peer receiver, long amount){
        System.out.println("===================\nBefore the transaction: ");
        System.out.println("Sender: " + sender.name + " | UTXO: " + sender.UTXO);
        System.out.println("Receiver: "+ receiver.name + " | UTXO " + receiver.UTXO);
        System.out.println("\nThe random amount generated for the transaction: " + amount);
    }

    public static void transactionInfo(transaction t){
        System.out.println("================");
        System.out.println("transaction id: " + t.transaction_id);
        System.out.println("block number: " + t.transactionblock.blockNumber);
        System.out.println("validate: " + t.validate);
        System.out.println("amount: " + t.amount);
        System.out.println("sender: " + t.sender.name + " | sender utxo after txn: " + t.sender.UTXO) ;
        System.out.println("receiver: " + t.receiver.name + " | receiver utxo after txn: " + t.receiver.UTXO);
        System.out.println("transaction successfully complete");
        System.out.println("================");
    }
}