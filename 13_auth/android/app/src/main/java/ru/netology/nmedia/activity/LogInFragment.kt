package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentLogInBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.LogInViewModel

class LogInFragment : Fragment() {

    private val viewModel: LogInViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding = FragmentLogInBinding.inflate(
            inflater,
            container,
            false
        )

        binding.signInBt.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val login = binding.loginEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            viewModel.onSignIn(login, password)
        }

        viewModel.isSignedIn.observe(viewLifecycleOwner, { isSignedId ->
            if (isSignedId){
                binding.progressBar.visibility = View.GONE
                AndroidUtils.hideKeyboard(requireView())
                findNavController().popBackStack()
                viewModel.invalidateSignedInState()
            }
        })




        return binding.root
        }


}