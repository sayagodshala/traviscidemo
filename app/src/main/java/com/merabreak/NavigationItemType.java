package com.merabreak;

public enum NavigationItemType {

    HOME("Home"),
    CHALLENGES("Challenges"),
    CHALLENGES_OFFLINE("Offline Challenge"),
    GAMES("Games"),
    PRIZE_BOOTH("Prize Booth"),
    RAFFLES("Raffles"),
    STORES("Stores"),
    WINNERS("Winners"),
    HELP("Help"),
    DAILY_SPIN("Daily Spin"),
    TERMS("Terms and Conditions"),
    PRIVACY("Privacy"),
    LOGOUT("Logout");

    private String title;

    NavigationItemType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
