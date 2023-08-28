package com.diva.pimpad.ui.resultdetect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.diva.pimpad.adapter.ResultDetectAdapter
import com.diva.pimpad.databinding.FragmentResultDetectBinding
import com.diva.pimpad.model.ResultDetect


class ResultDetectFragment : Fragment() {
    private var _binding: FragmentResultDetectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val resultDetectViewModel =
            ViewModelProvider(this).get(ResultDetectViewModel::class.java)
        resultDetectViewModel.allResultDetect.observe(viewLifecycleOwner, Observer {
            setupAction(it)
        })
        _binding = FragmentResultDetectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private fun setupAction(listResultDetect: ArrayList<ResultDetect>) {
        val adapter = ResultDetectAdapter(listResultDetect)
        binding.apply {
            resultDetectRv.layoutManager = LinearLayoutManager(activity)
            resultDetectRv.setHasFixedSize(true)
            resultDetectRv.adapter = adapter

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}