package PackageAccount;

public class ProgramAccount {
    public static void main(String[] args) {
        
        Account acc1 = new Account("A001", "Juan Pérez", 1000);
        Account acc2 = new Account("A002", "Ana Gómez", 500);
        System.out.println(acc1);
        System.out.println(acc2);
        
        acc1.credit(200);
        System.out.println("\nDespués de acreditar 200 a acc1: " + acc1);
        acc2.debit(100);
        System.out.println("Después de debitar 100 a acc2: " + acc2);
        acc2.debit(1000);
        
        acc1.transferTo(acc2, 500);
        System.out.println("\nDespués de transferir 500 de acc1 a acc2:");
        System.out.println(acc1);
        System.out.println(acc2);
    }
}
