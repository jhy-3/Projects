package pac.Behavior;

import pac.RealDoubleFFT.RealDoubleFFT;

public class Reciver {
    public static double[] receive(double[] data) {
        int n = data.length;
        int fs = 44100;

        RealDoubleFFT realdoublefft = new RealDoubleFFT(n);

        realdoublefft.ft(data);

        double[] f = new double[n];
        for (int i = 0; i < n; i++)
            f[i] = i * (double) fs / n;
        double[] px = new double[41];
        int k = 0;
        for (int j = 4000; j <= 20000; j = j + 400) {
            for (int i = 0; i < n; i++) {
                if (Math.abs(f[i] / 2 - j) < 5 && px[k] < Math.abs(data[i])) {
                    px[k] = Math.abs(data[i]);
                }
            }
            k++;
        }
        for (int i = 0; i < 41; i++) {
            px[i] = 20 * Math.log10(px[i] * 2 / n);
        }
        return px;
    }
}
