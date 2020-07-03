package com.noahjutz.gymroutines.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.noahjutz.gymroutines.R
import kotlinx.android.synthetic.main.activity_main.*

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO stuff...
        initActivity()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "Profile"
            bottom_nav.visibility = VISIBLE
        }
    }
}
