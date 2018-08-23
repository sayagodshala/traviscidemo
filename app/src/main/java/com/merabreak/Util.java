package com.merabreak;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.DownloadedChallenges;
import com.merabreak.models.HomeRechargePlans;
import com.merabreak.models.IPLConfig;
import com.merabreak.models.PointsWallet;
import com.merabreak.models.Raffle;
import com.merabreak.models.RechargePlans;
import com.merabreak.models.StartChallenge;
import com.merabreak.models.Store;
import com.merabreak.models.User;
import com.merabreak.models.challenge.Category;
import com.merabreak.models.challenge.Challenge;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import cz.msebera.android.httpclient.conn.util.InetAddressUtils;

public class Util {

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void makeToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    // in case of user's mobile is verified
    public static boolean isMobileVerified(Context context) {
        boolean login = false;
        if (context != null) {
            String raw = AppSettings.getValue(context, AppSettings.PREF_IS_MOBILE_VERIFIED, "");
            if (raw.equalsIgnoreCase("true"))
                login = true;
        }
        return login;
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }
    public static void setMobileVerified(Context context) {
        AppSettings.setValue(context, AppSettings.PREF_IS_MOBILE_VERIFIED, "true");
    }

    // in case of user's mobile is verified and user registered
    public static boolean isUserLoggedIn(Context context) {

        boolean login = false;
        if (context != null) {
            String raw = AppSettings.getValue(context, AppSettings.PREF_IS_USER_LOGGED_IN, "");
            if (raw.equalsIgnoreCase("true"))
                login = true;
        }
        return login;
    }

    public static void setUserLogin(Context context) {
        AppSettings.setValue(context, AppSettings.PREF_IS_USER_LOGGED_IN, "true");
    }

    public static void setOffline(Context context, String flag) {
        AppSettings.setValue(context, AppSettings.PREF_OFFLINE_DOWNLOAD, flag);
    }

    public static boolean isMobileDataForOfflineDownload(Context context) {
        boolean download = false;
        String raw = AppSettings.getValue(context, AppSettings.PREF_OFFLINE_DOWNLOAD, "");
        download = (!raw.equalsIgnoreCase("")) ? true : false;
        return download;
    }

    public static void setFirstOpened(Context context, String flag) {
        AppSettings.setValue(context, AppSettings.PREF_IS_USER_FIRST_OPENED, flag);
    }

    public static boolean isFirstOpened(Context context) {
        boolean first_opened = false;
        String raw = AppSettings.getValue(context, AppSettings.PREF_IS_USER_FIRST_OPENED, "");
        first_opened = (!raw.equalsIgnoreCase("")) ? true : false;
        return first_opened;
    }

    public static void setReferOpened(Context context, String flag) {
        AppSettings.setValue(context, AppSettings.PREF_IS_USER_REFER_OPENED, flag);
    }

    public static boolean isReferOpened(Context context) {
        boolean refer_opened = false;
        String raw = AppSettings.getValue(context, AppSettings.PREF_IS_USER_REFER_OPENED, "");
        refer_opened = (!raw.equalsIgnoreCase("")) ? true : false;
        return refer_opened;
    }

    public static void setSuperPointsOpened(Context context, String flag) {
        AppSettings.setValue(context, AppSettings.PREF_IS_USER_SUPER_POINTS_OPENED, flag);
    }

    public static boolean isSuperPointsOpened(Context context) {
        boolean points_opened = false;
        String raw = AppSettings.getValue(context, AppSettings.PREF_IS_USER_SUPER_POINTS_OPENED, "");
        points_opened = (!raw.equalsIgnoreCase("")) ? true : false;
        return points_opened;
    }

    public static User getUser(Context context) {
        User user = null;
        String raw = AppSettings.getValue(context, AppSettings.PREF_USER_DATA, "");
        if (!raw.equalsIgnoreCase("")) {
//            Log.d("User Data", raw);
            user = new Gson().fromJson(raw, User.class);
        }
        return user;
    }

