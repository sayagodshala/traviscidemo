package com.merabreak;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class FontUtils {

    public static interface FontTypes {
        public static String LIGHT = "Light";
        public static String BOLD = "Bold";
    }

    private static Map<String, String> fontMap = new HashMap<String, String>();

    static {
        fontMap.put(FontTypes.LIGHT, "fonts/roboto_regular.ttf");
        fontMap.put(FontTypes.BOLD, "fonts/roboto_bold.ttf");
      //  fontMap.put(FontTypes.LIGHT, "fonts/Montserrat_Regular.ttf");
      //  fontMap.put(FontTypes.BOLD, "fonts/Montserrat_SemiBold.ttf");
    }

    private static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();

    public static void setDefaultFont(Context context, View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setDefaultFont(context, ((ViewGroup) view).getChildAt(i));
            }
        } else if (view instanceof TextView) {
            Typeface currentTypeface = ((TextView) view).getTypeface();
            ((TextView) view).setTypeface(getDefaultTypeface(context,
                    currentTypeface));
        } else if (view instanceof EditText) {
            Typeface currentTypeface = ((EditText) view).getTypeface();
            ((EditText) view).setTypeface(getDefaultTypeface(context,
                    currentTypeface));
        } else if (view instanceof Button) {
            Typeface currentTypeface = ((Button) view).getTypeface();
            ((Button) view).setTypeface(getDefaultTypeface(context,
                    currentTypeface));
        }
    }

    private static Typeface getDefaultTypeface(Context context, String fontType) {
        String fontPath = fontMap.get(fontType);
        if (!typefaceCache.containsKey(fontType)) {
            typefaceCache.put(fontType,
                    Typeface.createFromAsset(context.getAssets(), fontPath));
        }
        return typefaceCache.get(fontType);
    }

    private static Typeface getDefaultTypeface(Context context,
                                               Typeface originalTypeface) {
        String defaultFontType = FontTypes.LIGHT;
        if (originalTypeface != null) {
            int style = originalTypeface.getStyle();
            switch (style) {
                case Typeface.BOLD:
                    defaultFontType = FontTypes.BOLD;
            }
        }
        return getDefaultTypeface(context, defaultFontType);
    }


}