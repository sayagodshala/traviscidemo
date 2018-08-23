package com.merabreak.network;

import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.Circles;
import com.merabreak.models.Cities;
import com.merabreak.models.Coin;
import com.merabreak.models.DownloadedChallenges;
import com.merabreak.models.GSAdLink;
import com.merabreak.models.GaneshaSpeaks;
import com.merabreak.models.HomeCategory;
import com.merabreak.models.HomeRechargePlans;
import com.merabreak.models.MobileDetails;
import com.merabreak.models.NotificationModel;
import com.merabreak.models.Operators;
import com.merabreak.models.PaginationChallenges;
import com.merabreak.models.PointsWallet;
import com.merabreak.models.ProfileDetails;
import com.merabreak.models.Raffle;
import com.merabreak.models.RafflesResponseModel;
import com.merabreak.models.RedeemVoucherModel;
import com.merabreak.models.ReferralText;
import com.merabreak.models.ShippingAddress;
import com.merabreak.models.SimpleObject;
import com.merabreak.models.SpinWheelDetails;
import com.merabreak.models.StartChallenge;
import com.merabreak.models.StepSaveResponse;
import com.merabreak.models.Store;
import com.merabreak.models.StripParams;
import com.merabreak.models.TotalRaffleStore;
import com.merabreak.models.User;
import com.merabreak.models.UserImageUploadResponse;
import com.merabreak.models.VoucherModel;
import com.merabreak.models.VoucherResponseModel;
import com.merabreak.models.challenge.Astrology;
import com.merabreak.models.challenge.Category;
import com.merabreak.models.challenge.Challenge;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Saya Godshala on 1/13/2016.
 */

public interface APIService {
    
    public static String AUTH_TOKEN = "Authorization";//"authToken";
    public static String MB_API_KEY = "MB-API-KEY";

    @FormUrlEncoded
    @POST("/guestuser/confirm_otp")
    Call<APIResponse<User>> confirmOtp(
            @Header(MB_API_KEY) String apiKey,
            @Field("mobile") String mobile,
            @Field("otpCode") String otpCode);

    @FormUrlEncoded
    @POST("/user/social/confirm_otp")
    Call<APIResponse<User>> socialConfirmOtp(
            @Header(MB_API_KEY) String apiKey,
            @Field("mobile") String mobile,
            @Field("otpCode") String otpCode);


    @FormUrlEncoded
    @POST("/user/register_mobile")
    Call<APIResponse> registerMobile(
            @Header(MB_API_KEY) String apiKey,
            @Field("mobile") String mobile,
            @Field("deviceId") String deviceId);

