package com.nbc.curtaincall.supabase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileModel(
	@SerialName("id")
	val id: Int,

	@SerialName("email")
	val email: String,

	@SerialName("name")
	val name: String,

	@SerialName("gender")
	val gender: String,

	@SerialName("age")
	val age: String,

	@SerialName("user_id")
	val userId: String,
)
