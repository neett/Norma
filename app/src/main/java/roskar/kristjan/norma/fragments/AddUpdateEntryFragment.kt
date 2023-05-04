package roskar.kristjan.norma.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentAddUpdateEntryBinding
import roskar.kristjan.norma.model.Month
import roskar.kristjan.norma.model.Productivity
import roskar.kristjan.norma.utilities.Constants
import roskar.kristjan.norma.utilities.Util
import roskar.kristjan.norma.viewModel.ViewModel
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.math.abs
import kotlin.properties.Delegates

class AddUpdateEntryFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private val item: Productivity = Productivity(null,"",0.0,0.0)
    //private val month: Month = Month(null,"",0,0.0,0.0)

    private lateinit var binding: FragmentAddUpdateEntryBinding

    private var productivity by Delegates.notNull<Double>()
    private var workHours by Delegates.notNull<Double>()
    private lateinit var date: String

    private lateinit var addUpdateBtn: Button
    private lateinit var editTxtWorkHours: EditText
    private lateinit var editTxtProductivity: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Util.log("on create view")

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add_update_entry, container, false)
        binding = FragmentAddUpdateEntryBinding.inflate(layoutInflater)



        productivity = viewModel.clickedItem.value?.productivityHours!!
        workHours = viewModel.clickedItem.value?.workingHours!!
        date = viewModel.clickedItem.value?.date.toString()

        editTxtProductivity = FragmentAddUpdateEntryBinding.bind(rootView).productivity
        editTxtWorkHours = FragmentAddUpdateEntryBinding.bind(rootView).workHours
        addUpdateBtn = FragmentAddUpdateEntryBinding.bind(rootView).addUpdate

        val txtFieldDate = binding.date
        val day = Util.formatDate(date,"d. EEEE",Constants.DAY_MONTH_YEAR_NUMBER)
        txtFieldDate.text = day

        setValues()

        addUpdateBtn.setOnClickListener {
            Util.log("listener")

            val eTxtProductivity = editTxtProductivity.text.toString()
            val eTxtWorkHours = editTxtWorkHours.text.toString()

            if (productivity.toString() != eTxtProductivity || workHours.toString() != eTxtWorkHours) {     // check if any changes ware actualy made
                if (eTxtProductivity.isNotEmpty() && eTxtWorkHours.isNotEmpty()) {    // check if edit-boxes are not empty

                    // productivity_table update
                    item.productivityHours = eTxtProductivity.toDoubleOrNull()!!
                    item.workingHours = eTxtWorkHours.toDoubleOrNull()!!
                    item.date = viewModel.clickedItem.value?.date.toString()
                    viewModel.updateEntryProductivityTable(item)

                    // month table update

                    var diff = abs(productivity - item.productivityHours)
                    var action = setAction(productivity,item.productivityHours)

                    viewModel.refreshMonthDb(action + "_productivity",item.date,diff.toBigDecimal())

                    diff = abs(workHours - item.workingHours)
                    action = setAction(workHours,item.workingHours)

                    viewModel.refreshMonthDb(action + "_workHours",item.date,diff.toBigDecimal())




                    Util.toast(requireContext(), "Success!")
                    parentFragmentManager.popBackStackImmediate()

                } else {
                    Util.toast(requireContext(), "INPUT ERROR")
                }
            } else {
                Util.toast(requireContext(), "WE DID NOTHING.")
                parentFragmentManager.popBackStackImmediate()
            }
        }




        return rootView
    }
    private fun setAction(value1: Double,value2 : Double): String {
        var result = ""
        if (value1 < value2) {
            result = "addition"
        } else if (value1 > value2) {
            result = "subtract"
        }
        return result
    }
    private fun setValues() {
        Util.log(productivity.toString())
        Util.log(workHours.toString())
        if (productivity == 0.0 && workHours == 0.0) {
            Util.log("nula")
            addUpdateBtn.text = "Add"

        } else {
            addUpdateBtn.text = "Update"
            editTxtProductivity.setText(productivity.toString())
        }
        if (workHours == 0.0) editTxtWorkHours.setText("8") else editTxtWorkHours.setText(workHours.toString())
        editTxtProductivity.setText(productivity.toString())

        editTxtProductivity.setText(productivity.toString())


    }
}