    @FormUrlEncoded
    @POST("/user/login")
    Call<APIResponse<User>> login(@Header(MB_API_KEY) String apiKey,
                                  @Field("mobile") String email,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/logout")
    Call<APIResponse> logout(@Header(MB_API_KEY) String apiKey,
                             @Header(AUTH_TOKEN) String logout,
                             @Field("mobile") String mobile,
                             @Field("gcmToken") String gcmToken);

    @FormUrlEncoded
    @POST("/user/device_info_update")
    Call<APIResponse> deviceInfoUpdate(@Header(MB_API_KEY) String apiKey,
                                       @Header(AUTH_TOKEN) String authToken,
                                       @Field("gcmToken") String gcmToken,
                                       @Field("deviceId") String deviceId,
                                       @Field("deviceType") String deviceType,
                                       @Field("manufacturer") String manufacturer,
                                       @Field("appVersion") String appVersion,
                                       @Field("osVersion") String osVersion,
                                       @Field("modelNumber") String modelNumber,
                                       @Field("simCardName") String simCardName,
                                       @Field("versionCode") String versionCode,
                                       @Field("contacts") String contacts,
                                       @Field("playerId") String playerId);

    @FormUrlEncoded
    @POST("/user/device_info_update")
    Call<APIResponse> deviceInfoUpdate(@Header(MB_API_KEY) String apiKey,
                                       @Header(AUTH_TOKEN) String authToken,
                                       @Field("gcmToken") String gcmToken,
                                       @Field("deviceId") String deviceId,
                                       @Field("deviceType") String deviceType,
                                       @Field("manufacturer") String manufacturer,
                                       @Field("appVersion") String appVersion,
                                       @Field("osVersion") String osVersion,
                                       @Field("modelNumber") String modelNumber,
                                       @Field("simCardName") String simCardName,
                                       @Field("versionCode") String versionCode,
                                       @Field("contacts") String contacts,
                                       @Field("playerId") String playerId,
                                       @Field("lat") String lat,
                                       @Field("lng") String lng);

    @FormUrlEncoded
    @POST("/user/device_info_update")
    Call<APIResponse> deviceInfoUpdate(@Header(MB_API_KEY) String apiKey,
                                       @Header(AUTH_TOKEN) String authToken,
                                       @Field("gcmToken") String gcmToken,
                                       @Field("deviceId") String deviceId,
                                       @Field("deviceType") String deviceType,
                                       @Field("manufacturer") String manufacturer,
                                       @Field("appVersion") String appVersion,
                                       @Field("osVersion") String osVersion,
                                       @Field("modelNumber") String modelNumber,
                                       @Field("simCardName") String simCardName,
                                       @Field("versionCode") String versionCode,
                                       @Field("contacts") String contacts,
                                       @Field("playerId") String playerId,
                                       @Field("cityId") String cityId);

    @FormUrlEncoded
    @POST("/user/edit_profile")
    Call<APIResponse> editProfile(@Header(MB_API_KEY) String apiKey,
                                  @Header(AUTH_TOKEN) String authToken,
                                  @Field("fullName") String fullName,
                                  @Field("email") String email,
                                  @Field("about") String about,
                                  @Field("dob") String dob,
                                  @Field("gender") String gender,
                                  @Field("zipCode") String zipCode,
                                  @Field("state") String state,
                                  @Field("city") String city,
                                  @Field("image") String image,
                                  @Field("location") String location);

    @FormUrlEncoded
    @POST("/user/details")
    Call<APIResponse<User>> updateDetails(@Header(MB_API_KEY) String apiKey,
                                          @Header(AUTH_TOKEN) String authToken,
                                          @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("/user/social_login")
    Call<APIResponse<User>> facebookLogin(@Header(MB_API_KEY) String apiKey,
                                          @Header(AUTH_TOKEN) String authToken,
                                          @Field("accessToken") String accessToken,
                                          @Field("applicationId") String applicationId,
                                          @Field("mobile") String mobile);

//    @FormUrlEncoded
//    @POST("/user/social_login")
//    Call<APIResponse<User>> facebookLogin(@Header(MB_API_KEY) String apiKey,
//                                          @Field("accessToken") String accessToken,
//                                          @Field("applicationId") String applicationId);
//
//    @FormUrlEncoded
//    @POST("/user/social_register")
//    Call<APIResponse<User>> facebookLoginConfirm(@Header(MB_API_KEY) String apiKey,
//                                                 @Header(AUTH_TOKEN) String authToken,
//                                                 @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/signup")
    Call<APIResponse<User>> formSignup(@Header(MB_API_KEY) String apiKey,
                                       @Header(AUTH_TOKEN) String authToken,
                                       @Field("fullName") String fullName,
                                       @Field("email") String email,
                                       @Field("mobile") String mobile,
                                       @Field("password") String password,
                                       @Field("gender") String gender,
                                       @Field("dob") String dob);

    @FormUrlEncoded
    @POST("/user/signup")
    Call<APIResponse<User>> newSignup(@Header(MB_API_KEY) String apiKey,
                                      @Field("userId") String userId,
                                      @Field("fullName") String fullName,
                                      @Field("email") String email,
                                      @Field("mobile") String mobile,
                                      @Field("password") String password,
                                      @Field("gender") String gender,
                                      @Field("dob") String dob,
                                      @Field("referrer") String referrer,
                                      @Field("promo_code") String promo_code);

    @FormUrlEncoded
    @POST("/user/mobileAuth/socialsignup")
    Call<APIResponse> authSocialSignup(@Header(MB_API_KEY) String apiKey,
                                       @Field("fullName") String fullName,
                                       @Field("email") String email,
                                       @Field("mobile") String mobile,
                                       @Field("gender") String gender,
                                       @Field("dob") String dob,
                                       @Field("image") String image);

    @FormUrlEncoded
    @POST("/user/mobileAuth/socialsignup")
    Call<APIResponse<User>> newAuthSocialSignup(@Header(MB_API_KEY) String apiKey,
                                                @Field("fullName") String fullName,
                                                @Field("email") String email,
                                                @Field("mobile") String mobile,
                                                @Field("image") String image,
                                                @Field("userId") String userId,
                                                @Field("referrer") String referrer,
                                                @Field("promo_code") String promo_code);

    @FormUrlEncoded
    @POST("/user/guest/socialsignup")
    Call<APIResponse<User>> socialSignup(@Header(MB_API_KEY) String apiKey,
                                         @Header(AUTH_TOKEN) String authToken,
                                         @Field("fullName") String fullName,
                                         @Field("email") String email,
                                         @Field("mobile") String mobile,
                                         @Field("gender") String gender,
                                         @Field("dob") String dob,
                                         @Field("image") String image);

    @FormUrlEncoded
    @POST("/user/facebook_register")
    Call<APIResponse<User>> facebookRegister(@Header(MB_API_KEY) String apiKey,
                                             @Field("accessToken") String accessToken,
                                             @Field("applicationId") String applicationId);


    @FormUrlEncoded
    @POST("/user/change_password")
    Call<APIResponse> changePassword(@Header(MB_API_KEY) String apiKey,
                                     @Header(AUTH_TOKEN) String authToken,
                                     @Field("oldPassword") String oldPassword,
                                     @Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("/user/fcm_info_update")
    Call<APIResponse> sendTokenToServer(@Header(MB_API_KEY) String apiKey,
                                        @Header(AUTH_TOKEN) String authToken,
                                        @Field("fcmToken") String fcmToken,
                                        @Field("deviceId") String deviceId);

    @FormUrlEncoded
    @POST("/user/forgot_password_code")
    Call<APIResponse> forgotPasswordConfirmationCode(@Header(MB_API_KEY) String apiKey,
                                                     @Field("mobile") String email);

    @FormUrlEncoded
    @POST("/user/forgot_password_update")
    Call<APIResponse> forgotPasswordUpdate(@Header(MB_API_KEY) String apiKey,
                                           @Field("confirmationCode") String confirmationCode,
                                           @Field("mobile") String mobile,
                                           @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/mobile/forgot_password_update")
    Call<APIResponse> forgotPassword(@Header(MB_API_KEY) String apiKey,
                                     @Field("mobile") String mobile,
                                     @Field("confirmationCode") String confirmationCode);

    @GET("/raffle/lists/{categorySlug}")
    Call<APIResponse<List<Raffle>>> getRaffles(
            @Header(MB_API_KEY) String apiKey,
            @Header(AUTH_TOKEN) String authToken,
            @Path("categorySlug") String slug);

    @GET("/raffle/lists")
    Call<APIResponse<List<Raffle>>> getRaffles(
            @Header(MB_API_KEY) String apiKey,
            @Header(AUTH_TOKEN) String authToken);

    @GET("/raffle/lists")
    Call<APIResponse<List<Raffle>>> getRaffles();

    @GET("/store/lists")
    Call<APIResponse<List<Store>>> getStores(
            @Header(MB_API_KEY) String apiKey,
            @Header(AUTH_TOKEN) String authToken);

    @GET("/store/lists/{categorySlug}")
    Call<APIResponse<List<Store>>> getStorePaginationData(
            @Header(MB_API_KEY) String apiKey,
            @Header(AUTH_TOKEN) String authToken,
            @Path("categorySlug") String slug,
            @Query("page") int page,
            @Query("limit") int limit
            );


    @GET("/store/lists") // unused
    Call<APIResponse<List<Store>>> getStores();

    @GET("/store/detail")
    Call<APIResponse<Store>> getStoreDetails(
            @Header(MB_API_KEY) String apiKey,
            @Header(AUTH_TOKEN) String authToken,
            @Query("slug") String storeSlug);


    @GET("/raffle/detail")
    Call<APIResponse<Raffle>> getRaffleDetails(
            @Header(MB_API_KEY) String apiKey,
            @Header(AUTH_TOKEN) String authToken,
            @Query("slug") String raffleSlug);

    //Challenges Related API's

    @GET("/challenge/category/list")
    Call<APIResponse<List<Category>>> getCategories(@Header(MB_API_KEY) String apiKey,
                                                    @Header(AUTH_TOKEN) String authToken);

    @GET("/challenge/category/list")
    Call<APIResponse<List<Category>>> getCategories(@Header(MB_API_KEY) String apiKey,
                                                    @Header(AUTH_TOKEN) String authToken,
                                                    @Query("lat") String lat,
                                                    @Query("lng") String lng);


    @GET("/challenge/category/list")
    Call<APIResponse<List<Category>>> getCategories(@Header(MB_API_KEY) String apiKey,
                                                    @Header(AUTH_TOKEN) String authToken,
                                                    @Query("cityId") String cityId);


    @GET("/challenge/tab/list")
    Call<APIResponse<List<HomeCategory>>> getChallengeWithTabs(@Header(MB_API_KEY) String apiKey,
                                                               @Header(AUTH_TOKEN) String authToken,
                                                               @Query("deviceDPI") String deviceDPI);

    @GET("/challenge/tab/list")
    Call<APIResponse<List<HomeCategory>>> getChallengeWithTabs(@Header(MB_API_KEY) String apiKey,
                                                               @Header(AUTH_TOKEN) String authToken,
                                                               @Query("lat") String lat,
                                                               @Query("lng") String lng,
                                                               @Query("deviceDPI") String deviceDPI);

    @GET("/challenge/tab/list")
    Call<APIResponse<List<HomeCategory>>> getChallengeWithTabs(@Header(MB_API_KEY) String apiKey,
                                                               @Header(AUTH_TOKEN) String authToken,
                                                               @Query("cityId") String cityId,
                                                               @Query("deviceDPI") String deviceDPI);

    @GET("/challenge/list/{category}")
    Call<APIResponse<List<Challenge>>> getChallengesAsPerCategory(@Header(MB_API_KEY) String apiKey,
                                                                  @Header(AUTH_TOKEN) String authToken,
                                                                  @Path("category") String category,
                                                                  @Query("deviceDPI") String deviceDPI);

    @GET("/challenge/list/{category}")
    Call<APIResponse<List<Challenge>>> getChallengesAsPerCategoryPagination(@Header(MB_API_KEY) String apiKey,
                                                                            @Header(AUTH_TOKEN) String authToken,
                                                                            @Path("category") String category,
                                                                            @Query("page") int page,
                                                                            @Query("limit") int limit,
                                                                            @Query("deviceDPI") String deviceDPI);

    @GET("/challenge/filter/popular")
    Call<APIResponse<List<Challenge>>> getPopularChallenges(@Header(MB_API_KEY) String apiKey,
                                                            @Header(AUTH_TOKEN) String authToken,
                                                            @Query("lat") String lat,
                                                            @Query("lng") String lng);

    @GET("/challenge/filter/{type}")
    Call<APIResponse<List<Challenge>>> getFilteredChallenges(@Header(MB_API_KEY) String apiKey,
                                                             @Header(AUTH_TOKEN) String authToken,
                                                             @Path("type") String type);


    @GET("/challenge/filter/popular")
    Call<APIResponse<List<Challenge>>> getPopularChallenges(@Header(MB_API_KEY) String apiKey,
                                                            @Header(AUTH_TOKEN) String authToken);

    @GET("/challenge/details/{challenge}")
    Call<APIResponse<Challenge>> getChallengeDetail(@Header(MB_API_KEY) String apiKey,
                                                    @Header(AUTH_TOKEN) String authToken,
                                                    @Path("challenge") String challenge,
                                                    @Query("deviceDPI") String deviceDPI);

    @GET("/challenge/live/details/{challenge}")
    Call<APIResponse<Challenge>> getLiveChallengeDetail(@Header(MB_API_KEY) String apiKey,
                                                        @Header(AUTH_TOKEN) String authToken,
                                                        @Path("challenge") String challenge,
                                                        @Query("deviceDPI") String deviceDPI);

    @GET("/user/downloaded/challenges")
    Call<APIResponse<List<DownloadedChallenges>>> getDownChallenges(@Header(MB_API_KEY) String apiKey,
                                                                    @Header(AUTH_TOKEN) String authToken);


    @GET("/challenge/mobile/list/{category}")
    Call<APIResponse<PaginationChallenges>> getPages(@Header(MB_API_KEY) String apiKey,
                                                     @Header(AUTH_TOKEN) String authToken,
                                                     @Path("category") String category,
                                                     @Query("page") int page,
                                                     @Query("limit") int limit,
                                                     @Query("deviceDPI") String deviceDPI);

    @GET("/challenge/filter/{type}")
    Call<APIResponse<List<Challenge>>> getFilteredChallengesPagination(@Header(MB_API_KEY) String apiKey,
                                                                       @Header(AUTH_TOKEN) String authToken,
                                                                       @Path("type") String type,
                                                                       @Query("page") int page,
                                                                       @Query("limit") int limit,
                                                                       @Query("deviceDPI") String deviceDPI);


    @FormUrlEncoded
    @POST("/challenge/start")
    Call<APIResponse<StartChallenge>> setChallengeStart(@Header(MB_API_KEY) String apiKey,
                                                        @Header(AUTH_TOKEN) String authToken,
                                                        @Field("challengeId") String challengeId);

    //
    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveStep(@Header(MB_API_KEY) String apiKey,
                                                 @Header(AUTH_TOKEN) String authToken,
                                                 @Field("challengeId") String challengeId,
                                                 @Field("stepId") String stepId,
                                                 @Field("playedId") String playedId);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveStepQuiz(@Header(MB_API_KEY) String apiKey,
                                                     @Header(AUTH_TOKEN) String authToken,
                                                     @Field("challengeId") String challengeId,
                                                     @Field("stepId") String stepId,
                                                     @Field("answerId") String answerId,
                                                     @Field("playedId") String playedId);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveStepPolling(@Header(MB_API_KEY) String apiKey,
                                                        @Header(AUTH_TOKEN) String authToken,
                                                        @Field("challengeId") String challengeId,
                                                        @Field("stepId") String stepId,
                                                        @Field("optionId") String optionId,
                                                        @Field("playedId") String playedId);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveSurvey(@Header(MB_API_KEY) String apiKey,
                                                   @Header(AUTH_TOKEN) String authToken,
                                                   @Field("challengeId") String challengeId,
                                                   @Field("stepId") String stepId,
                                                   @Field("answer") String answer,
                                                   @Field("playedId") String playedId);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveAnagram(@Header(MB_API_KEY) String apiKey,
                                                    @Header(AUTH_TOKEN) String authToken,
                                                    @Field("challengeId") String challengeId,
                                                    @Field("stepId") String stepId,
                                                    @Field("answer") String answer,
                                                    @Field("playedId") String playedId);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveMultiselectSurvey(@Header(MB_API_KEY) String apiKey,
                                                              @Header(AUTH_TOKEN) String authToken,
                                                              @Field("challengeId") String challengeId,
                                                              @Field("stepId") String stepId,
                                                              @Field("answerId") String answerIds,
                                                              @Field("playedId") String playedId);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveRiddle(@Header(MB_API_KEY) String apiKey,
                                                   @Header(AUTH_TOKEN) String authToken,
                                                   @Field("challengeId") String challengeId,
                                                   @Field("stepId") String stepId,
                                                   @Field("solved") String solved,
                                                   @Field("playedId") String playedId);


    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveJigsawPuzzle(@Header(MB_API_KEY) String apiKey,
                                                         @Header(AUTH_TOKEN) String authToken,
                                                         @Field("challengeId") String challengeId,
                                                         @Field("stepId") String stepId,
                                                         @Field("completed") String solved,
                                                         @Field("playedId") String playedId);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse<StepSaveResponse>> saveMemoryGame(@Header(MB_API_KEY) String apiKey,
                                                       @Header(AUTH_TOKEN) String authToken,
                                                       @Field("challengeId") String challengeId,
                                                       @Field("stepId") String stepId,
                                                       @Field("completed") String solved,
                                                       @Field("playedId") String playedId);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveAllSteps(@Header(MB_API_KEY) String apiKey,
                                   @Header(AUTH_TOKEN) String authToken,
                                   @Field("stepId") String stepKey,
                                   @Field("challengeId") String challengeId,
                                   @Field("step_id") String stepId,
                                   @Field("widget_slug") String stepType,
                                   @Field("playedId") String playedId,
                                   @Field("last_step") String isLastStep);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveStepQuizNew(@Header(MB_API_KEY) String apiKey,
                                      @Header(AUTH_TOKEN) String authToken,
                                      @Field("stepId") String stepKey,
                                      @Field("challengeId") String challengeId,
                                      @Field("step_id") String stepId,
                                      @Field("widget_slug") String stepType,
                                      @Field("answerId") String answerId,
                                      @Field("playedId") String playedId,
                                      @Field("last_step") String isLastStep);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveStepPollingNew(@Header(MB_API_KEY) String apiKey,
                                         @Header(AUTH_TOKEN) String authToken,
                                         @Field("stepId") String stepKey,
                                         @Field("challengeId") String challengeId,
                                         @Field("step_id") String stepId,
                                         @Field("widget_slug") String stepType,
                                         @Field("optionId") String optionId,
                                         @Field("playedId") String playedId,
                                         @Field("last_step") String isLastStep);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveSurveyNew(@Header(MB_API_KEY) String apiKey,
                                    @Header(AUTH_TOKEN) String authToken,
                                    @Field("stepId") String stepKey,
                                    @Field("challengeId") String challengeId,
                                    @Field("step_id") String stepId,
                                    @Field("widget_slug") String stepType,
                                    @Field("answer") String answer,
                                    @Field("playedId") String playedId,
                                    @Field("last_step") String isLastStep);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveAnagramNew(@Header(MB_API_KEY) String apiKey,
                                     @Header(AUTH_TOKEN) String authToken,
                                     @Field("stepId") String stepKey,
                                     @Field("challengeId") String challengeId,
                                     @Field("step_id") String stepId,
                                     @Field("widget_slug") String stepType,
                                     @Field("answer") String answer,
                                     @Field("playedId") String playedId,
                                     @Field("last_step") String isLastStep);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveMultiselectSurveyNew(@Header(MB_API_KEY) String apiKey,
                                               @Header(AUTH_TOKEN) String authToken,
                                               @Field("stepId") String stepKey,
                                               @Field("challengeId") String challengeId,
                                               @Field("step_id") String stepId,
                                               @Field("widget_slug") String stepType,
                                               @Field("answerId") String answerIds,
                                               @Field("playedId") String playedId,
                                               @Field("last_step") String isLastStep);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveRiddleNew(@Header(MB_API_KEY) String apiKey,
                                    @Header(AUTH_TOKEN) String authToken,
                                    @Field("stepId") String stepKey,
                                    @Field("challengeId") String challengeId,
                                    @Field("step_id") String stepId,
                                    @Field("widget_slug") String stepType,
                                    @Field("solved") String solved,
                                    @Field("playedId") String playedId,
                                    @Field("last_step") String isLastStep);


    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveJigsawPuzzleNew(@Header(MB_API_KEY) String apiKey,
                                          @Header(AUTH_TOKEN) String authToken,
                                          @Field("stepId") String stepKey,
                                          @Field("challengeId") String challengeId,
                                          @Field("step_id") String stepId,
                                          @Field("widget_slug") String stepType,
                                          @Field("completed") String solved,
                                          @Field("playedId") String playedId,
                                          @Field("last_step") String isLastStep);

    @FormUrlEncoded
    @POST("/challenge/save/step")
    Call<APIResponse> saveMemoryGameNew(@Header(MB_API_KEY) String apiKey,
                                        @Header(AUTH_TOKEN) String authToken,
                                        @Field("stepId") String stepKey,
                                        @Field("challengeId") String challengeId,
                                        @Field("step_id") String stepId,
                                        @Field("widget_slug") String stepType,
                                        @Field("completed") String solved,
                                        @Field("playedId") String playedId,
                                        @Field("last_step") String isLastStep);

    @FormUrlEncoded
    @POST("/contest/challenge/last_result")
    Call<APIResponse<RafflesResponseModel>> postChallengeLastResult(@Header(MB_API_KEY) String apiKey,
                                                                    @Header(AUTH_TOKEN) String authToken,
                                                                    @Field("challenge_id") String challengeId,
                                                                    @Field("is_right") String isRight);


    @FormUrlEncoded
    @POST("/challenge/last_result")
    Call<APIResponse> postPlayStep(@Header(MB_API_KEY) String apiKey,
                                   @Header(AUTH_TOKEN) String authToken,
                                   @Field("challengeId") String challengeId,
                                   @Field("playedId") String playedId,
                                   @Field("totalcoins") int totalPoints);


    @FormUrlEncoded
    @POST("/challenge/offline")
    Call<APIResponse<Challenge>> saveChallengeOffline(@Header(MB_API_KEY) String apiKey,
                                                      @Header(AUTH_TOKEN) String authToken,
                                                      @Field("challengeId") String challengeId);


    @FormUrlEncoded
    @POST("/challenge/offline/submit")
    Call<APIResponse<StepSaveResponse>> submitChallengeOffline(@Header(MB_API_KEY) String apiKey,
                                                               @Header(AUTH_TOKEN) String authToken,
                                                               @Field("challengeId") String challengeId,
                                                               @Field("challenge") String challenge,
                                                               @Field("playedId") String playedId);

    @GET("/user/coins/histrory/list")
    Call<APIResponse<List<Coin>>> getUserCoinsHistory(@Header(MB_API_KEY) String apiKey,
                                                      @Header(AUTH_TOKEN) String authToken);

    @GET("/user/product/list")
    Call<APIResponse<List<Store>>> getUserProductHistory(@Header(MB_API_KEY) String apiKey,
                                                         @Header(AUTH_TOKEN) String authToken);

    @GET("/user/raffle/list")
    Call<APIResponse<List<Raffle>>> getUserRaffleHistory(@Header(MB_API_KEY) String apiKey,
                                                         @Header(AUTH_TOKEN) String authToken);

    @GET("/user/deals/histrory/list")
    Call<APIResponse<List<Challenge>>> getDealsHistory(@Header(MB_API_KEY) String apiKey,
                                                       @Header(AUTH_TOKEN) String authToken);

    @GET("/raffle/store/total")
    Call<APIResponse<TotalRaffleStore>> getTotalRaffleStore(@Header(MB_API_KEY) String apiKey,
                                                            @Header(AUTH_TOKEN) String authToken);


    @FormUrlEncoded
    @POST("/product/purchase")
    Call<APIResponse> purchaseProduct(@Header(MB_API_KEY) String apiKey,
                                      @Header(AUTH_TOKEN) String authToken,
                                      @Field("productId") String productId);

    @FormUrlEncoded
    @POST("/product/purchase")
    Call<APIResponse> purchaseProduct(@Header(MB_API_KEY) String apiKey,
                                      @Header(AUTH_TOKEN) String authToken,
                                      @Field("productId") String productId,
                                      @Field("shippingAddressId") String addressId);

    @FormUrlEncoded
    @POST("/raffle/purchase")
    Call<APIResponse> purchaseRaffle(@Header(MB_API_KEY) String apiKey,
                                     @Header(AUTH_TOKEN) String authToken,
                                     @Field("raffleId") String raffleId,
                                     @Field("bulk_raffle") String bulk_raffle);


//    @FormUrlEncoded
//    @POST("/user/edit_profile_image")
//    Call<APIResponse<UserImageUploadResponse>> uploadImage(@Header(MB_API_KEY) String apiKey,
//                                                           @Header(AUTH_TOKEN) String authToken,
//                                                           @Field("userImage") String userImage);


    @Multipart
    @POST("/user/edit_profile_image")
    Call<APIResponse<UserImageUploadResponse>> uploadImage(@Header(MB_API_KEY) String apiKey,
                                                           @Header(AUTH_TOKEN) String authToken,
                                                           @Part("description") RequestBody description,
                                                           @Part("userImage") MultipartBody.Part file);

    @GET("/user/account/details")
    Call<APIResponse<AccountDetails>> getAccountDetails(@Header(MB_API_KEY) String apiKey,
                                                        @Header(AUTH_TOKEN) String authToken);

    @GET("/user/wallet")
    Call<APIResponse<PointsWallet>> getWalletDetails(@Header(MB_API_KEY) String apiKey,
                                                     @Header(AUTH_TOKEN) String authToken);

    @GET("/strip/image")
    Call<APIResponse<StripParams>> getStripImage(@Header(MB_API_KEY) String apiKey,
                                                 @Header(AUTH_TOKEN) String authToken);


    @GET("/challenge/completed/list")
    Call<APIResponse<List<Challenge>>> getCompletedChallenges(@Header(MB_API_KEY) String apiKey,
                                                              @Header(AUTH_TOKEN) String authToken,
                                                              @Query("page") int page,
                                                              @Query("limit") int limit);

    @GET("/operational/city/list")
    Call<APIResponse<List<Cities>>> getOperationalCities(@Header(MB_API_KEY) String apiKey,
                                                         @Header(AUTH_TOKEN) String authToken);

    @FormUrlEncoded
    @POST("/shipping/address/create")
    Call<APIResponse<ShippingAddress>> shippingAddressCreate(@Header(MB_API_KEY) String apiKey,
                                                             @Header(AUTH_TOKEN) String authToken,
                                                             @Field("fullName") String fullName,
                                                             @Field("phone") String phone,
                                                             @Field("alternatePhone") String alternatePhone,
                                                             @Field("addressLine1") String addressLine1,
                                                             @Field("addressLine1") String addressLine2,
                                                             @Field("state") String state,
                                                             @Field("city") String city,
                                                             @Field("zipCode") String zipCode);

    @FormUrlEncoded
    @POST("/shipping/address/update")
    Call<APIResponse<ShippingAddress>> shippingAddressUpdate(@Header(MB_API_KEY) String apiKey,
                                                             @Header(AUTH_TOKEN) String authToken,
                                                             @Field("id") String id,
                                                             @Field("fullName") String fullName,
                                                             @Field("phone") String phone,
                                                             @Field("alternatePhone") String alternatePhone,
                                                             @Field("addressLine1") String addressLine1,
                                                             @Field("addressLine1") String addressLine2,
                                                             @Field("state") String state,
                                                             @Field("city") String city,
                                                             @Field("zipCode") String zipCode);

    @FormUrlEncoded
    @POST("/shipping/address/delete")
    Call<APIResponse> shippingAddressDelete(@Header(MB_API_KEY) String apiKey,
                                            @Header(AUTH_TOKEN) String authToken,
                                            @Field("id") String id);


    @GET("/shipping/address/list")
    Call<APIResponse<List<ShippingAddress>>> getShippingAddressList(@Header(MB_API_KEY) String apiKey,
                                                                    @Header(AUTH_TOKEN) String authToken);

    @GET("/mobile/state/list")
    Call<APIResponse<List<SimpleObject>>> getStates(@Header(MB_API_KEY) String apiKey,
                                                    @Header(AUTH_TOKEN) String authToken);

    @GET("/mobile/city/list/{id}")
    Call<APIResponse<List<SimpleObject>>> getCities(@Header(MB_API_KEY) String apiKey,
                                                    @Header(AUTH_TOKEN) String authToken,
                                                    @Path("id") String stateId);

    @FormUrlEncoded
    @POST("/challenge/rating")
    Call<APIResponse> challengeRating(@Header(MB_API_KEY) String apiKey,
                                      @Header(AUTH_TOKEN) String authToken,
                                      @Field("challengeId") String id,
                                      @Field("userRating") String rating);

    @GET("/mobile/operator/list")
    Call<APIResponse<List<Operators>>> getOperatorList(@Header(MB_API_KEY) String apiKey,
                                                       @Header(AUTH_TOKEN) String authToken);

    @GET("/mobile/circle/list")
    Call<APIResponse<List<Circles>>> getCircleList(@Header(MB_API_KEY) String apiKey,
                                                   @Header(AUTH_TOKEN) String authToken);

    @FormUrlEncoded
    @POST("/mobile/details")
    Call<APIResponse<MobileDetails>> getOperatorAndCircle(@Header(MB_API_KEY) String apiKey,
                                                          @Header(AUTH_TOKEN) String authToken,
                                                          @Field("mobileno") String mobileno);

    @FormUrlEncoded
    @POST("/recharge/plan")
    Call<APIResponse<List<HomeRechargePlans>>> getAllRechargePlans(@Header(MB_API_KEY) String apiKey,
                                                                   @Header(AUTH_TOKEN) String authToken,
                                                                   @Field("operatorCode") String operatorCode,
                                                                   @Field("circleCode") String circleCode);

    @FormUrlEncoded
    @POST("/recharge/mobile")
    Call<APIResponse> getRecharge(@Header(MB_API_KEY) String apiKey,
                                  @Header(AUTH_TOKEN) String authToken,
                                  @Field("operatorcode") String operatorCode,
                                  @Field("mobileno") String mobileNum,
                                  @Field("recharge") String rechargeAmnt,
                                  @Field("circode") String circleCode,
                                  @Field("recharge_type") String tabName);

    @FormUrlEncoded
    @POST("user/challenge/like")
    Call<APIResponse> likeChallenge(@Header(MB_API_KEY) String apiKey,
                                    @Header(AUTH_TOKEN) String authToken,
                                    @Field("challengeId") String challengeId);

    @FormUrlEncoded
    @POST("/referral/number")
    Call<APIResponse> referalContact(@Header(MB_API_KEY) String apiKey,
                                     @Header(AUTH_TOKEN) String authToken,
                                     @Field("contacts") String contacts);

    @FormUrlEncoded
    @POST("user/data/enabled")
    Call<APIResponse> dataEnableDesable(@Header(MB_API_KEY) String apiKey,
                                        @Header(AUTH_TOKEN) String authToken,
                                        @Field("dataEnabled") int dataEnabled);

    @GET("user/first/opened")
    Call<APIResponse> userOepened(@Header(MB_API_KEY) String apiKey,
                                  @Header(AUTH_TOKEN) String authToken);

    @GET("user/refer/opened")
    Call<APIResponse> referOepened(@Header(MB_API_KEY) String apiKey,
                                   @Header(AUTH_TOKEN) String authToken);

    @GET("/user/referral/message")
    Call<APIResponse<ReferralText>> referalText(@Header(MB_API_KEY) String apiKey,
                                                @Header(AUTH_TOKEN) String authToken);

    @GET("/challenge/home/banner")
    Call<APIResponse<List<Challenge>>> homeBanner(@Header(MB_API_KEY) String apiKey,
                                                  @Header(AUTH_TOKEN) String authToken,
                                                  @Query("deviceDPI") String deviceDPI);

    @GET("/challenge/home/banner")
    Call<APIResponse<List<Challenge>>> homeBanner(@Header(MB_API_KEY) String apiKey,
                                                  @Header(AUTH_TOKEN) String authToken,
                                                  @Query("deviceDPI") String deviceDPI,
                                                  @Query("cityId") String cityId,
                                                  @Query("lat") String lat,
                                                  @Query("lng") String lng);



    @GET("contest/challenge/list")
    Call<APIResponse<List<Challenge>>> getContestChallengeList(@Header(MB_API_KEY) String apiKey,
                                                               @Header(AUTH_TOKEN) String authToken,
                                                               @Query("cityId") String cityId,
                                                               @Query("lat") String lat,
                                                               @Query("lng") String lng);

    @GET("contest/challenge/list")
    Call<APIResponse<List<Challenge>>> getContestChallengeList(@Header(MB_API_KEY) String apiKey,
                                                               @Header(AUTH_TOKEN) String authToken);

    @GET("product/category/list")
    Call<APIResponse<List<RedeemVoucherModel>>> getProductCategoryList(@Header(MB_API_KEY) String apiKey,
                                                               @Header(AUTH_TOKEN) String authToken);

    @GET("raffle/category/list")
    Call<APIResponse<List<RedeemVoucherModel>>> getRaffleCategoryList(@Header(MB_API_KEY) String apiKey,
                                                                      @Header(AUTH_TOKEN) String authToken);

    @FormUrlEncoded
    @POST("user/list-of-notification")
    Call<APIResponse<List<NotificationModel>>> getNotificationList(@Header(MB_API_KEY) String apiKey,
                                                                   @Header(AUTH_TOKEN) String authToken,
                                                                   @Field("start") int start);

    @GET("user/list-of-prize/{value}")
    Call<APIResponse<VoucherModel>> getVoucherList(@Header(MB_API_KEY) String apiKey,
                                                  @Header(AUTH_TOKEN) String authToken,
                                                   @Path("value") String value );

    @FormUrlEncoded
    @POST("user/claim-prize")
    Call<APIResponse<VoucherResponseModel>> claimPrize(@Header(MB_API_KEY) String apiKey,
                                                       @Header(AUTH_TOKEN) String authToken,
                                                       @Field("claim_id") int claimId,
                                                       @Field("notification_id") int notificationId,
                                                       @Field("notification_type") String notificationType);


    @GET("/spinwheel/detail")
    Call<APIResponse<SpinWheelDetails>> getSpinDetails(@Header(MB_API_KEY) String apiKey,
                                                       @Header(AUTH_TOKEN) String authToken);

    @FormUrlEncoded
    @POST("/spinwheel/user_spin")
    Call<APIResponse> sendSpinPoints(@Header(MB_API_KEY) String apiKey,
                                     @Header(AUTH_TOKEN) String authToken,
                                     @Field("spin_id") int spin_id,
                                     @Field("spin_coins") int spin_coins,
                                     @Field("s_id") int s_id);

    @GET("/heartBeat")
    Call<APIResponse> getHeartBeat();

    @GET("/user/profile/details")
    Call<APIResponse<ProfileDetails>> getProfileDetails(@Header(MB_API_KEY) String apiKey,
                                                     @Header(AUTH_TOKEN) String authToken);
    @FormUrlEncoded
    @POST("/user/profile/submission")
    Call<APIResponse<ProfileDetails>> postProfileDetails(@Header(MB_API_KEY) String apiKey,
                                                        @Header(AUTH_TOKEN) String authToken,
                                                         @Field("gender") String gender,
                                                         @Field("dob") String dob,
                                                         @Field("location") String location);

    @FormUrlEncoded
    @POST("/article/addview")
    Call<APIResponse> articleUpdate(@Header(MB_API_KEY) String apiKey,
                                    @Header(AUTH_TOKEN) String authToken,
                                    @Field("challenge_article_id") String chaId,
                                    @Field("article_step_id") String articleKey,
                                    @Field("time_spent") String spentTime);
//    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjU5LCJpc3MiOiJodHRwOlwvXC93cy5tZXJhYnJlYWsuY29tXC91c2VyXC9jb25maXJtX290cCIsImlhdCI6MTQ2NDI0OTAwNiwiZXhwIjoxNDY1NDU4NjA2LCJuYmYiOjE0NjQyNDkwMDYsImp0aSI6IjIwMjU1MDQ3Yjk4YTY4MmRkYTM1MWJhM2FiMWIwOTMwIn0.ZrGxHJMOw1No36wACzVt3E0l6i80HkL8f3e3uvEtZZ0

    @POST("/ipl/termcondition")
    Call<APIResponse> postTermsAccepted(@Header(MB_API_KEY) String apiKey,
                                        @Header(AUTH_TOKEN) String authToken);

    @FormUrlEncoded
    @POST("/ipl/ganesh_speak_count")
    Call<APIResponse<GSAdLink>> getGSAdLink(@Header(MB_API_KEY) String apiKey,
                                            @Header(AUTH_TOKEN) String authToken,
                                            @Field("predict_id") int predict_id,
                                            @Field("content_id") int content_id);

    @GET("/mb_gs/getAllAstrology/{deviceDPI}")
    Call<APIResponse<List<GaneshaSpeaks>>> getGSAstrologyList(@Header(MB_API_KEY) String apiKey,
                                                              @Header(AUTH_TOKEN) String authToken,
                                                              @Path("deviceDPI") String deviceDPI);


    @GET("/mb_gs/getAstrology/{astrology}")
    Call<APIResponse<Astrology>> getGSAstrologyDetails(@Header(MB_API_KEY) String apiKey,
                                                       @Header(AUTH_TOKEN) String authToken,
                                                       @Path("astrology") int astrology,
                                                       @Query("deviceDPI") String deviceDPI);

    @FormUrlEncoded
    @POST("/mb_gs/countView")
    Call<APIResponse> postMBGSCount(@Header(MB_API_KEY) String apiKey,
                                    @Header(AUTH_TOKEN) String authToken,
                                    @Field("astro_id") int astro_id,
                                    @Field("time_spent") String time_spent);

    @FormUrlEncoded
    @POST("/mb_gs/astrologyCount")
    Call<APIResponse<GSAdLink>> getZodiacAdLink(@Header(MB_API_KEY) String apiKey,
                                                @Header(AUTH_TOKEN) String authToken,
                                                @Field("astro_id") int astro_id,
                                                @Field("content_id") int content_id);

}
