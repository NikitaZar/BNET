package ru.nikitazar.bnet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ru.nikitazar.bnet.R
import ru.nikitazar.bnet.databinding.FragmentDrugDetailsBinding

@AndroidEntryPoint
class DrugDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDrugDetailsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}