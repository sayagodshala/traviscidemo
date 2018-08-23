package com.merabreak.models.enums;

import java.util.EnumSet;

public enum TeamEnum {

    KKR("Kolkata", "Kol"), DD("Delhi", "Del"), RCB("Bangalore", "Ban"), MI("Mumbai", "Mum"), KXIP("Punjab", "Pun"), CSK("Chennai", "Che"), RR("Rajasthan", "Raj"), SRH("Hyderabad", "Hyd"), RPS("Pune", "Pun"), GL("Gujarat", "Guj"), TBC("Tbc", "tbc");

    private String description;
    private String shortname;

    public static final EnumSet<TeamEnum> FILTER = EnumSet.of(KKR,DD,RCB, MI, KXIP, CSK, RR, SRH);

    TeamEnum(String description, String shortname) {
        this.description = description;
        this.shortname = shortname;
    }

    public String getDescription() {
        return description;
    }

    public String getShortname() {
        return shortname;
    }

    public static String shortName(String value) {
        return TeamEnum.valueOf(value).getShortname();
    }




}
