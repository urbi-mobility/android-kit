package co.urbi.android.kit.data_store.data

import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

internal class PreferencesSerializer(
    private val crypto: Crypto?
) : Serializer<Preferences> {

    override val defaultValue: Preferences
        get() = emptyPreferences()

    override suspend fun readFrom(input: InputStream): Preferences {
        val inputBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }

        if (inputBytes.isEmpty()) {
            return emptyPreferences()
        }

        val decryptedBytes = crypto?.decryption(inputBytes) ?: inputBytes

        return deserializePreferences(decryptedBytes)
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        val bytes = serializePreferences(t)
        val outputBytes = crypto?.encryption(bytes) ?: bytes

        withContext(Dispatchers.IO) {
            output.use {
                it.write(outputBytes)
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

    private fun Crypto.encryption(bytes: ByteArray): ByteArray {
        val encryptedBytes = this.encrypt(bytes)
        return Base64.getEncoder().encode(encryptedBytes)
    }

    private fun Crypto.decryption(bytes: ByteArray): ByteArray {
        val encryptedBytesDecoded = Base64.getDecoder().decode(bytes)
        return this.decrypt(encryptedBytesDecoded)
    }
}

