import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        NotasManager notasManager = new NotasManager("ruta_del_archivo_notas.txt", "ruta_de_la_carpeta_backup");

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("------App de Notas------");
                System.out.println("- ¿Qué quieres hacer?");
                System.out.println("1. Crear una nueva nota");
                System.out.println("2. Consultar una nota existente");
                System.out.println("3. Actualizar una nota");
                System.out.println("4. Eliminar una nota");
                System.out.println("5. Listar todas las notas");
                System.out.println("6. Búsqueda avanzada");
                System.out.println("7. Enlazar notas");
                System.out.println("8. Importar notas");
                System.out.println("9. Copia de seguridad automática");
                System.out.println("10. Eliminar todas las notas");
                System.out.println("0. Salir");

                int opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.println("Crear una nueva nota");
                        String tituloNota;
                        String contenidoNota;

                        do {
                            System.out.println("Agrega un título (no puede estar vacío):");
                            tituloNota = sc.nextLine();
                        } while (tituloNota.isEmpty());

                        do {
                            System.out.println("Agrega contenido (no puede estar vacío):");
                            contenidoNota = sc.nextLine();
                        } while (contenidoNota.isEmpty());

                        try {
                            notasManager.crearNuevaNota(tituloNota, contenidoNota);
                            System.out.println("Nota creada exitosamente.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("No se puede crear la nota. " + e.getMessage());
                        }

                        break;
                    case 2:
                        System.out.println("Ingrese el título de la nota a consultar:");
                        String tituloConsultar = sc.nextLine();
                        Notas notaConsultada = notasManager.consultarNota(tituloConsultar);
                        if (notaConsultada != null) {
                            System.out.println("Nota encontrada:");
                            System.out.println("Título: " + notaConsultada.getTitulo());
                            System.out.println("Contenido: " + notaConsultada.getContenido());
                        } else {
                            System.out.println("No se encontró la nota.");
                        }
                        break;
                    case 3:
                        System.out.println("Ingrese el título de la nota a actualizar:");
                        String tituloActualizar = sc.nextLine();
                        System.out.println("Ingrese el nuevo título:");
                        String nuevoTitulo = sc.nextLine();
                        System.out.println("Ingrese el nuevo contenido:");
                        String nuevoContenido = sc.nextLine();
                        notasManager.actualizarNota(tituloActualizar, nuevoTitulo, nuevoContenido);
                        break;
                    case 4:
                        System.out.println("Ingrese el título de la nota a eliminar:");
                        String tituloEliminar = sc.nextLine();
                        notasManager.eliminarNota(tituloEliminar);
                        break;
                    case 5:
                        System.out.println("Listado de todas las notas:");
                        List<Notas> todasLasNotas = notasManager.listarTodasLasNotas();
                        for (Notas nota : todasLasNotas) {
                            System.out.println("Título: " + nota.getTitulo());
                            System.out.println("Contenido: " + nota.getContenido());
                            System.out.println("Fecha de creación: " + nota.getFechaCreacion());
                            System.out.println();
                        }
                        break;
                    case 6:
                        System.out.println("Búsqueda avanzada:");
                        System.out.println("Ingrese el criterio de búsqueda:");
                        String criterioBusqueda = sc.nextLine();
                        List<Notas> notasEncontradas = notasManager.buscarNotas(criterioBusqueda);
                        if (notasEncontradas.isEmpty()) {
                            System.out.println("No se encontraron notas que coincidan con el criterio de búsqueda.");
                        } else {
                            System.out.println("Notas encontradas:");
                            for (Notas nota : notasEncontradas) {
                                System.out.println("Título: " + nota.getTitulo());
                                System.out.println("Contenido: " + nota.getContenido());
                                System.out.println("Fecha de creación: " + nota.getFechaCreacion());
                                System.out.println();
                            }
                        }
                        break;
                    case 7:
                        System.out.println("Enlazar notas:");
                        System.out.println("Ingrese la ruta del archivo de enlace:");
                        String archivoEnlace = sc.nextLine();
                        System.out.println("Ingrese los títulos de las notas a enlazar (separados por coma):");
                        String titulosEnlazar = sc.nextLine();
                        String[] titulos = titulosEnlazar.split(",");
                        List<Notas> notasEnlazadas = new ArrayList<>();
                        for (String titulo : titulos) {
                            Notas notaEnlazada = notasManager.consultarNota(titulo.trim());
                            if (notaEnlazada != null) {
                                notasEnlazadas.add(notaEnlazada);
                            } else {
                                System.out.println("No se encontró la nota con título: " + titulo);
                            }
                        }
                        notasManager.enlazarNotas(notasEnlazadas, archivoEnlace);
                        System.out.println("Notas enlazadas exitosamente.");
                        break;
                    case 8:
                        System.out.println("Importar notas:");
                        System.out.println("Ingrese la ruta del archivo a importar:");
                        String archivoImportar = sc.nextLine();
                        notasManager.importarNotas(archivoImportar);
                        System.out.println("Notas importadas exitosamente.");
                        break;
                    case 9:
                        System.out.println("Realizando copia de seguridad automática...");
                        notasManager.realizarCopiaDeSeguridad();
                        break;
                    case 10:
                        System.out.println("Eliminar todas las notas:");
                        notasManager.eliminarTodasLasNotas();
                        System.out.println("Todas las notas han sido eliminadas.");
                        break;
                    case 0:
                        System.out.println("Saliendo de la aplicación...");
                        System.exit(0);
                    default:
                        System.out.println("Opción no válida. Por favor, ingrese un número válido.");
                }
            }
        }
    }
}
