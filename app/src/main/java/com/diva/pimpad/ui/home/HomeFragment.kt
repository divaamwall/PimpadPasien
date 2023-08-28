package com.diva.pimpad.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.diva.pimpad.adapter.AcneAdapter
import com.diva.pimpad.databinding.FragmentHomeBinding
import com.diva.pimpad.model.Acne
import com.diva.pimpad.ui.detail.DetailHomeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.allAcnes.observe(viewLifecycleOwner, Observer {
            setupAction(it)
        })

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction(listAcne: ArrayList<Acne>) {
        val adapter = AcneAdapter(listAcne)
        binding.apply {
            acnesRv.layoutManager = GridLayoutManager(activity, 2)
            acnesRv.setHasFixedSize(true)
            acnesRv.adapter = adapter
        }
        adapter.setOnItemClickCallback(object: AcneAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Acne) {
                val intentDetail = Intent(requireActivity(), DetailHomeActivity::class.java)
                intentDetail.putExtra("Acne", data)
                startActivity(intentDetail)
                showSelectedAcne(data)
            }

        })
    }

    private fun showSelectedAcne(acne: Acne){
        Toast.makeText(activity, "Detail Dari " + acne.name, Toast.LENGTH_SHORT).show()
    }

}