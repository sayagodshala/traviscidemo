package com.merabreak.models.challenge;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.merabreak.models.SpinWheelIndex;

import java.util.ArrayList;
import java.util.List;

public class Step implements Parcelable {
    private String id;
    private int stepOrder;
    private String type;
    private String slug;
    private String videoId;
    private String title;
    private String description;
    private String url;
    private String questionId;
    private String question;
    private String jigsawId;
    private int columns;
    private int rows;
    private int isGrid;
    private List<Answer> answers;
    private List<Answer> options;
    private List<Article> articles = new ArrayList<Article>();
    private boolean userCompleted;
    private String stepAnswer;
    private String image;
    private int maxCharLength;
    private String userFeedback;
    private List<String> multiSelectSurveyAnswers;
    private String riddleId;
   // private String rightAnswer;
    private int totalCharacters;
    private int totalChances;
    private boolean riddleSolved;
    private boolean puzzleSolved;

    private List<Memimages> images;
    private boolean memoryGameSolved;
    private String adUrl;
    private List<SpinWheelIndex> spinDetails;
    private int points = -1;
    @SerializedName("step_coins")
    private int stepPoints;
    @SerializedName("last_step")
    private boolean isLastStep;
    @SerializedName("right_answer")
    private String rightAnswer;
    private String stepKey;
    private int rightAnswerCount;
    private String image_url;
    private String video_url;
    private String position;
    private int skip_time = 0;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSkip_time() {
        return skip_time;
    }

