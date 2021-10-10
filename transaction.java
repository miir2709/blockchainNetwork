import java.security.*;

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
        }
        byte realSig [] = signTransaction(amount, sender);
        verifyTransaction(amount, sender, realSig);
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

    private static void verifyTransaction(long amount, peer sender, byte[] realSig){
        byte data[] = intToByteArray((int)amount);
        try{
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");  
        sig.initVerify(sender.pubk);
        sig.update(data);
        
        boolean verifies = sig.verify(realSig);  
        System.out.println(verifies);
        }catch(NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            e.getStackTrace();
        }
    }
}