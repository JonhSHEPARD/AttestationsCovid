package ovh.jonhshepard.attestations.storage;

import ovh.jonhshepard.attestations.R;

public enum EnumReason {

    WORK("travail", R.string.reason_work, "Déplacements entre le domicile et le lieu d’exercice de l’activité professionnelle, " +
            "lorsqu’ils sont indispensables à l’exercice d’activités ne pouvant être organisées sous " +
            "forme de télétravail ou déplacements professionnels ne pouvant être différés"),
    FOOD("courses", R.string.reason_food, "Déplacements pour effectuer des achats de fournitures nécessaires à l’activité " +
            "professionnelle et des achats de première nécessité3 dans des établissements dont les " +
            "activités demeurent autorisées (liste sur gouvernement.fr)."),
    MEDIC("sante", R.string.reason_medic, "Consultations et soins ne pouvant être assurés à distance et ne pouvant être différés; " +
            "consultations et soins des patients atteints d'une affection de longue durée. "),
    ASSIST("famille", R.string.reason_assist, "Déplacements pour motif familial impérieux, pour l’assistance aux personnes " +
            "vulnérables ou la garde d’enfants. "),
    SPORT("sport", R.string.reason_sport, "Déplacements brefs, dans la limite d'une heure quotidienne et dans un rayon maximal " +
            "d'un kilomètre autour du domicile, liés soit à l'activité physique individuelle des " +
            "personnes, à l'exclusion de toute pratique sportive collective et de toute proximité avec " +
            "d'autres personnes, soit à la promenade avec les seules personnes regroupées dans un " +
            "même domicile, soit aux besoins des animaux de compagnie."),
    CONVOC("judiciaire", R.string.reason_convoc, "Convocation judiciaire ou administrative."),
    MISSION("missions", R.string.reason_mission, "Participation à des missions d’intérêt général sur demande de l’autorité administrative.");

    private final String identifier;
    private final int shortMsg;
    private final String longMsg;

    EnumReason(String identifier, int shortMsg, String longMsg) {
        this.identifier = identifier;
        this.shortMsg = shortMsg;
        this.longMsg = longMsg;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getShortMsg() {
        return shortMsg;
    }

    public String getLongMsg() {
        return longMsg;
    }

    @Override
    public String toString() {
        return getIdentifier();
    }
}
