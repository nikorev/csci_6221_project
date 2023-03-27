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
            //startActivity(parent.context, intent, null)
            startActivityForResult(intent, 1, null)
        }

       scoreboardEntries = testFakeEntries()
        val scoreboardAdapter = ScoreboardAdapter(scoreboardEntries)
        scoreboardRecyclerView.adapter = scoreboardAdapter

        scoreboardRecyclerView.layoutManager = LinearLayoutManager(this)

//        scoreboardAdapter.scoreboardEntries.add(ScoreboardEntry(Color.RED, "Ralph", 0))
//        scoreboardAdapter.notifyDataSetChanged()
    }

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

    private fun testFakeEntries(): MutableList<ScoreboardEntry> {
        return mutableListOf(
            ScoreboardEntry(R.color.red_700, "Bob", 0),
            ScoreboardEntry(R.color.deeppurple_700, "Gary", 0),
            ScoreboardEntry(R.color.blue_700, "Max", 0)
        )
    }
}