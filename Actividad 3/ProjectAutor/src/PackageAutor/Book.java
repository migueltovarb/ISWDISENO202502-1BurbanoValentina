package PackageAutor;


public class Book {
    private String name;
    private Autor author;  
    private double price;
    private int qty = 0;

    public Book(String name, Autor author, double price) {
        this.name = name;
        this.author = author;
        this.price = price;
    }

    public Book(String name, Autor author, double price, int qty) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public Autor getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Book[name=" + name + "," + author.toString() + ",price=" + price + ",qty=" + qty + "]";
    }
}
