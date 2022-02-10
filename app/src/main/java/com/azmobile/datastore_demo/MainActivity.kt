package com.azmobile.datastore_demo

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.azmobile.datastore_demo.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, MyViewModelFactory(UserPreferencesRepository(dataStore)))
                .get(MyViewModel::class.java)

        viewModel.initialSetupEvent.observe(this) {

            show(it)
            setupOnCheckedChangeListeners()
            observePreferenceChanges()
        }
    }

    private fun countBirthday(age: Int): Int {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        return year - age
    }

    private fun show(user: UserPreferences) {
        binding.edtName.setText(user.name)
        binding.edtAge.setText(user.age.toString())

        binding.rbMale.isChecked = user.gender == Gender.MALE
        binding.rbFemale.isChecked = user.gender != Gender.MALE
        binding.tvBirthday.text = getString(R.string.birthday) + ":${countBirthday(user.age)}"
    }

    private fun setupOnCheckedChangeListeners() {
        binding.apply {

            rg.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.rbMale -> {
                        viewModel.updateGender(true)
                    }
                    R.id.rbFemale -> {
                        viewModel.updateGender(false)
                    }
                }
            }

            binding.edtName.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.updateName(edtName.text.toString())
                }
                return@setOnEditorActionListener false
            }

            binding.edtAge.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.updateAge(edtAge.text.toString().toInt())
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun observePreferenceChanges() {
        viewModel.user.observe(this) {
            show(it)
        }
    }
}
