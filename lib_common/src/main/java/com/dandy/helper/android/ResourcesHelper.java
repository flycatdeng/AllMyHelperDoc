package com.dandy.helper.android;

import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ResourcesHelper {
	public static final String LAYOUT = "layout";
	public static final String DRAWABLE = "drawable";
	public static final String STYLE = "style";
	public static final String ID = "id";
	public static final String DIMEN = "dimen";
	private static String SUFFIX = ".";
	private static String SUFFIX_LAYOUT = ".xml";
	private static String[] SUFFIX_DRAWABLE = { ".jpg", ".png" };
	private static String EMPTY = "";

	public static int getIdentifier(Context context, String name, String defType) {
		if (TextUtils.isEmpty(name)) {
			return -1;
		}
		if (name.contains(SUFFIX)) {
			if (name.contains(SUFFIX_LAYOUT)) {
				name = name.toLowerCase(Locale.getDefault());
				name = name.replace(SUFFIX_LAYOUT, EMPTY);
			} else if (name.contains(SUFFIX_DRAWABLE[0])) {
				name = name.toLowerCase(Locale.getDefault());
				name = name.replace(SUFFIX_DRAWABLE[0], EMPTY);
			} else if (name.contains(SUFFIX_DRAWABLE[1])) {
				name = name.toLowerCase(Locale.getDefault());
				name = name.replace(SUFFIX_DRAWABLE[1], EMPTY);
			}
		}
		try {
			return context.getResources().getIdentifier(name, defType, context.getPackageName());
		} catch (Exception e) {
			return -1;
		}
	}

	public static int[] getIDsByName(Context context, String className, String name) {
		String packageName = context.getPackageName();
		Class r = null;
		int[] ids = null;
		try {
			r = Class.forName(packageName + ".R");
			Class[] classes = r.getClasses();
			Class desireClass = null;
			for (int i = 0; i < classes.length; i++) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}
			if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null)
					&& (desireClass.getField(name).get(desireClass).getClass().isArray())) {
				ids = (int[]) desireClass.getField(name).get(desireClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}

	public static int getIdByName(Context context, String className, String name) {
		String packageName = context.getPackageName();
		Class r = null;
		int id = 0;
		try {
			r = Class.forName(packageName + ".R");
			Class[] classes = r.getClasses();
			Class desireClass = null;
			for (int i = 0; i < classes.length; i++) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}
			if (desireClass != null) {
				id = desireClass.getField(name).getInt(desireClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static int getLayoutIdentifier(Context context, String name) {
		return getIdentifier(context, name, LAYOUT);
	}

	public static int getDrawableIdentifier(Context context, String name) {
		return getIdentifier(context, name, DRAWABLE);
	}

	public static int getStyleIdentifier(Context context, String name) {
		return getIdentifier(context, name, STYLE);
	}

	public static int getIdIdentifier(Context context, String name) {
		return getIdentifier(context, name, ID);
	}

	public static void display(ImageView iv, String icon) {
		int drawableId = ResourcesHelper.getDrawableIdentifier(iv.getContext(), icon);
		if (drawableId != -1) {
			iv.setImageResource(drawableId);
		}
	}

	public static void setTextStyle(TextView tv, String style) {
		int styleId = ResourcesHelper.getStyleIdentifier(tv.getContext(), style);
		if (styleId != -1) {
			tv.setTextAppearance(tv.getContext(), styleId);
		}
	}

	public static int getDimenIdentifier(Context context, String name) {
		return getIdentifier(context, name, DIMEN);
	}

    public static int getIdByIdNameFromR(Context context, String rSubClassName, String name) {
        int id = context.getResources().getIdentifier(name, rSubClassName, context.getPackageName());
        return id;
    }

    public static int getIdByIdNameFromLayout(Context context, String name) {
        return getIdByIdNameFromR(context, LAYOUT, name);
    }
}
