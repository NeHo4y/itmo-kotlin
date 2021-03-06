package com.github.neho4u.shared.model

import kotlinx.datetime.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LocalDateTime) =
        encoder.encodeLong(value.toInstant(TimeZone.UTC).toEpochMilliseconds())

    override fun deserialize(decoder: Decoder): LocalDateTime =
        Instant.fromEpochMilliseconds(decoder.decodeLong())
            .toLocalDateTime(TimeZone.UTC)
}
