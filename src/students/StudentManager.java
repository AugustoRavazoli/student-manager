package students;

import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe StudentManager é responsável por ler nomes de um arquivo
 * texto de entrada, converter em uma lista de nomes e em seguida embaralhar esta
 * lista em grupos com até 4 nomes aleatóriamente. Após a formação dos grupos,
 * eles são mostrados na tela e gravados em um arquivo texto de saída.
 * @author Augusto Ravazoli
 */
public class StudentManager {

  private static final Scanner input = new Scanner(System.in);
  private final Map<Integer, List<String>> groups = new HashMap<>();
  private final List<String> students = new ArrayList<>();
  private File inputFile, outputFile;
  private String formatedGroups;

  public static void main(String[] args) {
    StudentManager manager = new StudentManager();
    manager.askFileInputPath();
    manager.readStudentsFromFile();
    manager.shuffleStudentsInGroups();
    manager.formatGroups();
    manager.displayGroups();
    manager.askFileOutputPath();
    manager.writeGroupsToFile();
    input.close();
  }

  private void askFileInputPath() {
    while (true) {
      System.out.println("\nDigite o caminho do arquivo texto de entrada contento os nomes dos alunos: ");
      String inputPath = input.next();
      inputFile = new File(inputPath);
      if (inputFile.exists()) return;
      else showNotFound();
    }
  }

  private void askFileOutputPath() {
    while (true) {
      System.out.println("\nDigite o caminho do arquivo texto de saída, para gravar os grupos formados: ");
      String outputPath = input.next();
      outputFile = new File(outputPath);
      if (outputFile.exists()) return;
      else showNotFound();
    }
  }

  private void showNotFound() {
    System.out.println("Arquivo não encontrado");
    while (true) {
      System.out.println("1 - Tentar novamente");
      System.out.println("2 - Sair");
      int choice = input.nextInt();
      switch (choice) {
        case 1: return;
        case 2: System.exit(0);
        default: System.out.println("Essa opção não existe");
      }
    }
  }

  // Método para formatar groups em uma String
  private void formatGroups() {
   formatedGroups = "\nGrupos formados:\n"
    	.concat(groups
    		.entrySet()
    		.stream()
    		.map(entry -> String.format("\nGrupo %d:\n", entry.getKey())
    			.concat(entry
    				.getValue()
    				.stream()
    				.map(student -> String.format("\t%s\n", student))
    				.collect(Collectors.joining(""))
    			)
    		)
    		.collect(Collectors.joining(""))
    	);
  }

  private void readStudentsFromFile() {
    int size = (int) inputFile.length();
    char[] data = new char[size];
    try (FileReader file = new FileReader(inputFile)) {
      file.read(data);
    } catch (IOException exception) {
      exception.getStackTrace();
    }
    String[] studentNames = new String(data).split("\\n");
    for (String student : studentNames) students.add(student);
  }

  private void shuffleStudentsInGroups() {
    final int partitionSize = 4;
    Collections.shuffle(students);
    for (int i = 0, id = 0; i <= students.size(); i += partitionSize) {
      List<String> partition = students.subList(i, Math.min(i + partitionSize, students.size()));
      groups.put(++id, partition);
    }
  }

  private void displayGroups() {
    System.out.println(formatedGroups);
  }

  private void writeGroupsToFile() {
    try (FileWriter file = new FileWriter(outputFile)) {
      file.write(formatedGroups);
    } catch (IOException exception) {
      exception.getStackTrace();
    }
    System.out.println("\nGravado\n");
  }

}
