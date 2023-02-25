package ru.nikitazar.bnet.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import dagger.hilt.android.AndroidEntryPoint
import ru.nikitazar.bnet.BuildConfig
import ru.nikitazar.bnet.MainActivity
import ru.nikitazar.bnet.databinding.FragmentDrugDetailsBinding
import ru.nikitazar.bnet.ui.utils.IntArg
import ru.nikitazar.bnet.ui.utils.load
import ru.nikitazar.bnet.viewModel.DrugViewModel

@AndroidEntryPoint
class DrugDetailsFragment : Fragment() {

    companion object {
        var Bundle.intArg: Int? by IntArg
    }

    lateinit var binding: FragmentDrugDetailsBinding
    private val viewModel: DrugViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrugDetailsBinding.inflate(layoutInflater, container, false)

        (requireActivity() as MainActivity).supportActionBar?.title = ""

        arguments?.intArg?.let { id -> viewModel.getById(id) }

        viewModel.data.observe(viewLifecycleOwner) { drug ->
            with(binding) {
                drug.image?.let { image.load(BuildConfig.BASE_URL + it) }

                drug.categoryIcon?.let { categoryIcon.load(BuildConfig.BASE_URL + it) }

                name.text = drug.name

                description.text = drug.description
            }
        }

        return binding.root
    }
}