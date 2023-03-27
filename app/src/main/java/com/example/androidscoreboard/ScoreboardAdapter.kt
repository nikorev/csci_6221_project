package com.example.androidscoreboard

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ScoreboardAdapter(private var scoreboardEntries:List<ScoreboardEntry>): RecyclerView.Adapter<ScoreboardAdapter.ViewHolder>() {

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val nameButton: Button = rootLayout.findViewById(R.id.nameButton)
        val scoreButton: Button = rootLayout.findViewById(R.id.scoreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        val rootLayout: View = layoutInflater.inflate(R.layout.scoreboard_entry, parent, false)

        val viewHolder = ViewHolder(rootLayout)

        // For each of the rows/players, open a PlayerActivity Intent based upon that player's data
        viewHolder.nameButton.setOnClickListener {
            val intent = Intent(parent.context, PlayerActivity::class.java)
            intent.putExtra("editIdx", viewHolder.adapterPosition)
            intent.putExtra("scoreboardEntries", scoreboardEntries as java.io.Serializable)
            (parent.context as Activity).startActivityForResult(intent, 1, null)
        }

        viewHolder.scoreButton.setOnClickListener {
            val intent = Intent(parent.context, PlayerActivity::class.java)
            intent.putExtra("editIdx", viewHolder.adapterPosition)
            intent.putExtra("scoreboardEntries", scoreboardEntries as java.io.Serializable)
            (parent.context as Activity).startActivityForResult(intent, 1, null)
        }


        return viewHolder
    }

    // Binding a viewholder means data is ready to be populated, grab it from the scoreboardEntries list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scoreboardEntry = scoreboardEntries[position]

        holder.nameButton.text = scoreboardEntry.name
        holder.scoreButton.text = scoreboardEntry.score.toString()

        // Color
        holder.nameButton.backgroundTintList =
            ContextCompat.getColorStateList(holder.nameButton.context, scoreboardEntry.color)
        holder.scoreButton.backgroundTintList =
            ContextCompat.getColorStateList(holder.scoreButton.context, scoreboardEntry.color)
    }

    override fun getItemCount(): Int {
        return scoreboardEntries.size
    }

}