package com.example.androidscoreboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity() {

    private lateinit var nameEntryText:EditText
    private lateinit var scoreEntryText:EditText

    private lateinit var submitButton:Button
    private lateinit var cancelButton:Button
    private lateinit var deleteButton:Button

    private lateinit var colorButtons:List<Button>
    private var selectedColorButtonIdx = 0
    private var colors: List<Int> = listOf(R.color.red_700, R.color.deeppurple_700, R.color.blue_700, R.color.green_700,
        R.color.yellow_700, R.color.deeporange_700, R.color.teal2_700, R.color.gray_700)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        nameEntryText = findViewById(R.id.nameEntryText)
        scoreEntryText = findViewById(R.id.scoreEntryText)

        submitButton = findViewById(R.id.submitButton)
        cancelButton = findViewById(R.id.cancelButton)
        deleteButton = findViewById(R.id.deleteButton)

        val editIdx = intent.getIntExtra("editIdx", -1)
        @Suppress("UNCHECKED_CAST")
        var scoreboardEntries = intent.getSerializableExtra("scoreboardEntries") as MutableList<ScoreboardEntry>

        if(editIdx > -1) { // existing entry, populate fields with data
            nameEntryText.setText(scoreboardEntries[editIdx].name)
            scoreEntryText.setText(scoreboardEntries[editIdx].score.toString())
            submitButton.isEnabled = true
            deleteButton.visibility = View.VISIBLE
            title = "Edit Player"

            for(i in colors.indices) {
                if (scoreboardEntries[editIdx].color == colors[i]) {
                    selectedColorButtonIdx = i
                }
            }
        } else {
            title = "Add Player"
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
                scoreboardEntries.add(ScoreboardEntry(colors[selectedColorButtonIdx], nameEntryText.text.toString(), scoreEntryText.text.toString().toInt()))
            } else { // existing entry
                scoreboardEntries[editIdx].color = colors[selectedColorButtonIdx]
                scoreboardEntries[editIdx].name = nameEntryText.text.toString()
                scoreboardEntries[editIdx].score = scoreEntryText.text.toString().toInt()
            }

            scoreboardEntries = scoreboardEntries.sortedByDescending { it.score }.toMutableList()

            val intent = Intent()
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

            val intent = Intent()
            intent.putExtra("scoreboardEntries", scoreboardEntries as java.io.Serializable)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun initializeColorButtons() {
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

        for (i in colorButtons.indices) {
            colorButtons[i].setOnClickListener {
                colorButtons[selectedColorButtonIdx].text = ""
                selectedColorButtonIdx = i
                colorButtons[selectedColorButtonIdx].text = "✓"
            }
        }

    }

    // Ensures users can't submit an invalid entry to the table
    private fun validateEntry() {
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