package com.merabreak.models.challenge;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.merabreak.models.PrizeModel;

import java.util.ArrayList;
import java.util.List;

public class Challenge implements Parcelable {

    private String title;
    private String slug;
    private String description;
    private String coinsRequire;
    private String termsCondition;
    private String rules;
    private String id;
    private int averageRating;
    private String backgroundImage;
    private String featureImage;
    private int timeLimit;
    private boolean started;
    private List<Step> steps = new ArrayList<Step>();
    private List<Category> category = new ArrayList<Category>();
    private List<Winner> winners = new ArrayList<Winner>();
    private int payToPlay;
    private boolean completed;
    private int coins;
    private int offline;
    private int isDownloaded;
    private int isFc;
    private String totalPlayed;
    private int isOffline;
    private int dealId;
    private String dealTitle;
    private String dealAbout;
    private String dealImage;
    private String dealTC;
    private String dealAmount;
    private String dealCode;
    private String dealValidity;
    private int dealFlag;
    private boolean offlineCompleted;
    private String offlineSavedAnwers;
    private String coverImage;
    private String startBackgroundImage;
    private String endBackgroundImage;
    private int offlineRating;
    private String playedId;
    private String backgroundColor;
    private String challengeTitleColor;
    private int canReplay;
    private int isLiveChallenge;
    private int isParticipated;
    private int countQuestion;
    private String bannerImage;
    private int bannerFlag;
    private String categoryName;
    private String categoryColor;
    private String contentLanguage;
    private String contest_challenge = "0";
    private List<PrizeModel> prize_list = new ArrayList<PrizeModel>();
    private String prize_subtitle;

    public String getPrize_subtitle() {
        return prize_subtitle;
    }

    public void setPrize_subtitle(String prize_subtitle) {
        this.prize_subtitle = prize_subtitle;
    }



    public List<PrizeModel> getPrize_list() {
        return prize_list;
    }

    public void setPrize_list(List<PrizeModel> prize_list) {
        this.prize_list = prize_list;
    }



    public int getAlready_played() {
        return already_played;
    }

    public void setAlready_played(int already_played) {
        this.already_played = already_played;
    }

    public static Creator<Challenge> getCREATOR() {
        return CREATOR;
    }

    private int already_played;

    @SerializedName("campaign_rules")
    private String challengeRules = "";

    @SerializedName("challenge_type")
    private String challengeType;

    public Challenge() {
    }

