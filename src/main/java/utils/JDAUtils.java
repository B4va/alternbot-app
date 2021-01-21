package utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

import static java.util.Objects.isNull;
import static utils.EnvironmentVariablesUtils.BOT_TOKEN;

/**
 * Initialisation et récupération de l'instance du {@link JDA}.
 */
public class JDAUtils {

  private static JDA JDA;

  /**
   * Initialise la connexion au bot.
   *
   * @throws LoginException       erreur de token
   * @throws InterruptedException interruption de la connexion
   */
  public static void initializeJDA() throws LoginException, InterruptedException {
    if (isNull(JDA)) {
      JDA = JDABuilder.createDefault(EnvironmentVariablesUtils.getString(BOT_TOKEN)).build();
      JDA.awaitReady();
    }
  }

  /**
   * Renvoie l'instance de connexion au bot.
   *
   * @return instance de connexion
   */
  public static JDA getJDAInstance() {
    return JDA;
  }
}
