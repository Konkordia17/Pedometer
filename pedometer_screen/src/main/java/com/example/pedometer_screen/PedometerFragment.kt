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
import javax.inject.Inject

class PedometerFragment : Fragment(R.layout.fragment_pedometer) {
    private var _binding: FragmentPedometerBinding? = null
    private val binding get() = _binding!!
    private lateinit var pedometerComponent: PedometerComponent

    @Inject
    lateinit var vmFactory: PedometerViewModelFactory
    lateinit var vm: PedometerViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerDependencies(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
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
    }

    private fun observeLiveData() {
        vm.count.observe(viewLifecycleOwner) {
            binding.counterTv.text = it.toString()
        }

        vm.isUpdatedCounter.observe(viewLifecycleOwner) {
            vm.getDataFromDatabase()
        }

        vm.previousSteps.observe(viewLifecycleOwner) {
            binding.previousStepsCountTv.text =
                requireContext().getString(R.string.steps_number_for_yesterday, it.stepsCount)
        }
    }

    private fun getPreviousStepsCount(): Int {
        return sharedPreferences.getInt(PREVIOUS_STEPS_TAG, 0)
    }

    companion object {
        private const val PREVIOUS_STEPS_TAG = "previousSteps"
        private const val PREFS_TAG = "prefs"
        fun newInstance(): PedometerFragment {
            return PedometerFragment()
        }
    }
}