package site.remlit.policysync.util

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import site.remlit.aster.util.jsonConfig

@OptIn(InternalSerializationApi::class)
object KtSerialization {
	@JvmStatic
	fun <T : Any> serialize(clazz: Class<T>, input: String): T {
		return jsonConfig.decodeFromString(clazz.kotlin.serializer(), input)
	}

	@JvmStatic
	fun <T : Any> serialize(serializer: KSerializer<T>, input: String): T {
		return jsonConfig.decodeFromString(serializer, input)
	}
}