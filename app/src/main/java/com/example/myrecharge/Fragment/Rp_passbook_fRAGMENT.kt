package com.example.myrecharge.Fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecharge.Adapter.ListAdapter
import com.example.myrecharge.Helper.*
import com.example.myrecharge.Pojo.ModelPassbook_Att
import com.example.myrecharge.Pojo.ModelTransactionFilter_Att
import com.example.myrecharge.Pojo.Model_passbook
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentRpPassbookBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Rp_passbook_fRAGMENT : Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentRpPassbookBinding
    var data:ArrayList<ModelTransactionFilter_Att> ?=null
    var cal = Calendar.getInstance()
    var date_type=0
    var ttype="0"
    var tag1="@@paasbook"
    var string_type="0"

    lateinit var pass:ArrayList<ModelPassbook_Att>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        mainBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_rp_passbook, container, false)
        thiscontext=container!!.context

        data= ArrayList()
        pass= ArrayList()
        Log.e(tag1,"1")
//        setdata()
        settype()

        mainBinding.btnSend.setOnClickListener {
            if(mainBinding.fromDate.text.toString().isEmpty())
            {
                mainBinding.fromDate.setError("Select Start date")

            }else if(mainBinding.toDate.text.toString().isEmpty())
            {
                mainBinding.toDate.setError("Select Ent Date")
            }
            else if(ttype.toString().equals("0"))
            {
                mainBinding.fromDate.setError("Transaction Type")
            }
            else
            {
                setdata()
                Log.e(tag1,"2")
            }
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        mainBinding.fromDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    thiscontext,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
                date_type = 1
            }
        })

        mainBinding.toDate.setOnClickListener {
            mainBinding.fromDate.performClick()
            date_type=2
        }

        return mainBinding.root
    }
    private fun updateDateInView() {

        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        if(date_type==1)
        {
            mainBinding.fromDate!!.text = sdf.format(cal.getTime())
        }
        else if(date_type==2)
        {
            mainBinding.toDate!!.text = sdf.format(cal.getTime())
        }
        else if(date_type==0)
        {

        }
    }

    @SuppressLint("WrongConstant")
    fun setdata()
    {
        var pref= Local_data(thiscontext)
        var methods= Method_collection(thiscontext)
        methods.show_Progress_dialog()

        pass.clear()
        pref.setMyappContext(thiscontext)

        val apiInterface: ApiInterface =
            RetrofitManager.instance!!.create(ApiInterface::class.java)

        val call: Call<Model_passbook?>? = apiInterface.passbook_getdaat(
            "t1",
            pref.ReadStringPreferences(Var.PREF_Login_Id),
            mainBinding.fromDate.text.toString(),
            mainBinding.toDate.text.toString(),
            string_type,
            "0",
            "1000"
        )

        call!!.enqueue(object : Callback<Model_passbook?>
        {
            override fun onResponse(
                call: Call<Model_passbook?>,
                response: Response<Model_passbook?>
            ) {
//                mainBinding.l1.visibility=View.GONE
                methods.dismis_Progress_dialog()
                if(response.isSuccessful) {
                    Log.e(tag1+"messeges",response.body()!!.message.toString()+" ")
                    for (i in 0 until response.body()!!.data.size) {
                        pass.addAll(Arrays.asList(response!!.body()!!.data.get(i)))
                    }


                    mainBinding.recyclerview.layoutManager =
                        LinearLayoutManager(thiscontext, LinearLayout.VERTICAL, false)

                    mainBinding.recyclerview.adapter =
                        ListAdapter(pass!!, thiscontext)
                    mainBinding.recyclerview.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Model_passbook?>, t: Throwable) {
                methods.dismis_Progress_dialog()
                Log.e("@@error",t.message.toString()+" ")
            }

        })
/*Hey check out this amazing app and Earn money. Use my refer https://www.therippco.com/registration.aspx?ref=<loginid>*/


    }
    fun settype()
    {
        val languages = resources.getStringArray(R.array.type)
        val spinner = mainBinding.spinnerType

        if (spinner != null) {
            val adapter = ArrayAdapter(
                thiscontext,
                android.R.layout.simple_spinner_item, languages
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        thiscontext, " " +
                                "" + languages[position], Toast.LENGTH_SHORT
                    ).show()
                    ttype=languages.get(position).toString()
                    if(ttype.equals("Debit"))
                    {
                        string_type="dr"
                    }
                    else
                        if(ttype.equals("Cradit"))
                        {
                            string_type="cr"
                        }else{
                            string_type=" "
                        }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
}