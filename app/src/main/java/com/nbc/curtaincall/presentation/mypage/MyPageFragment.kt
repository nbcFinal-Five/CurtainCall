package com.nbc.curtaincall.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentMyPageBinding
import com.nbc.curtaincall.presentation.TicketViewModel
import com.nbc.curtaincall.presentation.auth.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private val ticketViewModel by activityViewModels<TicketViewModel>()
    private val interestAdapter by lazy {
        BookmarkAdapter {
            ticketViewModel.posterClick(it, childFragmentManager)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        ticketViewModel.myPageInterestList.observe(viewLifecycleOwner) {
            interestAdapter.submitList(it)
        }
        binding.rvBookmarks.adapter = interestAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            updateUI(user != null)
        }
        auth.addAuthStateListener(authStateListener)
        with(binding) {
            btnSignIn.setOnClickListener {
                if (etInputEmail.text.isNullOrBlank() || etInputPassword.text.isNullOrBlank()) return@setOnClickListener
                Firebase.auth.signInWithEmailAndPassword(
                    etInputEmail.text.toString(),
                    etInputPassword.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        layoutMyPage.isVisible = true
                        layoutSignIn.isVisible = false
                    } else {
                        tvFailSignIn.isVisible = true
                    }
                }
            }

            tvSignOut.setOnClickListener {
                Firebase.auth.signOut()
                Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show()
                with(binding) {
                    layoutMyPage.isVisible = false
                    layoutSignIn.isVisible = true
                }
            }
            tvRegister.setOnClickListener {
                startActivity(Intent(context, SignUpActivity::class.java))
            }
            tvShowcaseMore.setOnClickListener {
                findNavController().navigate(R.id.action_mypage_to_showcase)
            }
            tvInterestListMore.setOnClickListener {
                findNavController().navigate(R.id.action_mypage_to_interest)
            }
        }
    }

    private fun updateUI(isLoggedIn: Boolean) {
        with(binding) {
            layoutMyPage.isVisible = isLoggedIn
            layoutSignIn.isVisible = !isLoggedIn
            ticketViewModel.myPageInterestList.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    tvLikeDetail.isVisible = true
                    ivLikeDetail.isVisible = true
                    rvBookmarks.isVisible = false
                } else {
                    tvLikeDetail.isVisible = false
                    ivLikeDetail.isVisible = false
                    rvBookmarks.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        auth.removeAuthStateListener(authStateListener)
        _binding = null
    }
}