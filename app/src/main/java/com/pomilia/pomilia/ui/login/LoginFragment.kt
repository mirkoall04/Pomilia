package com.pomilia.pomilia.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pomilia.pomilia.R
import com.pomilia.pomilia.databinding.FragmentLoginBinding
import com.pomilia.pomilia.security.SessionManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        if (sessionManager.isLoggedIn()) {
            goToHome()
            return
        }

        binding.buttonLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = binding.editUsername.text.toString().trim()

        if (username.isEmpty()) {
            binding.editUsername.error = "Inserisci uno username"
            return
        }

        sessionManager.saveSession(username)
        goToHome()
    }

    private fun goToHome() {
        findNavController().navigate(
            R.id.action_loginFragment_to_homeFragment
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}