package com.nbc.curtaincall.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
	private lateinit var binding: FragmentRegisterBinding
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentRegisterBinding.inflate(inflater, container, false)
		return binding.root
	}
}