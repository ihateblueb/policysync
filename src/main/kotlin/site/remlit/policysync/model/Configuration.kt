package site.remlit.policysync.model

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
	val sources: List<PolicySource> = emptyList(),
	val frequency: Int = 120
) {
	companion object {
		@JvmStatic
		fun serializerKt() = this.serializer()
	}
}
