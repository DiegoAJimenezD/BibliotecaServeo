import java.io.*;
import java.net.*;

public class BibliotecaCliente {

    public static void main(String[] args) {
        try (Socket socket = new Socket("serveo.net", 1600);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            // Proceso de login
            System.out.println("Ingrese su código de estudiante:");
            String codigo = teclado.readLine();
            System.out.println("Ingrese su clave:");
            String clave = teclado.readLine();

            // Enviar la solicitud de login al servidor
            salida.println("LOGIN " + codigo + " " + clave);
            String respuesta = entrada.readLine();

            // Verificar si el login fue exitoso
            if (respuesta.equals("LOGIN_OK")) {
                System.out.println("¡Login exitoso!");

                boolean continuar = true;
                while (continuar) {
                    // Mostrar menú de opciones
                    System.out.println("\nMenú de opciones:");
                    System.out.println("1. Buscar libro");
                    System.out.println("2. Reservar libro");
                    System.out.println("3. Cambiar clave");
                    System.out.println("4. Salir");
                    System.out.print("Elija una opción: ");
                    String opcion = teclado.readLine();

                    switch (opcion) {
                        case "1":
                            // Búsqueda de libros
                            buscarLibro(salida, entrada, teclado);
                            break;
                        case "2":
                            // Reservar un libro
                            reservarLibro(salida, entrada, teclado);
                            break;
                        case "3":
                            // Cambiar la clave del usuario
                            cambiarClave(salida, entrada, teclado, codigo);
                            break;
                        case "4":
                            continuar = false;
                            System.out.println("Saliendo...");
                            break;
                        default:
                            System.out.println("Opción no válida. Inténtelo de nuevo.");
                    }
                }
            } else {
                System.out.println("Login fallido. Verifique sus credenciales.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void buscarLibro(PrintWriter salida, BufferedReader entrada, BufferedReader teclado) throws IOException {
        System.out.println("Buscar por: (NOMBRE, AUTOR, TEMA)");
        String criterio = teclado.readLine();
        System.out.println("Ingrese el valor de búsqueda:");
        String valor = teclado.readLine();

        // Enviar solicitud al servidor
        salida.println("BUSCAR_LIBRO " + criterio + " " + valor);

        // Recibir respuesta hasta "END_OF_MESSAGE"
        String respuesta;
        respuesta=entrada.readLine();
        System.out.println(respuesta);
    }

    private static void reservarLibro(PrintWriter salida, BufferedReader entrada, BufferedReader teclado) throws IOException {
        System.out.println("Ingrese el nombre del libro a reservar:");
        String nombreLibro = teclado.readLine();

        // Enviar solicitud al servidor
        salida.println("RESERVAR_LIBRO " + nombreLibro);

        // Recibir respuesta del servidor
        String respuesta = entrada.readLine();
        System.out.println(respuesta);
    }

    private static void cambiarClave(PrintWriter salida, BufferedReader entrada, BufferedReader teclado, String codigo) throws IOException {
        System.out.println("Ingrese su nueva clave:");
        String nuevaClave = teclado.readLine();

        // Enviar solicitud al servidor
        salida.println("CAMBIAR_CLAVE " + codigo + " " + nuevaClave);

        // Recibir respuesta del servidor
        String respuesta = entrada.readLine();
        System.out.println(respuesta);
    }
}