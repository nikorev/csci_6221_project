package com.example.androidscoreboard

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager

class PlayerActivity : AppCompatActivity() {

    lateinit var nameEntryText:EditText
    lateinit var scoreEntryText:EditText

    lateinit var submitButton:Button
    lateinit var cancelButton:Button
    lateinit var deleteButton:Button

    lateinit var colorButtons:List<Button>
    var selectedColorButtonIdx = 0
    var colors: List<Int> = listOf(R.color.red_700, R.color.deeppurple_700, R.color.blue_700, R.color.green_700,
        R.color.yellow_700, R.color.deeporange_700, R.color.teal2_700, R.color.gray_700)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        nameEntryText = findViewById(R.id.nameEntryText)
        scoreEntryText = findViewById(R.id.scoreEntryText)

        submitButton = findViewById(R.id.submitButton)
        cancelButton = findViewById(R.id.cancelButton)
        deleteButton = findViewById(R.id.deleteButton)

        var editIdx = intent.getIntExtra("editIdx", -1)
        var scoreboardEntries = intent.getSerializableExtra("scoreboardEntries") as MutableList<ScoreboardEntry>

        if(editIdx > -1) { // existing entry, populate fields with data
            nameEntryText.setText(scoreboardEntries[editIdx].name)
            scoreEntryText.setText(scoreboardEntries[editIdx].score.toString())
            submitButton.isEnabled = true
            deleteButton.visibility = View.VISIBLE
            setTitle("Edit Player")

            for(i in 0..colors.size-1) {
                if (scoreboardEntries[editIdx].color == colors[i]) {
                    selectedColorButtonIdx = i
                }
            }
        } else {
            setTitle("Add Player")
        }

        initializeColorButtons()

        // Listeners
        nameEntryText.addTextChangedListener {
            validateEntry()
        }

        scoreEntryText.addTextChangedListener {
            validateEntry()
        }

        submitButton.setOnClickListener {
            if(editIdx < 0) { // new entry
                // Teal 200 for now
                scoreboardEntries.add(ScoreboardEntry(colors[selectedColorButtonIdx], nameEntryText.text.toString(), scoreEntryText.text.toString().toInt()))
            } else { // existing entry
                scoreboardEntries[editIdx].color = colors[selectedColorButtonIdx]
                scoreboardEntries[editIdx].name = nameEntryText.text.toString()
                scoreboardEntries[editIdx].score = scoreEntryText.text.toString().toInt()
            }

            scoreboardEntries = scoreboardEntries.sortedByDescending { it.score }.toMutableList()

            val intent:Intent = Intent()
            intent.putExtra("scoreboardEntries", scoreboardEntries as java.io.Serializable)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish() // no result necessary on return intent
        }

        deleteButton.setOnClickListener {
            // Button is invisible if editIdx < 0
            scoreboardEntries.removeAt(editIdx)

            val intent:Intent = Intent()
            intent.putExtra("scoreboardEntries", scoreboardEntries as java.io.Serializable)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun initializeColorButtons() {
        colorButtons = listOf(
            findViewById(R.id.colorButton1),
            findViewById(R.id.colorButton2),
            findViewById(R.id.colorButton3),
            findViewById(R.id.colorButton4),
            findViewById(R.id.colorButton5),
            findViewById(R.id.colorButton6),
            findViewById(R.id.colorButton7),
            findViewById(R.id.colorButton8),
        )

        // Initial default color
        colorButtons[selectedColorButtonIdx].text = "✓"

        for (i in 0..colorButtons.size-1) {
            colorButtons[i].setOnClickListener {
                colorButtons[selectedColorButtonIdx].text = ""
                selectedColorButtonIdx = i
                colorButtons[selectedColorButtonIdx].text = "✓"
            }
        }

    }

    fun validateEntry() {
        if (nameEntryText.text.toString().isEmpty()) {
            submitButton.isEnabled = false
            return
        }

        try{
            scoreEntryText.text.toString().toInt()
        }catch(e: NumberFormatException){
            submitButton.isEnabled = false
            return
        }

        submitButton.isEnabled = true
    }
}