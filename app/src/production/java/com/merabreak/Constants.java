package com.merabreak;

import com.merabreak.models.challenge.Challenge;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public class Constants {
    public static final long ASYNC_LOAD_TIME = 2 * 1000;
    public static final String BASE_URL = "https://ws.merabreak.com";
    public static final String WEB_BASE_URL = "http://dev.merabreak.com";
    public static final String BASE_URL_GOOGLE = "https://maps.googleapis.com";
    public static final String TERMS_PRIVACY_BASE_URL = "https://www.merabreak.com";

    public static final String GOOGLE_APP_API_BROWSER_KEY = "AIzaSyBLsLHfvTwlKHch2TbE9Ucba03fC59ZHrQ";    //old - AIzaSyAsLeyeKHCFjW4oejcDoIk0tyNJM9SgumQ

    //785952948493-ge1qi7kk9kjfa40j4gfj7vogiau8dect.apps.googleusercontent.com // google app client id
    //U1CpZZCe-bhmNFHad-S3qATY

    public static final String MB_API_KEY = "merabreak";

    public static final int REQUEST_CODE_USER_PHOTO = 111;
    public static final int REQUEST_CODE_CAMERA_PHOTO = 112;
    public static final int REQUEST_CODE_ERROR = 404;

    public static final String GCM_INTENT_SERVICE = "GcmIntentService";
    public static final String GCM_DELETED_MESSAGE = "Deleted messages on server: ";
    public static final String EXTRA_MESSAGE = "message";
    public static final String GCM_SEND_ERROR = "Send error: ";
    public static final String GCM_RECEIVED = "Received: ";
    public static final String GCM_NOTIFICATION = "GCM Notification";
    public static final String NEW_PUSH_EVENT = "new-push-event";

    public static final String GCM_SENDER_ID = "419461069305";   //old - 186338338397
    public static final int GCM_NOTIFICATION_ID = 1111;

    public static String AF_DEV_KEY = "XJqew5Bx2Kbg7oBwFSX4ZN";
    public static final String GCM_API_KEY = "AIzaSyD-NYUGKOyIIh6F44A5v7YKEGKTd4QkaaM";

    public static final int GCM_MESSAGE_NOTIFICATION_ID = 1111;
    public static final int ADDRESS_UPDATE = 1001;
    public static String USER_AUTH_TOKEN = "";

    public static String FACEBOOK_GRAPH_URL = "https://graph.facebook.com/v2.6";
    public static final String FACEBOOK_PROFILE_API = "cover,id,name,picture.type(square).height(480).width(480),email,gender,first_name,last_name";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "com.merabreak";

    public static final String[] SMS_SENDER_IDs = {"mbreak"};

    public static String CHALLENGES = "{\"title\":\"Employee Satisfaction\",\"slug\":\"employee-satisfaction\",\"description\":\"Employees can rate their satisfaction with their jobs using this 14-question survey, which includes questions about salary, leadership, and stress.\",\"backgroundImage\":\"http:\\/\\/cadmin.merabreak.com\\/assets\\/images\\/challenges\\/challenge_background\\/1458551806s8t3T.jpg\",\"coinsRequire\":5,\"termsCondition\":\"\",\"timeLimit\":120,\"rules\":\"-\",\"offline\":\"1\",\"category\":[{\"slug\":\"people\",\"title\":\"People\"}],\"steps\":[{\"id\":\"u9gq0-7zk9h-a9uml-skkbb-awdck\",\"type\":\"Quiz\",\"slug\":\"quiz\",\"questionId\":\"bsywh-oiwsu-suas9-1d1yg-wdely-gkcjt\",\"question\":\"How meaningful is your work?\",\"view\":\"\",\"answers\":[{\"id\":\"aetdy-nba44-kqipo-kacyw-pwdiv-f5k18\",\"title\":\"Extremely meaningful\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"5toae-hmrno-wi67g-leo8o-xofjp-awthv\",\"title\":\"Very meaningful\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"qdjxu-avxiw-hvqpe-ufjuo-nrkgz-hx4cn\",\"title\":\"Moderately meaningful\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"mrdr4-e0wse-wuvap-jrsph-3v9kg-pdiws\",\"title\":\"Slightly meaningful\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"mdyxx-8qj6d-y8y4u-k8xlw-ksyjo-aievm\",\"title\":\"Not at all meaningful\",\"url\":\"\",\"type\":\"Text\"}]},{\"id\":\"erl41-ng6yg-d5eoj-2tl1r-f0nkr\",\"type\":\"Quiz\",\"slug\":\"quiz\",\"questionId\":\"0ggqm-jt5cn-2jail-bsynk-2c7sj-4pvrt\",\"question\":\"How challenging is your job?\",\"view\":\"\",\"answers\":[{\"id\":\"o0jlz-6psjf-ehxho-peujm-s1zvq-f6qhn\",\"title\":\"Extremely challenging\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"6fbld-im12x-oq4eu-ljcqz-vgrgg-bchfm\",\"title\":\"Very challenging\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"rk4fy-elcqv-xtej6-mykqy-76vqf-7uyxl\",\"title\":\"Moderately challenging\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"za0oc-cttcu-r9har-hqro9-lzgcd-futbw\",\"title\":\"Slightly challenging\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"z8izh-hbjoy-apbdw-wy8ik-tgqhb-786fy\",\"title\":\"Not at all challenging\",\"url\":\"\",\"type\":\"Text\"}]},{\"id\":\"z3scj-vzepw-fvawo-cm4cr-s9gxr\",\"type\":\"Quiz\",\"slug\":\"quiz\",\"questionId\":\"zofhb-fryej-6h5bn-41mtv-3byc1-fu6xt\",\"question\":\"How realistic are the expectations of your supervisor?\",\"view\":\"\",\"answers\":[{\"id\":\"fokxr-ylcz8-tqvfo-zafyd-ur0jl-8h9z7\",\"title\":\"Extremely realistic\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"nnd7m-2lnau-iryt7-j8guy-dgpfv-bwbog\",\"title\":\"Very realistic\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"h9vvp-tox22-eyjc3-kz4ar-cfnpm-ew40v\",\"title\":\"Moderately realistic\",\"url\":\"\",\"type\":\"Text\"},{\"id\":\"xdp4p-w9fci-lua2d-acmha-va8bo-ficfv\",\"title\":\"Slightly realistic\",\"url\":\"\",\"type\":\"Text\"}]},{\"id\":\"qwfyy-zldpt-oaxod-mulyl-jsc5w\",\"type\":\"Video\",\"slug\":\"videos\",\"videoId\":\"cabnx-j1pd0-vlam7-bzgpv-j84sl-4rsom\",\"title\":\"Find your office people in this video?\",\"description\":\"\",\"url\":\"http:\\/\\/www.sample-videos.com\\/video\\/mp4\\/480\\/big_buck_bunny_480p_2mb.mp4\"},{\"id\":\"i2d5v-rjakn-zkxsc-e2ikd-e1o5g\",\"type\":\"Jigsaw\",\"slug\":\"jigsaw\",\"jigsawId\":\"a3jdr-u6hal-29dvx-jwh4r-hzlf1-cr8fq\",\"title\":\"Check how familiar are you with your office by rearranging this grid?\",\"url\":\"http:\\/\\/cadmin.merabreak.com\\/assets\\/images\\/challenges\\/jigsaw\\/1458545534V5e3N.jpg\",\"columns\":3,\"rows\":3}]}";

    public static String FOLDER_PATH(String challengeSlug, String fileName) {
        return FileDownloadUtils.getDefaultSaveRootPath() + "/" + challengeSlug.replaceAll("-", ".") + "/" + fileName;
    }

    public static String FOLDER_PATH(String challengeSlug) {
        return FileDownloadUtils.getDefaultSaveRootPath() + "/" + challengeSlug.replaceAll("-", ".");
    }

    public static String GOOGLE_API_KEY = "AIzaSyDuWYpT_oBhquOnqZWMaQpkLD2Gpg9Zrs0";        //old - AIzaSyDrECfAzx4zsRmlaIxhNKk8vsZc2Or2aXk

    public static Challenge CHALLENGE = null;

    public static final String PHOTO_FOLDER_NAME = "merabreak";
    public static final String SOME_THING_WRONG = "Something went wrong. Please try again.";

}
