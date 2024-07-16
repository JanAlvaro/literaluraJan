package com.literalurajan.literalurajan;

import com.literalurajan.literalurajan.helpers.ConvertirDatos;
import com.literalurajan.literalurajan.models.Autor;
import com.literalurajan.literalurajan.models.DatosLibro;
import com.literalurajan.literalurajan.models.DatosLibros;
import com.literalurajan.literalurajan.models.Libro;
import com.literalurajan.literalurajan.repository.AutorRepository;
import com.literalurajan.literalurajan.repository.LibroRepository;
import com.literalurajan.literalurajan.services.ConsultaApi;

import java.util.*;

public class Principal { private Scanner keyBoard = new Scanner(System.in);
    private final String BASE_URL = "https://gutendex.com/books";
    private List<Libro> bookSearched = new ArrayList<>();
    private List<Autor> authorsSearched = new ArrayList<>();


    //Inyeccion de dependencias
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void printMenu() {
        String menu = """
                ******************************
                \tBienvenido a LiterAlura
       
                1. Buscar libro por titulo
                2. Listar libros registrados
                3. Listar autores registrados
                4. Listar autores vivos año
                5. Listar libros por idioma
                6. Salir
                
                ******************************
                Elige una opción:
                """;
        System.out.println(menu);
    }

    public void printMenuIdioma(){
        String Idiomas = """
                ---------------------
                Idiomas disponibles:
                
                  -en  (Inglés)
                  -es  (Español)
                  -fr  (Francés)
                  -de  (Alemán)
                  -it  (Italiano)
                  -pt  (Portugués)
                  -ja  (Japonés)
                --------------------
                """;
        System.out.println(Idiomas);
    }


    public void showMenu() {
        int option = 0;
        do {
            printMenu();

            option = getNumeroUsuario();

            switch (option) {
                case 1:
                    buscarLibroPorNombre();
                    break;
                case 2:
                    getAllBooks();
                    break;
                case 3:
                    getAllAuthors();
                    break;
                case 4:
                    getAuthorsAliveInYear();
                    break;
                case 5:
                    getBooksByLanguage();
                    break;
                case 6:
                    System.out.println("Adios!");
                    break;

                default:
                    System.out.println("Invalid option");
                    break;
            }
        } while (option != 6);

    }


    public int getNumeroUsuario() {
        int number = 0;
        while (true) {
            try {
                number = keyBoard.nextInt();
                keyBoard.nextLine();
                if(number > 0){
                    return number;
                }
                System.out.println("Por favor, introduce un número válido.");
            } catch (InputMismatchException e) {
                System.out.println("Por favor, introduce un número válido.");
                keyBoard.nextLine();
            }
        }
    }

    private String getNombreUsuario(String message) {
        String data = "";
        while (true) {
            System.out.println(message);
            data = keyBoard.nextLine();
            if (!data.isEmpty()) {
                return data;
            }
        }

    }

    public String getWebData(String title) {
        ConsultaApi request = new ConsultaApi();
        var url = BASE_URL + "/?search=" + title.replace(" ", "+");
        return request.requestData(url);
    }

    public DatosLibros jsonToDatosLibros(String data) {
        ConvertirDatos dataConversion = new ConvertirDatos();
        return dataConversion.obtenerDatos(data, DatosLibros.class);
    }

    public DatosLibro getFirstBookWithAuthor(List<DatosLibro> libros) {
        return libros.stream()
                .filter(libro -> !libro.autor().isEmpty())
                .findFirst()
                .orElse(null);
    }

    public Libro searchOrSaveBook(Autor author, DatosLibro libro) {
        Libro bookToSave = null;
        List <Libro> books = author.getLibros();

        Optional <Libro> bookFromAuthor = books.stream()
                .filter(libro1 -> libro1.getTitulo().equals(libro.titulo()))
                .findFirst();

        if (bookFromAuthor.isPresent()) {
            System.out.println("El libro ya registrado!");
            bookToSave = bookFromAuthor.get();
        } else {

            bookToSave = new Libro(libro.titulo(), author,
                    libro.idioma().get(0), libro.numeroDeDescargas());

            author.setLibros(bookToSave);
            libroRepository.save(bookToSave);

            System.out.println("Libro guardado!");
        }
        return bookToSave;
    }

    public Autor searchOrSaveAuthor(DatosLibro libro) {
        Optional<Autor> autorBuscado = autorRepository.findByNombre(libro.autor().get(0).nombre());
        Autor authorToSave = null;


        if (!autorBuscado.isPresent()) {
            authorToSave = new Autor(libro.autor().get(0).nombre(),
                    libro.autor().get(0).nacimiento(), libro.autor().get(0).muerte());
            autorRepository.save(authorToSave);
            System.out.println("Autor guardado!");
        } else {
            authorToSave = autorBuscado.get();
            System.out.println("Autor ya registrado!");
        }
        return authorToSave;

    }


    public void buscarLibroPorNombre() {

        String message = "Introduce el titulo: ";
        var title = getNombreUsuario(message);

        String data = getWebData(title);
        DatosLibros libros = jsonToDatosLibros(data);

        if (!libros.libros().isEmpty()) {
            DatosLibro libro = getFirstBookWithAuthor(libros.libros());

            Autor author = searchOrSaveAuthor(libro);
            Libro book = searchOrSaveBook(author, libro);
            System.out.println(author);
            System.out.println(book);

        } else {
            System.out.println("No se encontraron resultados");
        }
    }

    private void getAllBooks() {

        bookSearched = libroRepository.findAll();
        if (bookSearched.isEmpty()) {
            System.out.println("No se encontraron libros registrados ");
        }
        bookSearched.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {
                    System.out.println(libro.toString());
                });
    }

    private void getAllAuthors() {
        authorsSearched = autorRepository.findAll();
        if (bookSearched.isEmpty()) {
            System.out.println("No se encontraron autores registrados");
        }
        authorsSearched.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> {
                    System.out.println(autor.toString());
                    System.out.println(autor.getLibros());
                });
    }

    private void getAuthorsAliveInYear() {
        System.out.println("Ingrese año: ");

        var year = getNumeroUsuario();
        List<Autor> autoresVivos = autorRepository.getAliveAuthors(year);
        if (autoresVivos.isEmpty()) {
            System.out.println("No hay autores vivos registrados del año: " + year);
        } else {
            autoresVivos.stream()
                    .forEach(autor -> {
                        System.out.println(autor.toString());
                    });
        }

    }

    private void getBooksByLanguage() {


        printMenuIdioma();

        String message= "Introduce el idioma: ";
        String language = getNombreUsuario(message);

        List<Libro> librosPorIdioma = libroRepository.findBookByLanguage(language);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + language);
        } else {
            librosPorIdioma.stream()
                    .forEach(libro -> {
                        System.out.println(libro.toString());
                    });
        }
    }



}
