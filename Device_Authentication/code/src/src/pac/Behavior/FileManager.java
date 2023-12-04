package pac.Behavior;

import java.io.*;

public class FileManager {
    public static double[] readFile(String fileName) {
        File file = new File(fileName);
        //如果文件不存在，创建文件
        if (!file.exists()) {
            System.out.println("record does not exist!");
            System.exit(0);
        }

        //创建BufferedReader读取文件内容
        BufferedReader br = null;
        double[] res = new double[41];
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                res[index] = Double.parseDouble(line);
                index++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public static void writeFile(String fileName, double[] data) {
        File file = new File(fileName);
        //如果文件不存在，创建文件
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //创建FileOutputStream对象，写入内容
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //向文件中写入内容
            for (int i=0;i<data.length;i++){
                fos.write(Double.toString(data[i]).getBytes());
                fos.write("\n".getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
