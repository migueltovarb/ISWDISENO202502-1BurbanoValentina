package PackageInvoiceItem;

public class ProgramInvoiceItem {
    public static void main(String[] args) {
        
        InvoiceItem item1 = new InvoiceItem("P001", "Teclado Mecánico", 2, 150.75);
        
        System.out.println(item1);
        System.out.println("ID: " + item1.getId());
        System.out.println("Descripción: " + item1.getDesc());
        System.out.println("Cantidad: " + item1.getQty());
        System.out.println("Precio unitario: " + item1.getUnitPrice());
        System.out.println("Total: " + item1.getTotal());
        
        item1.setQty(3);
        item1.setUnitPrice(140.50);
        
        System.out.println("\nDespués de actualizar:");
        System.out.println(item1);
        System.out.println("Nuevo total: " + item1.getTotal());
    }
}