    public static void setUser(Context context, User user) {
        if (user != null) {
            //String prefixAuthToken = "mb" + user.getAuthToken();
            String prefixAuthToken = "Bearer" + user.getAuthToken();

            user.setAuthToken(prefixAuthToken);
            AppSettings.setValue(context, AppSettings.PREF_USER_DATA, new Gson().toJson(user));
            //Log.d("User Data", new Gson().toJson(user));
        } else
            AppSettings.setValue(context, AppSettings.PREF_USER_DATA, "");
    }


    public static PointsWallet getPointsWallet(Context context) {
        PointsWallet pointsWallet = null;
        String raw = AppSettings.getValue(context, AppSettings.PREF_USER_POINTS_WALLET, "");
        if (!raw.equalsIgnoreCase("")) {
//            Log.d("pointsWallet Data", raw);
            pointsWallet = new Gson().fromJson(raw, PointsWallet.class);
        }
        return pointsWallet;
    }

    public static Spanned getHtmlFormatString(String string)
    {
        Spanned formattedStr;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            formattedStr = Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY);
        } else {
            formattedStr = Html.fromHtml(string);
        }
        return formattedStr;
    }

    public static void setPointWallet(Context context, PointsWallet pointsWallet) {
        if (pointsWallet != null) {

            AppSettings.setValue(context, AppSettings.PREF_USER_POINTS_WALLET, new Gson().toJson(pointsWallet));
            //Log.d("User Data", new Gson().toJson(user));
        } else
            AppSettings.setValue(context, AppSettings.PREF_USER_POINTS_WALLET, "");
    }


//    public static void setEmployee(Context context, Employee employee) {
//        AppSettings.setValue(context, AppSettings.PREF_USER_DATA, new Gson().toJson(employee));
//    }

//    public static Employee getEmployee(Context context) {
//        Employee employee = null;
//        String raw = AppSettings.getValue(context, AppSettings.PREF_USER_DATA, "");
//        ;
//        if (!raw.equalsIgnoreCase("")) {
//            employee = new Gson().fromJson(raw, Employee.class);
//        }
//        return employee;
//    }

    public static String getGCMToken(Context context) {
        String raw = AppSettings.getValue(context, AppSettings.PREF_GCM_REGISTRATION_ID, "");
        //Log.d("GCMToken", raw);
        return raw;
    }

    public static void setGCMToken(Context context, String gcm) {
        AppSettings.setValue(context, AppSettings.PREF_GCM_REGISTRATION_ID, gcm);
    }

    public static String getKeyHash(Context context) {
        String keyHash = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        return keyHash;
    }

    public static String getCertificateSHA1Fingerprint(Context context) {
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    public static void intentRateUs(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("http://play.google.com/store/apps/details?id="
                            + activity.getPackageName())));
        }
    }

    public static void intentShareApp(Activity activity) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Mera-Break. Download at " + Uri.parse("http://play.google.com/store/apps/details?id="
                        + activity.getPackageName()));
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);

    }

    public static void challengeShareApp(Activity activity, String title, String windowTitle, String uri) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sendIntent.putExtra(Intent.EXTRA_TEXT, uri);
        activity.startActivity(Intent.createChooser(sendIntent, windowTitle));

    }

    public static void inviteFriend(Activity activity, String title, String windowTitle, String uri) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sendIntent.putExtra(Intent.EXTRA_TEXT, uri);
        activity.startActivity(Intent.createChooser(sendIntent, windowTitle));

    }

    public static void intentSendFeedBack(Activity activity, String[] emailIds,
                                          String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, emailIds);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        activity.startActivity(Intent.createChooser(intent, ""));
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }

    }

    public static boolean textIsEmpty(String value) {
        boolean empty = false;

        if (value == null)
            return true;

        String message = value.trim();

        if (TextUtils.isEmpty(message)) {
            empty = true;
        }

        boolean isWhitespace = message.matches("^\\s*$");

        if (isWhitespace) {
            empty = true;
        }

        return empty;
    }

    public static String uniqueDeviceID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public static String getLocalIpv4Address() {
        try {
            String ipv4;
            List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (nilist.size() > 0) {
                for (NetworkInterface ni : nilist) {
                    List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                    if (ialist.size() > 0) {
                        for (InetAddress address : ialist) {
                            if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
                                return ipv4;
                            }
                        }
                    }

                }
            }

        } catch (SocketException ex) {

        }
        return "";
    }

    public static boolean checkConnection(Context c) {

        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            System.out.println("true wifi");
            return true;
        }

        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            System.out.println("true edge");
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            System.out.println("true net");
            return true;
        }

        System.out.println("false");
        return false;
    }

    public static void clearApplicationData(Context context) {

        context.deleteDatabase(Constants.DB_NAME);

        AppSettings.clearAllPrefs(context);

        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG",
                            "**************** File /data/data/APP_PACKAGE/" + s
                                    + " DELETED *******************");
                }
            }
        }

    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }


