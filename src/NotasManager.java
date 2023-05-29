
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NotasManager {
    private List<Notas> notas;
    private SimpleDateFormat dateFormat;
    private String notasFilePath;
    private String backupFolderPath;

    public NotasManager(String notasFilePath, String backupFolderPath) {
        notas = new ArrayList<>();
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.notasFilePath = notasFilePath;
        this.backupFolderPath = backupFolderPath;
        cargarNotasDesdeArchivo();
    }

    public void crearNuevaNota(String titulo, String contenido) {
        Notas nota = new Notas(titulo, contenido);
        notas.add(nota);
        guardarNotasEnArchivo();
    }

    public Notas consultarNota(String titulo) {
        return notas.stream()
                .filter(n -> n.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }
    
    public void eliminarTodasLasNotas() {
        notas.clear();
        guardarNotasEnArchivo();
    }

    public void actualizarNota(String titulo, String nuevoTitulo, String nuevoContenido) {
        Notas nota = consultarNota(titulo);
        if (nota != null) {
            nota.setTitulo(nuevoTitulo);
            nota.setContenido(nuevoContenido);
            guardarNotasEnArchivo();
        }
    }

    public void eliminarNota(String titulo) {
        Notas nota = consultarNota(titulo);
        if (nota != null) {
            notas.remove(nota);
            guardarNotasEnArchivo();
        }
    }

    public List<Notas> listarTodasLasNotas() {
        return notas;
    }

    public List<Notas> buscarNotas(String criterio) {
        return notas.stream()
                .filter(n -> n.getTitulo().toLowerCase().contains(criterio.toLowerCase())
                        || n.getContenido().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void enlazarNotas(List<Notas> notasEnlazadas, String archivoEnlace) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivoEnlace, true))) {
            for (Notas nota : notasEnlazadas) {
                writer.println(nota.getTitulo());
                writer.println(nota.getContenido());
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importarNotas(String archivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String titulo = null;
            StringBuilder contenido = new StringBuilder();

            while ((linea = reader.readLine()) != null) {
                if (titulo == null) {
                    titulo = linea;
                } else if (linea.isEmpty()) {
                    crearNuevaNota(titulo, contenido.toString());
                    titulo = null;
                    contenido.setLength(0);
                } else {
                    contenido.append(linea).append("\n");
                }
            }

            if (titulo != null && contenido.length() > 0) {
                crearNuevaNota(titulo, contenido.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void realizarCopiaDeSeguridad() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String backupFilePath = backupFolderPath + "/backup_" + timeStamp + ".txt";
            Files.copy(Paths.get(notasFilePath), Paths.get(backupFilePath));
            System.out.println("Copia de seguridad creada correctamente en: " + backupFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            
        }
    }

    private void cargarNotasDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(notasFilePath))) {
            String linea;
            String titulo = null;
            StringBuilder contenido = new StringBuilder();

            while ((linea = reader.readLine()) != null) {
                if (titulo == null) {
                    titulo = linea;
                } else if (linea.isEmpty()) {
                    crearNuevaNota(titulo, contenido.toString());
                    titulo = null;
                    contenido.setLength(0);
                } else {
                    contenido.append(linea).append("\n");
                }
            }

            if (titulo != null && contenido.length() > 0) {
                crearNuevaNota(titulo, contenido.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarNotasEnArchivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(notasFilePath))) {
            for (Notas nota : notas) {
                writer.println(nota.getTitulo());
                writer.println(nota.getContenido());
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

}
