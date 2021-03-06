package fr.esimed.jshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esimed.jshop.model.data.Liste
import fr.esimed.jshop.services.ApiInstance
import fr.esimed.jshop.services.ApiInstance.Companion.tokenUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()

        getListCurrentUser()
        //val result = getListCurrentUser()
        //On récupère le composant
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(tokenUser == "" || tokenUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
        getListCurrentUser()
    }

    fun getListCurrentUser(){
        val call = ApiInstance.createList().getCurrentList()
        call.enqueue(object : Callback<List<Liste>> {
            override fun onResponse(call: Call<List<Liste>>, response: Response<List<Liste>>) {
                if (response.isSuccessful) {
                    val result = response.body()?.toMutableList()
                    if(!result.isNullOrEmpty()){
                        var list : String = " liste"
                        if(result.count() > 1)
                            list = " listes"

                        Toast.makeText(this@MainActivity, "Vous avez "+ result.count() + list ,Toast.LENGTH_SHORT).show()

                        val mesListes = findViewById<RecyclerView>(R.id.recyclerMainList)
                        mesListes.layoutManager = LinearLayoutManager(this@MainActivity)
                        mesListes.adapter = ListeMainAdapter(this@MainActivity, response.body()!!)
                    } else {
                        Toast.makeText(this@MainActivity, "Vous n'avez pas de résultats." ,Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<kotlin.collections.List<Liste>>, t: Throwable) {
                    //recyclerListData.postValue(null)
                    Toast.makeText(this@MainActivity, t.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        })
    }
}