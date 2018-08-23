package com.merabreak.models;

public class IPLConfig {

    private boolean iplStarted = true;
    private String socketUrl = "ws://13.126.79.214:6020";
    private boolean showIPL = true;
    private boolean selectHomeTeam = true;
    private boolean termsAccepted = false;

    public IPLConfig(boolean iplStarted, String socketUrl, boolean showIPL) {
        this.iplStarted = iplStarted;
        this.socketUrl = socketUrl;
        this.showIPL = showIPL;
    }

    public IPLConfig() {
    }

    public boolean isIplStarted() {
        return iplStarted;
    }

    public void setIplStarted(boolean iplStarted) {
        this.iplStarted = iplStarted;
    }

    public String getSocketUrl() {
        return socketUrl;
    }

    public void setSocketUrl(String socketUrl) {
        this.socketUrl = socketUrl;
    }

    public boolean isShowIPL() {
        return showIPL;
    }

    public void setShowIPL(boolean showIPL) {
        this.showIPL = showIPL;
    }

    public boolean isSelectHomeTeam() {
        return selectHomeTeam;
    }

    public void setSelectHomeTeam(boolean selectHomeTeam) {
        this.selectHomeTeam = selectHomeTeam;
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }
}