//    public static void setMapMarker(GoogleMap googleMap, LatLng latLng, int id) {
//        MarkerOptions options = new MarkerOptions();
//        options.position(latLng);
//
//        options.icon(BitmapDescriptorFactory.fromResource(id));
//        googleMap.addMarker(options);
//    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static boolean isSelectedDateLesser(String date) {

        boolean isRight = false;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date selectedDate = simpleDateFormat.parse(date);
            Date currentDate = new Date();

            if (selectedDate.before(currentDate) && selectedDate.getDate() < currentDate.getDate()) {
                isRight = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isRight;
    }

    public static String getTimeToSimpleTime(String time) {

        String changedTime = "";

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa");
        try {
            Date date1 = simpleDateFormat1.parse(time);
            changedTime = simpleDateFormat2.format(date1).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return changedTime;
    }

    public static String getTimeToSimpleTime(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String hour = calendar.get(Calendar.HOUR) + "";
        String min = calendar.get(Calendar.MINUTE) + "";


        String changedTime = "";

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa");
        try {
            Date date1 = simpleDateFormat1.parse(hour + ":" + min);
            changedTime = simpleDateFormat2.format(date1).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return changedTime;

    }

    public static String getDateToSimpleDate(String date) {
        String changedDate = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date1 = simpleDateFormat.parse(date);
            changedDate = simpleDateFormat2.format(date1).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return changedDate;
    }

    public static void saveOrUpdateChallenge(Context context, Challenge challenge) {
        List<Challenge> challenges = getAllChallenges(context);
        boolean exist = false;
        for (int i = 0; i < challenges.size(); i++) {
            if (challenges.get(i).equals(challenge)) {
                exist = true;
                challenges.set(i, challenge);
                break;
            }
        }
        if (!exist) {
            challenges.add(challenge);
        }
        AppSettings.setValue(context, AppSettings.PREF_CHALLENGES, new Gson().toJson(challenges));
    }

    public static void saveHomeChallenge(Context context, List<Challenge> items) {
        if (items != null)
            AppSettings.setValue(context, AppSettings.PREF_HOME_CHALLENGES, new Gson().toJson(items));
        else
            AppSettings.setValue(context, AppSettings.PREF_HOME_CHALLENGES, "");
    }

    public static void saveHomeRaffles(Context context, List<Raffle> items) {
        if (items != null)
            AppSettings.setValue(context, AppSettings.PREF_HOME_RAFFLES, new Gson().toJson(items));
        else
            AppSettings.setValue(context, AppSettings.PREF_HOME_RAFFLES, "");
    }

    public static void saveHomeStores(Context context, List<Store> items) {
        if (items != null)
            AppSettings.setValue(context, AppSettings.PREF_HOME_STORES, new Gson().toJson(items));
        else
            AppSettings.setValue(context, AppSettings.PREF_HOME_STORES, "");
    }

    public static Challenge getChallenge(Context context, Challenge challenge) {
        Challenge newChallenge = challenge;
        List<Challenge> challenges = getAllChallenges(context);
        boolean exist = false;
        for (int i = 0; i < challenges.size(); i++) {
            if (challenges.get(i).equals(challenge)) {
                newChallenge = challenges.get(i);
                break;
            }
        }
        return newChallenge;
    }

    public static boolean deleteChallenge(Context context, Challenge challenge) {

        boolean deleted = false;

        List<Challenge> challenges = getAllChallenges(context);
        for (int i = 0; i < challenges.size(); i++) {
            if (challenges.get(i).equals(challenge)) {
                challenges.remove(i);
                deleted = true;
                break;
            }
        }

        if (challenges.size() > 0)
            AppSettings.setValue(context, AppSettings.PREF_CHALLENGES, new Gson().toJson(challenges));
        else
            AppSettings.setValue(context, AppSettings.PREF_CHALLENGES, "");

        return deleted;
    }

    public static boolean deleteDownChallenge(Context context, String downloadedChallenges) {

        boolean deleted = false;

        List<DownloadedChallenges> challenges = getDownChalSlugs(context);
        for (int i = 0; i < challenges.size(); i++) {
            if (challenges.get(i).getSlug().equals(downloadedChallenges)) {
                challenges.remove(i);
                deleted = true;
                break;
            }
        }

        if (challenges.size() > 0)
            AppSettings.setValue(context, AppSettings.PREF_DOWN_CHALLENGES, new Gson().toJson(challenges));
        else
            AppSettings.setValue(context, AppSettings.PREF_DOWN_CHALLENGES, "");

        return deleted;
    }

    public static List<Challenge> getAllChallenges(Context context) {
        List<Challenge> items = new ArrayList<Challenge>();
        String raw = AppSettings.getValue(context, AppSettings.PREF_CHALLENGES, "");
        Type collectionType = new TypeToken<Collection<Challenge>>() {
        }.getType();
        if (!raw.equalsIgnoreCase("")) {
            items = new Gson().fromJson(raw, collectionType);
        }
        return items;
    }

    public static List<Challenge> getHomeChallenges(Context context) {
        List<Challenge> items = new ArrayList<Challenge>();
        String raw = AppSettings.getValue(context, AppSettings.PREF_HOME_CHALLENGES, "");
        Type collectionType = new TypeToken<Collection<Challenge>>() {
        }.getType();
        if (!raw.equalsIgnoreCase("")) {
            items = new Gson().fromJson(raw, collectionType);
        }
        return items;
    }

    public static List<Raffle> getHomeRaffles(Context context) {
        List<Raffle> items = new ArrayList<Raffle>();
        String raw = AppSettings.getValue(context, AppSettings.PREF_HOME_RAFFLES, "");
        Type collectionType = new TypeToken<Collection<Raffle>>() {
        }.getType();
        if (!raw.equalsIgnoreCase("")) {
            items = new Gson().fromJson(raw, collectionType);
        }
        return items;
    }

    public static List<Store> getHomeStores(Context context) {
        List<Store> items = new ArrayList<Store>();
        String raw = AppSettings.getValue(context, AppSettings.PREF_HOME_STORES, "");
        Type collectionType = new TypeToken<Collection<Store>>() {
        }.getType();
        if (!raw.equalsIgnoreCase("")) {
            items = new Gson().fromJson(raw, collectionType);
        }
        return items;
    }

    public static void setImage(ImageLoader imageLoader, String path, ImageView image) {
        if (path != null && path.contains("http")) {
            imageLoader.DisplayImage(path, image);
        } else {
            Log.d("Image Path ", "Not Defined");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void changeSystemBarColor(Activity activity, int color) {
//        SystemBarTintManager mTintManager = new SystemBarTintManager(activity);
//        mTintManager.setStatusBarTintEnabled(true);
//        mTintManager.setTintColor(color);


        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(activity.getResources().getColor(color));
        //window.setStatusBarColor(ContextCompat.getColor(activity,color));

    }

    public static int getScreenHeight(Context context) {
        int measuredHeight;
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            measuredHeight = size.y;
        } else {
            Display d = wm.getDefaultDisplay();
            measuredHeight = d.getHeight();
        }

        return measuredHeight;

    }

    public static void saveStartChallengeDetails(Context context, StartChallenge startChallenge) {
        if (startChallenge != null) {
            AppSettings.setValue(context, AppSettings.PREF_START_CHALLENGE, new Gson().toJson(startChallenge));
        }
    }

    public static StartChallenge getStartChallengeDetails(Context context) {
        StartChallenge startChallenge = null;
        String values = AppSettings.getValue(context, AppSettings.PREF_START_CHALLENGE, "");
        if (!values.equalsIgnoreCase("")) {
            //Log.d("StartChallengeDetails", values);
            startChallenge = new Gson().fromJson(values, StartChallenge.class);
        }
        return startChallenge;
    }


    public static void saveUserAccountDetails(Context context, AccountDetails ad) {
        AppSettings.setValue(context, AppSettings.PREF_ACCOUNT_DETAILS, new Gson().toJson(ad));
    }

    public static AccountDetails getUserAccountDetails(Context context) {
        AccountDetails ad = null;
        String values = AppSettings.getValue(context, AppSettings.PREF_ACCOUNT_DETAILS, "");
        if (!values.equalsIgnoreCase("")) {
            //Log.d("AccountDetails", values);
            ad = new Gson().fromJson(values, AccountDetails.class);
        }
        return ad;
    }

    public static String convertToCommaSeparated(List<String> values) {
        StringBuilder builder = new StringBuilder();
        // Append all Integers in StringBuilder to the StringBuilder.
        for (String value : values) {
            builder.append(value);
            builder.append(",");
        }
        // Remove last delimiter with setLength.
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }


    public static void saveCategories(Context context, List<Category> categories) {
        if (categories != null && categories.size() > 0) {
            AppSettings.setValue(context, AppSettings.PREF_CATEGORIES, new Gson().toJson(categories));
        }
    }

    public static List<Category> getCategories(Context context) {
        String value = AppSettings.getValue(context, AppSettings.PREF_CATEGORIES, "");
        List<Category> items = new ArrayList<>();
        if (!value.equalsIgnoreCase("")) {
            Type types = new TypeToken<Collection<Category>>() {
            }.getType();
            items = new Gson().fromJson(value, types);
        }
        return items;
    }

    public static void saveDownChalSlugs(Context context, List<DownloadedChallenges> downloadedChallenges) {
        if (downloadedChallenges != null && downloadedChallenges.size() > 0) {
            AppSettings.setValue(context, AppSettings.PREF_DOWN_CHALLENGES, new Gson().toJson(downloadedChallenges));
            //Log.d("downlo challenge slugs", new Gson().toJson(downloadedChallenges));
        }
    }

    public static List<DownloadedChallenges> getDownChalSlugs(Context context) {
        String value = AppSettings.getValue(context, AppSettings.PREF_DOWN_CHALLENGES, "");
        List<DownloadedChallenges> items = new ArrayList<>();
        if (!value.equalsIgnoreCase("")) {
            Type types = new TypeToken<Collection<DownloadedChallenges>>() {
            }.getType();
            items = new Gson().fromJson(value, types);
        }
        //Log.d("downlo challenge slugs1", value);
        return items;
    }

    public static void savePlans(Context context, List<HomeRechargePlans> homeRechargePlans) {
        if (homeRechargePlans != null && homeRechargePlans.size() > 0) {
            for (HomeRechargePlans homre : homeRechargePlans) {
                for (RechargePlans repl : homre.getPlans()) {
                    repl.setTabName(homre.getName());
                }
            }
            AppSettings.setValue(context, AppSettings.PREF_RECHARGE_PLANS, new Gson().toJson(homeRechargePlans));
        }
    }

    public static List<HomeRechargePlans> getPlans(Context context) {
        String value = AppSettings.getValue(context, AppSettings.PREF_RECHARGE_PLANS, "");
        List<HomeRechargePlans> items = new ArrayList<>();
        if (!value.equalsIgnoreCase("")) {
            Type types = new TypeToken<Collection<HomeRechargePlans>>() {
            }.getType();
            items = new Gson().fromJson(value, types);
        }
        return items;
    }

    public static void saveLatLng(Context context, String latLng) {
        AppSettings.setValue(context, AppSettings.PREF_LAT_LNG, latLng);
    }

    public static String getLatLng(Context context) {
        return AppSettings.getValue(context, AppSettings.PREF_LAT_LNG, "");
    }

    public static void saveSpinId(Context context, String spin_id) {
        AppSettings.setValue(context, AppSettings.PREF_SPIN_ID, spin_id);
    }

    public static String getSpinId(Context context) {
        return AppSettings.getValue(context, AppSettings.PREF_SPIN_ID, "");
    }

    public static void saveCampaignReferrer(Context context, String referrer) {
        AppSettings.setValue(context, AppSettings.PREF_CAMPAIGN_REFERRER, referrer);
    }

    public static String getCampaignReferrer(Context context) {
        return AppSettings.getValue(context, AppSettings.PREF_CAMPAIGN_REFERRER, "");
    }

    public static void saveCampaignContent(Context context, String referrer) {
        AppSettings.setValue(context, AppSettings.PREF_CAMPAIGN_CONTENT, referrer);
    }

    public static String getCampaignContent(Context context) {
        return AppSettings.getValue(context, AppSettings.PREF_CAMPAIGN_CONTENT, "");
    }

    public static void saveCampaignName(Context context, String referrer) {
        AppSettings.setValue(context, AppSettings.PREF_CAMPAIGN_NAME, referrer);
    }

    public static String getCampaignName(Context context) {
        return AppSettings.getValue(context, AppSettings.PREF_CAMPAIGN_NAME, "");
    }

    public static void saveSelectedCity(Context context, String cityName) {
        AppSettings.setValue(context, AppSettings.PREF_SELECTED_CITY, cityName);
    }

    public static String getSelectedCity(Context context) {
        return AppSettings.getValue(context, AppSettings.PREF_SELECTED_CITY, "");
    }

    public static void saveSelectedCityId(Context context, String cityId) {
        AppSettings.setValue(context, AppSettings.PREF_SELECTED_CITY_ID, cityId);
    }

    public static String getSelectedCityId(Context context) {
        return AppSettings.getValue(context, AppSettings.PREF_SELECTED_CITY_ID, "");
    }

    public static String getDeviceDPI(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        String dpi = "xhdpi";
        if (density == 0.75)
            dpi = "ldpi";
        else if (density == 1.0)
            dpi = "mdpi";
        else if (density == 1.5)
            dpi = "hdpi";
        else if (density == 2.0)
            dpi = "xhdpi";
        else if (density == 3.0)
            dpi = "xxhdpi";
        else if (density == 4.0)
            dpi = "xxxhdpi";

        return dpi;
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static void setOwnTeamSelected(Context context, String key) {
        AppSettings.setValue(context, AppSettings.PREF_OWN_TEAM_SELECTED_KEY, key);
    }

    public static void setOwnTeamNotSelected(Context context, String val) {
        AppSettings.setValue(context, AppSettings.PREF_IS_HOME_TEAM_SELECTED, val);
    }

    public static String getOwnTeamNotSelected(Context context) {
        String raw = AppSettings.getValue(context, AppSettings.PREF_IS_HOME_TEAM_SELECTED, "");
        return raw;
    }

    public static String getOwnTeamSelected(Context context) {
        String raw = AppSettings.getValue(context, AppSettings.PREF_OWN_TEAM_SELECTED_KEY, "");
        return raw;
    }

    public static void setIPLConfig(Context context, IPLConfig iplConfig) {
        AppSettings.setValue(context, AppSettings.PREF_IPL_CONFIG, new Gson().toJson(iplConfig));
    }

    public static IPLConfig getIPLConfig(Context context) {
        IPLConfig iplConfig = new IPLConfig();
        String raw = AppSettings.getValue(context, AppSettings.PREF_IPL_CONFIG, "");
        if (!Util.textIsEmpty(raw)) {
            iplConfig = new Gson().fromJson(raw, IPLConfig.class);
        }
        return iplConfig;
    }

    public static void setQuizRightAnswers(Context context, String value) {
        AppSettings.setValue(context, AppSettings.PREF_QUIZ_RIGHT_ANSWERS, value);
    }

    public static String getQuizRightAnswers(Context context) {
        String raw = AppSettings.getValue(context, AppSettings.PREF_QUIZ_RIGHT_ANSWERS, "");
        return raw;
    }

    public static boolean isBetween(int min, int max, int val) {
        return val >= min && val <= max;
    }
}
