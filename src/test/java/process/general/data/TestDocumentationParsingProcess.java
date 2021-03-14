package process.general.data;

import org.junit.jupiter.api.Test;
import process.general.data.DocumentationParsingProcess;

import java.nio.file.FileSystemNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static process.general.data.DocumentationParsingProcess.DOCUMENTATION_FILE;
import static process.general.data.DocumentationParsingProcess.DOCUMENTATION_TEST_FILE;

/**
 * Classe de test de {@link DocumentationParsingProcess}.
 */
public class TestDocumentationParsingProcess {

  private static final DocumentationParsingProcess PROCESS = new DocumentationParsingProcess();
  private static final String DOCUMENTATION_TEST = "```\nHello world !\n\nCeci est un test.\n```\n";
  private static final String INVALID_FILE = "invalid.md";

  @Test
  public void testParse() {
    String test = PROCESS.parse(DOCUMENTATION_TEST_FILE);
    assertEquals(test, DOCUMENTATION_TEST);
  }

  @Test
  public void testParse_invalid_file() {
    assertThrows(FileSystemNotFoundException.class, () -> PROCESS.parse(INVALID_FILE));
  }

  @Test
  public void testExistingDocFile() {
    assertNotNull(DocumentationParsingProcess.class.getResourceAsStream(DOCUMENTATION_FILE));
  }
}
