package PackageAutor;

public class ProgramAutor {
    public static void main(String[] args) {

        Autor autor1 = new Autor("Gabriel García Márquez", "gabo@email.com", 'm');
        System.out.println(autor1);
        
        autor1.setEmail("gabriel.garcia@email.com");
        System.out.println("Después de cambiar el email:");
        System.out.println(autor1);
       
        System.out.println("Nombre: " + autor1.getName());
        System.out.println("Email: " + autor1.getEmail());
        System.out.println("Género: " + autor1.getGender());
    }
}
