package com.ds.kang.searchtest.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ds.kang.searchtest.R

class NetworkConnectionFailDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_network_connection_fail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.ok_button)
            .setOnClickListener {
                dismiss()
            }
    }

    companion object {
        val CLASS_NAME = NetworkConnectionFailDialogFragment::javaClass.name
    }
}