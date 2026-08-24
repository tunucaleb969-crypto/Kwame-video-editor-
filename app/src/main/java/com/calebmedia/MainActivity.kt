package com.calebmedia

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {

    private var selectedVideoUri: Uri? = null
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView

    private val pickVideoLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedVideoUri = it
            findViewById<TextView>(R.id.tvSelectedFile).text = "Selected: ${it.lastPathSegment}"
            playVideo(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.playerView)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        findViewById<Button>(R.id.btnPickVideo).setOnClickListener {
            pickVideoLauncher.launch("video/*")
        }

        findViewById<Button>(R.id.btnTrim).setOnClickListener {
            toolPlaceholder("Trim")
        }

        findViewById<Button>(R.id.btnFilters).setOnClickListener {
            toolPlaceholder("Filters")
        }

        findViewById<Button>(R.id.btnText).setOnClickListener {
            toolPlaceholder("Text")
        }

        findViewById<Button>(R.id.btnMusic).setOnClickListener {
            toolPlaceholder("Music")
        }
    }

    private fun playVideo(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun toolPlaceholder(toolName: String) {
        if (selectedVideoUri == null) {
            Toast.makeText(this, "Import a video first", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "$toolName tool coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
