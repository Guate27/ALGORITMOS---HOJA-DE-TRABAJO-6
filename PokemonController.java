import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

class PokemonController {
    private Map<String, Pokemon> pokemonMap;
    private Collection<Pokemon> userCollection;
    private Scanner scanner;
    private PokemonFileReader fileReader;
    
    public PokemonController() {
        scanner = new Scanner(System.in);
        fileReader = new PokemonFileReader();
    }
    
    public void init() {
        // Cargar datos de Pokémon
        List<Pokemon> pokemonList = fileReader.readPokemonFile("pokemon.csv");
        
        // Seleccionar implementación de Map
        pokemonMap = selectMapImplementation();
        
        // Cargar Pokémon en el mapa
        for (Pokemon pokemon : pokemonList) {
            pokemonMap.put(pokemon.getName(), pokemon);
        }
        
        // Inicializar colección del usuario
        userCollection = new ArrayList<>();
        
        // Mostrar menú principal
        showMenu();
    }
    
    private Map<String, Pokemon> selectMapImplementation() {
        System.out.println("Seleccione la implementación de Map a utilizar:");
        System.out.println("1) HashMap");
        System.out.println("2) TreeMap");
        System.out.println("3) LinkedHashMap");
        
        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea
        
        MapFactory factory = new MapFactory();
        return factory.createMap(option);
    }
    
    private void showMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Agregar un Pokémon a tu colección");
            System.out.println("2. Mostrar datos de un Pokémon");
            System.out.println("3. Mostrar tu colección ordenada por tipo primario");
            System.out.println("4. Mostrar todos los Pokémon ordenados por tipo primario");
            System.out.println("5. Buscar Pokémon por habilidad");
            System.out.println("6. Guardar colección");
            System.out.println("7. Cargar colección");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea
            
            switch (option) {
                case 1:
                    addPokemonToUserCollection();
                    break;
                case 2:
                    showPokemonDetails();
                    break;
                case 3:
                    showUserCollectionByType();
                    break;
                case 4:
                    showAllPokemonByType();
                    break;
                case 5:
                    searchPokemonByAbility();
                    break;
                case 6:
                    saveUserCollection();
                    break;
                case 7:
                    loadUserCollection();
                    break;
                case 0:
                    exit = true;
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
    
    // Implementación de las operaciones del menú
    private void addPokemonToUserCollection() {
        System.out.print("Ingrese el nombre del Pokémon que desea agregar: ");
        String pokemonName = scanner.nextLine();
        
        Pokemon pokemon = pokemonMap.get(pokemonName);
        
        if (pokemon == null) {
            System.out.println("Error: El Pokémon " + pokemonName + " no existe en la base de datos.");
            return;
        }
        
        // Verificar si ya existe en la colección del usuario
        boolean exists = userCollection.stream().anyMatch(p -> p.getName().equals(pokemonName));
        
        if (exists) {
            System.out.println("El Pokémon " + pokemonName + " ya está en tu colección.");
        } else {
            userCollection.add(pokemon);
            System.out.println("¡" + pokemonName + " ha sido agregado a tu colección!");
        }
    }
    
    private void showPokemonDetails() {
        System.out.print("Ingrese el nombre del Pokémon: ");
        String pokemonName = scanner.nextLine();
        
        Pokemon pokemon = pokemonMap.get(pokemonName);
        
        if (pokemon == null) {
            System.out.println("Error: El Pokémon " + pokemonName + " no existe en la base de datos.");
        } else {
            System.out.println(pokemon.toString());
        }
    }
    
    private void showUserCollectionByType() {
        if (userCollection.isEmpty()) {
            System.out.println("Tu colección está vacía.");
            return;
        }
        
        List<Pokemon> sortedCollection = new ArrayList<>(userCollection);
        sortedCollection.sort(Comparator.comparing(Pokemon::getType1));
        
        System.out.println("\n=== TU COLECCIÓN (ordenada por tipo primario) ===");
        for (Pokemon pokemon : sortedCollection) {
            System.out.println("Nombre: " + pokemon.getName() + " | Tipo primario: " + pokemon.getType1());
        }
    }
    
    private void showAllPokemonByType() {
        List<Pokemon> allPokemon = new ArrayList<>(pokemonMap.values());
        allPokemon.sort(Comparator.comparing(Pokemon::getType1));
        
        System.out.println("\n=== TODOS LOS POKÉMON (ordenados por tipo primario) ===");
        for (Pokemon pokemon : allPokemon) {
            System.out.println("Nombre: " + pokemon.getName() + " | Tipo primario: " + pokemon.getType1());
        }
    }
    
    private void searchPokemonByAbility() {
        System.out.print("Ingrese la habilidad a buscar: ");
        String ability = scanner.nextLine();
        
        List<Pokemon> pokemonWithAbility = new ArrayList<>();
        
        for (Pokemon pokemon : pokemonMap.values()) {
            if (pokemon.hasAbility(ability)) {
                pokemonWithAbility.add(pokemon);
            }
        }
        
        if (pokemonWithAbility.isEmpty()) {
            System.out.println("No se encontraron Pokémon con la habilidad " + ability);
        } else {
            System.out.println("\n=== POKÉMON CON LA HABILIDAD: " + ability + " ===");
            for (Pokemon pokemon : pokemonWithAbility) {
                System.out.println("Nombre: " + pokemon.getName());
            }
        }
    }
    
    private void saveUserCollection() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_pokemon_collection.dat"))) {
            oos.writeObject(new ArrayList<>(userCollection));
            System.out.println("¡Colección guardada exitosamente!");
        } catch (IOException e) {
            System.out.println("Error al guardar la colección: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadUserCollection() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_pokemon_collection.dat"))) {
            userCollection = new ArrayList<>((List<Pokemon>) ois.readObject());
            System.out.println("¡Colección cargada exitosamente!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar la colección: " + e.getMessage());
        }
    }
}