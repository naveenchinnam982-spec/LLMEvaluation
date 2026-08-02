package project.llmevaluation.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("llm_prefs", Context.MODE_PRIVATE)

    fun saveApiKey(apiKey: String) {
        prefs.edit().putString("api_key", apiKey).apply()
    }

    fun getApiKey(): String? {
        return prefs.getString("api_key", null)
    }
}
