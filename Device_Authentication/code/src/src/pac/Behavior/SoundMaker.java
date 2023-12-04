package pac.Behavior;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class SoundMaker {
    public byte[] codeConvert2Sound(int GenRate, int Duration, int SampleRate) {
        int mGenRate = GenRate;//声音的频率，单位Hz
        int mDuration = Duration;//这个频率的声音持续时间，单位ms
        int mSampleRate = SampleRate;//采样率，每秒钟采样几次
        ArrayList<Byte> rawData = new ArrayList<Byte>();
        for (int k = 0; k < mDuration * (float) mSampleRate / 1000; k++) {
            double angle = k / ((float) mSampleRate / mGenRate) * 2.0 * Math.PI;
            rawData.add((byte) (Math.sin(angle) * 100));
        }
        //list转换byte
        byte[] audio = new byte[rawData.size()];
        for (int i = 0; i < rawData.size(); i++) {
            audio[i] = rawData.get(i);
        }
        return audio;
    }

    private void save2File(byte[] audio) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(audio);
        AudioFormat audioFormat = new AudioFormat((float) 44100, 8, 1, true, false);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audio.length / audioFormat.getFrameSize());

        try {
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File("D:\\morse-code-" + format + ".wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2      * @param totalAudioLen  不包括header的音频数据总长度
     * 3      * @param longSampleRate 采样率,也就是录制时使用的频率、音频采样级别 8000 = 8KHz
     * 4      * @param channels       audioRecord的声道数1/2
     * 5      * @param audioFormat    采样精度; 譬如 16bit
     * 6      * @throws IOException 写文件错误
     * 7
     */
    public static byte[] writeWavFileHeader(long totalAudioLen, long longSampleRate,
                                            int channels, int audioFormat) throws IOException {
        byte[] header = generateWavFileHeader(totalAudioLen, longSampleRate, channels, audioFormat);
        return header;
    }

    /**
     * 15      * @param totalAudioLen  不包括header的音频数据总长度
     * 16      * @param longSampleRate 采样率,也就是录制时使用的频率
     * 17      * @param channels       audioRecord的频道数量
     * 18      * @param audioFormat    采样精度; 譬如 16bit
     * 19
     */
    private static byte[] generateWavFileHeader(long totalAudioLen, long longSampleRate, int channels, int audioFormat) {

        long totalDataLen = totalAudioLen + 36;

        long byteRate = longSampleRate * 2 * channels;

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF

        header[1] = 'I';

        header[2] = 'F';

        header[3] = 'F';

        //文件长度  4字节文件长度，这个长度不包括"RIFF"标志(4字节)和文件长度本身所占字节(4字节),即该长度等于整个文件长度 - 8

        header[4] = (byte) (totalDataLen & 0xff);

        header[5] = (byte) ((totalDataLen >> 8) & 0xff);

        header[6] = (byte) ((totalDataLen >> 16) & 0xff);

        header[7] = (byte) ((totalDataLen >> 24) & 0xff);

        //fcc type：4字节 "WAVE" 类型块标识, 大写

        header[8] = 'W';

        header[9] = 'A';

        header[10] = 'V';

        header[11] = 'E';

        //FMT Chunk   4字节 表示"fmt" chunk的开始,此块中包括文件内部格式信息，小写, 最后一个字符是空格

        header[12] = 'f'; // 'fmt '

        header[13] = 'm';

        header[14] = 't';

        header[15] = ' ';//过渡字节

        //数据大小  4字节，文件内部格式信息数据的大小，过滤字节（一般为00000010H）

        header[16] = 16;

        header[17] = 0;

        header[18] = 0;

        header[19] = 0;

        //编码方式 10H为PCM编码格式   FormatTag：2字节，音频数据的编码方式，1：表示是PCM 编码

        header[20] = 1; // format = 1

        header[21] = 0;

        //通道数  Channels：2字节，声道数，单声道为1，双声道为2

        header[22] = (byte) channels;

        header[23] = 0;

        //采样率，每个通道的播放速度

        header[24] = (byte) (longSampleRate & 0xff);

        header[25] = (byte) ((longSampleRate >> 8) & 0xff);

        header[26] = (byte) ((longSampleRate >> 16) & 0xff);

        header[27] = (byte) ((longSampleRate >> 24) & 0xff);

        //音频数据传送速率,采样率*通道数*采样深度/8

        //4字节，音频数据传送速率, 单位是字节。其值为采样率×每次采样大小。播放软件利用此值可以估计缓冲区的大小

        //byteRate = sampleRate * (bitsPerSample / 8) * channels

        header[28] = (byte) (byteRate & 0xff);

        header[29] = (byte) ((byteRate >> 8) & 0xff);

        header[30] = (byte) ((byteRate >> 16) & 0xff);

        header[31] = (byte) ((byteRate >> 24) & 0xff);

        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数

        header[32] = (byte) (2 * channels);

        header[33] = 0;

        //每个样本的数据位数

        //2字节，每个声道的采样精度; 譬如 16bit 在这里的值就是16。如果有多个声道，则每个声道的采样精度大小都一样的；

        header[34] = (byte) audioFormat;

        header[35] = 0;

        //Data chunk

        //ckid：4字节，数据标志符（data），表示 "data" chunk的开始。此块中包含音频数据，小写；

        header[36] = 'd';

        header[37] = 'a';

        header[38] = 't';

        header[39] = 'a';

        //音频数据的长度，4字节，audioDataLen = totalDataLen - 36 = fileLenIncludeHeader - 44

        header[40] = (byte) (totalAudioLen & 0xff);

        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);

        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);

        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        return header;
    }

    public void morseCodeTest() throws IOException {
//        pac.Behavior.MorseEncoder encoder = new pac.Behavior.MorseEncoder();
//        byte[] bytes = encoder.codeConvert2Sound(4000,3000,44100);
//
//        byte[] header = encoder.writeWavFileHeader(bytes.length, 44100, 1, 8);
//
//        ByteArrayBuilder builder  = new ByteArrayBuilder();
//        builder.write(header);
//        builder.write(bytes);
//
//        byte[] data = builder.toByteArray();
//
//        File file = new File("D:\\test.wav");
//        OutputStream os = new FileOutputStream(file);
//        os.write(data);
    }
    private static double[] createRandom(int code)
    {
        String dir = Integer.toString(code)+"Random.txt" ;
        File file = new File(dir);
        if (file.exists())
            return FileManager.readFile(dir);
        Random r = new Random();
        int[] ran = new int[41];
        double[] Mrandom=new double[41];
        int m = 0;
        for (int i = 0; i < 41; i++) {
            ran[i] = r.nextInt(20)+1;
            m = m + ran[i];
        }
        for (int i = 0; i < 41; i++) {
            Mrandom[i]=(double) ran[i]/(double)m;
        }
        FileManager.writeFile(dir,Mrandom);
        return Mrandom;
    }
    public byte[] createMixSound(int code,int Duration, int SampleRate) {
        int num = (int) Math.ceil((float) Duration * (float) SampleRate / 1000);
        byte[][] bytes = new byte[41][num];
        SoundMaker encoder = new SoundMaker();
        for (int i = 0; i < 41; i++) {
            bytes[i] = encoder.codeConvert2Sound(4000 + (i - 1) * 400, 3000, 44100);
        }
        double[] audio = new double[num];
        for (int j = 0; j < audio.length; j++) audio[j] = 0;
        double[] random=createRandom(code);
        for (int i = 0; i < 41; i++)
            for (int j = 0; j < bytes[1].length; j++)
                audio[j] = audio[j] + (double) bytes[i][j] * random[i];
        byte[] mybytes = new byte[num];
        for (int j = 0; j < bytes[1].length; j++)
            mybytes[j] = (byte) audio[j];
        return mybytes;
    }
}
