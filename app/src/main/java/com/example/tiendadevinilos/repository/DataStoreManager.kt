package com.example.tiendadevinilos.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tiendadevinilos.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreferencesRepository(private val context: Context) {
    private val dataStore = context.dataStore
    private val ID_USER_KEY = stringPreferencesKey("id_user")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val FULL_NAME_KEY = stringPreferencesKey("full_name")
    private val PICTURE_KEY = stringPreferencesKey("picture")

    suspend fun saveUserData(idUser: String, email: String, fullName: String, picture: String) {
        dataStore.edit { preferences ->
            preferences[ID_USER_KEY] = idUser
            preferences[EMAIL_KEY] = email
            preferences[FULL_NAME_KEY] = fullName
            preferences[PICTURE_KEY] = picture
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val userData: Flow<UserModel> = dataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserModel(
                user_id = preferences[ID_USER_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                fullName = preferences[FULL_NAME_KEY] ?: "",
                picture = preferences[PICTURE_KEY] ?: ""
            )
        }



}