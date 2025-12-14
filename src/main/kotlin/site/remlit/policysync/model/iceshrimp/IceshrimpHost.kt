package site.remlit.policysync.model.iceshrimp

import java.io.Serializable

data class IceshrimpHost(
	val host: String = "",
	val reason: String? = null,
	val imported: Boolean = false,
) : Serializable
