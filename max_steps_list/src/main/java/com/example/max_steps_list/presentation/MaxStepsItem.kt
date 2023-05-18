package com.example.max_steps_list.presentation

import com.example.max_steps_list.R

data class MaxStepsItem(
    val title: String,
    val maxSteps: Int,
    val isCustomInput: Boolean = false,
    val swipeImg: Int = R.drawable.ic_swipe_horizontal
)