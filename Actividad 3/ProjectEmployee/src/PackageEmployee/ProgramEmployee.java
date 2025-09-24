package PackageEmployee;

public class ProgramEmployee {

    public static void main(String[] args) {
        
        Employee emp1 = new Employee(1, "Juan", "Pérez", 2000);
        
        System.out.println(emp1);
        System.out.println("Nombre completo: " + emp1.getName());
        System.out.println("Salario mensual: " + emp1.getSalary());
        System.out.println("Salario anual: " + emp1.getAnnualSalary());

        int nuevoSalario = emp1.raiseSalary(20); 
        System.out.println("Salario tras aumento: " + nuevoSalario);
        System.out.println(emp1);
    }
}

