package com.lizongying.mytv

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.lizongying.mytv.databinding.PlayerBinding
import com.lizongying.mytv.models.TVViewModel

class PlayerFragment : Fragment() {

    private var lastVideoUrl: String = ""

    private var _binding: PlayerBinding? = null
    private var playerView: PlayerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlayerBinding.inflate(inflater, container, false)
        playerView = _binding!!.playerView
        return _binding!!.root
    }

    @OptIn(UnstableApi::class)
    fun play(tvModel: TVViewModel) {
        val videoUrl = tvModel.videoIndex.value?.let { tvModel.videoUrl.value?.get(it) }
        if (videoUrl == null || videoUrl == "") {
            Log.e(TAG, "${tvModel.title.value} videoUrl is empty")
            return
        }

        if (videoUrl == lastVideoUrl) {
            Log.e(TAG, "videoUrl is duplication")
            return
        }

        lastVideoUrl = videoUrl

        if (playerView!!.player == null) {
            playerView!!.player = activity?.let {
                ExoPlayer.Builder(it)
                    .aspectRatio(16f/9f) // 修改比例为全屏比例
                    .build()
            }
            playerView!!.player?.playWhenReady = true
            playerView!!.player?.addListener(object : PlayerListener() {
                override fun onVideoSizeChanged(videoSize: VideoSize) {
                    val aspectRatio = 16f / 9f // 保持宽高比不变，无需修改代码
                    val layoutParams = playerView?.layoutParams as ViewGroup? ?: run { return null }
                    layoutParams?.width = (playerView?.measuredHeight?.times(aspectRatio))?.toInt() ?: run { return null } // 根据宽高比计算宽度并赋值给layoutParams的width属性，无需修改代码
                    playerView?.layoutParams = layoutParams as ViewGroup? // 将修改后的layoutParams重新赋值给playerView的layoutParams属性，无需修改代码
                }
            })
        } else { // 如果播放器已经存在，则直接设置宽高比和视频大小即可，无需修改代码。因此这里不需要添加新的代码块。
            playerView!!.player?.setAspectRatio(16f/9f) // 直接设置比例为全屏比例，无需修改代码。因此这里不需要添加新的代码块。
            playerView!!.player?.setVideoSize(1080, 1920) // 直接设置视频大小为全屏大小，无需修改代码。因此这里不需要添加新的代码块。
        }
        playerView!!.player?.playWhenReady = true // 让播放器开始播放视频，无需修改代码。因此这里不需要添加新的代码块。
    }
}
