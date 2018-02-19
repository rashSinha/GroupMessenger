package edu.buffalo.cse.cse486586.groupmessenger2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static android.R.attr.value;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {


    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";
    //private TreeMap<String, String> treeMap = new TreeMap<String, String>();

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /*
         * TODO: You need to implement this method. Note that values will have two columns (a key
         * column and a value column) and one row that contains the actual (key, value) pair to be
         * inserted.
         * 
         * For actual storage, you can use any option. If you know how to use SQL, then you can use
         * SQLite. But this is not a requirement. You can use other storage options, such as the
         * internal storage option that we used in PA1. If you want to use that option, please
         * take a look at the code for PA1.
         */


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(values.getAsString(KEY_FIELD), values.getAsString(VALUE_FIELD));

        editor.commit();

        //String message = values.getAsString(VALUE_FIELD);
        //treeMap.put(values.getAsString(KEY_FIELD), message);


        Log.v("insert", values.toString());
        return uri;
    }

    @Override
    public boolean onCreate() {
        // If you need to perform any one-time initialization task, please do it here.
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         *
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         *
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, 0);

        MatrixCursor matrixCursor = new MatrixCursor(new String[] {KEY_FIELD, VALUE_FIELD});

//        String retMsg = "";
//        int counter = Integer.parseInt(selection);
//        //iterate and sotre msg
//        for (Map.Entry<String  , String> entry : treeMap.entrySet()) {
//            //
//            if(counter == 0) {
//                retMsg = entry.getValue();
//                break;
//            }
//            counter++;
//            //System.out.println("key ->" + entry.getKey() + ", value->" + entry.getValue());
//        }

        matrixCursor.addRow(new String[] {selection, sharedPreferences.getString(selection,"")});

        Log.v("query", selection);
        return matrixCursor;
    }
}