package site.remlit.policysync.model.iceshrimp

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer
import site.remlit.policysync.model.JavaSerializable


@Serializable
data class IceshrimpHost(
	val host: String = "",
	val reason: String? = null,
	val imported: Boolean = false
) {
	companion object : JavaSerializable<IceshrimpHost> {
		override val serializer: KSerializer<IceshrimpHost>
			get() = serializer()
	}
}