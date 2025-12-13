package site.remlit.policysync.model

data class Configuration(
	val sources: List<PolicySource>,
	val frequency: Int
)
