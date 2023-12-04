package pac.Behavior;

public class Match {
    public static boolean MatchTheMusic(double learn[], double getAudio[], double bandwidth, double CumulativeError) {
        double sum = 0;
        for (int i = 0; i < 41; i++) {
            if (Math.abs(learn[i] - getAudio[i]) >= bandwidth)
                sum += Math.abs(learn[i] - getAudio[i]);
        }
        if (sum < CumulativeError)
            return true;
        return false;
    }

    public static boolean MatchTheMusic(double learn[], int num, double getAudio[][], double bandwidth, double CumulativeError) {
        int minErrorId, maxCount = 0, maxCountId = 0;
        double minError;
        int[] sum = new int[num];
        for (int i = 0; i < num; i++)
            sum[i] = 0;
        for (int i = 0; i < 41; i++) {
            minError = 1000;
            minErrorId = 0;
            for (int j = 0; j < num; j++) {
                if (Math.abs(learn[i] - getAudio[j][i]) < minError) {
                    minError = Math.abs(learn[i] - getAudio[j][i]);
                    minErrorId = j;
                }
            }
            sum[minErrorId]++;
        }
        for (int i = 0; i < num; i++)
            if (sum[i] > maxCount) {
                maxCount = sum[i];
                maxCountId = i;
            }
        return MatchTheMusic(learn, getAudio[maxCountId], bandwidth, CumulativeError);
    }
}