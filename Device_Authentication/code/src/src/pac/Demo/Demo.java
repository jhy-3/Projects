package pac.Demo;

import pac.Behavior.*;
import pac.WaveAccess.WaveDraw;
import pac.WaveAccess.WaveFileReader;
import pac.plot.Plot;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class Demo {

    public static void ShakeHandDemoSend(int code) {
        byte[] audio = HandShaker.creatShakeHandVoice(code, 1000, 44100);
        InputStream byteArrayInputStream = new ByteArrayInputStream(audio);
        AudioFormat audioFormat = new AudioFormat((float) 44100, 8, 1, true, false);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audio.length / audioFormat.getFrameSize());
        String filename = "ShakeHandSend.wav";
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ShakeHandDemoReceive() {
        String filename = "ShakeHandReceive.wav";
        WaveFileReader reader = new WaveFileReader(filename);
        int[] res = reader.getData()[0];
        byte[] anotherAudio = new byte[res.length];
        for (int i = 0; i < res.length; i++) {
            res[i] -= 128;
            anotherAudio[i] = (byte) res[i];
        }
        System.out.println(HandShaker.doShakeHandVoice(anotherAudio, 1000, 44100));
    }

    public static void SingleWaveDemo(int GenRate) {
        SoundMaker soundMaker = new SoundMaker();
        byte[] audio = soundMaker.codeConvert2Sound(GenRate, 3000, 44100);
        InputStream byteArrayInputStream = new ByteArrayInputStream(audio);
        AudioFormat audioFormat = new AudioFormat((float) 44100, 8, 1, true, false);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audio.length / audioFormat.getFrameSize());
        String filename = "SingleWave.wav";
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        WaveDraw.drawWaveFile(filename);
        Plot.axis(0, 2000, -500, 1000);
    }

    public static void MixWaveDemo(int code) {
        SoundMaker soundMaker = new SoundMaker();
        byte[] audio = soundMaker.createMixSound(code,3000, 44100);
        InputStream byteArrayInputStream = new ByteArrayInputStream(audio);
        AudioFormat audioFormat = new AudioFormat((float) 44100, 8, 1, true, false);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audio.length / audioFormat.getFrameSize());
        String filename = Integer.toString(code)+"MixWave.wav";
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        WaveDraw.drawWaveFile(filename);
        Plot.axis(0, 2000, -500, 1000);
    }
    public static void LearnDemo(int code) {
        String dir=Integer.toString(code)+"Learn.txt";
        File file = new File(dir);
        if(file.exists()) return;//如果已经进行过学习，直接退出
        SoundMaker soundMaker = new SoundMaker();
        //学习音频
        byte[] recordAudio = soundMaker.createMixSound(code,3000, 44100);
        InputStream recordByteArrayInputStream = new ByteArrayInputStream(recordAudio);
        AudioFormat recordAudioFormat = new AudioFormat((float) 44100, 8, 1, true, false);
        AudioInputStream audioInputStream = new AudioInputStream(recordByteArrayInputStream, recordAudioFormat, recordAudio.length / recordAudioFormat.getFrameSize());
        String recordFilename = "RecordWave.wav";
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(recordFilename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        WaveFileReader recordReader = new WaveFileReader(recordFilename);
        int[] recordData = recordReader.getData()[0];
        double[] recordDataD = new double[recordData.length];
        for (int i = 0; i < recordData.length; i++) {
            recordData[i] -= 128;
            recordDataD[i] = (double) recordData[i];
        }
        double[] match_learn = Reciver.receive(recordDataD);
        FileManager.writeFile(dir,match_learn);
    }
    public static void MatchDemo(int code) {
        //匹配音频
        SoundMaker soundMaker = new SoundMaker();
        byte[] matchAudio = soundMaker.createMixSound(code,3000, 44100);
        InputStream matchByteArrayInputStream = new ByteArrayInputStream(matchAudio);
        AudioFormat matchAudioFormat = new AudioFormat((float) 44100, 8, 1, true, false);
        AudioInputStream matchInputStream = new AudioInputStream(matchByteArrayInputStream, matchAudioFormat, matchAudio.length / matchAudioFormat.getFrameSize());
        String matchFilename = "MatchWave.wav";
        try {
            AudioSystem.write(matchInputStream, AudioFileFormat.Type.WAVE, new File(matchFilename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        WaveFileReader matchReader = new WaveFileReader(matchFilename);
        int[] matchData = matchReader.getData()[0];
        double[] matchDataD = new double[matchData.length];
        for (int i = 0; i < matchData.length; i++) {
            matchData[i] -= 128;
            matchDataD[i] = (double) matchData[i];
        }
        double[] match_get = Reciver.receive(matchDataD);
//        for (int i =0;i<match_learn.length;i++){
//            System.out.print(match_learn[i]);
//            System.out.print("\t");
//            System.out.print(match_get[i]);
//            System.out.print("\n");
//        }
        String dir=Integer.toString(code)+"Learn.txt";
        double[] match_learn = FileManager.readFile(dir);
        System.out.println(Match.MatchTheMusic(match_learn, match_get, 10.8, 100));
    }
}
