package io.github.beomjo.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.beomjo.search.databinding.FragmentCafeAndBlogBinding

class CafeAndBlogFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCafeAndBlogBinding.inflate(layoutInflater)
        setView(binding)
        return binding.root
    }

    private fun setView(binding: FragmentCafeAndBlogBinding) {
        binding.text.setOnClickListener {
            CafeAndBlogFragmentDirections.actionCafeAndBlogDestToDetailFragment().let {
                findNavController().navigate(it)
            }
        }
    }
}