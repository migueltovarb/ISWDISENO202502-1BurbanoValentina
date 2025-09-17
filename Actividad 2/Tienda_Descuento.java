package ExamplesJava;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Tienda_Descuento {

    private static final double DESCUENTO_ROPA = 0.10;
    private static final double DESCUENTO_TECNOLOGIA = 0.05;
    private static final double DESCUENTO_ALIMENTOS = 0.02;
    private static final double DESCUENTO_ADICIONAL = 0.05;
    private static final double UMBRAL_DESCUENTO = 500000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> productos = new ArrayList<>();
        ArrayList<Double> preciosLista = new ArrayList<>();
        int cantidadProductos = 0;

        try {
            while (cantidadProductos < 1) {
                System.out.print("Ingrese el número de productos a comprar : ");
                cantidadProductos = scanner.nextInt();
                if (cantidadProductos < 1) {
                    System.out.println("Debe ingresar al menos 1 producto.");
                }
            }

            double totalSinDescuento = 0;
            double totalConDescuento = 0;

            for (int i = 0; i < cantidadProductos; i++) {
                System.out.println("\nProducto " + (i + 1) + ":");

                scanner.nextLine();
                System.out.print("Ingrese el nombre del producto: ");
                String nombre = scanner.nextLine();
                productos.add(nombre);

                System.out.print("Ingrese el tipo (1: ropa, 2: tecnología, 3: alimentos): ");
                int tipo = scanner.nextInt();

                System.out.print("Ingrese el precio del producto: ");
                double precio = scanner.nextDouble();
                preciosLista.add(precio);

                double precioConDescuento = precio;

                switch (tipo) {
                    case 1:
                        precioConDescuento = precio - (precio * DESCUENTO_ROPA);
                        break;
                    case 2:
                        precioConDescuento = precio - (precio * DESCUENTO_TECNOLOGIA);
                        break;
                    case 3:
                        precioConDescuento = precio - (precio * DESCUENTO_ALIMENTOS);
                        break;
                    default:
                        System.out.println("No se aplica descuento.");
                }

                totalSinDescuento += precio;
                totalConDescuento += precioConDescuento;

                System.out.println("→ Precio original: " + precio);
                System.out.println("→ Precio con descuento: " + precioConDescuento);
                System.out.println("→ Ahorro en este producto: " + (precio - precioConDescuento));
            }

            if (totalSinDescuento > UMBRAL_DESCUENTO) {
                double descuentoExtra = totalConDescuento * DESCUENTO_ADICIONAL;
                totalConDescuento -= descuentoExtra;
                System.out.println("\nSe aplicó un descuento adicional del 5% por superar " + UMBRAL_DESCUENTO);
            }
            double ahorro = totalSinDescuento - totalConDescuento;

            System.out.println("\n RESUMEN DE COMPRA ");
            for (int i = 0; i < productos.size(); i++) {
                System.out.println("- " + productos.get(i) + ":" + preciosLista.get(i));
            }
            System.out.println("\nTotal sin descuento: " + totalSinDescuento);
            System.out.println("Total con descuento: " + totalConDescuento);
            System.out.println("Ahorro total: " + ahorro);

        } catch (InputMismatchException e) {
            System.out.println("Ingresó un valor inválido. ");
            scanner.close();
            System.out.println("\nfin.");
        }
    }
}
