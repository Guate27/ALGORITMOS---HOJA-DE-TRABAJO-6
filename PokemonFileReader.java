import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PokemonFileReader {
    private static final String DEFAULT_PATH = "pokemon_data_pokeapi.csv"; // Nombre correcto
    
    public List<Pokemon> readPokemonFile(String filename) {
        List<Pokemon> pokemonList = new ArrayList<>();
        String filePath = Paths.get(filename).toAbsolutePath().toString(); // Obtiene la ruta absoluta
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Pokemon pokemon = parsePokemonLine(line);
                    pokemonList.add(pokemon);
                } catch (Exception e) {
                    System.err.println("Error al procesar línea: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo en la ruta: " + filePath);
            System.err.println("Detalles: " + e.getMessage());
        }
        
        return pokemonList;
    }

    private Pokemon parsePokemonLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString().trim());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString().trim());

        // Verificar si hay suficientes campos
        if (fields.size() < 12) {
            throw new IllegalArgumentException("Línea mal formada: " + line);
        }

        int index = 0;
        String name = fields.get(index++);
        String type1 = fields.get(index++);
        String type2 = fields.get(index++);
        int total = Integer.parseInt(fields.get(index++));
        int hp = Integer.parseInt(fields.get(index++));
        int attack = Integer.parseInt(fields.get(index++));
        int defense = Integer.parseInt(fields.get(index++));
        int spAtk = Integer.parseInt(fields.get(index++));
        int spDef = Integer.parseInt(fields.get(index++));
        int speed = Integer.parseInt(fields.get(index++));
        int generation = Integer.parseInt(fields.get(index++));
        boolean legendary = Boolean.parseBoolean(fields.get(index++));

        List<String> abilities = new ArrayList<>();
        while (index < fields.size()) {
            String ability = fields.get(index++).trim();
            if (!ability.isEmpty()) {
                abilities.add(ability);
            }
        }

        return new Pokemon(name, type1, type2, total, hp, attack, defense, spAtk, spDef, speed, generation, legendary, abilities);
    }

    public static void main(String[] args) {
        PokemonFileReader reader = new PokemonFileReader();
        List<Pokemon> pokemonList = reader.readPokemonFile(DEFAULT_PATH);
        
        if (pokemonList.isEmpty()) {
            System.out.println("No se encontraron datos en el archivo.");
        } else {
            System.out.println("Se cargaron " + pokemonList.size() + " Pokémon.");
        }
    }
}
