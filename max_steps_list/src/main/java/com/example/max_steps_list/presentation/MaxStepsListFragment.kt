package com.example.max_steps_list.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.max_steps_list.R
import com.example.max_steps_list.Screens
import com.example.max_steps_list.databinding.FragmentMaxListBinding
import com.example.max_steps_list.di.DaggerMaxStepsListComponent
import com.example.max_steps_list.di.MaxStepsListComponent
import com.example.max_steps_list.di.MaxStepsListDependenciesProvider
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class MaxStepsListFragment : Fragment(R.layout.fragment_max_list) {

    private var _binding: FragmentMaxListBinding? = null
    private val binding get() = _binding!!
    private lateinit var maxStepsComponent: MaxStepsListComponent
    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screen: Screens

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val maxStepsComponentDependencies =
            (context.applicationContext as MaxStepsListDependenciesProvider)
                .maxStepsListComponentDependencies()
        maxStepsComponent = DaggerMaxStepsListComponent.builder()
            .maxStepsListComponentDependencies(maxStepsComponentDependencies)
            .build()
        maxStepsComponent.injectMaxListFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaxListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        initList()
    }

    private fun getStepsList(): List<MaxStepsItem> {
        val levelArray = resources.getStringArray(R.array.level)
        return listOf(
            MaxStepsItem(
                title = levelArray[0],
                maxSteps = STEPS_5000,
                swipeImg = R.drawable.ic_swipe_right
            ),
            MaxStepsItem(title = levelArray[1], maxSteps = STEPS_10000),
            MaxStepsItem(title = levelArray[2], maxSteps = STEPS_15000),
            MaxStepsItem(title = levelArray[3], maxSteps = STEPS_20000),
            MaxStepsItem(title = levelArray[4], maxSteps = STEPS_25000),
            MaxStepsItem(title = levelArray[5], maxSteps = STEPS_30000),
            MaxStepsItem(
                title = "",
                maxSteps = MIN_STEPS,
                isCustomInput = true,
                swipeImg = R.drawable.ic_swipe_left
            ),
        )
    }

    private fun saveChosenMaxSteps(steps: Int) {
        sharedPreferences.edit().putInt(CHOSEN_MAX_STEPS, steps)
            .apply()
    }

    private fun initList() {
        with(binding.maxStepsRv) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = MaxStepsListAdapter(getStepsList()) {
                saveChosenMaxSteps(it)
                navigateToPedometerScreen(it)
            }
        }

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.maxStepsRv)
    }

    private fun navigateToPedometerScreen(steps: Int) {
        router.navigateTo(screen.pedometerFragment(steps))
    }


    companion object {
        private const val STEPS_5000 = 5000
        private const val STEPS_10000 = 10000
        private const val STEPS_15000 = 15000
        private const val STEPS_20000 = 20000
        private const val STEPS_25000 = 25000
        private const val STEPS_30000 = 30000
        private const val MIN_STEPS = 0
        private const val PREFS_TAG = "prefs"
        private const val CHOSEN_MAX_STEPS = "chosen_steps"

        fun newInstance(): MaxStepsListFragment {
            return MaxStepsListFragment()
        }
    }
}