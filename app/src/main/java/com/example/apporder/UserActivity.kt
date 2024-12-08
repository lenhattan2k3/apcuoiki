package com.example.apporder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class UserActivity : AppCompatActivity() {

    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodAdapter: DishAdapter
    private val foodList = mutableListOf<Dish>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        foodRecyclerView = findViewById(R.id.foodRecyclerView)
        foodRecyclerView.layoutManager = LinearLayoutManager(this)
        foodAdapter = DishAdapter(foodList)
        foodRecyclerView.adapter = foodAdapter

        loadFoodItems()
    }

    private fun loadFoodItems() {
        FirebaseDatabase.getInstance().getReference("dishes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodList.clear()
                snapshot.children.forEach {
                    val dish = it.getValue(Dish::class.java)
                    dish?.let { foodList.add(it) }
                }
                foodAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserActivity, "Error loading dishes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
