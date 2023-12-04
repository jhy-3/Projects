package pac.Behavior;

public class HandShaker {
    private static int StatisticalFrequency(byte[] target) {
        int minSum = 99999, mincode = -1, sum;
        int[] Dur = {28, 25, 22, 19, 16, 13, 10, 7, 4, 3};//对应编码下多少个采样点呈现一个周期
        for (int i = 0; i < 10; i++) {
            sum = 0;
            for (int k = 1; k <= 10; k++) {
                if (Math.abs(target[k] - target[k + Dur[i]]) < 5)
                    continue;
                sum += Math.abs(target[k] - target[k + Dur[i]]);
            }
            if (sum <= minSum) {
                minSum = sum;
                mincode = i;
            }
        }
        return mincode;
    }

    //返回长度为5*Duration，Code默认5位
    public static byte[] creatShakeHandVoice(int Code, int Duration, int SampleRate) {
        int num = (int) Math.ceil((float) Duration * (float) SampleRate / 1000);
        SoundMaker encoder = new SoundMaker();
        byte[] audio = new byte[5 * num];
        byte[][] mbyte = new byte[6][num];
        int[] changeCodetoFrequency = {1575, 1764, 2004, 2321, 2756, 3392, 4410, 6300, 11025, 14700};
        for (int i = 1; i <= 5; i++)
            mbyte[i] = encoder.codeConvert2Sound(changeCodetoFrequency[(int) ((Code / (int) Math.pow(10, 5 - i)) % 10)], Duration, SampleRate);
        for (int i = 0; i < 5 * num; i++)
            audio[i] = mbyte[i / num + 1][i % num];
        return audio;
    }

//    private static int findCodeInVoice(int frequency) {
//        int ma = 1000000, Code = -1;
//        int[] changeCodetoFrequency = {1575, 1764, 2004, 2321, 2756, 3392, 4410, 6300, 11025, 14700};
//        for (int i = 0; i < 10; i++)
//            if (ma > Math.abs(changeCodetoFrequency[i] - frequency)) {
//                ma = Math.abs(changeCodetoFrequency[i] - frequency);
//                Code = i;
//            }
//        return Code;
//    }

    public static int doShakeHandVoice(byte[] audio, int Duration, int SampleRate) {
        int Code = 0, temp;
        int num = (int) Math.ceil((float) Duration * (float) SampleRate / 1000);
        byte[][] mbyte = new byte[5][num];
        for (int i = 0; i < 5; i++)
            for(int j=0;j<200;j++)
                mbyte[i][j] = audio[i*num+num/3+j];//预防采样的时间精度不高
        for (int i = 0; i < 5; i++) {
            temp = StatisticalFrequency(mbyte[i]);
            Code = Code * 10 + temp;
        }
        return Code;
    }
}
