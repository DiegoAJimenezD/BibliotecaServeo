import java.io.*;
import java.net.Socket;
import java.util.Map;
public  class ManejadorCliente implements Runnable {
    private Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = reader.readLine()) != null) {
                if (request.startsWith("LOGIN")) {
                    manejarLogin(request, writer);
                } else if (request.startsWith("BUSCAR_LIBRO")) {
                    manejarBusquedaLibro(request, writer);
                } else if (request.startsWith("RESERVAR_LIBRO")) {
                    manejarReservaLibro(request, writer);
                } else if (request.startsWith("CAMBIAR_CLAVE")) {
                    manejarCambioClave(request, writer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void manejarLogin(String request, PrintWriter writer) {
        String[] partes = request.split(" ");
        String codigo = partes[1];
        String clave = partes[2];

        if (BibliotecaServer.usuarios.containsKey(codigo) && BibliotecaServer.usuarios.get(codigo).equals(clave)) {
            writer.println("LOGIN_OK");
        } else {
            writer.println("LOGIN_FALLIDO");
        }
        writer.flush();  // Asegura que el mensaje se envíe de inmediato
    }

    private void manejarBusquedaLibro(String request, PrintWriter writer) {
        String[] partes = request.split(" ", 2);
        String criterio = partes[1];
        String[] subCriterios = criterio.split(" ");
        String tipoBusqueda = subCriterios[0];
        String valorBusqueda = subCriterios[1];

        StringBuilder resultado = new StringBuilder();

        for (Libro libro : BibliotecaServer.libros.values()) {
            if ((tipoBusqueda.equalsIgnoreCase("NOMBRE") && libro.getNombre().equalsIgnoreCase(valorBusqueda))
                    || (tipoBusqueda.equalsIgnoreCase("AUTOR") && libro.getAutor().equalsIgnoreCase(valorBusqueda))
                    || (tipoBusqueda.equalsIgnoreCase("TEMA") && libro.getTema().equalsIgnoreCase(valorBusqueda))) {
                resultado.append(libro.toString()).append("\n");
            }
        }

        if (resultado.length() == 0) {
            writer.println("No se encontraron libros que coincidan con la búsqueda.");
        } else {
            writer.println(resultado.toString());
        }

        writer.flush();
    }

    private void manejarReservaLibro(String request, PrintWriter writer) {
        String[] partes = request.split(" ", 2);
        String nombreLibro = partes[1];

        Libro libro = BibliotecaServer.libros.get(nombreLibro);

        if (libro != null && libro.getEstado().equalsIgnoreCase("disponible")) {
            libro.setEstado("reservado");
            actualizarArchivoLibros();
            writer.println("Libro reservado exitosamente.");
        } else {
            writer.println("El libro no está disponible o ya está reservado.");
        }
        writer.flush();
    }

    private void manejarCambioClave(String request, PrintWriter writer) {
        String[] partes = request.split(" ", 3);
        String codigo = partes[1];
        String nuevaClave = partes[2];

        if (BibliotecaServer.usuarios.containsKey(codigo)) {
            BibliotecaServer.usuarios.put(codigo, nuevaClave);
            actualizarArchivoUsuarios();
            writer.println("Clave cambiada exitosamente.");
        } else {
            writer.println("El código de usuario no existe.");
        }
        writer.flush();
    }

    private void actualizarArchivoLibros() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("src/libros.txt"))) {
            for (Libro libro : BibliotecaServer.libros.values()) {
                pw.println(libro.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarArchivoUsuarios() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("src/usuarios.txt"))) {
            for (Map.Entry<String, String> usuario : BibliotecaServer.usuarios.entrySet()) {
                pw.println(usuario.getKey() + ";" + usuario.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}