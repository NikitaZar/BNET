package ru.nikitazar.bnet.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.nikitazar.bnet.BuildConfig
import ru.nikitazar.bnet.databinding.CardDrugBinding
import ru.nikitazar.bnet.ui.utils.load
import ru.nikitazar.domain.models.DrugDomain

class DrugAdapter : PagingDataAdapter<DrugDomain, DrugViewHolder>(DrugDiffCallback()) {
    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder {
        val binding = CardDrugBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrugViewHolder(binding)
    }
}

class DrugViewHolder(
    private val binding: CardDrugBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(drug: DrugDomain) {
        with(binding) {
            drug.image?.let { image.load(BuildConfig.BASE_URL + it) }
            name.text = drug.name
            description.text = drug.description
        }
    }
}

class DrugDiffCallback : DiffUtil.ItemCallback<DrugDomain>() {
    override fun areItemsTheSame(oldItem: DrugDomain, newItem: DrugDomain) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: DrugDomain, newItem: DrugDomain) = oldItem == newItem
}