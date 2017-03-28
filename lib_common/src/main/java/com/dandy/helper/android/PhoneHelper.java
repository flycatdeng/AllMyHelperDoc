package com.dandy.helper.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dandy.helper.java.ObjectHelper;
import com.dandy.helper.java.StringHelper;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 手机状态帮助类 如IMEI,手机型号,Rom版本
 * 
 * @author dengchukun
 * 
 */
public class PhoneHelper {
	private static final String TAG = "PhoneHelper";
	// sd卡的最小空间
	public static final int MIN_SDCARD_SIZE = 50 * 1024 * 1024;

	/**
	 * 检查sd卡的空间是否足够
	 * 
	 * @param sizeMb
	 * @return
	 */
	public static boolean isAvaiableSpace() {
		boolean ishasSpace = false;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = (blocks * blockSize);
			if (availableSpare > MIN_SDCARD_SIZE) {
				ishasSpace = true;
			} else {
				LogHelper.d(TAG, "==剩余空间MB,availableSpare = " + availableSpare / (1024 * 1024));
			}
		}
		return ishasSpace;
	}

	/**
	 * 判断用户到底使用的是模拟器还是真机
	 * 
	 * @return
	 */
	public static boolean isEmulator(Context context) {
		if (Build.MODEL.toLowerCase(Locale.getDefault()).equals("sdk")
				|| (Build.MODEL.toLowerCase(Locale.getDefault()).equals("google_sdk"))) {
			return true;
		}

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (imei == null || imei.equals("000000000000000")) {
			return true;
		}

		// 是真机
		return false;
	}

	/**
	 * 判断是否为金立手机
	 * 
	 * @return
	 */
	public static boolean isGioneePhone() {
		return StringHelper.equalsIgnoreCase("gionee", getPhoneManufacturer());
	}

	/**
	 * 得到手机生产商
	 * 
	 * @return
	 */
	public static String getPhoneManufacturer() {
		try {
			String className = "android.os.SystemProperties";
			String methodName = "get";
			Class<?>[] parameterTypes = new Class<?>[] { String.class, String.class };
			Object[] args = new Object[] { "ro.product.manufacturer", "" };
			Object roProductManufacturer = ReflectionHelper.getMethodReturnObject(className, methodName, parameterTypes,
					args);
			return roProductManufacturer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断手机是否是飞行模式
	 */
	public static boolean isAirplaneMode(Context context) {
		int isAirplaneMode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		return (isAirplaneMode == 1) ? true : false;
	}

	/**
	 * 获取IMEI
	 */
	public static String getIMEINumber(Context context) {
		TelephonyManager telManage = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		String deviceId = telManage.getDeviceId();
		if (deviceId == null || deviceId.length() <= 0) {
			return "";
		}
		return deviceId;
	}

	/**
	 * 获取手机型号
	 */
	public static String getModel() {
		String model = Build.MODEL;
		if (model == null || model.length() <= 0) {
			return "";
		}
		return model;
	}

	/**
	 * 获取Rom版本
	 */
	public static String getRomVersion() {
		String romVersion = "";
		try {
			String className = "android.os.SystemProperties";
			String methodName = "get";
			Class<?>[] parameterTypes = new Class<?>[] { String.class, String.class };
			Object[] args = new Object[] { "ro.gn.gnromvernumber", "" };
			Object roProductManufacturer = ReflectionHelper.getMethodReturnObject(className, methodName, parameterTypes,
					args);

			romVersion = roProductManufacturer.toString();

			String regex = "\\d";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(romVersion);
			int index = 0;
			if (matcher.find() && !ObjectHelper.isNull(matcher.group())) {
				index = matcher.start();
			}
			romVersion = romVersion.substring(index);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ObjectHelper.isNull(romVersion)) {
			// 我用 Build.VERSION.RELEASE，获取到的是操作系统的版本，我这里想获取ROM的版本
			romVersion = Build.VERSION.RELEASE;
			// SystemProperties.get("ro.build.display.id");

		}
		return romVersion;
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * SDCARD是否存
	 */
	public static boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取手机内部剩余存储空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内部总的存储空间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	private static final int ERROR = -1;

	/**
	 * 获取SDCARD剩余存储空间
	 * 
	 * @return
	 */
	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	/**
	 * 获取SDCARD总的存储空间
	 * 
	 * @return
	 */
	public static long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	/**
	 * 获取系统总内存
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 * @return 总内存大单位为B。
	 */
	public static long getTotalMemorySize(Context context) {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
			br.close();
			return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前可用内存，返回数据以字节为单位。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 * @return 当前可用内存单位为B。
	 */
	public static long getAvailableMemory(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}

}
