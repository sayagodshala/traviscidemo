package com.merabreak.models.challenge;

import com.google.gson.annotations.SerializedName;

public class ChallengeContestStatus {

    private String contestStatus;
    private int challengeId;

    public String getContestStatus() {
        return contestStatus;
    }

    public void setContestStatus(String contestStatus) {
        this.contestStatus = contestStatus;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
