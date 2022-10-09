package ahmadkabi.storyapp.helper

import android.content.Context

internal class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val TOKEN = "token"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(name: String, token: String) {
        val editor = preferences.edit()
        editor.putString(NAME, name)
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getUserName(): String? {
        return preferences.getString(NAME, null)
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

}