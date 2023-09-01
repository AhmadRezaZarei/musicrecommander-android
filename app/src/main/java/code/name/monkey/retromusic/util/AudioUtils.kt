package code.name.monkey.retromusic.util

import code.name.monkey.retromusic.audiotrimmer.CheapSoundFile
import code.name.monkey.retromusic.audiotrimmer.Util
import java.io.File

object AudioUtils {

    fun trim(inputPath: String, outputPath: String, startSecond: Int, endSecond: Int) {

        val cheapSoundFile = CheapSoundFile.create(inputPath, null)

        val mSampleRate = cheapSoundFile.sampleRate

        val mSamplesPerFrame = cheapSoundFile.samplesPerFrame

        val startFrame: Int = Util.secondsToFrames(startSecond.toDouble(), mSampleRate, mSamplesPerFrame)

        val endFrame: Int = Util.secondsToFrames(endSecond.toDouble(), mSampleRate, mSamplesPerFrame)

        cheapSoundFile.WriteFile(File(outputPath), startFrame, endFrame - startFrame)

    }


}