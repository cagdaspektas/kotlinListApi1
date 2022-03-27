package com.example.kotlinapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinapi.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val TAG ="MainActivity"
class MainActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityMainBinding
    private  lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecycleView()

        lifecycleScope.launchWhenCreated {
            binding.progressId.isVisible=true

            val response=try {
                RetrofitInstance.api.getTodos()
            }catch (e:IOException){
            Log.e(TAG,"internet bağlantısı yokken gelir genelde!")
                binding.progressId.isVisible=false

                return@launchWhenCreated

            }catch (e:HttpException){
                Log.e(TAG,"http exception zaten")
                binding.progressId.isVisible=false

                return@launchWhenCreated

            }
            if (response.isSuccessful && response.body()!=null){
                todoAdapter.todos=response.body()!!
            }
            else{
                Log.e(TAG,"datalar gelmiyor")

            }
            binding.progressId.isVisible=false
        }
    }
    private fun setRecycleView()=binding.rvTodos.apply {
        todoAdapter= TodoAdapter()
        adapter= todoAdapter
        layoutManager=LinearLayoutManager(this@MainActivity)
    }
}