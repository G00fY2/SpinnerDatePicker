package com.g00fy2.spinnerdatepicker

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.g00fy2.spinnerdatepicker.databinding.ActivityMainBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSpinner.setOnClickListener { openSpinnerBirthdayDialog() }
        binding.buttonMdc.setOnClickListener { openMDCDatePicker() }
    }

    override fun onResume() {
        super.onResume()
        binding.themeSwitch.setOnCheckedChangeListener { _, _ -> switchTheme() }
    }

    override fun onPause() {
        super.onPause()
        binding.themeSwitch.setOnCheckedChangeListener(null)
    }

    private fun openSpinnerBirthdayDialog() {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.YEAR, -18)
        }

        DatePickerDialog(this, R.style.SpinnerDatePickerDialog, { _, year, month, dayOfMonth ->
            Toast.makeText(this, formatDate(year, month, dayOfMonth), Toast.LENGTH_SHORT).show()
        },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
        ).apply {
            datePicker.maxDate = Date().time
        }.show()
    }

    private fun openMDCDatePicker() {
        val calendar = Calendar.getInstance().apply { add(Calendar.YEAR, -18) }
        val calendarConstraints = CalendarConstraints.Builder().apply {
            setEnd(Date().time)
            setValidator(DateValidatorPointBackward.now())
            setOpenAt(calendar.timeInMillis)
        }.build()

        MaterialDatePicker.Builder.datePicker().apply {
            setCalendarConstraints(calendarConstraints)
        }.build().apply {
            addOnPositiveButtonClickListener {
                Toast.makeText(this@MainActivity, formatDate(Date(it)), Toast.LENGTH_SHORT).show()
            }
        }.show(supportFragmentManager, "mdcDatePicker")
    }

    private fun formatDate(date: Date) = SimpleDateFormat.getDateInstance().apply { timeZone = TimeZone.getTimeZone("UTC") }.format(date)

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int) = formatDate(Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("UTC")
        set(year, month, dayOfMonth)
    }.time)

    private fun switchTheme() {
        val currentlyNight = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(if (currentlyNight) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES)
    }
}