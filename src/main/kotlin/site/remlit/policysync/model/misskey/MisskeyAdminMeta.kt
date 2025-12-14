package site.remlit.policysync.model.misskey

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import site.remlit.policysync.model.JavaSerializable

@Serializable
data class MisskeyAdminMeta(
	val blockedHosts: List<String>,
) {
	companion object : JavaSerializable<MisskeyAdminMeta> {
		override val serializer: KSerializer<MisskeyAdminMeta>
			get() = serializer()
	}
}
