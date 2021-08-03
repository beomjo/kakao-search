package io.github.beomjo.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.beomjo.search.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(layoutInflater)
        setView(binding)
        return binding.root
    }

    private fun setView(binding: FragmentHomeBinding) {
        binding.text.setOnClickListener {
            HomeFragmentDirections.actionHomeDestToDetailDest().let {
                findNavController().navigate(it)
            }
        }
    }
}