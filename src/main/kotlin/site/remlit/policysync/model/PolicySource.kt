package site.remlit.policysync.model

data class PolicySource(
	val type: PolicySourceType,
	val url: String,
	val token: String,
)
