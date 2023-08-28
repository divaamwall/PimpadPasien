package com.diva.pimpad.ui.listchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.diva.pimpad.adapter.UserAdapter
import com.diva.pimpad.databinding.FragmentChatBinding
import com.diva.pimpad.model.Dokters

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(ChatViewModel::class.java)
        chatViewModel.allDokters.observe(viewLifecycleOwner, Observer {
            setupAction(it)
        })


        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private fun setupAction(listDokter: ArrayList<Dokters>) {
        val adapter = UserAdapter(requireContext(),listDokter, true)
        binding.apply {
            usersRv.layoutManager = LinearLayoutManager(activity)
            usersRv.setHasFixedSize(true)
            usersRv.adapter = adapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}