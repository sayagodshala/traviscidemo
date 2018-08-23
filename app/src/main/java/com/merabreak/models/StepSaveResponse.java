package com.merabreak.models;

/**
 * Created by sayagodshala on 12/03/16.
 */
public class StepSaveResponse {
    private String id;
    private String coins;
    private int numOfPlays;
    private int msgFlag;
    private int right_answer_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public int getNumOfPlays() {
        return numOfPlays;
    }

    public void setNumOfPlays(int numOfPlays) {
        this.numOfPlays = numOfPlays;
    }

    public int getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(int msgFlag) {
        this.msgFlag = msgFlag;
    }

    public int getRight_answer_count() {
        return right_answer_count;
    }

    public void setRight_answer_count(int right_answer_count) {
        this.right_answer_count = right_answer_count;
    }

    public StepSaveResponse() {
    }

}
