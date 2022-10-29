package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentShowImageBinding
import ru.netology.nmedia.view.loadFitCenter
import ru.netology.nmedia.viewmodel.PostViewModel

class ShowImageFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private lateinit var binding: FragmentShowImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowImageBinding.inflate(layoutInflater, container, false)

        with(binding){
            imageContent.loadFitCenter("${BASE_URL}/media/${requireArguments().getString("url" , "")}")
            buttonBack.setOnClickListener{
                findNavController().navigateUp()
            }
        }
        return binding.root
    }


}