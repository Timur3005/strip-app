package edu.timurmakhmutov.bottomnavstrip.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.timurmakhmutov.bottomnavstrip.R
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentRegistrationBinding
import edu.timurmakhmutov.bottomnavstrip.view_model.HomeViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistrationFragment : Fragment() {

    private val model: HomeViewModel by activityViewModels()
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var baseReference: DatabaseReference
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentRegistrationBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        baseReference = firebaseDatabase.reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonReg.setOnClickListener{
            if (isEmail(binding.email.text.toString()) && binding.password.text.toString().isNotEmpty()
                && binding.password.text.toString().length>5){
                mAuth.createUserWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString()).
                        addOnCompleteListener { p0 ->
                            if (p0.isSuccessful) {
                                baseReference.child("Users").child(mAuth.currentUser?.uid.toString()).child("email").setValue(binding.email.text.toString())
                                model.uId.value = mAuth.currentUser?.uid.toString()
                                findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
                            } else {
                                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
            else{
                Toast.makeText(context,"Вы ввели некорректные данные", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isEmail(string: String): Boolean {
        val emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"
        val pattern: Pattern = Pattern.compile(emailPattern)
        val matcher: Matcher = pattern.matcher(string)
        return matcher.matches()
    }
}