    public Challenge(String title, String totalPlayed, String challengeTitleColor, String categoryName, String categoryColor, String contentLanguage ) {
        this.title = title;
        this.totalPlayed = totalPlayed;
        this.challengeTitleColor = challengeTitleColor;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.contentLanguage = contentLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoinsRequire() {
        return coinsRequire;
    }

    public void setCoinsRequire(String coinsRequire) {
        this.coinsRequire = coinsRequire;
    }

    public String getTermsCondition() {
        return termsCondition;
    }

    public void setTermsCondition(String termsCondition) {
        this.termsCondition = termsCondition;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Winner> getWinners() {
        return winners;
    }

    public void setWinners(List<Winner> winners) {
        this.winners = winners;
    }

    public int getPayToPlay() {
        return payToPlay;
    }

    public void setPayToPlay(int payToPlay) {
        this.payToPlay = payToPlay;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getOffline() {
        return offline;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public String getDealAbout() {
        return dealAbout;
    }

    public void setDealAbout(String dealAbout) {
        this.dealAbout = dealAbout;
    }

    public String getDealImage() {
        return dealImage;
    }

    public void setDealImage(String dealImage) {
        this.dealImage = dealImage;
    }

    public String getDealTC() {
        return dealTC;
    }

    public void setDealTC(String dealTC) {
        this.dealTC = dealTC;
    }

    public String getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(String dealAmount) {
        this.dealAmount = dealAmount;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public String getDealValidity() {
        return dealValidity;
    }

    public void setDealValidity(String dealValidity) {
        this.dealValidity = dealValidity;
    }

    public int getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(int dealFlag) {
        this.dealFlag = dealFlag;
    }

    public boolean isOfflineCompleted() {
        return offlineCompleted;
    }

    public void setOfflineCompleted(boolean offlineCompleted) {
        this.offlineCompleted = offlineCompleted;
    }

    public String getOfflineSavedAnwers() {
        return offlineSavedAnwers;
    }

    public void setOfflineSavedAnwers(String offlineSavedAnwers) {
        this.offlineSavedAnwers = offlineSavedAnwers;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getOfflineRating() {
        return offlineRating;
    }

    public void setOfflineRating(int offlineRating) {
        this.offlineRating = offlineRating;
    }

    public int getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(int isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public int getIsFc() {
        return isFc;
    }

    public void setIsFc(int isFc) {
        this.isFc = isFc;
    }

    public String getPlayedId() {
        return playedId;
    }

    public void setPlayedId(String playedId) {
        this.playedId = playedId;
    }

    public String getTotalPlayed() {
        return totalPlayed;
    }

    public void setTotalPlayed(String totalPlayed) {
        this.totalPlayed = totalPlayed;
    }

    public int getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(int isOffline) {
        this.isOffline = isOffline;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getStartBackgroundImage() {
        return startBackgroundImage;
    }

    public void setStartBackgroundImage(String startBackgroundImage) {
        this.startBackgroundImage = startBackgroundImage;
    }

    public String getEndBackgroundImage() {
        return endBackgroundImage;
    }

    public void setEndBackgroundImage(String endBackgroundImage) {
        this.endBackgroundImage = endBackgroundImage;
    }

    public int getCanReplay() {
        return canReplay;
    }

    public void setCanReplay(int canReplay) {
        this.canReplay = canReplay;
    }

    public int getIsLiveChallenge() {
        return isLiveChallenge;
    }

    public void setIsLiveChallenge(int isLiveChallenge) {
        this.isLiveChallenge = isLiveChallenge;
    }

    public int getIsParticipated() {
        return isParticipated;
    }

    public void setIsParticipated(int isParticipated) {
        this.isParticipated = isParticipated;
    }

    public int getCountQuestion() {
        return countQuestion;
    }

    public void setCountQuestion(int countQuestion) {
        this.countQuestion = countQuestion;
    }

    public String getFeatureImage() {
        return featureImage;
    }

    public void setFeatureImage(String featureImage) {
        this.featureImage = featureImage;
    }

    public String getChallengeTitleColor() {
        return challengeTitleColor;
    }

    public void setChallengeTitleColor(String challengeTitleColor) {
        this.challengeTitleColor = challengeTitleColor;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public int getBannerFlag() {
        return bannerFlag;
    }

    public void setBannerFlag(int bannerFlag) {
        this.bannerFlag = bannerFlag;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public String getChallengeRules() {
        return challengeRules;
    }

    public void setChallengeRules(String challengeRules) {
        this.challengeRules = challengeRules;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    public String getContest_challenge() {
        return contest_challenge;
    }

    public void setContest_challenge(String contest_challenge) {
        this.contest_challenge = contest_challenge;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.slug);
        dest.writeString(this.description);
        dest.writeString(this.coinsRequire);
        dest.writeString(this.termsCondition);
        dest.writeString(this.rules);
        dest.writeString(this.id);
        dest.writeInt(this.averageRating);
        dest.writeString(this.backgroundImage);
        dest.writeInt(this.timeLimit);
        dest.writeByte(this.started ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.steps);
        dest.writeTypedList(this.category);
        dest.writeTypedList(this.winners);
        dest.writeInt(this.payToPlay);
        dest.writeByte(this.completed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.coins);
        dest.writeInt(this.offline);
        dest.writeInt(this.dealId);
        dest.writeString(this.dealTitle);
        dest.writeString(this.dealAbout);
        dest.writeString(this.dealImage);
        dest.writeString(this.dealTC);
        dest.writeString(this.dealAmount);
        dest.writeString(this.dealCode);
        dest.writeString(this.dealValidity);
        dest.writeInt(this.dealFlag);
        dest.writeByte(this.offlineCompleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.offlineSavedAnwers);
        dest.writeString(this.coverImage);
        dest.writeString(this.startBackgroundImage);
        dest.writeString(this.endBackgroundImage);
        dest.writeInt(this.offlineRating);
        dest.writeInt(this.isDownloaded);
        dest.writeInt(this.isFc);
        dest.writeString(this.playedId);
        dest.writeString(this.totalPlayed);
        dest.writeInt(this.isOffline);
        dest.writeString(this.backgroundColor);
        dest.writeInt(this.canReplay);
        dest.writeInt(this.isLiveChallenge);
        dest.writeInt(this.isParticipated);
        dest.writeInt(this.countQuestion);
        dest.writeString(this.featureImage);
        dest.writeString(this.challengeTitleColor);
        dest.writeString(this.bannerImage);
        dest.writeInt(this.bannerFlag);
        dest.writeString(this.categoryName);
        dest.writeString(this.categoryColor);
        dest.writeString(this.challengeType);
        dest.writeString(this.challengeRules);
        dest.writeString(this.contentLanguage);
        dest.writeString(this.contest_challenge);
        dest.writeInt(this.already_played);
        dest.writeTypedList(this.prize_list);
        dest.writeString(this.prize_subtitle);
    }

    protected Challenge(Parcel in) {
        this.title = in.readString();
        this.slug = in.readString();
        this.description = in.readString();
        this.coinsRequire = in.readString();
        this.termsCondition = in.readString();
        this.rules = in.readString();
        this.id = in.readString();
        this.averageRating = in.readInt();
        this.backgroundImage = in.readString();
        this.timeLimit = in.readInt();
        this.started = in.readByte() != 0;
        this.steps = in.createTypedArrayList(Step.CREATOR);
        this.category = in.createTypedArrayList(Category.CREATOR);
        this.winners = in.createTypedArrayList(Winner.CREATOR);
        this.payToPlay = in.readInt();
        this.completed = in.readByte() != 0;
        this.coins = in.readInt();
        this.offline = in.readInt();
        this.dealId = in.readInt();
        this.dealTitle = in.readString();
        this.dealAbout = in.readString();
        this.dealImage = in.readString();
        this.dealTC = in.readString();
        this.dealAmount = in.readString();
        this.dealCode = in.readString();
        this.dealValidity = in.readString();
        this.dealFlag = in.readInt();
        this.offlineCompleted = in.readByte() != 0;
        this.offlineSavedAnwers = in.readString();
        this.coverImage = in.readString();
        this.startBackgroundImage = in.readString();
        this.endBackgroundImage = in.readString();
        this.offlineRating = in.readInt();
        this.isDownloaded = in.readInt();
        this.isFc = in.readInt();
        this.playedId = in.readString();
        this.totalPlayed = in.readString();
        this.isOffline = in.readInt();
        this.backgroundColor = in.readString();
        this.canReplay = in.readInt();
        this.isLiveChallenge = in.readInt();
        this.isParticipated = in.readInt();
        this.countQuestion = in.readInt();
        this.featureImage = in.readString();
        this.challengeTitleColor = in.readString();
        this.bannerImage = in.readString();
        this.bannerFlag = in.readInt();
        this.categoryName = in.readString();
        this.categoryColor = in.readString();
        this.challengeType = in.readString();
        this.challengeRules = in.readString();
        this.contentLanguage = in.readString();
        this.contest_challenge = in.readString();
        this.already_played = in.readInt();
        this.prize_list = in.createTypedArrayList(PrizeModel.CREATOR);
        this.prize_subtitle = in.readString();
    }

    public static final Creator<Challenge> CREATOR = new Creator<Challenge>() {
        @Override
        public Challenge createFromParcel(Parcel source) {
            return new Challenge(source);
        }

        @Override
        public Challenge[] newArray(int size) {
            return new Challenge[size];
        }
    };
}