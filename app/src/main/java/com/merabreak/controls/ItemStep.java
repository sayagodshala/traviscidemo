package com.merabreak.controls;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.merabreak.Constants;
import com.merabreak.MemoryButton;
import com.merabreak.R;
import com.merabreak.StepType;
import com.merabreak.Util;
import com.merabreak.activities.ChallengePlayActivity;
import com.merabreak.games.puzzle.SlidePuzzle;
import com.merabreak.games.puzzle.SlidePuzzleView;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.GridImagesModel;
import com.merabreak.models.challenge.Answer;
import com.merabreak.models.challenge.Step;
import com.merabreak.timer.MBCountDownTimer;
import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by Ewebcore on 06-Feb-16.
 */

@EViewGroup(R.layout.item_step)
public class ItemStep extends RelativeLayout implements YouTubePlayer.OnInitializedListener, SlidePuzzleView.SlidePuzzleListener, MBCountDownTimer.MBTimerListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private boolean offline = false;
    private Context context;

    Animation outAnimation;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.answer_container)
    LinearLayout answerContainer;

    @ViewById(R.id.answer_container_grid)
    LinearLayout answerContainerGrid;

    @ViewsById({R.id.option1, R.id.option2, R.id.option3, R.id.option4})
    List<Button> gridOptions;

    @ViewById(R.id.video)
    FullscreenVideoLayout video;

    @ViewById(R.id.youTubePlayerView)
    YouTubePlayerView youTubePlayerView;

    String youtubeVideoId = "";

    @ViewById(R.id.image)
    ImageView image;

    @ViewById(R.id.add_image)
    ImageView add_image;

    @ViewById(R.id.no_type)
    TextView noType;

    @ViewById(R.id.feedback)
    TextView feedback;

    @ViewById(R.id.main_content)
    LinearLayout mainContent;

    @ViewById(R.id.puzzle_main_container)
    RelativeLayout puzzleContainer;

    @ViewById(R.id.grid_main)
    GridLayout grid_main;

    @ViewById(R.id.puzzle_image)
    RelativeLayout puzzleImage;

    @ViewById(R.id.puzzle_preloader)
    ImageView puzzlePreloader;

    @ViewById(R.id.memorygame_preloader)
    ImageView memorygame_preloader;

    public boolean isWidgetDefined = false;

    Step step;


    //this object is same for answers and options
    List<Answer> answers;

    LayoutInflater layoutInflater;

    public boolean answerd;

    ImageLoader imageLoader;
    public String answerId = "";
    private String challengeSlug = "";
    private String contentLanguage = "";

    private static final int RECOVERY_REQUEST = 1;
    private String videoId = "";
    private YouTubePlayer youTubePlayer = null;

    private List<String> multiSelectSurveyAnswers = new ArrayList<>();

    @ViewById(R.id.riddle)
    LinearLayout riddle;

    @ViewById(R.id.wrong_riddle_letters)
    TextView wrongRiddleLetters;

    @ViewById(R.id.riddle_chances)
    TextView riddleChances;

    @ViewById(R.id.riddle_letters)
    LinearLayout riddleLetters;

    @ViewById(R.id.anagram_letters)
    LinearLayout anagramLetters;

    private int riddleFailCounts = 0;
    private int riddleSuccessCounts = 0;
    private boolean isRiddleRightGuess = false;


    private boolean portrait;
    private SlidePuzzle slidePuzzle;
    private SlidePuzzleView view;
    protected static final int DEFAULT_SIZE = 5;
    private int puzzleWidth = 1;
    private int puzzleHeight = 1;
    private boolean expert;
    private Uri imageUri;
    private int targetWidth;
    private int targetHeight;
    private Bitmap puzzleBitmap;
    private Bitmap memGameBitmap;
    private InputStream puzzleBitmapInput;

    private MBCountDownTimer mbCountDownTimer;

    private int timeLimit = 0;

    private int total;
    private MemoryButton[] buttons;
    private int[] imageLocation;
    //private int[] allImages;
    private ArrayList<GridImagesModel> allImages = new ArrayList<GridImagesModel>();
    private String[] ids = new String[]{"image_0", "image_1", "image_2", "image_3", "image_4", "image_5", "image_6", "image_7", "image_8", "image_9", "image_10",
            "image_11", "image_12", "image_13", "image_14", "image_15", "image_16", "image_17", "image_18", "image_19", "image_20"};
    // private String[] ids;
    private int[] drawableImages;
    private MemoryButton selectedImage1;
    private MemoryButton selectedImage2;
    private boolean isBusy = false;
    private boolean allImagesFlipped = false;
    private int memoryCounter = 0;
    private int memImageCounter = 0;
    private Bitmap[] memoryBitmap;
    MemoryButton memoryButton;
    private int imageUrlCount = 0;
    List<String> downloadURLs = new ArrayList<>();
    private int totalCounts = 0;
    private int finalCounts = 0;
    int tempCounter = 0;
    int contestRightAnswerCount = 0;

    //private List<Drawable> images;

    public ItemStep(Context context) {
        super(context);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ItemStep(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Step step, Context context) {

    }

    public void init(Step step, Context context, boolean offline, String challengeSlug , String contentLanguage) {

        noType.setVisibility(View.GONE);
        isWidgetDefined = true;

        imageLoader = new ImageLoader(context);
        this.offline = offline;
        this.context = context;
        this.challengeSlug = challengeSlug;
        this.contentLanguage  = contentLanguage;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.step = step;

        if(step.getRightAnswer()== null || step.getRightAnswer().isEmpty())
        {
            step.setRightAnswer("0");
        }

        //Log.d("Quiz Type", step.getType());

        switch (step.getType().toLowerCase()) {
            case StepType.QUIZ:
                if (step.getIsGrid() == 0)
                    setQuiz();
                else
                    setQuizGrid();
                break;
            case StepType.POLLING:
                if (step.getIsGrid() == 0)
                    setQuiz();
                else
                    setQuizGrid();
                break;
            case StepType.SURVEYMULTISELECT:
                setSurveyMultiselect();
                break;
            case StepType.SURVEY:
                setSurvey();
                break;
            case StepType.ANAGRAMS:
            case StepType.ANAGRAM:
                setAnagram();
                break;
            case StepType.VIDEO:
                setVideo();
                break;
            case StepType.JIGSAW:
                setJigsawPuzzle();
                break;
            case StepType.ADURI:
                setAdUrl();
                break;
            case StepType.RIDDLE:
                setRiddle();
                break;
            case StepType.SPIN:

                break;
            case StepType.MEMORYGAME:
                memorygame_preloader.setVisibility(View.VISIBLE);

                if (offline) {
                    for (tempCounter = 0; tempCounter < step.getImages().size(); tempCounter++) {
                        String id = ids[tempCounter];
                        String filePath = step.getImages().get(tempCounter).getUrl();
                        if (filePath != null && filePath.contains("http")) {
                            String fileName = step.getImages().get(tempCounter).getUrl().substring(step.getImages().get(tempCounter).getUrl().lastIndexOf('/') + 1, step.getImages().get(tempCounter).getUrl().length());
                            String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                            File file = new File(savedFile);
                            if (file.exists()) {
                                Uri imageURI = Uri.parse(savedFile);
                                //Log.d("Downloaded IMGURI", savedFile);
                                filePath = imageURI.toString();
                                try {
                                    FileInputStream fileInputStream = new FileInputStream(file);
                                    memGameBitmap = BitmapFactory.decodeStream(fileInputStream);
                                    Drawable drawable = new BitmapDrawable(context.getResources(), memGameBitmap);
                                    GridImagesModel gim = new GridImagesModel(drawable, id);
                                    allImages.add(gim);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                                }

                            } else {
                                Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                            }
                        }
                    }
                    setMemoryGame();

                } else {
                    for (tempCounter = 0; tempCounter < step.getImages().size(); tempCounter++) {
                        String images = step.getImages().get(tempCounter).getUrl();
                        String id = ids[tempCounter];
                        new DownloadImagesForMemoryGame() {
                            @Override
                            protected void onPostExecute(Drawable file_url) {
                                super.onPostExecute(file_url);
                                GridImagesModel gim = new GridImagesModel(file_url, id);
                                allImages.add(gim);
                                if (allImages.size() >= tempCounter) {
                                    setMemoryGame();
                                }
                            }
                        }.execute(images);
                    }
                }
               /* if(memImageCounter<step.getImages().size()) {
                    String images = step.getImages().get(memImageCounter).getUrl();
                    new DownloadImagesForMemoryGame().execute(images);
                }*/

                break;
            default:
                mainContent.setVisibility(View.GONE);
                isWidgetDefined = false;
                noType.setVisibility(View.VISIBLE);
                break;
        }
    }


   /*  case StepType.ARTICLE:
            step.setUserCompleted(true);
                ((ChallengePlayActivity) context).onNext();
                break;*/

    public Step getStep() {
        return step;
    }

    private void setQuizGrid() {
        answerContainerGrid.setVisibility(View.VISIBLE);
        title.setText(step.getQuestion());
        if (!contentLanguage.equalsIgnoreCase("en"))
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        switch (step.getType().toLowerCase()) {
            case StepType.QUIZ:
                answers = step.getAnswers();
                break;
            default:
                answers = step.getOptions();
                break;
        }

        if (offline) {
            String filePath = step.getImage();
            if (filePath != null && filePath.contains("http")) {
                String fileName = filePath.substring(step.getImage().lastIndexOf('/') + 1, step.getImage().length());
                String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    image.setImageURI(imageURI);
                } else {
                    Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                }
            }
        } else {
            if (step.getImage() != null && !step.getImage().equalsIgnoreCase("")) {
                imageLoader.DisplayImage(step.getImage(), image);
                image.setVisibility(View.VISIBLE);
                //title.setVisibility(View.GONE);
            }
        }

        if (answers.size() > 0 && answers.size() == 4) {
            for (int i = 0; i < answers.size(); i++) {
                Button check = gridOptions.get(i);
                check.setVisibility(View.VISIBLE);
                check.setTag(i);
                check.setText(answers.get(i).getTitle());
                if(!contentLanguage.equalsIgnoreCase("en"))
                    check.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                if (step.getStepAnswer() != null && step.getStepAnswer().equalsIgnoreCase(answers.get(i).getId())) {
                    check.setSelected(true);
                    step.setStepAnswer(answers.get(i).getId());
                    step.setUserCompleted(true);
                }

                check.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //     Animation shakeAnswerCheck = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                        //     check.startAnimation(shakeAnswerCheck);
                        if(v.isSelected())
                            return;
                        deselectAnswersGrid();
                        if (!check.isSelected()) {
                            check.setSelected(true);
                            answerd = true;
                            if (v.getTag() != null) {
                                int position = Integer.parseInt(v.getTag().toString());
                                answerId = answers.get(position).getId();
                                if (answers.get(position).getIsAnswer() == 1 && step.getPoints() == -1) {
                                    int points = step.getPoints() == -1 ? 0 : step.getPoints();
                                    step.setPoints(points + step.getStepPoints());
                                }
                                if (answers.get(position).getIsAnswer() == 1) {

                                    if(step.getRightAnswer()== null || step.getRightAnswer().isEmpty())
                                    {
                                        step.setRightAnswer("0");
                                    }

                                    contestRightAnswerCount = Integer.valueOf(step.getRightAnswer()) +1;

                                    step.setRightAnswer(String.valueOf(contestRightAnswerCount));

                                }
                                step.setUserCompleted(true);
                                step.setStepAnswer(answerId);
                            }
                        }
                    }
                });
            }
        } else {
            step.setUserCompleted(true);
            step.setStepAnswer("");
            Util.makeToast(context, context.getResources().getString(R.string.item_step_no_options));
        }
    }

    private void setQuiz() {

        answerContainer.setVisibility(View.VISIBLE);

        switch (step.getType().toLowerCase()) {
            case StepType.QUIZ:
                answers = step.getAnswers();
                break;
            default:
                answers = step.getOptions();
                break;
        }

        if (offline) {
            String filePath = step.getImage();
            if (filePath != null && filePath.contains("http")) {
                String fileName = filePath.substring(step.getImage().lastIndexOf('/') + 1, step.getImage().length());
                String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    image.setImageURI(imageURI);
                } else {
                    Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                }
            }
        } else {
            if (step.getImage() != null && !step.getImage().equalsIgnoreCase("")) {
                imageLoader.DisplayImage(step.getImage(), image);
                image.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);
            }
        }

        if (answers.size() > 0) {
            title.setText(step.getQuestion());
            if (!contentLanguage.equalsIgnoreCase("en"))
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            if (answerContainer.getChildCount() > 0)
                answerContainer.removeAllViews();

            for (int i = 0; i < answers.size(); i++) {
                View answerView = layoutInflater.inflate(R.layout.item_answer, null);
                final Button check = (Button) answerView.findViewById(R.id.check);
                check.setTag(i);
                check.setText(answers.get(i).getTitle());
                if (!contentLanguage.equalsIgnoreCase("en"))
                    check.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                if (step.getStepAnswer() != null && step.getStepAnswer().equalsIgnoreCase(answers.get(i).getId())) {
                    check.setSelected(true);
                    step.setStepAnswer(answers.get(i).getId());
                    step.setUserCompleted(true);
                }

                check.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   Animation shakeAnswerCheck = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                        //    check.startAnimation(shakeAnswerCheck);
                        if(v.isSelected())
                            return;
                        deselectAnswers();
                        if (!check.isSelected()) {
                            check.setSelected(true);
                            answerd = true;
                            if (v.getTag() != null) {
                                int position = Integer.parseInt(v.getTag().toString());
                                answerId = answers.get(position).getId();
                                if (answers.get(position).getIsAnswer() == 1 && step.getPoints() == -1) {
                                    int points = step.getPoints() == -1 ? 0 : step.getPoints();
                                    step.setPoints(points + step.getStepPoints());

                                }
                                if (answers.get(position).getIsAnswer() == 1) {

                                    if(step.getRightAnswer()== null || step.getRightAnswer().isEmpty())
                                    {
                                        step.setRightAnswer("0");
                                    }

                                    contestRightAnswerCount = Integer.valueOf(step.getRightAnswer())+1;

                                    step.setRightAnswer(String.valueOf(contestRightAnswerCount));

                                }

                                step.setUserCompleted(true);
                                step.setStepAnswer(answerId);
                            }
                        }
                    }
                });
                answerContainer.addView(answerView);
            }
        } else {
            step.setUserCompleted(true);
            step.setStepAnswer("");
            Util.makeToast(context, context.getResources().getString(R.string.item_step_no_options));
        }
    }

    private void setAdUrl() {
        title.setText(step.getTitle());
        if (step.getAdUrl().contains("http://res.cloudinary.com/merabreak-media/image/upload/v1/advertisement/")) {
            step.setUserCompleted(true);
            add_image.setVisibility(View.VISIBLE);
            if (offline) {
                String filePath = step.getAdUrl();
                if (filePath != null && filePath.contains("http")) {
                    String fileName = filePath.substring(step.getAdUrl().lastIndexOf('/') + 1, step.getAdUrl().length());
                    String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                    File file = new File(savedFile);
                    if (file.exists()) {
                        Uri imageURI = Uri.parse(savedFile);
                        //Log.d("Downloaded IMGURI", savedFile);
                        add_image.setImageURI(imageURI);
                    } else {
                        Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                    }
                }
            } else {
                if (step.getAdUrl() != null && !step.getAdUrl().equalsIgnoreCase("")) {
                    imageLoader.DisplayImage(step.getAdUrl(), add_image);
                }
            }
        } else {
            Uri videoUri = Uri.parse(step.getAdUrl());
            step.setUserCompleted(true);
            if (videoUri.getHost().contains("youtube")) {
                //Log.d("first video", "first video");
                title.setText(step.getTitle());
                step.setUserCompleted(true);
                youTubePlayerView.setVisibility(View.VISIBLE);
                youtubeVideoId = videoUri.getQueryParameter("v");
                youTubePlayerView.initialize(Constants.GOOGLE_API_KEY, this);
            } else {
                //Log.d("second video", "second video");
                video.setVisibility(View.VISIBLE);
                //video.setFullscreen(false);
                if (offline) {
                    if (step.getAdUrl() != null && step.getAdUrl().contains("http://")) {
                        String fileName = step.getAdUrl().substring(step.getAdUrl().lastIndexOf('/') + 1, step.getAdUrl().length());
                        String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                        File file = new File(savedFile);
                        if (file.exists()) {
                            videoUri = Uri.parse(savedFile);
                            try {
                                video.setVideoURI(videoUri);
                                //Log.d("Downloaded Video", Constants.FOLDER_PATH(challengeSlug, fileName));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Util.makeToast(context, context.getResources().getString(R.string.item_step_video_not_found));
                        }
                    }
                } else {
                    //Log.d("third video", "third video");
                    if (step.getAdUrl() != null && step.getAdUrl().contains("http://")) {
                        videoUri = Uri.parse(step.getAdUrl());
                        try {
                            video.setVideoURI(videoUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Click(R.id.add_image)
    void addImage() {

    }

    private void setSurveyMultiselect() {
        answerContainer.setVisibility(View.VISIBLE);
        answers = step.getAnswers();

        if (offline) {
            String filePath = step.getImage();
            if (filePath != null && filePath.contains("http")) {
                String fileName = filePath.substring(step.getImage().lastIndexOf('/') + 1, step.getImage().length());
                String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    image.setImageURI(imageURI);
                } else {
                    Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                }
            }
        } else {
            if (step.getImage() != null && !step.getImage().equalsIgnoreCase("")) {
                imageLoader.DisplayImage(step.getImage(), image);
                image.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);
            }
        }

        if (answers.size() > 0) {

            title.setText(step.getQuestion());
            if (answerContainer.getChildCount() > 0)
                answerContainer.removeAllViews();

            for (int i = 0; i < answers.size(); i++) {
                View answerView = layoutInflater.inflate(R.layout.item_answer, null);
                final Button check = (Button) answerView.findViewById(R.id.check);
                check.setTag(i);
                check.setText(answers.get(i).getTitle());

                if (step.getStepAnswer() != null && step.getStepAnswer().equalsIgnoreCase(answers.get(i).getId())) {
                    check.setSelected(true);
                    step.setStepAnswer(answers.get(i).getId());
                    step.setUserCompleted(true);
                }

                check.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!check.isSelected()) {
                            check.setSelected(true);
                        } else {
                            check.setSelected(false);
                        }
                        captureMultiselectSurveyAnswers();
                    }
                });
                answerContainer.addView(answerView);
            }
        } else {
            step.setUserCompleted(true);
            step.setStepAnswer("");
            Util.makeToast(context, context.getResources().getString(R.string.item_step_no_options));
        }
    }

    private void setVideo() {
        Uri videoUri = Uri.parse(step.getUrl());
        title.setText(step.getTitle());
        step.setUserCompleted(true);
        if (videoUri.getHost().contains("youtube")) {
           // Log.d("first video", "first video");
            title.setText(step.getTitle());
            step.setUserCompleted(true);
            youTubePlayerView.setVisibility(View.VISIBLE);
            youtubeVideoId = videoUri.getQueryParameter("v");
            youTubePlayerView.initialize(Constants.GOOGLE_API_KEY, this);
        } else {
            //Log.d("second video", "second video");
            video.setVisibility(View.VISIBLE);
            //video.setFullscreen(false);
            if (offline) {
                if (step.getUrl() != null && step.getUrl().contains("http://")) {
                    String fileName = step.getUrl().substring(step.getUrl().lastIndexOf('/') + 1, step.getUrl().length());
                    String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                    File file = new File(savedFile);
                    if (file.exists()) {
                        videoUri = Uri.parse(savedFile);
                        try {
                            video.setVideoURI(videoUri);
                           // Log.d("Downloaded Video", Constants.FOLDER_PATH(challengeSlug, fileName));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Util.makeToast(context, context.getResources().getString(R.string.item_step_video_not_found));
                    }
                }
            } else {
               // Log.d("third video", "third video");
                if (step.getUrl() != null && step.getUrl().contains("http://")) {
                    videoUri = Uri.parse(step.getUrl());
                    try {
                        video.setVideoURI(videoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setSurvey() {
        feedback.setVisibility(View.VISIBLE);
        title.setText(step.getQuestion());
        feedback.setFilters(new InputFilter[]{new InputFilter.LengthFilter(step.getQuestion().length())});
    }

    private void setAnagram() {
        feedback.setVisibility(View.VISIBLE);
        feedback.setHint(context.getResources().getString(R.string.item_step_answer));
        title.setText(step.getTitle());
        title.setVisibility(View.GONE);
        for (int i = 0; i < step.getTitle().length(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_anagram_letters, null);
            TextView letter = (TextView) view.findViewById(R.id.name);
            char str = step.getTitle().charAt(i);
            letter.setText(String.valueOf(str).toUpperCase());
            anagramLetters.addView(view);
        }
        InputFilter[] feedBackFilter = new InputFilter[1];
        feedBackFilter[0] = new InputFilter.LengthFilter(step.getTitle().length());
        feedback.setFilters(feedBackFilter);
    }

    private void setRiddle() {
        riddle.setVisibility(View.VISIBLE);
        title.setText(step.getTitle());

        if (step.getTotalChances() > 0)
            riddleChances.setText("Left " + step.getTotalChances() + "");
        else
            riddleChances.setText(R.string.item_step_no_chances);

        riddleFailCounts = step.getTotalChances();

        //Log.d("Riddle Right Anwser", step.getRightAnswer());

        for (int i = 0; i < step.getTotalCharacters(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_riddle_letter, null);
            TextView letter = (TextView) view.findViewById(R.id.name);
            char str = step.getRightAnswer().charAt(i);
            letter.setTag(String.valueOf(str).toUpperCase());
            riddleLetters.addView(view);
        }
    }



    @TextChange(R.id.feedback)
    void onTextChange() {
        String value = feedback.getText().toString();
        if (!Util.textIsEmpty(value)) {
            if (step.getType().equalsIgnoreCase("anagram")) {
                if (step.getRightAnswer().equalsIgnoreCase(value)) {
                    int points = step.getPoints() == -1 ? 0 : step.getPoints();
                    step.setPoints(points + step.getStepPoints());
                }
            }
            step.setUserCompleted(true);
            step.setUserFeedback(value);
        } else {
            step.setUserCompleted(false);
        }
    }

    private void setImage() {
        image.setVisibility(View.VISIBLE);
        title.setText(step.getTitle());
        String filePath = step.getUrl();
        if (offline) {
            if (filePath != null && filePath.contains("http")) {
                String fileName = step.getUrl().substring(step.getUrl().lastIndexOf('/') + 1, step.getUrl().length());
                String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    image.setImageURI(imageURI);
                } else {
                    Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                }

            }
        } else {
            if (filePath != null && filePath.contains("http")) {
                imageLoader.DisplayImage(filePath, image);
            }
        }
    }

    private void deselectAnswersGrid() {
        for (int i = 0; i < gridOptions.size(); i++) {
            Button check = gridOptions.get(i);
            check.setSelected(false);
        }
    }

    private void deselectAnswers() {
        for (int i = 0; i < answerContainer.getChildCount(); i++) {
            View answerView = answerContainer.getChildAt(i);
            Button check = (Button) answerView.findViewById(R.id.check);
            check.setSelected(false);
        }
    }

    private void captureMultiselectSurveyAnswers() {
        multiSelectSurveyAnswers = new ArrayList<>();
        for (int i = 0; i < answerContainer.getChildCount(); i++) {
            View answerView = answerContainer.getChildAt(i);
            Button check = (Button) answerView.findViewById(R.id.check);
            if (check.isSelected()) {
                multiSelectSurveyAnswers.add(answers.get(i).getId());
            }
        }

        if (multiSelectSurveyAnswers.size() > 0) {
            step.setUserCompleted(true);
            step.setMultiSelectSurveyAnswers(multiSelectSurveyAnswers);
           // Log.d("Multiselect Surveys", Util.convertToCommaSeparated(multiSelectSurveyAnswers));
        } else
            step.setUserCompleted(false);
    }

    public boolean onBackPressed() {

        if (step.getType().equalsIgnoreCase(StepType.VIDEO)) {
            Uri videoUri = Uri.parse(step.getUrl());
            if (videoUri.getHost().contains("youtube")) {
                if (youTubePlayer != null) {
                    youTubePlayer.release();
                    youTubePlayer = null;
                }
                return true;
            } else {
                if (video.isFullscreen()) {
                    video.setFullscreen(false);
                }
                return true;
            }
        } else if (step.getType().equalsIgnoreCase(StepType.JIGSAW)) {
            if (mbCountDownTimer != null) {
                mbCountDownTimer.cancel();
                mbCountDownTimer = null;
            }
        } else if (step.getType().equalsIgnoreCase(StepType.QUIZ)) {

        } else if (step.getType().equalsIgnoreCase(StepType.POLLING)) {

        } else if (step.getType().toLowerCase().contains(StepType.ANAGRAM)) {

        } else if (step.getType().toLowerCase().contains(StepType.ADURI)) {
            if (!step.getAdUrl().contains("http://res.cloudinary.com/merabreak-media/image/upload/v1/advertisement/")) {
                Uri videoUri = Uri.parse(step.getAdUrl());
                if (videoUri.getHost().contains("youtube")) {
                    if (youTubePlayer != null) {
                        youTubePlayer.release();
                        youTubePlayer = null;
                    }
                    return true;
                } else {
                    if (video.isFullscreen()) {
                        video.setFullscreen(false);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void reset() {
        if (step.getType().equalsIgnoreCase(StepType.VIDEO)) {
            Uri videoUri = Uri.parse(step.getUrl());
            if (videoUri.getHost().contains("youtube")) {
                if (youTubePlayer != null) {
                    youTubePlayer.release();
                    youTubePlayer = null;
                }
            } else {
                if (video.isPlaying()) {
                    video.stop();
                }
            }
        } else if (step.getType().equalsIgnoreCase(StepType.JIGSAW)) {
            if (mbCountDownTimer != null) {
                mbCountDownTimer.cancel();
                mbCountDownTimer = null;
            }
        } else if (step.getType().equalsIgnoreCase(StepType.QUIZ)) {

        } else if (step.getType().equalsIgnoreCase(StepType.POLLING)) {

        } else if (step.getType().toLowerCase().contains(StepType.ANAGRAM)) {

        } else if (step.getType().toLowerCase().contains(StepType.ADURI)) {
            if (!step.getAdUrl().contains("http://res.cloudinary.com/merabreak-media/image/upload/v1/advertisement/")) {
                Uri videoUri = Uri.parse(step.getAdUrl());
                if (videoUri.getHost().contains("youtube")) {
                    if (youTubePlayer != null) {
                        youTubePlayer.release();
                        youTubePlayer = null;
                    }
                } else {
                    if (video.isPlaying()) {
                        video.stop();
                    }
                }
            }
        }
    }

    public boolean isUserCompleted() {
        if (step.isUserCompleted())
            reset();
        return step.isUserCompleted();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        this.youTubePlayer = youTubePlayer;
        //  youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        //  youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        //   youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

        if (youtubeVideoId != null) {
            if (b) {
                youTubePlayer.play();
            } else {
                youTubePlayer.loadVideo(youtubeVideoId);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog((Activity) context, RECOVERY_DIALOG_REQUEST).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.item_step_error_load_video), Toast.LENGTH_LONG).show();
        }
    }

    private void searchMachines(String search) {

        List<String> allData = new ArrayList<String>();
        List<String> tempSearchedList = new ArrayList<String>();

        for (int i = 0; i < allData.size(); i++) {
            if (allData.get(i).toLowerCase().contains(search.toLowerCase())) {
                tempSearchedList.add(allData.get(i));
            }
        }

        String[] splitSearchedString = search.split(" ");

        for (int i = 0; i < splitSearchedString.length; i++) {
            for (int j = 0; j < allData.size(); j++) {
                if (allData.get(j).toLowerCase().contains(splitSearchedString[i].toLowerCase())) {
                    tempSearchedList.add(allData.get(j));
                }
            }
        }

        ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();

    }

    public void validateRiddle(String letter) {

        if (step.isUserCompleted()) {
            ((ChallengePlayActivity) context).onNext();
            return;
        }

        for (int i = 0; i < step.getRightAnswer().length(); i++) {
            View view = riddleLetters.getChildAt(i);
            TextView tv = (TextView) view.findViewById(R.id.name);
            if (tv.getText().toString().equalsIgnoreCase("")) {
                if (tv.getTag().toString().equalsIgnoreCase(letter.toLowerCase())) {
                    riddleSuccessCounts++;
                    isRiddleRightGuess = true;
                    tv.setText(letter);
                    break;
                }
            }
        }

        if (isRiddleRightGuess) {
            isRiddleRightGuess = false;
        } else {

            String ltrs = wrongRiddleLetters.getText().toString();

            if (ltrs.equalsIgnoreCase(""))
                wrongRiddleLetters.setText(letter);
            else {
                wrongRiddleLetters.setText(ltrs + " " + letter);
            }
            riddleFailCounts--;
        }

        riddleChances.setText("Left " + riddleFailCounts + "");

        if (riddleFailCounts <= 0) {
            riddleChances.setVisibility(View.GONE);
            step.setUserCompleted(true);
            step.setRiddleSolved(false);
            ((ChallengePlayActivity) context).onNext();
            //Log.d("Riddle", "Failed to solve");
        }

        if (riddleSuccessCounts >= step.getTotalCharacters()) {
            int points = step.getPoints() == -1 ? 0 : step.getPoints();
            step.setPoints(points + step.getStepPoints());
            step.setUserCompleted(true);
            step.setRiddleSolved(true);
            ((ChallengePlayActivity) context).onNext();
            //Log.d("Riddle", "Solved");
        }

    }

    private void setMemoryGame() {
        title.setText(step.getTitle());
        memorygame_preloader.setVisibility(View.GONE);
        grid_main.setVisibility(View.VISIBLE);

        int numColumns = step.getColumns();
        int numRows = step.getRows();

        total = numColumns * numRows;

        buttons = new MemoryButton[total];

        imageLocation = new int[total];

        suffleImages();

        int counter = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                //memoryButton = new MemoryButton(context, r, c, allImages[imageLocation[r * numColumns + c]]);
               /* if(memImageCounter>=allImages.size()){
                    break;
                }*/
                memoryButton = new MemoryButton(context, r, c, allImages.get(imageLocation[r * numColumns + c]), counter);
                // memImageCounter++;
                memoryButton.setId(View.generateViewId());
                buttons[r * numColumns + c] = memoryButton;
                grid_main.addView(memoryButton);

                memoryButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isBusy) {
                            return;
                        }

                        MemoryButton button = (MemoryButton) v;

                        if (button.isMatched) {
                            return;
                        }

                        if (selectedImage1 == null) {
                            selectedImage1 = button;
                            selectedImage1.flip();
                            return;
                        }

                        if (selectedImage1.getId() == button.getId()) {
                            return;
                        }

                        //Log.v("slected and butonimages", "Vinay selectedImage1[" + selectedImage1.getShowingImageId() + "]button[" + button.getShowingImageId() + "]");
                        if (selectedImage1.getShowingImageId().equals(button.getShowingImageId())) {//selectedImage1.getShowingImageId() == button.getShowingImageId()) {
                            button.flip();
                            button.setIsMatched(true);
                            selectedImage1.setIsMatched(true);
                            button.setEnabled(false);
                            selectedImage1.setEnabled(false);
                            selectedImage1 = null;

                            memoryCounter++;
                            if (memoryCounter >= allImages.size()) {//.length) {
                                // show win message here
                                //  Toast.makeText(context, "done...!", Toast.LENGTH_SHORT).show();
                                int points = step.getPoints() == -1 ? 0 : step.getPoints();
                                step.setPoints(points + step.getStepPoints());
                                step.setUserCompleted(true);
                                step.setMemoryGameSolved(true);
                                ((ChallengePlayActivity) context).onNext();
                                return;
                            }

                            return;
                        } else {
                            selectedImage2 = button;
                            selectedImage2.flip();
                            isBusy = true;

                            final Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    selectedImage2.flip();
                                    selectedImage1.flip();
                                    selectedImage1 = null;
                                    selectedImage2 = null;
                                    isBusy = false;
                                }
                            }, 500);
                        }
                    }
                });
            }
        }

    }

    protected void suffleImages() {
        Random random = new Random();

        for (int i = 0; i < total; i++) {
            imageLocation[i] = i % (total / 2);
        }

        for (int i = 0; i < total; i++) {
            int temp = imageLocation[i];
            int swap = random.nextInt(total);
            imageLocation[i] = imageLocation[swap];
            imageLocation[swap] = temp;
        }
    }

    class DownloadImagesForMemoryGame extends AsyncTask<String, String, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {
            Drawable drawable;
            try {
                java.net.URL url = new java.net.URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                memGameBitmap = BitmapFactory.decodeStream(input);
                drawable = new BitmapDrawable(context.getResources(), memGameBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable file_url) {
        }
    }

    private void setJigsawPuzzle() {
        //title.setText(step.getTitle());
        // title.setTextColor(getResources().getColor(R.color.white));
        title.setVisibility(View.GONE);
        String filePath = step.getUrl();

        if (offline) {
            if (filePath != null && filePath.contains("http")) {
                String fileName = step.getUrl().substring(step.getUrl().lastIndexOf('/') + 1, step.getUrl().length());
                String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    filePath = imageURI.toString();

                    try {
                        slidePuzzle = new SlidePuzzle();
                        view = new SlidePuzzleView(context, slidePuzzle, this);
                        puzzleImage.addView(view);
                        puzzleContainer.setVisibility(View.VISIBLE);

                        FileInputStream fileInputStream = new FileInputStream(file);
                        puzzleBitmap = BitmapFactory.decodeStream(fileInputStream);
                        setBitmap(puzzleBitmap);
                        puzzlePreloader.setVisibility(View.GONE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                    }
                } else {
                    Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                }

            }
        } else {
            slidePuzzle = new SlidePuzzle();
            view = new SlidePuzzleView(context, slidePuzzle, this);
            puzzleImage.addView(view);
            puzzleContainer.setVisibility(View.VISIBLE);

            shuffle();
            setPuzzleSize(DEFAULT_SIZE, true);

            targetWidth = view.getTargetWidth();
            targetHeight = view.getTargetHeight();

            new DownloadImageFromURL().execute(filePath);
        }

    }

    private void shuffle() {
        slidePuzzle.init(puzzleWidth, puzzleHeight, step.getColumns(), step.getRows());
        // preview image for few seconds
        setPuzzlePreviewTimer();
//        slidePuzzle.shuffle();
        view.invalidate();
        expert = view.getShowNumbers() == SlidePuzzleView.ShowNumbers.NONE;
    }

    public BitmapFactory.Options getBitmapOptions() {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        if (o.outWidth > o.outHeight && targetWidth < targetHeight) {
            int i = targetWidth;
            targetWidth = targetHeight;
            targetHeight = i;
        }

        if (targetWidth < o.outWidth || targetHeight < o.outHeight) {
            double widthRatio = (double) targetWidth / (double) o.outWidth;
            double heightRatio = (double) targetHeight / (double) o.outHeight;
            double ratio = Math.max(widthRatio, heightRatio);

            o.inSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(ratio) / Math.log(0.5)));
        } else {
            o.inSampleSize = 1;
        }

        o.inScaled = false;
        o.inJustDecodeBounds = false;

        return o;
    }

    private void setBitmap(Bitmap bitmap) {
        portrait = bitmap.getWidth() < bitmap.getHeight();

        view.setBitmap(bitmap);
        setPuzzleSize(DEFAULT_SIZE, true);
//        setRequestedOrientation(portrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private float getImageAspectRatio() {
        Bitmap bitmap = view.getBitmap();

        if (bitmap == null) {
            return 1;
        }

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        return width / height;
    }

    protected void setPuzzleSize(int size, boolean scramble) {

        float ratio = getImageAspectRatio();

        //Log.d("getImageAspectRatio1", ratio + "");


        if (ratio < 1) {
            ratio = 1f / ratio;
        }

        //Log.d("getImageAspectRatio2", ratio + "");

        int newWidth;
        int newHeight;

        if (portrait) {
            newWidth = size;
            newHeight = (int) (size * ratio);
            //Log.d("getImageAspectRatio3", newWidth + " : " + newHeight);
        } else {
            newWidth = (int) (size * ratio);
            newHeight = size;
            //Log.d("getImageAspectRatio4", newWidth + " : " + newHeight);
        }

        if (scramble || newWidth != puzzleWidth || newHeight != puzzleHeight) {
            puzzleWidth = newWidth;
            puzzleHeight = newHeight;
            shuffle();
            //Log.d("getImageAspectRatio4", puzzleWidth + " : " + puzzleHeight);
        }
    }

    @Override
    public void isSolved() {
        int points = step.getPoints() == -1 ? 0 : step.getPoints();
        step.setPoints(points + step.getStepPoints());
        step.setUserCompleted(true);
        step.setPuzzleSolved(true);
        ((ChallengePlayActivity) context).onNext();
    }

    class DownloadImageFromURL extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                java.net.URL url = new java.net.URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                puzzleBitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            //Util.makeToast(context,"images downloading");
            setBitmap(puzzleBitmap);
            puzzlePreloader.setVisibility(View.GONE);
        }
    }

    private void setPuzzlePreviewTimer() {
        long totalMillis = 3 * 1000;
        //Log.d("Total Millis", totalMillis + "");
        mbCountDownTimer = new MBCountDownTimer(totalMillis, 1000, this);
        mbCountDownTimer.start();
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        if (mbCountDownTimer != null)
            mbCountDownTimer.cancel();
        mbCountDownTimer = null;
        slidePuzzle.shuffle();
        view.invalidate();
    }
}