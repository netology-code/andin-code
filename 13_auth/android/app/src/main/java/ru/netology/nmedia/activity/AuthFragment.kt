package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.application.KEY
import ru.netology.nmedia.application.VALUE
import ru.netology.nmedia.databinding.FragmentAuthBinding
import ru.netology.nmedia.viewmodel.LogInViewModel


class AuthFragment : Fragment() {
    lateinit var binding: FragmentAuthBinding
    private val viewModel: LogInViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAuthBinding.inflate(inflater, container, false)

        with(binding) {
            if (arguments?.getString(KEY) == VALUE) {
                buttonLogIn.setText(R.string.sign_up)
                textViewSignIn.setText(R.string.sign_up)
                groupSignUp.visibility = View.VISIBLE

                buttonLogIn.setOnClickListener {
                    if (editTextPassword.text.toString() == editTextRepeatPassword.text.toString()) {
                        progressBar.visibility = View.VISIBLE
                        viewModel.userRegister(
                            login = editTextUserName.text.toString(),
                            pass = editTextPassword.text.toString(),
                            name = editTextName.text.toString()
                        )
                    } else Toast.makeText(context , "Passwords do not match" , Toast.LENGTH_LONG).show()
                }
            } else {
                buttonLogIn.setOnClickListener {
                    progressBar.visibility = View.VISIBLE
                    viewModel.userAuth(
                        this.editTextUserName.text.toString(),
                        this.editTextPassword.text.toString()
                    )

                }
            }


        }

        viewModel.authState.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.authState.value = false
                findNavController().navigateUp()
            }
        }


        return binding.root
    }


}