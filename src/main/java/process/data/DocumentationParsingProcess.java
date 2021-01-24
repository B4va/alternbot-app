package process.data;

import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystemNotFoundException;

/**
 * Process de récupération de la documentation utilisateur du bot.
 */
public class DocumentationParsingProcess {

  public static final String DOCUMENTATION_FILE = "documentation.md";
  public static final String DOCUMENTATION_TEST_FILE = "documentation_test.md";

  private static final Logger LOGGER = LoggerUtils.buildLogger(DocumentationParsingProcess.class);

  /**
   * Récupère la documentation à partir du fichier ressource.
   *
   * @param file fichier ressources (resources > process > data)
   * @return documentation utilisateur
   */
  public String parse(String file) {
    try {
      InputStream inputStream = DocumentationParsingProcess.class.getResourceAsStream(file);
      return readFromInputStream(inputStream);
    } catch (IOException | NullPointerException e) {
      LOGGER.error("Impossible d'accéder au fichier : {}", file);
      throw new FileSystemNotFoundException();
    }
  }

  private String readFromInputStream(InputStream inputStream) throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    }
    return resultStringBuilder.toString();
  }
}
