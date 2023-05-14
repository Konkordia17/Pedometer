package com.example.pedometer_screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerDependencies(context)
    }

    private fun registerDependencies(context: Context) {
        val pedometerComponentDependencies =
            (context.applicationContext as PedometerComponentDependenciesProvider).getPedometerComponentDependencies()
        pedometerComponent = DaggerPedometerComponent.builder()
            .pedometerComponentDependencies(pedometerComponentDependencies).build()
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
        requestPermission()
        observeLiveData()
    }

    private fun observeLiveData() {
        vm.count.observe(viewLifecycleOwner) {
            binding.counterTv.text = it.toString()
        }
    }

    private fun requestPermission() {
        val permission =( Manifest.permission.ACTIVITY_RECOGNITION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        val intent = Intent(INTENT_ACTION)
                        requireActivity().sendBroadcast(intent)
                    } else {
                        println()
                    }
                }

            if (ContextCompat.checkSelfPermission(
                    requireActivity(), permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    companion object {
        private const val INTENT_ACTION = "com.example.ACTION_CUSTOM_BROADCAST"

        fun newInstance(): PedometerFragment {
            return PedometerFragment()
        }
    }
}