package process;

/**
 * Process de récupération des données IUT.
 */
public class ScheduleUpdateProcess {

  /**
   * Lance le process.
   */
  public void update() {
    /*
    pour chaque schedule enregistré :
    -> recuperer ical raw -> string
      -> parsing :
        - seulement cours futurs
        - si cours déjà enregistré à cet horaire :
          - si identique, ne rien faire
          - si différent, modifier et envoyer publication alerte

     modifs modélisation :
     -> ajouter un attribut url à Schedule
     -> faire passer la cardinalité à 1-1 pour l'association Schedule -> Server

     plus tard :
     - gérer les renvoi au même emploi du temps pour des serveurs différents
     */
  }
}
