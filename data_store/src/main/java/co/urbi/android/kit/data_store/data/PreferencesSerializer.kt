package co.urbi.android.kit.data_store.data

import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import co.urbi.android.kit.data_store.data.crypto.CryptoManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

internal class PreferencesSerializer(
    private val cryptoManager: CryptoManager?,
) : Serializer<Preferences> {

    override val defaultValue: Preferences
        get() = emptyPreferences()

    override suspend fun readFrom(input: InputStream): Preferences {
        val inputBytes = withContext(Dispatchers.IO) {
            val inputStream = cryptoManager?.decrypt(inputStream = input) ?: input
            inputStream.use { it.readBytes() }
        }
        return deserializePreferences(inputBytes)
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        val bytes = serializePreferences(preferences = t)
        withContext(Dispatchers.IO) {
            val info = cryptoManager?.encrypt(bytes = bytes) ?: bytes
            output.use {
                it.write(info)
            }
        }
    }

    private fun serializePreferences(preferences: Preferences): ByteArray {
        val builder = StringBuilder()
        preferences.asMap().forEach { (key, value) ->
            builder.append("${key.name}=${serializeValue(value)}\n")
        }
        return builder.toString().toByteArray()
    }

    private fun deserializePreferences(bytes: ByteArray): Preferences {
        val content = bytes.decodeToString()
        if (content.isBlank()) {
            return emptyPreferences()
        }

        val prefs = mutablePreferencesOf()
        content.lines().forEach { line ->
            if (line.isNotBlank() && line.contains("=")) {
                val parts = line.split("=", limit = 2)
                if (parts.size == 2) {
                    val keyName = parts[0]
                    val value = parts[1]
                    deserializeValue(keyName, value, prefs)
                }
            }
        }
        return prefs
    }

    private fun serializeValue(value: Any): String {
        return when (value) {
            is String -> "s:$value"
            is Int -> "i:$value"
            is Long -> "l:$value"
            is Float -> "f:$value"
            is Double -> "d:$value"
            is Boolean -> "b:$value"
            is Set<*> -> "ss:${value.joinToString(",")}"
            else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
        }
    }

    private fun deserializeValue(keyName: String, serialized: String, prefs: MutablePreferences) {
        val typePrefix = serialized.substringBefore(":", "")
        val valueStr = serialized.substringAfter(":", serialized)

        when (typePrefix) {
            "s" -> prefs[androidx.datastore.preferences.core.stringPreferencesKey(keyName)] = valueStr
            "i" -> prefs[androidx.datastore.preferences.core.intPreferencesKey(keyName)] = valueStr.toInt()
            "l" -> prefs[androidx.datastore.preferences.core.longPreferencesKey(keyName)] = valueStr.toLong()
            "f" -> prefs[androidx.datastore.preferences.core.floatPreferencesKey(keyName)] = valueStr.toFloat()
            "d" -> prefs[androidx.datastore.preferences.core.doublePreferencesKey(keyName)] = valueStr.toDouble()
            "b" -> prefs[androidx.datastore.preferences.core.booleanPreferencesKey(keyName)] = valueStr.toBoolean()
            "ss" -> {
                val set = if (valueStr.isBlank()) emptySet() else valueStr.split(",").toSet()
                prefs[androidx.datastore.preferences.core.stringSetPreferencesKey(keyName)] = set
            }
        }
    }

}
