package ovh.jonhshepard.attestations.storage;

public enum EnumReason {

    WORK("Travail", "Déplacements entre le domicile et le lieu d’exercice de l’activité professionnelle, " +
            "lorsqu’ils sont indispensables à l’exercice d’activités ne pouvant être organisées sous " +
            "forme de télétravail ou déplacements professionnels ne pouvant être différés"),
    FOOD("Achats néccésaires", "Déplacements pour effectuer des achats de fournitures nécessaires à l’activité " +
            "professionnelle et des achats de première nécessité3 dans des établissements dont les " +
            "activités demeurent autorisées (liste sur gouvernement.fr)."),
    MEDIC("Soins", "Consultations et soins ne pouvant être assurés à distance et ne pouvant être différés; " +
            "consultations et soins des patients atteints d'une affection de longue durée. "),
    ASSIST("Famille/Assistance", "Déplacements pour motif familial impérieux, pour l’assistance aux personnes " +
            "vulnérables ou la garde d’enfants. "),
    SPORT("Sport/Animaux", "Déplacements brefs, dans la limite d'une heure quotidienne et dans un rayon maximal " +
            "d'un kilomètre autour du domicile, liés soit à l'activité physique individuelle des " +
            "personnes, à l'exclusion de toute pratique sportive collective et de toute proximité avec " +
            "d'autres personnes, soit à la promenade avec les seules personnes regroupées dans un " +
            "même domicile, soit aux besoins des animaux de compagnie."),
    CONVOC("Convocation", "Convocation judiciaire ou administrative."),
    MISSION("Mission intérêt général", "Participation à des missions d’intérêt général sur demande de l’autorité administrative.");

    private final String shortMsg;
    private final String longMsg;

    EnumReason(String shortMsg, String longMsg) {
        this.shortMsg = shortMsg;
        this.longMsg = longMsg;
    }

    public String getShortMsg() {
        return shortMsg;
    }

    public String getLongMsg() {
        return longMsg;
    }
}
