package com.litmon.app.musicplayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.storage.OnObbStateChangeListener
import android.os.storage.StorageManager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.File
import java.io.FileInputStream

class MainActivity : AppCompatActivity() {

    private val storageManager by lazy {
        getSystemService(Context.STORAGE_SERVICE) as StorageManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.d("obbDir ${obbDir.absolutePath}")
        Timber.d("${obbDir.listFiles().map { it.name }}")
        val file = obbDir.listFiles().first()

        play_button.setOnClickListener { _ ->
            storageManager.mountObb(file.absolutePath) { path ->
                Timber.d("obb mounted: $path")
                // list files in obb
                val musicFile = File(path).listFiles()
                    .sortedBy { it.name }
                    .first { it.name.contains(".mp3") }

                Timber.d("music file ${musicFile.absolutePath}")

                // use function can close file input stream automatically
                FileInputStream(musicFile).use { inputStream ->
                    val mediaPlayer = MediaPlayer().apply {
                        // setDataSource(musicFile.absolutePath)
                        // file pathからいくとクラッシュする(Caused by: java.io.IOException: Prepare failed.: status=0x1)
                        // のでfile descriptorを使う
                        setDataSource(inputStream.fd)

                        // ↓これなくても動くっぽい
//                        setAudioAttributes(
//                            AudioAttributes.Builder()
//                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                                .build()
//                        )
                    }
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                }
            }
        }
    }

    private fun StorageManager.mountObb(path: String, func: (String?) -> Unit) {
        mountObb(path, null, object : OnObbStateChangeListener() {
            override fun onObbStateChange(path: String?, state: Int) {
                if (state == OnObbStateChangeListener.MOUNTED) {
                    func.invoke(getMountedObbPath(path))
                }
            }
        })
    }
}
