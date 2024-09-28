public class Libro {
    private String nombre;
    private String autor;
    private String tema;
    private String estado;

    public Libro(String nombre, String autor, String tema, String estado) {
        this.nombre = nombre;
        this.autor = autor;
        this.tema = tema;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAutor() {
        return autor;
    }

    public String getTema() {
        return tema;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre + " - " + autor + " - " + tema + " (" + estado + ")";
    }

    public String toFileString() {
        return nombre + ";" + autor + ";" + tema + ";" + estado;
    }
}
