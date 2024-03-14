package com.nbc.curtaincall.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetBookmarkModel(
	@SerialName("id")
	val id: Int,

	@SerialName("user_id")
	val userId: String,

	@SerialName("created_at")
	val createdAt: String,

	@SerialName("poster")
	val poster: String,

	@SerialName("mt20id")
	val mt20id: String,

	@SerialName("mt10id")
	val mt10id: String,
)

@Serializable
data class PostBookmarkModel(
	@SerialName("user_id")
	val userId: String,

	@SerialName("poster")
	val poster: String,

	@SerialName("mt20id")
	val mt20id: String,

	@SerialName("mt10id")
	val mt10id: String,
)
