package dk.mrspring.fileexplorer.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by MrSpring on 09-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class Player extends Thread
{
    File toPlay;
    SourceDataLine line;
    boolean paused;

    public Player(File musicFile)
    {
        this.toPlay = musicFile;
    }

    @Override
    public void run()
    {
        super.run();
        this.play();
    }

    private void play()
    {
        if (toPlay.exists())
        {
            try
            {
                AudioInputStream in = AudioSystem.getAudioInputStream(toPlay);
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
                System.out.println("Playing raw");
                rawPlay(decodedFormat, din);
                in.close();
                System.out.println("Closing in");
            } catch (Exception e)
            {
                System.out.println("Crash!");
                e.printStackTrace();
            }
        }
    }

    private void rawPlay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
    {
        byte[] data = new byte[4096];
        line = getLine(targetFormat);
        if (line != null)
        {
            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1)
            {
                if (!paused)
                {
                    nBytesRead = din.read(data, 0, data.length);
                    if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
                }
            }
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

    public void pausePlayback()
    {
        System.out.println("Pausing playback!");
        if (line != null && !paused)
        {
//            line.stop();
            paused = true;
        }
    }

    public void resumePlayback()
    {
        System.out.println("Resuming playback!");
        if (line != null && paused)
        {
//            line.start();
            paused = false;
        }
    }

    public void togglePlayback()
    {
        System.out.println("Toggling playback!");
        if (paused)
            this.resumePlayback();
        else this.pausePlayback();
    }
}
