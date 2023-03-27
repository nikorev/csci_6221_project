package com.example.androidscoreboard

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var scoreboardRecyclerView: RecyclerView
    private lateinit var newPlayerButton:FloatingActionButton

    // Will automatically be populated+updated based on PlayerActivity intent (return intent)
    private var scoreboardEntries:MutableList<ScoreboardEntry> = mutableListOf<ScoreboardEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Android Scoreboard")

        scoreboardRecyclerView = findViewById(R.id.scoreboardRecyclerView)

        newPlayerButton = findViewById(R.id.newPlayerButton)

        newPlayerButton.setOnClickListener {
            val intent:Intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("editIdx", -1)
            intent.putExtra("scoreboardEntries", scoreboardEntries as java.io.Serializable)
            startActivityForResult(intent, 1, null)
        }

        val scoreboardAdapter = ScoreboardAdapter(scoreboardEntries)
        scoreboardRecyclerView.adapter = scoreboardAdapter

        scoreboardRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    // This is when the PlayerActivity returns back to the main screen
    // Our scoreboardEntries has been updated+sorted, pass it to the RecyclerView Adapter
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // A change was made, update our list
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            scoreboardEntries = (data?.getSerializableExtra("scoreboardEntries") ?: return) as MutableList<ScoreboardEntry>
            val scoreboardAdapter = ScoreboardAdapter(scoreboardEntries)
            scoreboardRecyclerView.adapter = scoreboardAdapter

            scoreboardRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
}