package loli.ui.control.audiovideo;

import loli.helper.AssTime;

import java.awt.image.BufferedImage;

public class AudioImage {
    private final BufferedImage image;
    private final AssTime startTime;
    private final AssTime endTime;

    public AudioImage(BufferedImage image, AssTime startTime, AssTime endTime) {
        this.image = image;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Create an audio image with a start and an end (10 seconds of duration).
     * @param image the audio image
     * @param startTime the millisecond time of start
     */
    public AudioImage(BufferedImage image, AssTime startTime) {
        this(image, startTime, new AssTime(startTime.getMsTime() + 10_000d));
    }

    public BufferedImage getImage() {
        return image;
    }

    public AssTime getStartTime() {
        return startTime;
    }

    public AssTime getEndTime() {
        return endTime;
    }
}
