package com.example.events_live.presentation.sharedPreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.events_live.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public class SharedPreference
	{
		SharedPreferences sharedPreferences;
		SharedPreferences.Editor shareEditor;

		private static SharedPreference ourInstance = new SharedPreference();

		public static final String AUTH_TOKEN_KEY="auth_key";

		public static final String EMPTY_KEY="empty";

        public static final String SAVED_MATCHES_TOKEN="saved_matches_key";
        @NotNull
        public static final String MAX_PAGE_REACHED="max_page_reached";

		public static final String ALL_LEAGUES_TAG="All Leagues";
        @Nullable
        public static final String MATCH_ID_TOKEN="matchid";
        @NotNull
        public static final String TODAY_ITEM="Today Matches";
		@NotNull
		public static final String PAST_ITEM="Previous Matches";
		@NotNull
		public static final String FUTURE_ITEM="Future Matches";
        @Nullable
        public static final String LOCALE_KEY="LOCALE_KEY";
		@NotNull
		public static final String ENGLISH="en";
		@NotNull
		public static final String CHINESE="cn";
		public static final String INDONESIAN="id";
		public static final String VIETNAMESE="vi";
		public static final String THAI="th";
        @Nullable
        public static final String IS_FIRST_TIME="firsttime";
        @Nullable
        public static final String HAS_SELECTED_FIRST_LANG="has selected first lang";

        public static SharedPreference getInstance()
			{
				return ourInstance;
			}

		private SharedPreference()
			{
			}

		public void saveIntToPreferences(String key, int value, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}
				shareEditor = sharedPreferences.edit();
				shareEditor.putInt(key, value);
				shareEditor.commit();
			}

		public int getIntValueFromPreference(String key, int defaultValue, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}

				return sharedPreferences.getInt(key, defaultValue);

			}

		public void saveStringToPreferences(String key, String value, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}

				shareEditor = sharedPreferences.edit();
				shareEditor.putString(key, value);
				shareEditor.commit();
			}

		public void saveBooleanToPreferences(String key, Boolean value, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}
				shareEditor = sharedPreferences.edit();
				shareEditor.putBoolean(key, value);
				shareEditor.commit();
			}

		public void saveLongToPreferences(String key, long value, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}
				shareEditor = sharedPreferences.edit();
				shareEditor.putLong(key, value);
				shareEditor.commit();
			}

		public boolean containsKey(String key, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}
				return sharedPreferences.contains(key);
			}

		public Boolean getBooleanValueFromPreference(String key, boolean defaultValue, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}

				return sharedPreferences.getBoolean(key, defaultValue);

			}

		public long getLongValueFromPreference(String key, long defaultValue, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}

				return sharedPreferences.getLong(key, defaultValue);
			}

		public String getStringValueFromPreference(String key, String defaultValue, Context context)
			{
				if (sharedPreferences == null)
					{
						sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
					}

				return sharedPreferences.getString(key, defaultValue);
			}

		public void initPref(Context context)
			{
				sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref_name), 0);
			}



		public String getPackageName(Activity context)
			{
				return context.getPackageName().toString();
			}



	}
