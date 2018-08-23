package com.merabreak.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.merabreak.Constants;
import com.merabreak.models.challenge.Challenge;

import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static DbHelper instance = null;

    private SqlLiteHelper helper;
    private SQLiteDatabase db;
    private Context mContext;

    public DbHelper(Context context) {
        mContext = context;
        helper = new SqlLiteHelper(context);
        db = helper.getWritableDatabase();
    }

    public void closeDb() {
        db.close();
        helper.close();
    }

    public void dropTable() {
        SQLiteDatabase sdb;
        sdb = instance.mContext.openOrCreateDatabase(Constants.DB_NAME, Context.MODE_WORLD_WRITEABLE, null);
        instance.helper.dropTable(sdb);
    }

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    private class SqlLiteHelper extends SQLiteOpenHelper {

        private static final int DB_VERSION = 2;

        public SqlLiteHelper(Context context) {
            super(context, Constants.DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String challenges = "CREATE TABLE  IF NOT EXISTS challenges"
                    + "( _id INTEGER PRIMARY KEY  AUTOINCREMENT, id text, status text, challenge text)";
            db.execSQL(challenges);
            onUpgrade(db, db.getVersion(), DB_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            if (oldVersion == 1 && newVersion == 2) {
//                version2Schema();
//            }
            if (oldVersion < newVersion) {
                version2Schema(db);
            }

        }

        private void dropTable(SQLiteDatabase db) {
            String sql = "DROP TABLE IF EXISTS challenges";
            String team = "DROP TABLE IF EXISTS team";
            String player = "DROP TABLE IF EXISTS player";
            String favTeam = "DROP TABLE IF EXISTS fav_team";
            String fixture = "DROP TABLE IF EXISTS fixture";
            String quiz = "DROP TABLE IF EXISTS quiz";
            db.execSQL(sql);
            db.execSQL(team);
            db.execSQL(player);
            db.execSQL(favTeam);
            db.execSQL(fixture);
            db.execSQL(quiz);
        }

        private void version2Schema(SQLiteDatabase db) {
            String team = "CREATE TABLE  IF NOT EXISTS team"
                    + "(id text PRIMARY KEY, name text, short_name text, captain text, " +
                    "keeper text," +
                    "owner text," +
                    "home_team integer default 0," +
                    "logo text)";

            String player = "CREATE TABLE  IF NOT EXISTS player"
                    + "( id text PRIMARY KEY, name text, short_name text, role text, " +
                    "picture text," +
                    "matches integer," +
                    "runs integer," +
                    "wickets integer," +
                    "overseas text," +
                    "playing_xi text," +
                    "team_id text)";

            String favTeam = "CREATE TABLE  IF NOT EXISTS fav_team"
                    + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, team_id text)";

            String fixture = "CREATE TABLE IF NOT EXISTS fixture"
                    + "(id integer PRIMARY KEY, match_title text, team_a text, team_b text, " +
                    "venue text," +
                    "match_date text," +
                    "match_time text," +
                    "winning_team text," +
                    "winning_msg text," +
                    "fav_team text," +
                    "fav_team_selected text," +
                    "match_time_expired text," +
                    "status text," +
                    "season_match_key text)";

            String quiz = "CREATE TABLE IF NOT EXISTS quiz"
                    + "(question_id integer PRIMARY KEY, question text, options text, played text, right text, match_key text)";

            db.execSQL(favTeam);
            db.execSQL(team);
            db.execSQL(player);
            db.execSQL(fixture);
            db.execSQL(quiz);
        }

    }

    public boolean saveChallenge(Challenge challenge, String status) {
        boolean flag = true;
        String sql = "select * from challenges where id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(challenge.getSlug())});
        ContentValues values = new ContentValues();
        values.put("id", challenge.getSlug());
        values.put("status", status);
        values.put("challenge", new Gson().toJson(challenge));

        if (cursor.getCount() > 0) {
            int count = db.update("challenges", values, "id='" + challenge.getSlug() + "'", null);
            if (count != 0) {
                //Log.d(DbHelper.class.getSimpleName(), "Successfully record updated - " + new Gson().toJson(challenge));
            } else {
                //Log.d(DbHelper.class.getSimpleName(), "Error while updating - " + new Gson().toJson(challenge));
                flag = false;
            }
        } else {
            long count = db.insert("challenges", "_id", values);
            if (count != -1) {
                //Log.d(DbHelper.class.getSimpleName(), "Successfully record inserting - " + new Gson().toJson(challenge));
            } else {
                //Log.d(DbHelper.class.getSimpleName(), "Error while inserting - " + new Gson().toJson(challenge));
                flag = false;
            }
        }
        cursor.close();
        return flag;
    }

    public List<Challenge> getChallengesByStatus(String status) {
        List<Challenge> challenges = new ArrayList<Challenge>();
        String sql = " select challenge from challenges where status = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{status});
        Challenge challengeToAdd;
        while (cursor.moveToNext()) {
            challengeToAdd = new Gson().fromJson(cursor.getString(0), Challenge.class);
            challenges.add(challengeToAdd);
            challengeToAdd = null;
        }
        cursor.close();
        return challenges;
    }

    public List<Challenge> getAllChallenges() {
        List<Challenge> challenges = new ArrayList<Challenge>();
        String sql = " select challenge from challenges";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        Challenge challengeToAdd;
        while (cursor.moveToNext()) {
            challengeToAdd = new Gson().fromJson(cursor.getString(0), Challenge.class);
            if (!challengeToAdd.isOfflineCompleted()) {
                challenges.add(challengeToAdd);
            }
            challengeToAdd = null;
        }
        cursor.close();
        return challenges;
    }

    public List<Challenge> getAllChallengesThatNeedsToBeSubmitted() {
        List<Challenge> challenges = new ArrayList<Challenge>();
        String sql = " select challenge from challenges";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        Challenge challengeToAdd;
        while (cursor.moveToNext()) {
            challengeToAdd = new Gson().fromJson(cursor.getString(0), Challenge.class);
            if (challengeToAdd.isOfflineCompleted()) {
                challenges.add(challengeToAdd);
            }
            challengeToAdd = null;
        }
        cursor.close();
        return challenges;
    }

    public Challenge getChallengeById(String id) {

        String sql = " select challenge, status from challenges where id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        Challenge challenge = null;

        while (cursor.moveToNext()) {
            challenge = new Gson().fromJson(cursor.getString(0), Challenge.class);
        }
        cursor.close();
        //Log.d("Challenge Data", new Gson().toJson(challenge));
        return challenge;
    }

    public boolean deleteChallengeById(String id) {
        String table = "challenges";
        String whereClause = "id" + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.delete(table, whereClause, whereArgs);
        int ids = db.delete(table, whereClause, whereArgs);

        if (ids > 0)
            return true;
        else
            return false;

    }

    public void clearTable() {
        db.delete("challenges", null, null);
        db.delete("team", null, null);
        db.delete("player", null, null);
        db.delete("fav_team", null, null);
        db.delete("fixture", null, null);
        db.delete("quiz", null, null);
    }

    public void clearFixtureAndTeamTable() {
        db.delete("team", null, null);
        db.delete("player", null, null);
        db.delete("fixture", null, null);
    }

    public void clearQuizTable() {
        db.delete("quiz", null, null);
    }
}
