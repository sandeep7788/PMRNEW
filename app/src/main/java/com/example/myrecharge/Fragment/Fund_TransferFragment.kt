package com.example.myrecharge.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentFundTransferBinding


class Fund_TransferFragment : Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentFundTransferBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        mainBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_fund_transfer, container, false)
        thiscontext=container!!.context






        return mainBinding.root
    }

}