package loli.helper;

import loli.io.db.AudioDB;
import loli.ui.control.audiovideo.AudioImage;
import org.bytedeco.javacpp.Loader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FFSpectrogram {
    private final AudioDB audioDB;
    private final String cmd;

    public FFSpectrogram(String name) {
        audioDB = new AudioDB(name);
        cmd = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
    }

    // Extract by 10 seconds and put in database with 'name' database
    public void extract(String media, AssTime start, AssTime end, int width, int height) throws IOException, InterruptedException {
        AssTime current = start;
        while(end.getMsTime() < current.getMsTime()) {
            ProcessBuilder pb = new ProcessBuilder(
                    cmd,
                    "-ss", String.format("%dms", (long)current.getMsTime()),
                    "-t", "10000ms",
                    "-i", String.format("\"%s\"", media),
                    "-lavfi", String.format("showspectrumpic=s=%dx%d:legend=0:scale=cbrt", width, height),
                    "output.png"
            );
            Process p = pb.redirectErrorStream(true).start();
            p.waitFor();
            BufferedImage img = ImageIO.read(new File("output.png"));
            audioDB.add(new AudioImage(img, current));

            current = new AssTime(current.getMsTime() + 10_000d); // +10 seconds
            p.close();
        }
    }

    public AudioDB getAudioDB() {
        return audioDB;
    }
}
