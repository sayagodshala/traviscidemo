package com.merabreak.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;
import com.merabreak.SearchedLocationItem;
import com.merabreak.SearchedLocationItem_;
import com.merabreak.Util;
import com.merabreak.fragments.DatePickerFragment;
import com.merabreak.models.APIResponse;
import com.merabreak.models.APIResponseForLoopJ;
import com.merabreak.models.GooglePrediction;
import com.merabreak.models.User;
import com.merabreak.network.APIService;
import com.merabreak.network.Connectivity;
import com.merabreak.network.GoogleAPIClient;
import com.merabreak.network.GoogleAPIResponse;
import com.merabreak.network.GoogleAPIService;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**ja
 * Created by Ewebcore on 14-Jan-16.
 */
@EActivity(R.layout.edit_profile_new)
public class EditProfileActivity extends BaseActivity implements DatePickerFragment.DatePickerFragmentListener {

    int mYear, mMonth, mDay;

    @ViewById(R.id.dob_textview)
    TextView dobTv;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.profile_image)
    ImageView image;

    @ViewById(R.id.email)
    EditText email;

    @ViewById(R.id.full_name)
    EditText fullName;

    @ViewById(R.id.location)
    EditText location;

    @ViewById(R.id.linear_list)
    LinearLayout linearList;

    @ViewById(R.id.list_views)
    ListView locationList;

    @ViewById(R.id.zipcode)
    EditText zipcode;

    @ViewById(R.id.date_text_d)
    EditText date_text_d;

    @ViewById(R.id.date_text_m)
    EditText date_text_m;

    @ViewById(R.id.date_text_y)
    EditText date_text_y;

    @ViewById(R.id.d_slash)
    TextView d_slash;

    @ViewById(R.id.m_slash)
    TextView m_slash;

    @ViewById(R.id.date)
    TextView birthday;

    @ViewById(R.id.about)
    EditText about;

    @ViewById(R.id.save)
    TextView save;

    @ViewById(R.id.male)
    Button male;

    @ViewById(R.id.female)
    Button female;

    @ViewById(R.id.offline)
    Switch offline;

    @ViewById(R.id.take_photo)
    FloatingActionButton take_photo;

    private String gender = "";

    private Uri mCameraFileUri;

    boolean isPhotoModified = false;

    private static final int MY_PERMISSION_CAMERA = 101;
    GoogleAPIService googleAPIService;
    private CustomListAdapter projectItemsAdapter = null;

    @AfterViews
    void init() {
        googleAPIService = GoogleAPIClient.getAPIService();
        title.setText(R.string.edit_profile_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        fullName.requestFocus();

        getUserDetails();

       /* email.setText(user.getEmail());
        fullName.setText(user.getFullName());
        birthday.setText(user.getDob());
        zipcode.setText(user.getZipCode());
        gender = user.getGender();

        if (gender.toLowerCase().equalsIgnoreCase("male"))
            male.setSelected(true);
        else if (gender.toLowerCase().equalsIgnoreCase("female"))
            female.setSelected(true);

        about.setText(user.getAbout());

        Picasso.with(context).load(user.getImage()).placeholder(R.drawable.dummy_profile_image).into(image, new com.squareup.picasso.Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
            }
        });*/

        // if (android.os.Build.VERSION.SDK_INT >= 21)
        //     Util.changeSystemBarColor(this, R.color.white_transparent);

        if (user != null && user.getAccountType() != null) {
            if (user.getAccountType().equalsIgnoreCase("facebook")) {
                editProfileVisibility();
            }
        }

        about.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSave();
                    return true;
                }
                return false;
            }
        });

        dobTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                Date today = new Date();
                c.setTime(today);
                c.add( Calendar.YEAR, -5 ); // set 5 years as a minimum age limit of child

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                long minDate = c.getTime().getTime();


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                int dd = view.getDayOfMonth();
                                int mm = view.getMonth();
                                int yy = view.getYear();

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(yy,mm,dd);
                                String userDoB = format.format(calendar.getTime());

                                dobTv.setText(userDoB);

                            }
                        }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();
            }
        });
    }

    private void editProfileVisibility() {
        email.setEnabled(false);
    }

    private void getUserDetails() {
//        showLoader();
        Call<APIResponse<User>> callback = apiService.updateDetails(Constants.MB_API_KEY, user.getAuthToken(), user.getPhone());

        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                //   hideLoader();
                if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(EditProfileActivity.this, user);
                                    getUserDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        User user = response.body().getValues();
                                        Util.setUser(EditProfileActivity.this, user);
                                        //Log.d("updated details", new Gson().toJson(user));
                                        // Log.d("saved updated details", new Gson().toJson(Util.getUser(EditProfileActivity.this)));
                                        setData(user);
                                    } else {
                                        Util.makeToast(EditProfileActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(EditProfileActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(EditProfileActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Log.d("ErrorBody", response.errorBody().toString());
                        Util.makeToast(EditProfileActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(EditProfileActivity.this, response.body().getMeta().getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //    hideLoader();
                //Util.makeToast(EditProfileActivity.this, "Failed to edit profile");
                if (t.getMessage() != null) {
                    Log.d("JSONError", t.getMessage());
                    Util.makeToast(EditProfileActivity.this, t.getMessage());
                } else {
                    Log.d("API Error", Constants.SOME_THING_WRONG);
                }
            }
        });
    }

    private void setData(User user) {
        email.setText(user.getEmail());
        fullName.setText(user.getFullName());
        //birthday.setText(user.getDob());
        dobTv.setText(user.getDob());
        if (!user.getDob().equalsIgnoreCase("") && user.getDob() != null && !user.getDob().contains("00/00/0000")) {
            date_text_d.setText(user.getDob().substring(0) + user.getDob().substring(1));
            date_text_m.setText(user.getDob().substring(3) + user.getDob().substring(4));
            date_text_y.setText(user.getDob().substring(6) + user.getDob().substring(7) + user.getDob().substring(8) + user.getDob().substring(9));
        } else {
            date_text_d.setHint("DD");
            date_text_m.setHint("MM");
            date_text_y.setHint("YYYY");
        }
        if (user.getZipCode() != null)
            zipcode.setText(user.getZipCode());
        if (user.getGender() != null) {
            gender = user.getGender();
            if (gender.toLowerCase().equalsIgnoreCase("male"))
                male.setSelected(true);
            else if (gender.toLowerCase().equalsIgnoreCase("female"))
                female.setSelected(true);
        }
        if (user.getAbout() != null)
            about.setText(user.getAbout());
        if (user.getLocation() != null && !user.getLocation().equalsIgnoreCase(""))
            location.setText(user.getLocation());

        Picasso.with(context).load(user.getImage()).placeholder(R.drawable.dummy_profile_image).into(image, new com.squareup.picasso.Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
            }
        });

        if (Util.isMobileDataForOfflineDownload(this))
            offline.setChecked(true);

        offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Util.setOffline(EditProfileActivity.this, "true");
                    sendDataStatus(1);
                } else {
                    Util.setOffline(EditProfileActivity.this, "");
                    sendDataStatus(0);
                }
            }
        });
        fullName.requestFocus();
    }

    @Click(R.id.save)
    void onSave() {
        if (Connectivity.isConnected(this)) {
            if (isFielsValid()) {
                editProfile();
            }
        } else
            Util.makeToast(context, context.getResources().getString(R.string.main_activity_still_no_internet));
    }

    @Click(R.id.date)
    void dobCalend() {
        showDatePickerDialog();
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(String date) {
        birthday.setText(date);
    }

    @Click(R.id.male)
    void onMale() {
        male.setSelected(true);
        female.setSelected(false);
        gender = "male";
    }

    @Click(R.id.female)
    void onFemale() {
        female.setSelected(true);
        male.setSelected(false);
        gender = "female";
    }

    @TextChange(R.id.date_text_d)
    void addRequestDate() {
        String dob = date_text_d.getText().toString();
        if (dob.length() == 2) {
            date_text_m.requestFocus();
        }
    }

    @TextChange(R.id.date_text_m)
    void addRequestMonth() {
        String dob = date_text_m.getText().toString();
        if (dob.length() == 2) {
            date_text_y.requestFocus();
        }
    }

    private boolean isFielsValid() {
        String validationMessage = "";
        if (Util.textIsEmpty(fullName.getText().toString().trim()))
            validationMessage = getResources().getString(R.string.edit_profile_enter_complete_info);
        else if (!Util.isValidEmail(email.getText().toString()))
            validationMessage = getResources().getString(R.string.edit_profile_enter_complete_info);
        /*else if (Util.textIsEmpty(location.getText().toString().trim()))
            validationMessage = getResources().getString(R.string.edit_profile_enter_complete_info);
        else if(location.getText().toString().trim().length() < 6)
            validationMessage = getResources().getString(R.string.edit_profile_enter_zipcode);
        else if (dobTv.getText().toString().trim().length() == 0)
            validationMessage = getResources().getString(R.string.edit_profile_enter_complete_info);
        else if (Util.textIsEmpty(about.getText().toString().trim()))
            validationMessage = getResources().getString(R.string.edit_profile_enter_complete_info);*/

        if (validationMessage.length() != 0) {
            Util.makeToast(this, validationMessage);
        }
        return validationMessage.length() == 0;
    }

    private void uploadPhoto() {
        hideLoader(progress);
        showLoader(progress);
        File file = new File(mCameraFileUri.getPath());
        //Log.d("FileExist", file.exists() + "");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader(APIService.MB_API_KEY, Constants.MB_API_KEY);
        client.addHeader(APIService.AUTH_TOKEN, user.getAuthToken());
        try {
            params.put("userImage", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post(Constants.BASE_URL + "/user/edit_profile_image", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                hideLoader(progress);
                try {
                    String str = new String(responseBody, "UTF-8");
                    //Log.d("ImageResponse", str);
                    APIResponseForLoopJ response = new Gson().fromJson(str, APIResponseForLoopJ.class);
                    if (response.getMeta() != null) {
                        if (response.getMeta().isStatus()) {
                            user.setImage(response.getValues().getUserImage());
                            Util.setUser(EditProfileActivity.this, user);
                        } else {
                            Util.makeToast(EditProfileActivity.this, response.getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(EditProfileActivity.this, response.getMeta().getMessage());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Util.makeToast(EditProfileActivity.this, getResources().getString(R.string.some_thing_wrong_pic_upload));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                hideLoader(progress);
                String str;
                try {
                    if (responseBody != null && error.getMessage() != null) {
                        str = new String(responseBody, "UTF-8");
                        //Log.d("ImageResponse", str + " " + error.getMessage());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (Connectivity.isConnected(context))
                    Util.makeToast(EditProfileActivity.this, getResources().getString(R.string.some_thing_wrong_pic_upload));
                else
                    Util.makeToast(EditProfileActivity.this, getResources().getString(R.string.no_internet));
            }
        });
    }

    private void editProfile() {
        hideLoader(progress);
        showLoader(progress);
       // String dob = date_text_d.getText().toString() + d_slash.getText().toString() + date_text_m.getText().toString() + m_slash.getText().toString() + date_text_y.getText().toString();

        String dob = dobTv.getText().toString();
        //Log.d("user image", user.getImage());
        Call<APIResponse> callback = apiService.editProfile(Constants.MB_API_KEY, user.getAuthToken(),
                fullName.getText().toString(),
                email.getText().toString(),
                about.getText().toString(),
                dob,
                gender,
                zipcode.getText().toString(),
                "",
                "",
                user.getImage(),
                location.getText().toString());

        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(EditProfileActivity.this, user);
                                    editProfile();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        user.setFullName(fullName.getText().toString());
                                        user.setEmail(email.getText().toString());
                                        user.setAbout(about.getText().toString());
                                        user.setDob(dob);
                                        user.setGender(gender);
                                        user.setZipCode(zipcode.getText().toString());
                                        user.setImage(user.getImage());
                                        user.setLocation(location.getText().toString());
                                        Util.setUser(EditProfileActivity.this, user);
                                        ProfileActivity_.intent(EditProfileActivity.this).start();
                                    } else {
                                        Util.makeToast(EditProfileActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(EditProfileActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(EditProfileActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Log.d("ErrorBody", response.errorBody().toString());
                        Util.makeToast(EditProfileActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(EditProfileActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(EditProfileActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    @Click(R.id.take_photo)
    void onTakePhoto() {
        showPhotoPickerDialog();
    }

    private void showPhotoPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select");

        if (user.getAccountType().equalsIgnoreCase("facebook")) {
            builder.setItems(R.array.photo_picker_options_with_facebook, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<String> values = Arrays.asList(getResources().getStringArray(R.array.photo_picker_options_with_facebook));
                    if (values.get(which).toLowerCase().equalsIgnoreCase("gallery")) {
                        openGallery();
                    } else if (values.get(which).toLowerCase().contains("facebook")) {
                        editFacebookPhoto();
                    } else {
                        //openCamera();
                        requestPermissions();
                    }
                }
            });
        } else {
            builder.setItems(R.array.photo_picker_options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<String> values = Arrays.asList(getResources().getStringArray(R.array.photo_picker_options));

                    if (values.get(which).toLowerCase().equalsIgnoreCase("gallery")) {
                        openGallery();
                    } else {
                        // openCamera();
                        requestPermissions();
                    }
                }
            });
        }
        builder.show();
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_CODE_USER_PHOTO);
    }

    public void openCamera() {
        String fileName = Constants.PHOTO_FOLDER_NAME + "_"
                + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mCameraFileUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory()
                + "/"
                + Constants.PHOTO_FOLDER_NAME, fileName));

        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCameraFileUri);
        startActivityForResult(i, Constants.REQUEST_CODE_CAMERA_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_CAMERA_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri orgUri = mCameraFileUri;
                //Log.d("URI", orgUri.toString());
                File file = new File(orgUri.getPath());
                // Log.d("URI PATH", orgUri.getPath());
                // Log.d("FILE ABSOLUTE PATH", file.getAbsolutePath());
                // Log.d("FILE NAME", file.getPath());
                Uri destination;
                File directory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PHOTO_FOLDER_NAME);
                if (!directory.exists() && !directory.isDirectory()) {
                    if (directory.mkdirs()) {
                        destination = Uri.fromFile(new File(directory, imageFileName()));
                        Crop.of(orgUri, destination).withMaxSize(600, 400).start(this);
                    } else {
                        Util.makeToast(this, getResources().getString(R.string.cannot_create_image_dir));
                    }
                } else {
                    destination = Uri.fromFile(new File(directory, imageFileName()));
//                        Crop.of(orgUri, destination).asSquare().startBtn(this);
                    Crop.of(orgUri, destination).withMaxSize(600, 400).start(this);
                }
            } else {
                if (data != null) {
                    if (data.hasExtra("error")) {
                        Throwable throwable = data.getParcelableExtra("error");
                        Log.d("Capture Error", throwable.getMessage());
                    }
                }
                //  Util.makeToast(this, "Something went wrong while capturing photo!");
            }
        } else if (requestCode == Constants.REQUEST_CODE_USER_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri orgUri = data.getData();
                // Log.d("URI", orgUri.toString());
                File file = new File(orgUri.getPath());
                // Log.d("URI PATH", orgUri.getPath());
                // Log.d("FILE ABSOLUTE PATH", file.getAbsolutePath());
                // Log.d("FILE NAME", file.getPath());
                Uri destination;
                File directory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PHOTO_FOLDER_NAME);
                if (!directory.exists() && !directory.isDirectory()) {
                    if (directory.mkdirs()) {
                        destination = Uri.fromFile(new File(directory, imageFileName()));
//                            Crop.of(orgUri, destination).asSquare().startBtn(this);
                        Crop.of(orgUri, destination).withMaxSize(600, 400).start(this);
                    } else {
                        Util.makeToast(this, getResources().getString(R.string.cannot_create_image_dir));
                    }
                } else {
                    destination = Uri.fromFile(new File(directory, imageFileName()));
                    Crop.of(orgUri, destination).withMaxSize(600, 400).start(this);
                }
            } else if (resultCode == Constants.REQUEST_CODE_ERROR) {
                // Util.makeToast(this, "Something went wrong while picking up photo!");
                Util.makeToast(this, getResources().getString(R.string.no_internet));
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                isPhotoModified = true;
                mCameraFileUri = Crop.getOutput(data);
                // Log.d("SELECTED URI", mCameraFileUri.toString());
                File file = new File(mCameraFileUri.getPath());
                // Log.d("SELECTED URI PATH", mCameraFileUri.getPath());
                // Log.d("FILE ABSOLUTE PATH", file.getAbsolutePath());
                // Log.d("SELECTED FILE NAME", file.getPath());
                image.setImageURI(mCameraFileUri);
                uploadPhoto();
            } else if (resultCode == Crop.RESULT_ERROR) {
                //Util.makeToast(this, "Something went wrong while cropping photo!");
                Util.makeToast(this, getResources().getString(R.string.no_internet));
            }
        }
    }

    private String imageFileName() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return "merabreak_" + tsLong.toString() + ".jpg";
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void editFacebookPhoto() {
        Uri destination;
        Uri facebookURI = Uri.parse(user.getImage());
        File directory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PHOTO_FOLDER_NAME);
        if (!directory.exists() && !directory.isDirectory()) {
            if (directory.mkdirs()) {
                destination = Uri.fromFile(new File(directory, imageFileName()));
                Crop.of(facebookURI, destination).withMaxSize(600, 400).start(this);
            } else {
                Util.makeToast(this, getResources().getString(R.string.cannot_create_image_dir));
            }
        } else {
            destination = Uri.fromFile(new File(directory, imageFileName()));
            Crop.of(facebookURI, destination).withMaxSize(600, 400).start(this);
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 900);
        c.getTimeInMillis();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermissions() {
        if (!hasPermissions(this, Manifest.permission.CAMERA)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                appAlert();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSION_CAMERA);
            }
        } else {
            openCamera();
        }
    }

    private void appAlert() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.camera_access_title));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(EditProfileActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSION_CAMERA);
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Util.makeToast(EditProfileActivity.this, getResources().getString(R.string.camera_access_toast));
                }
                return;
            }
        }
    }

    private void sendDataStatus(int data) {
        Call<APIResponse> callback = apiService.dataEnableDesable(Constants.MB_API_KEY, user.getAuthToken(), data);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(EditProfileActivity.this, user);
                                sendDataStatus(data);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {

                            }
                        }
                    } else {
                        Util.makeToast(EditProfileActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(EditProfileActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Util.makeToast(EditProfileActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }


    @TextChange(R.id.location)
    void searchLocation() {
        if (location.getText().length() > 0)
            getGooglePlaces(location.getText().toString(), linearList, locationList);
        else
            linearList.setVisibility(View.GONE);

    }

    @ItemClick(R.id.list_views)
    void selectLocation(GooglePrediction item) {
        location.setText(item.getDescription());
        linearList.setVisibility(View.GONE);
    }

    public void getGooglePlaces(String text, LinearLayout linearList, ListView locationList) {
        Call<GoogleAPIResponse<List<GooglePrediction>>> callBack = null;
        try {
            callBack = googleAPIService.getGooglePlaces("false", "en", Constants.GOOGLE_APP_API_BROWSER_KEY, URLEncoder.encode(text, "utf8"), "country:in");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        callBack.enqueue(new Callback<GoogleAPIResponse<List<GooglePrediction>>>() {
            @Override
            public void onResponse(Response<GoogleAPIResponse<List<GooglePrediction>>> response) {
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                List<GooglePrediction> predictions = new ArrayList<GooglePrediction>();

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        predictions = response.body().getPredictions();
                        drawSearchedLocationList(predictions, linearList, locationList);
                    } else {
                        //Util.makeToast(MainActivity.this, "Not able to fetch location at this points of time.Please try again or enter manually");
                    }
                } else {
                    Util.makeToast(EditProfileActivity.this, "Not able to fetch location at this points of time.Please try again or enter manually");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null)
                    Log.d("Retrofit Response", new Gson().toJson(t));
            }
        });
    }

    private void drawSearchedLocationList(List<GooglePrediction> values, LinearLayout linearList, ListView loc) {
        linearList.setVisibility(View.VISIBLE);
        projectItemsAdapter = new CustomListAdapter<>(values, this::searchedLocationItem);
        loc.setAdapter(projectItemsAdapter);
    }

    private SearchedLocationItem searchedLocationItem() {
        SearchedLocationItem item = SearchedLocationItem_.build(this);
        return item;
    }
}
