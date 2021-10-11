import java.security.*;

import javax.sound.midi.Receiver;

public class transaction {
    long transaction_id;
    peer sender;
    peer receiver;
    block block;
    long amount;
    boolean validate = true;

    transaction(long id, block block, peer sender, peer receiver) {
        this.transaction_id = id;
        this.block = block;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = generateRandomAmount();

        if (amount > sender.UTXO) {
            System.out.println("Double spending detected. Aborting!!!");
            validate = false;
        }else{
            byte realSig [] = signTransaction(amount, sender);
            verifyTransaction(amount, sender, receiver, realSig);
        }
    }

    private static long generateRandomAmount() {
        long min = 0;
        long max = 150;
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
            System.out.println("The transaction amount is: " + amount);
            sender.UTXO = sender.UTXO - amount;
            receiver.UTXO = receiver.UTXO + amount;
        }
        }catch(NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            e.getStackTrace();
        }
    }

    public static void transactionInfo(transaction t){
        System.out.println("================");
        System.out.println("transaction id: " + t.transaction_id);
        System.out.println("block number: " + t.block.blockNumber);
        System.out.println("validate: " + t.validate);
        System.out.println("amount: " + t.amount);
        System.out.println("sender: " + t.sender.name) ;
        System.out.println("receiver: " + t.receiver.name);
        System.out.println("sender utxo after txn: " + t.sender.UTXO) ;
        System.out.println("receiver utxo after txn: " + t.receiver.UTXO);
        
        

    }
}