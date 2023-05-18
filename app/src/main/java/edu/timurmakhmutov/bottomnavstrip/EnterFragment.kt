package edu.timurmakhmutov.bottomnavstrip

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentEnterBinding
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern

class EnterFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var baseReference: DatabaseReference
    private lateinit var binding: FragmentEnterBinding
    private lateinit var description:String
    private lateinit var ImagesURL:ArrayList<String>
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
        baseReference = firebaseDatabase.reference
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
                                baseReference.child("Users").child(mAuth.currentUser?.uid.toString()).child("Liked").addValueEventListener(object :
                                    ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (i in snapshot.children) {
                                            parsePlace(i.child("id").value.toString()){

                                                tableForDBRepository.insert(TableForDB(
                                                    i.child("id").value.toString(),
                                                    i.child("title").value.toString(),
                                                    i.child("address").value.toString(),
                                                    description,
                                                    i.child("location").value.toString(),
                                                    ImagesURL.toString().replace("[", "").replace("]", ""),
                                                    i.child("lat").value.toString(),
                                                    i.child("lon").value.toString(),
                                                    i.child("inLiked").value.toString().toInt(),
                                                    i.child("inPath").value.toString().toInt())
                                                )
                                            }

                                        }
                                        findNavController().navigate(R.id.action_enterFragment_to_homeFragment)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })

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
    private fun parsePlace(identification: String, onPlaceParsed:()->Unit){
        val url = "https://kudago.com/public-api/v1.4/places/$identification/?lang=&fields=&expand="
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                ImagesURL = setMyData(result)
                description = setDes(result)
                onPlaceParsed()

            },
            { error -> Log.d("MyTaggg", "error $error") }
        )
        queue.add(request)
    }
    private fun setMyData(result: String):ArrayList<String>{
        val list = ArrayList<String>()
        val mainObj = JSONObject(result)
        val images = mainObj.getJSONArray("images")
        for (i in 0 until images.length()){
            val image = images[i] as JSONObject
            list.add(image.getString("image"))
        }
        return list
    }
    private fun setDes(result: String): String {
        val mainObj = JSONObject(result)
        return mainObj.getString("description").replace("<p>", "").replace("</p>", "")
    }

}