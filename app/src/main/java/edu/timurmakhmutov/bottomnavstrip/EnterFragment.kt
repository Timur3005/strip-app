package edu.timurmakhmutov.bottomnavstrip

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.childEvents
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentEnterBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class EnterFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var baseReference: DatabaseReference
    private lateinit var binding: FragmentEnterBinding
    private lateinit var mAuth: FirebaseAuth
    private val tableForDBRepository = TableForDBRepository(Application())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEnterBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        baseReference = firebaseDatabase.getReference("Users")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRegInEnter.setOnClickListener{
            findNavController().navigate(R.id.action_enterFragment_to_registrationFragment)
        }
        binding.buttonEnter.setOnClickListener{
            if (isEmail(binding.email.text.toString()) && binding.password.text.toString().isNotEmpty()){
                mAuth.signInWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString()).
                        addOnCompleteListener {
                            if (it.isSuccessful){
//                                baseReference.child(mAuth.currentUser?.uid.toString()).addValueEventListener(object :
//                                    ValueEventListener{
//                                    override fun onDataChange(snapshot: DataSnapshot) {
//                                        for (i in snapshot.children){
//                                            tableForDBRepository.insert(TableForDB(
//
//                                            ))
//                                        }
//                                    }
//
//                                    override fun onCancelled(error: DatabaseError) {
//                                        TODO("Not yet implemented")
//                                    }
//
//                                })
                                findNavController().navigate(R.id.action_enterFragment_to_homeFragment)
                            }
                            else{
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