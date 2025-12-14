package site.remlit.policysync.model

import kotlinx.serialization.Serializable

@Serializable
data class PolicySource(
	val type: PolicySourceType,
	val url: String,
	val token: String,
)