    public void setSkip_time(int skip_time) {
        this.skip_time = skip_time;
    }



    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @SerializedName("step_bgimage")
    private String backgroundImage = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(int stepOrder) {
        this.stepOrder = stepOrder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getJigsawId() {
        return jigsawId;
    }

    public void setJigsawId(String jigsawId) {
        this.jigsawId = jigsawId;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getOptions() {
        return options;
    }

    public void setOptions(List<Answer> options) {
        this.options = options;
    }

    public boolean isUserCompleted() {
        return userCompleted;
    }

    public void setUserCompleted(boolean userCompleted) {
        this.userCompleted = userCompleted;
    }

    public String getStepAnswer() {
        return stepAnswer;
    }

    public void setStepAnswer(String stepAnswer) {
        this.stepAnswer = stepAnswer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMaxCharLength() {
        return maxCharLength;
    }

    public void setMaxCharLength(int maxCharLength) {
        this.maxCharLength = maxCharLength;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public List<String> getMultiSelectSurveyAnswers() {
        return multiSelectSurveyAnswers;
    }

    public void setMultiSelectSurveyAnswers(List<String> multiSelectSurveyAnswers) {
        this.multiSelectSurveyAnswers = multiSelectSurveyAnswers;
    }

    public List<Memimages> getImages() {
        return images;
    }

    public void setImages(List<Memimages> images) {
        this.images = images;
    }

    public String getRiddleId() {
        return riddleId;
    }

    public void setRiddleId(String riddleId) {
        this.riddleId = riddleId;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getTotalCharacters() {
        return totalCharacters;
    }

    public void setTotalCharacters(int totalCharacters) {
        this.totalCharacters = totalCharacters;
    }

    public int getTotalChances() {
        return totalChances;
    }

    public void setTotalChances(int totalChances) {
        this.totalChances = totalChances;
    }

    public boolean isRiddleSolved() {
        return riddleSolved;
    }

    public void setRiddleSolved(boolean riddleSolved) {
        this.riddleSolved = riddleSolved;
    }

    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    public void setPuzzleSolved(boolean puzzleSolved) {
        this.puzzleSolved = puzzleSolved;
    }

    public boolean isMemoryGameSolved() {
        return memoryGameSolved;
    }

    public void setMemoryGameSolved(boolean memoryGameSolved) {
        this.memoryGameSolved = memoryGameSolved;
    }

    public int getIsGrid() {
        return isGrid;
    }

    public void setIsGrid(int isGrid) {
        this.isGrid = isGrid;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public List<SpinWheelIndex> getSpinDetails() {
        return spinDetails;
    }

    public void setSpinDetails(List<SpinWheelIndex> spinDetails) {
        this.spinDetails = spinDetails;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getStepPoints() {
        return stepPoints;
    }

    public void setStepPoints(int stepPoints) {
        this.stepPoints = stepPoints;
    }

    public boolean isLastStep() {
        return isLastStep;
    }

    public void setLastStep(boolean lastStep) {
        isLastStep = lastStep;
    }

    public String getStepKey() {
        return stepKey;
    }

    public void setStepKey(String stepKey) {
        this.stepKey = stepKey;
    }

    public Step() {
    }

    public Step(String id) {
        this.id = id;
    }

    public int getRightAnswerCount() {
        return rightAnswerCount;
    }

    public void setRightAnswerCount(int rightAnswerCount) {
        this.rightAnswerCount = rightAnswerCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.stepOrder);
        dest.writeString(this.type);
        dest.writeString(this.slug);
        dest.writeString(this.videoId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeString(this.questionId);
        dest.writeString(this.question);
        dest.writeString(this.jigsawId);
        dest.writeInt(this.columns);
        dest.writeInt(this.rows);
        dest.writeInt(this.isGrid);
        dest.writeTypedList(this.answers);
        dest.writeTypedList(this.options);
        dest.writeTypedList(this.articles);
        dest.writeByte(this.userCompleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.stepAnswer);
        dest.writeString(this.image);
        dest.writeInt(this.maxCharLength);
        dest.writeString(this.userFeedback);
        dest.writeStringList(this.multiSelectSurveyAnswers);
        dest.writeTypedList(this.images);
        dest.writeString(this.riddleId);
        dest.writeString(this.rightAnswer);
        dest.writeInt(this.totalCharacters);
        dest.writeInt(this.totalChances);
        dest.writeByte(this.riddleSolved ? (byte) 1 : (byte) 0);
        dest.writeByte(this.puzzleSolved ? (byte) 1 : (byte) 0);
        dest.writeByte(this.memoryGameSolved ? (byte) 1 : (byte) 0);
        dest.writeString(this.adUrl);
        dest.writeTypedList(this.spinDetails);
        dest.writeInt(this.points);
        dest.writeInt(this.stepPoints);
        dest.writeByte(this.isLastStep ? (byte) 1 : (byte) 0);
        dest.writeString(this.stepKey);
        dest.writeInt(this.rightAnswerCount);
        dest.writeString(this.backgroundImage);
        dest.writeString(this.image_url);
        dest.writeString(this.video_url);
        dest.writeString(this.position);
        dest.writeInt(this.skip_time);
    }

    protected Step(Parcel in) {
        this.id = in.readString();
        this.stepOrder = in.readInt();
        this.type = in.readString();
        this.slug = in.readString();
        this.videoId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.questionId = in.readString();
        this.question = in.readString();
        this.jigsawId = in.readString();
        this.columns = in.readInt();
        this.rows = in.readInt();
        this.isGrid = in.readInt();
        this.answers = in.createTypedArrayList(Answer.CREATOR);
        this.options = in.createTypedArrayList(Answer.CREATOR);
        this.articles = in.createTypedArrayList(Article.CREATOR);
        this.userCompleted = in.readByte() != 0;
        this.stepAnswer = in.readString();
        this.image = in.readString();
        this.maxCharLength = in.readInt();
        this.userFeedback = in.readString();
        this.multiSelectSurveyAnswers = in.createStringArrayList();
        this.images = in.createTypedArrayList(Memimages.CREATOR);
        this.riddleId = in.readString();
        this.rightAnswer = in.readString();
        this.totalCharacters = in.readInt();
        this.totalChances = in.readInt();
        this.riddleSolved = in.readByte() != 0;
        this.puzzleSolved = in.readByte() != 0;
        this.memoryGameSolved = in.readByte() != 0;
        this.adUrl = in.readString();
        this.spinDetails = in.createTypedArrayList(SpinWheelIndex.CREATOR);
        this.points = in.readInt();
        this.stepPoints = in.readInt();
        this.isLastStep = in.readByte() != 0;
        this.stepKey = in.readString();
        this.rightAnswerCount = in.readInt();
        this.backgroundImage = in.readString();
        this.image_url = in.readString();
        this.video_url = in.readString();
        this.position = in.readString();
        this.skip_time = in.readInt();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}