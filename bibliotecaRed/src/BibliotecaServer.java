import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

// ssh -R 1500:localhost:1400 serveo.net
public class BibliotecaServer {

    static Map<String, String> usuarios = new HashMap<>();
    static Map<String, Libro> libros = new HashMap<>();

    public static void main(String[] args) {
        cargarUsuarios();
        cargarLibros();

        try (ServerSocket serverSocket = new ServerSocket(1400)) {
            System.out.println("Servidor iniciado...");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ManejadorCliente(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cargarUsuarios() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/usuarios.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                usuarios.put(datos[0], datos[1]); // Código y clave
            }
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
    }

    private static void cargarLibros() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/libros.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                libros.put(datos[0], new Libro(datos[0], datos[1], datos[2], datos[3]));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar libros: " + e.getMessage());
        }
    }


}