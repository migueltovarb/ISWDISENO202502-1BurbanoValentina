package AsistenciaGrupoEstudiantes;

import java.util.ArrayList;
import java.util.Scanner;

public class AsistenciaGrupoEstudiantes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int DIAS_SEMANA = 5;
        final int NUM_ESTUDIANTES = 4;
        ArrayList<String> estudiantes = new ArrayList<>();
        ArrayList<ArrayList<String>> asistencia = new ArrayList<>();
        boolean salir = false;

        try {
            System.out.println("Registrar a los estudiantes");
            for (int i = 0; i < NUM_ESTUDIANTES; i++) {
                System.out.print("Nombre del estudiante" + (i + 1) + ": ");
                String nombre = scanner.nextLine();
                estudiantes.add(nombre);
                ArrayList<String> diasEstudiante = new ArrayList<>();
                for (int j = 0; j < DIAS_SEMANA; j++) {
                    String valor;
                    do {
                        System.out.print("Asistencia de " + nombre + " en día " + (j + 1) + " (P/A): ");
                        valor = scanner.nextLine().toUpperCase();
                    } while (!valor.equals("P") && !valor.equals("A"));
                    diasEstudiante.add(valor);}
                asistencia.add(diasEstudiante);}

            while (!salir) {
                System.out.println("\nBienvenido al control de asistencia");
                System.out.println("1 Ver asistencia individual");
                System.out.println("2 Ver resumen general");
                System.out.println("3 Volver a registrar asistencia");
                System.out.println("4 Salir");
                System.out.println("Ingrese una opcion: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese el número de estudiante (1" + NUM_ESTUDIANTES + "): ");
                        int estudiantes_M = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (estudiantes_M >= 0 && estudiantes_M < NUM_ESTUDIANTES) {
                            System.out.println("Asistencia de " + estudiantes.get(estudiantes_M) + ": ");
                            for (int j = 0; j < DIAS_SEMANA; j++) {
                                System.out.println("Día " + (j + 1) + ": " + asistencia.get(estudiantes_M).get(j));}
                        } else {
                            System.out.println("Número inválido.");}
                        break;

		                    case 2:
		                        System.out.println("\nRESUMEN GENERAL");
		                        int[] totalAsistencias = new int[NUM_ESTUDIANTES];
		                        int[] ausenciasPorDia = new int[DIAS_SEMANA];
		                        for (int i = 0; i < NUM_ESTUDIANTES; i++) {
		                            int countP = 0;
		                            for (int j = 0; j < DIAS_SEMANA; j++) {
		                                if (asistencia.get(i).get(j).equals("P")) {
		                                    countP++;
		                                } else {
		                                    ausenciasPorDia[j]++;}
		                            }totalAsistencias[i] = countP;}
	
		                        for (int i = 0; i < NUM_ESTUDIANTES; i++) {
		                            System.out.println(estudiantes.get(i) + ": " + totalAsistencias[i] + " asistencias");}
		                        System.out.print("Estudiantes con asistencia completa: ");
		                        boolean encontrado = false;
		                        for (int i = 0; i < NUM_ESTUDIANTES; i++) {
		                            if (totalAsistencias[i] == DIAS_SEMANA) {
		                                System.out.print(estudiantes.get(i) + " ");
		                                encontrado = true;}
		                        }
		                        if (!encontrado) {
		                            System.out.print("Ninguno");}
		                        System.out.println();
		                        int max_Ausencias = 0;
		                        for (int j = 0; j < DIAS_SEMANA; j++) {
		                            if (ausenciasPorDia[j] > max_Ausencias) {
		                                max_Ausencias = ausenciasPorDia[j];}
		                        }
		                        System.out.print("Día(s) con más ausencias: ");
		                        for (int j = 0; j < DIAS_SEMANA; j++) {
		                            if (ausenciasPorDia[j] == max_Ausencias) {
		                                System.out.print((j + 1) + " ");}
		                        }
		                        System.out.println();
		                        break;

				                    case 3:
				                        System.out.println("Volviendo a registrar asistencia...");
				                        main(args);
				                        return;

					                    case 4:
					                        salir = true;
					                        break;

						                    default:
						                        System.out.println("Opción inválida.");
                						}
            					}

        			} catch (Exception e) {
        				System.err.println("Opcion invalida");
        				scanner.nextLine();
        		}
        scanner.close();
    	}

}
