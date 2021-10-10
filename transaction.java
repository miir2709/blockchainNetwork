public class transaction {
    long transaction_id;
    peer sender;
    peer receiver;
    block block;
    long amount;
    boolean validate = true;
    
    transaction(long id, block block, peer sender, peer receiver){
        this.transaction_id = id;
        this.block = block;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = generateRandomAmount();

        if(amount > sender.UTXO) {
            System.out.println("Double spending detected. Aborting!!!");
            validate = false;
        }


    }

    private static long generateRandomAmount(){
        long min = 0;
        long max = 150;
        long a = (long) Math.round(Math.random()*(max-min+1)+min);  
        // String amount = String.valueOf(a); 
        return a;
    }
}

