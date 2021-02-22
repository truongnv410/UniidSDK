package com.ttc.uniid.view.ui.updateAccountInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.response.VerifierPhoneRespond
import com.ttc.uniid.databinding.LayoutUpdateAccountInfoBinding
import com.ttc.uniid.extension.replaceFragment
import com.ttc.uniid.view.ui.login.LoginFragment
import kotlinx.android.synthetic.main.layout_update_account_info.*
import java.util.*

class UpdateAccountInfoFragment : Fragment(){
    private lateinit var viewDataBinding: LayoutUpdateAccountInfoBinding
    var listGender = arrayOf("Choose gender", "Male", "Female", "Rather not say")
    var calendarDateYear : Int = 0
    var calendarDateMonth: Int =0
    var calendarDateDay: Int =0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = LayoutUpdateAccountInfoBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@UpdateAccountInfoFragment)
                .get(UpdateAccountInfoViewModel::class.java)
            setLifecycleOwner(viewLifecycleOwner)
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        subscribeUI()
    }
    private fun subscribeUI() = with(viewDataBinding?.viewModel!!) {
        fillerSuccess?.observe(viewLifecycleOwner, Observer {
            requireActivity().replaceFragment(LoginFragment(), Bundle())
        })
        invalidEmail?.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                if (it) {
                    layoutEmail.suffixTextView.visibility = View.VISIBLE
                    layoutEmail.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutEmail.suffixTextView.visibility = View.VISIBLE
                    }
                    layoutEmail.suffixText = getString(R.string.error)
                    layoutEmail.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_error_10dp)
                } else {
                    layoutEmail.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        email?.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                if (it) {
                    invalidEmail?.value = ""
                    layoutEmail.suffixTextView.visibility = View.GONE
                    layoutEmail.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutEmail.suffixTextView.visibility = View.GONE
                    }
                    layoutEmail.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        toastMessage?.observe(viewLifecycleOwner, Observer { msg ->
            activity?.let {
                Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupUI() = with(viewDataBinding) {
        viewModel?.verifierPhoneRespond?.postValue(arguments?.getParcelable<VerifierPhoneRespond>("VerifierPhoneRespond"))
        edtBirthday.setOnClickListener {
            showCalendar()
        }
        btnNext.setOnClickListener {
            viewModel?.filler()
        }
        btnAcceptDate.setOnClickListener {
            edtBirthday.setText("$calendarDateYear-$calendarDateMonth-$calendarDateDay")
            edtBirthday.visibility = View.VISIBLE
            lnCalenderPicker.visibility = View.GONE
        }
        spnGender.setOnClickListener {
            initSpinnerGender()
        }
    }

    fun initSpinnerGender() {
        spnGender.visibility = View.GONE
        lnSpnGender.visibility = View.VISIBLE
        var array_adapter = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_item_system,
            listGender)
        lstGender.setAdapter(array_adapter)
        lstGender.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            spnGender.visibility = View.VISIBLE
            lnSpnGender.visibility = View.GONE
            if (i==0){
                viewDataBinding?.viewModel?.gender?.value = ""
                spnGender.text= getString(R.string.hint_gender)
            }else{
                viewDataBinding?.viewModel?.gender?.value = listGender[i]
                spnGender.text= listGender[i]
            }
        })
    }
    fun showCalendar() {
        edtBirthday.visibility = View.GONE
        lnCalenderPicker.visibility = View.VISIBLE
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        if (edtBirthday.text.isNullOrEmpty()){
            calendarDateYear = year
            calendarDateMonth = month+1
            calendarDateDay =  day
        }
        c.set(calendarDateYear, calendarDateMonth - 1, calendarDateDay)
        simpleCalendarView.date = c.timeInMillis
        simpleCalendarView.maxDate = c.timeInMillis

        // perform setOnDateChangeListener event on CalendarView
        simpleCalendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView, yearSelect, monthSelect, dayOfMonthSelect ->
            calendarDateYear = yearSelect
            calendarDateMonth = monthSelect + 1
            calendarDateDay = dayOfMonthSelect
        })
    }

}