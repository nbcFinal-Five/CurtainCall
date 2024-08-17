package com.nbc.curtaincall.presentation.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nbc.curtaincall.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        with(binding) {
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val passwordConfirm = etPasswordConfirm.text.toString()
                val nickName = etNickname.text.toString()
                if (email.isNullOrBlank() || password
                        .isNullOrBlank() || passwordConfirm.isNullOrBlank() || nickName.isNullOrBlank()
                ) return@setOnClickListener
                if (isValid(email, password, passwordConfirm, nickName))
                    Firebase.auth.createUserWithEmailAndPassword(
                        email, password
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val user = Firebase.auth.currentUser
                            val userId = user?.uid

                            userId?.let {
                                val database = Firebase.firestore
                                val userRef = database.collection("users").document(it)
                                val userData = hashMapOf(
                                    "email" to email,
                                    "nickname" to nickName,
                                )
                                userRef.set(userData).addOnSuccessListener {
                                    Firebase.auth.signOut()
                                    Toast.makeText(baseContext, "회원가입 성공", Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                }
                            }
                        } else {
                        }
                    }
            }

        }
    }

    private fun isValid(
        email: String,
        password: String,
        passwordConfirm: String,
        nickName: String
    ): Boolean {
        var isValid = true

        with(binding) {
            if (email.isBlank()) {
                tvEmailWarning.isVisible = true
                isValid = false
            } else {
                tvEmailWarning.isVisible = false
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // 이메일 형식 검사
                    tvEmailWarning.text = "유효한 이메일 형식이 아닙니다."
                    tvEmailWarning.isVisible = true
                    isValid = false
                }
            }

            if (password.isBlank()) {
                tvPasswordWarning.isVisible = true
                isValid = false
            } else {
                tvPasswordWarning.isVisible = false
                if (password.length < 6) { // 비밀번호 길이 6자리 이상 검사
                    tvPasswordWarning.text = "비밀번호는 6자리 이상이어야 합니다."
                    tvPasswordWarning.isVisible = true
                    isValid = false
                }
            }

            if (passwordConfirm.isBlank()) {
                tvPasswordConfirmWarning.isVisible = true
                isValid = false
            } else {
                tvPasswordConfirmWarning.isVisible = false
                if (password != passwordConfirm) {
                    tvPasswordConfirmWarning.isVisible = true
                    isValid = false
                }
            }

            if (nickName.isBlank()) {
                tvNicknameWarning.isVisible = true
                isValid = false
            } else {
                tvNicknameWarning.isVisible = false
                if (nickName.length < 2 || nickName.length > 10) { // 닉네임 길이 검사
                    tvNicknameWarning.text = "닉네임은 2~10자 사이여야 합니다."
                    tvNicknameWarning.isVisible = true
                    isValid = false
                }
            }
        }

        return isValid
    }


}