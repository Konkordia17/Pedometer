package com.example.pedometer_screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.database.di.DataBaseModule
import com.example.pedometer_screen.databinding.FragmentPedometerBinding
import com.example.pedometer_screen.di.DaggerPedometerComponent
import com.example.pedometer_screen.di.PedometerComponent
import com.example.pedometer_screen.di.PedometerComponentDependenciesProvider
import com.example.pedometer_screen.di.PedometerScreen
import com.github.terrakok.cicerone.Router
import javax.inject.Inject


class PedometerFragment : Fragment(R.layout.fragment_pedometer) {
    private var _binding: FragmentPedometerBinding? = null
    private val binding get() = _binding!!
    private lateinit var pedometerComponent: PedometerComponent

    @Inject
    lateinit var vmFactory: PedometerViewModelFactory
    lateinit var vm: PedometerViewModel
    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screen: PedometerScreen

    private var maxSteps = 0
    private var stepsColorIndicator = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerDependencies(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        maxSteps = requireArguments().getInt(ARG_PARAM)
        vm.updateMaxSteps(maxSteps)
        stepsColorIndicator = maxSteps / COLORS_NUMBER

    }

    private fun registerDependencies(context: Context) {
        val pedometerComponentDependencies =
            (context.applicationContext as PedometerComponentDependenciesProvider).getPedometerComponentDependencies()
        pedometerComponent = DaggerPedometerComponent.builder()
            .pedometerComponentDependencies(pedometerComponentDependencies)
            .dataBaseModule(DataBaseModule(requireContext()))
            .build()

        pedometerComponent.injectPedometerFragment(this)
        vmFactory = pedometerComponent.getViewModelFactory()
        vm = ViewModelProvider(this, vmFactory)[PedometerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedometerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        binding.counterTv.text = getPreviousStepsCount().toString()
        setStepsToProgressCircle(getPreviousStepsCount())
        binding.circleProgress.progressMax = maxSteps.toFloat()
        binding.description.text = requireContext().getString(R.string.steps_per_day, maxSteps)
        setOnClickListeners()
    }

    private fun observeLiveData() {
        vm.count.observe(viewLifecycleOwner) {
            binding.counterTv.text = it.toString()
            setStepsToProgressCircle(it)
        }

        vm.isUpdatedCounter.observe(viewLifecycleOwner) {
            vm.getDataFromDatabase()
        }

        vm.previousSteps.observe(viewLifecycleOwner) {
            binding.previousStepsCountTv.text =
                requireContext().getString(R.string.steps_number_for_yesterday, it.stepsCount)
        }
    }


    private fun setOnClickListeners() {
        binding.changeGoalBtn.setOnClickListener {
            router.navigateTo(screen.changeMaxSteps())
        }

        binding.openStatisticBtn.setOnClickListener {
            router.navigateTo(screen.openStatisticScreen())
        }
    }

    private fun setStepsToProgressCircle(steps: Int) {
        binding.circleProgress.apply {
            progressBarColor = resources.getColor(setProgressColor(steps), null)
            setProgressWithAnimation(steps.toFloat())
        }
    }

    private fun setProgressColor(steps: Int): Int {
        return when {
            steps > maxSteps -> com.example.core.R.color.darkRed
            steps > (stepsColorIndicator * 6) -> com.example.core.R.color.red
            steps > (stepsColorIndicator * 5) -> com.example.core.R.color.coral
            steps > (stepsColorIndicator * 4) -> com.example.core.R.color.orange
            steps > (stepsColorIndicator * 3) -> com.example.core.R.color.yellow
            steps > (stepsColorIndicator * 2) -> com.example.core.R.color.yellowGreen
            else -> com.example.core.R.color.green
        }
    }

    private fun getPreviousStepsCount(): Int {
        return sharedPreferences.getInt(PREVIOUS_STEPS_TAG, 0)
    }

    companion object {
        private const val PREVIOUS_STEPS_TAG = "previousSteps"
        private const val PREFS_TAG = "prefs"
        private const val ARG_PARAM = "param"
        private const val COLORS_NUMBER = 7
        fun newInstance(steps: Int): PedometerFragment {
            val fragment = PedometerFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM, steps)
            fragment.arguments = args
            return fragment
        }
    }
}