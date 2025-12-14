package site.remlit.policysync.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

interface JavaSerializable<T> {
	val serializer: KSerializer<T>
	fun listSerializer(): KSerializer<List<T>> = ListSerializer(serializer)
}