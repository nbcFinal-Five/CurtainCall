package com.nbc.curtaincall.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetExpectationModel(
	@SerialName("id")
	val id: Int,

	@SerialName("user_id")
	val userId: String,

	@SerialName("created_at")
	val createdAt: String,

	@SerialName("is_expect")
	val isExpect: Boolean,

	@SerialName("comment")
	val comment: String,

	@SerialName("poster")
	val poster: String,

	@SerialName("mt20id")
	val mt20id: String,

	@SerialName("profile")
	val profile: ProfileModel
)
