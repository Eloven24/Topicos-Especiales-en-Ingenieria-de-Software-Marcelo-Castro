import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Tarea001 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Por favor, ingresa tu nombre: ");
        String nombre = scanner.nextLine();

        try {
            FileWriter writer = new FileWriter("nombres.txt", true); // 'true' para agregar sin sobrescribir
            writer.write(nombre + "\n");
            writer.close();
            System.out.println("Nombre guardado exitosamente en nombres.txt");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar el nombre.");
            e.printStackTrace();
        }

        scanner.close();
    }
}
