package com.example.mysharedpreferences

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysharedpreferences.databinding.ActivityFormUserPreferenceBinding

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        const val EXTRA_TYPE_FORM = "extra_type_from"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101
        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }
    private lateinit var userModel: UserModel
    private lateinit var binding: ActivityFormUserPreferenceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener(this)

        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)
        var actionBarTitle = ""
        var btnTitle = ""
        when (formType){
            TYPE_ADD -> {
                actionBarTitle = "Tambah Baru"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPrefereceInForm()

            }
        }
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSave.text = btnTitle

    }

    @SuppressLint("SetTextI18n")
    private fun showPrefereceInForm() {
        binding.edtNama.setText(userModel.name)
        binding.edtEmail.setText(userModel.email)
        binding.edtAge.setText(userModel.age.toString())
        binding.edtPhone.setText(userModel.phoneNumber)

        if (userModel.isLove){
            binding.rbYes.isChecked = true
        }else{
            binding.rbNo.isChecked = true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View){
        if (view.id == R.id.btn_save){
            val nama = binding.edtNama.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val age = binding.edtAge.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()
            val isLoveCd = binding.rgLoveCd.checkedRadioButtonId == R.id.rb_yes
            if (nama.isEmpty()){
                binding.edtNama.error = FIELD_REQUIRED
                return
            }
            if (!isValidEmail(email)){
                binding.edtEmail.error = FIELD_IS_NOT_VALID
                return
            }
            if (email.isEmpty()){
                binding.edtEmail.error = FIELD_REQUIRED
                return
            }
            if (age.isEmpty()){
                binding.edtAge.error = FIELD_REQUIRED
                return
            }
            if (phone.isEmpty()){
                binding.edtPhone.error = FIELD_REQUIRED
                return
            }
            if (!TextUtils.isDigitsOnly(phone)){
                binding.edtPhone.error = FIELD_DIGIT_ONLY
                return
            }
            saveUser(nama, email, age, phone, isLoveCd)
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, userModel)
            setResult(RESULT_CODE, resultIntent)
            finish()
        }
    }

    private fun saveUser(name: String, email: String, age: String, phone: String, isLoveCd: Boolean) {
        val userPreference = UserPreference(this)
        userModel.name = name
        userModel.email = email
        userModel.age = Integer.parseInt(age)
        userModel.phoneNumber = phone
        userModel.isLove = isLoveCd
        userPreference.setUser(userModel)
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()


    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}