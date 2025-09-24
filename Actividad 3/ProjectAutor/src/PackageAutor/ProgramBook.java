package PackageAutor;

public class ProgramBook {
    public static void main(String[] args) {
        Autor autor1 = new Autor("Isabel Allende", "isabel@email.com", 'f');
        Book book1 = new Book("La Casa de los Espíritus", autor1, 59.99);
        Book book2 = new Book("Paula", autor1, 39.99, 5);
        System.out.println(book1);
        System.out.println(book2);

        book2.setPrice(45.50);
        book2.setQty(10);
        System.out.println("Después de actualización:");
        System.out.println(book2);
        System.out.println("Autor del libro 1: " + book1.getAuthor().getName());
    }
}
