package com.example.max_steps_list.presentation

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.max_steps_list.R
import com.example.max_steps_list.databinding.ItemMaxStepsBinding

class MaxStepsListAdapter(
    private val itemList: List<MaxStepsItem>,
    private val onAcceptCallback: (steps: Int) -> Unit
) :
    RecyclerView.Adapter<MaxStepsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMaxStepsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onAcceptCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(
        private val binding: ItemMaxStepsBinding,
        private val onAcceptCallback: (maxSteps: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemView: MaxStepsItem) {
            var textToAdd = ""
            binding.maxStepsTv.text = itemView.maxSteps.toString()
            binding.acceptBtn.setOnClickListener {
                if (textToAdd.isBlank()) {
                    onAcceptCallback.invoke(itemView.maxSteps)
                } else {
                    onAcceptCallback.invoke(textToAdd.toIntOrNull() ?: 0)
                }
            }

            binding.title.text = itemView.title

            binding.swipeIV.setImageDrawable(
                ResourcesCompat.getDrawable(
                    binding.root.resources,
                    itemView.swipeImg,
                    null
                )
            )

            binding.maxStepsInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    Unit

                override fun afterTextChanged(s: Editable?) {
                    val input = s?.toString()

                    if (!input.isNullOrEmpty()) {
                        if (!input.matches("[1-9][0-9]*".toRegex())) {
                            binding.textInputLayout.error =
                                binding.root.resources.getString(R.string.incorrect_input)
                        } else {
                            binding.textInputLayout.error = null
                            textToAdd = input
                        }
                    }
                }
            })

            if (itemView.isCustomInput) {
                binding.editStepsContainer.visibility = View.VISIBLE
                binding.maxStepsTv.visibility = View.INVISIBLE
            } else {
                binding.maxStepsTv.visibility = View.VISIBLE
                binding.editStepsContainer.visibility = View.GONE
            }
        }
    }
}






