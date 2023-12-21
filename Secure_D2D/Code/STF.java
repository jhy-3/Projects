package pac.newkork;

import pac.Demo.Demo;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class STF {
    static BigInteger KEY, X;

    static BigInteger DHp=new BigInteger("1000000007"), al=new BigInteger("8368358");
    static BigInteger rsap, rsaq, rsan, rsasn, rsae, rsad,rsadq,rsadp,rsaqInv,eitherRSAn,eitherRSAe;




    void creatX() {
        long RandomRange = (long) 1e8;
        X = BigInteger.valueOf ((long) (Math.random() * RandomRange+1));
        System.out.println("------------------");
        System.out.println("生成X，" + X);
        System.out.println("------------------");
        try {
            test.fileWriterMethod("X.txt", X.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void loadX() {

        String fileName = "X.txt";

        String sb;
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BigInteger xxx = new BigInteger(sb);
        X = xxx;

        System.out.println("------------------");
        System.out.println("读取X，" + X);
        System.out.println("------------------");

    }

    void sendVoice(BigInteger aa,String filename)
    {
        BigInteger temp=aa;
        int numm=0;
        while(temp.compareTo(BigInteger.ZERO)==1)
        {
            temp=temp.divide(BigInteger.TEN);
            numm++;
        }
        int code[]=new int[numm];
        int num2=0;
        while(aa.compareTo(BigInteger.ZERO)==1)
        {
            code[numm-1-num2]=(aa.mod(BigInteger.TEN)).intValue();
            aa=aa.divide(BigInteger.TEN);
            num2++;
        }

        Demo.ShakeHandDemoSend(code,filename);
    }

    void sendY() {
        BigInteger Y = fastPower(al, X, DHp);
        System.out.println("------------------");
        System.out.println("生成Y，" + Y);
        System.out.println("已发送");
        System.out.println("------------------");
        sendVoice(Y,"Y.wav");
    }

    void sendMessage(String s1){
        System.out.println("------------------");
        byte[] bytes = BigIntegerTobyte(Coding(messageToBig(s1)));
        BigInteger send=byteToBigInteger(DES.encrypt(bytes,DES.passwordToKey(KEY.toString())));
        sendVoice(send,"message.wav");
        System.out.println("已发送");
        System.out.println("------------------");
    }

    void reciveMessage()
    {
        BigInteger bigInteger = reciveVoice("Message.wav");
        System.out.println("------------------");
        byte[] bytes = BigIntegerTobyte(bigInteger);
        BigInteger Message=byteToBigInteger(DES.decrypt(bytes,DES.passwordToKey(KEY.toString())));
        int Messagecoutcout = (Message.mod((BigInteger.TEN))).intValue();
        Message = Message.divide(BigInteger.TEN);
        int Messagecout = (Message.mod((BigInteger.TEN).pow(Messagecoutcout))).intValue();
        Message = Message.divide((BigInteger.TEN).pow(Messagecoutcout));
        BigInteger sign = Message.mod((BigInteger.TEN).pow(Messagecout));
        Message = Message.divide((BigInteger.TEN).pow(Messagecout));
        System.out.println("提取明文 " + Message);
        System.out.println("提取签名 " + sign);
        if((eitherENcode(sign)).equals(Message))
            System.out.println("*** 签名正确 ***");
        System.out.println("数字转化文本 " + BigIntegerToMessage(Message));
        System.out.println("------------------");
    }

    String BigIntegerToMessage(BigInteger Message)
    {
        String s1=null;
        return s1;
    }

    BigInteger eitherENcode (BigInteger N)
    {
        return fastPower(N,eitherRSAe,eitherRSAn);
    }

    BigInteger Coding(BigInteger message)
    {
        String s1;
        BigInteger sign=decode(message);
        BigInteger temp =sign;
        int num=0;
        while(temp.compareTo(BigInteger.ZERO)==1){
            num++;
            temp=temp.divide(BigInteger.TEN);
        }
        int nnum=0;
        int a=num;
        while (a>0)
        {
            nnum++;
            a/=10;
        }
        s1= message.toString() + decode(message).toString() + num + nnum;
        BigInteger ed = new BigInteger(s1);
        System.out.println("签名结果 " +ed);
        return ed;
    }


    BigInteger reciveVoice(String filename) {
        int Rcode[];
        Rcode=Demo.ShakeHandDemoReceive(filename);
        BigInteger ans = BigInteger.ZERO;
        for(int i=0;i< Rcode.length;i++)
            ans = (ans.multiply(BigInteger.TEN)).add(BigInteger.valueOf(Rcode[i]));
        return ans;
    }

    void reciveY() {

        BigInteger Y = reciveVoice("inY.wav");
        System.out.println("------------------");
        System.out.println("接到Y，" + Y);
        KEY = fastPower(Y, X, DHp);
        System.out.println("生成KEY，" + KEY);
        System.out.println("------------------");
        try {
            test.fileWriterMethod("KEY.txt", X.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void loadKEY()
    {
        String fileName = "KEY.txt";

        String sb;
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BigInteger xxx = new BigInteger(sb);
        KEY = xxx;

        System.out.println("------------------");
        System.out.println("读取KEY，" + KEY);
        System.out.println("------------------");
    }

    void loadRSA()
    {
        String fileName;
        String sb;
        BigInteger xxx;

        fileName = "rsad.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsad = xxx;

        fileName = "rsadp.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsadp = xxx;

        fileName = "rsadq.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsadq = xxx;

        fileName = "rsae.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsae = xxx;

        fileName = "rsan.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsan = xxx;

        fileName = "rsap.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsap = xxx;

        fileName = "rsaq.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsaq = xxx;

        fileName = "rsaqInv.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsaqInv = xxx;

        fileName = "rsasn.txt";
        try {
            sb = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        xxx = new BigInteger(sb);
        rsasn = xxx;

        System.out.println("------------------");
        System.out.println("读取p，" + rsap);
        System.out.println("读取q，" + rsaq);
        System.out.println("读取n，" + rsan);
        System.out.println("读取sn，" + rsasn);
        System.out.println("读取e，" + rsae);
        System.out.println("读取d，" + rsad);
        System.out.println("------------------");
    }

    void creatPublicKey() {
        rsap = CreatPrimeNumber();
        rsaq = CreatPrimeNumber();
        while (rsap == rsaq)
            rsaq = CreatPrimeNumber();
        rsan = rsap.multiply(rsaq);
        rsasn = (rsap.subtract(BigInteger.ONE)).multiply(rsaq.subtract(BigInteger.ONE));
        rsae = creatE(rsasn);
        rsad = niyuan(rsasn, rsae);
        rsadp = niyuan(rsap.subtract(BigInteger.ONE), rsae);
        rsadq = niyuan(rsaq.subtract(BigInteger.ONE), rsae);
        rsaqInv = niyuan(rsap, rsaq);
        System.out.println("------------------");
        System.out.println("rsa密钥生成");
        System.out.println("生成p，" + rsap);
        System.out.println("生成q，" + rsaq);
        System.out.println("生成n，" + rsan);
        System.out.println("生成sn，" + rsasn);
        System.out.println("生成e，" + rsae);
        System.out.println("生成d，" + rsad);
        System.out.println("------------------");
        try {
            test.fileWriterMethod("rsap.txt", rsap.toString());
            test.fileWriterMethod("rsaq.txt", rsaq.toString());
            test.fileWriterMethod("rsan.txt", rsan.toString());
            test.fileWriterMethod("rsasn.txt", rsasn.toString());
            test.fileWriterMethod("rsae.txt", rsae.toString());
            test.fileWriterMethod("rsad.txt", rsad.toString());
            test.fileWriterMethod("rsadp.txt", rsadp.toString());
            test.fileWriterMethod("rsadq.txt", rsadq.toString());
            test.fileWriterMethod("rsaqInv.txt", rsaqInv.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendRSAn()
    {
        System.out.println("------------------");
        byte[] bytes = BigIntegerTobyte(rsan);
        BigInteger send=byteToBigInteger(DES.encrypt(bytes,DES.passwordToKey(KEY.toString())));
        sendVoice(send,"RSAn.wav");
        System.out.println("已发送");
        System.out.println("------------------");
    }

    public void sendRSAe() {
        System.out.println("------------------");
        byte[] bytes = BigIntegerTobyte(rsae);
        BigInteger send=byteToBigInteger(DES.encrypt(bytes,DES.passwordToKey(KEY.toString())));
        sendVoice(send,"RSAe.wav");
        System.out.println("已发送");
        System.out.println("------------------");
    }

    public void reciveRSAn()
    {
        BigInteger bigInteger = reciveVoice("RSAn.wav");
        System.out.println("------------------");
        System.out.println("接收到 " + bigInteger);
        byte[] bytes = BigIntegerTobyte(bigInteger);
        eitherRSAn=byteToBigInteger(DES.decrypt(bytes,DES.passwordToKey(KEY.toString())));
        System.out.println("转码 " + eitherRSAn);
        System.out.println("------------------");
    }

    public void reciveRSAe()
    {
        BigInteger bigInteger = reciveVoice("RSAe.wav");
        byte[] bytes = BigIntegerTobyte(bigInteger);
        eitherRSAe=byteToBigInteger(DES.decrypt(bytes,DES.passwordToKey(KEY.toString())));
        System.out.println("------------------");
        System.out.println("接到" + bigInteger);
        System.out.println("转码" + eitherRSAe);
        System.out.println("------------------");
    }

    //出来的数一定是 60位的16进制 40位64进制
    BigInteger messageToBig(String s)
    {
        BigInteger ans = BigInteger.ZERO;
        for(int i=0;i<s.length();i++)
        {
            int tep;
            if(s.charAt(i)>='0'&&s.charAt(i)<='9')
                tep=s.charAt(i)-'0'+1;
            else if(s.charAt(i)>='a'&&s.charAt(i)<='z')
                tep=s.charAt(i)-'a'+11;
            else if(s.charAt(i)>='A'&&s.charAt(i)<='Z')
                tep=s.charAt(i)-'A'+37;
            else
                tep=63;
            ans=(ans.multiply(BigInteger.valueOf(64))).add(BigInteger.valueOf(tep));
        }
        for(int i=1;i<=40-s.length();i++)
            ans=ans.multiply(BigInteger.valueOf(64));
        System.out.println("文本转化为数字 " + ans);
        return ans;
    }

    byte[] BigIntegerTobyte(BigInteger bigInteger)
    {
        char[] inttochar={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        StringBuilder sb = new StringBuilder();
        int num=0;
        while(bigInteger.compareTo(BigInteger.ZERO)==1)
        {
            BigInteger tf = bigInteger.mod(BigInteger.valueOf(16));
            sb.append(inttochar[tf.intValue()]);
            bigInteger=bigInteger.divide(BigInteger.valueOf(16));
            num++;
        }
        if(num%2==1)
            sb.append('0');//s1是反着的
        String s1= String.valueOf(sb.reverse());
        System.out.println("十进制转十六进制 " + s1);
        return DES.parseBytes(s1);
    }

    BigInteger byteToBigInteger(byte[] bytes)
    {
        String s1=DES.hex(bytes);
        System.out.println("加解密变化 " + s1);

        String ret = s1;
        BigInteger ans=BigInteger.ZERO;
        for(int i=0;i<ret.length();i++)
        {
            if(ret.charAt(i)==' ') continue;
            long temppp;
            if(ret.charAt(i)>='0'&&ret.charAt(i)<='9')
                temppp=ret.charAt(i)-'0';
            else
                temppp=ret.charAt(i)-'A'+10;
            ans = (ans.multiply(BigInteger.valueOf(16))).add(BigInteger.valueOf(temppp));
        }
        System.out.println("十六进制转十进制 " + ans);
        return ans;
    }

    String byteTomessage(byte[] bytes)
    {
        String s1=DES.hex(bytes);
        System.out.println("文本接收 " + s1);

        String ret = s1;
        System.out.println("文本还原1 " + ret);
        long ans=0,tep=1;//tep是阶数，每次乘16
        for(int i=0;i<ret.length();i++)
        {
            if(ret.charAt(i)==' ') continue;
            long temppp;
            if(ret.charAt(i)>='0'&&ret.charAt(i)<='9')
                temppp=ret.charAt(i)-'0';
            else
                temppp=ret.charAt(i)-'A'+10;
            ans+=temppp*tep;
            tep*=16;
        }
        System.out.println("文本还原2 " + ans);
        StringBuilder fin = new StringBuilder();
        while(ans>0)
        {
            long teeee=ans%63;
            char aaa;
            if(teeee==62)
                aaa=' ';
            else if(teeee>=36&&teeee<62)
                aaa= (char) (teeee-36+'A');
            else if(teeee>=10&&teeee<36)
                aaa= (char) (teeee-10+'a');
            else
                aaa= (char) (teeee-'0');
            fin.append(aaa);
            ans/=63;
        }
        String s2= String.valueOf(fin.reverse());
        System.out.println("文本还原3 " + s2);
        return s2;
    }

    //传输，先String 转 BigInterger ，然后签名，然后加上公钥
    void sendPublic(String message) {
        BigInteger clear = messageToBig(message);//信息转数字
        BigInteger ciphe=decode(clear);//签名

    }

    void sendOut(byte[] bytes)
    {
        byte[] received = DES.encrypt(bytes, DES.passwordToKey(KEY.toString()));//比特加密
        String s1 = DES.hex(received);//转16进制
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= s1.length(); i++)
            if (s1.charAt(i) != ' ')
                sb.append(s1.charAt(i));
        //发送

    }

    String reveiceVoice(String s1)
    {
        //接收
        byte[] received = DES.parseBytes(s1);//16转比特
        byte[] decd = DES.decrypt(received,DES.passwordToKey(KEY.toString()));//比特解密
        return byteTomessage(decd);//比特转信息
    }

    BigInteger encode(BigInteger numberword)
    {
        BigInteger end=fastPower(numberword,rsae,rsan);
        System.out.println("加密结果 " + end);
        return end;
    }

    BigInteger decode(BigInteger numberword)
    {
        BigInteger m1 = fastPower(numberword,rsadp,rsap);
        BigInteger m2 = fastPower(numberword,rsadq,rsaq);
        BigInteger h = (rsaqInv.multiply(m1.subtract(m2))).mod(rsap);
        BigInteger m = m2.add(h.multiply(rsaq));
        return m;
        //return fastPower(numberword,rsad,rsan);
    }

    //m的k次模p
    public static BigInteger fastPower(BigInteger m, BigInteger k, BigInteger p) {
        BigInteger ans = BigInteger.ONE;
        while (k.compareTo(BigInteger.ZERO)==1) {//k>0
            if ((k.mod(BigInteger.valueOf(2))).equals(BigInteger.ONE) ) {
                ans = ans.multiply(m);
                ans = ans.mod(p);
            }
            m = m.multiply(m);
            m = m.mod(p);
            k=k.divide(BigInteger.TWO);
        }
        return ans;
    }

    public BigInteger niyuan(BigInteger a, BigInteger b)  //求550关于模1769的乘法逆元
    // 550*X(mod1769)=1
    // niyuan(1769,550)
    {
        BigInteger[] m = {BigInteger.ONE, BigInteger.ZERO, a};
        BigInteger[] n = {BigInteger.ZERO, BigInteger.ONE, b};
        BigInteger[] temp = new BigInteger[3];
        BigInteger q = BigInteger.ZERO;  //初始化
        boolean flag = true;
        while (flag) {
            q = m[2] .divide(n[2]) ;
            for (int i = 0; i < 3; i++) {
                temp[i] = m[i].subtract(q.multiply(n[i]))  ;//可能有问题
                m[i] = n[i];
                n[i] = temp[i];
            }
            if (n[2] .equals(BigInteger.ONE) ) {
                if (n[1].compareTo(BigInteger.ZERO)==-1) {
                    n[1] = n[1] .add(a) ;
                }
                return n[1];
            }
            if (n[2].compareTo(BigInteger.ZERO) == 0) {
                flag = false;
            }
        }
        return BigInteger.ZERO;
    }

    /*public static long CreatPrimeNumber() {
        while (true) {
            long RandomRange = (long) 1e10;
            long x = (long) (Math.random() * RandomRange) + 1;
            long i = 2;
            for (; i < Math.sqrt(x); i++) {
                if (x % i == 0) {
                    break;
                }
            }
            if (i >= Math.sqrt(x)) {
                return x;
            }
        }
    }*/
    public static BigInteger CreatPrimeNumber(){
        while(true){
            BigInteger x = creatRandom(30);
            if (isPrime(x)==1){
                return x;
            }
        }
    }

    public static int isPrime(BigInteger n){
        int count = 10;
        Random t = new Random();
        while(count > 0){
            long a = t.nextLong( 1000000000)+2;
            BigInteger A=BigInteger.valueOf(a);
            if(getPrime(n,A)==0){
                return 0;
            }
            count--;
        }
        return 1;
    }

    //素性检测
    public static int getPrime(BigInteger n,BigInteger a){
        BigInteger t=BigInteger.ONE;
        int k=0;
        if((n.gcd(a)).compareTo(BigInteger.ONE)!=0){
            return 0;
        }else {
            BigInteger q= n.subtract(BigInteger.ONE);
            while( (q.mod(BigInteger.TWO)).equals(BigInteger.ZERO))
            {
                q=q.divide((BigInteger.TWO));
                k++;
            }
            t = fastPower(a,q,n);//快速米
            t = t.mod(n);
            if(t.equals(BigInteger.ONE)){
                return 1;
            }else {
                for(int i=0;i<k;i++){
                    if(t.equals(n.subtract(BigInteger.ONE))){
                        return 1;
                    }
                    t=t.multiply(t);
                    t=t.mod(n);
                }
                return 0;
            }

        }
    }

    public static BigInteger creatE(BigInteger n) {
        while (true) {
            BigInteger a= creatRandom(50);
            if ((a.gcd(n)).equals(BigInteger.ONE) ) {
                return a;
            }
        }
    }

    //生成一个 k位的随机数(16进制）
    public static BigInteger creatRandom(int k)
    {
        Random r = new Random();
        BigInteger ans;
        int t=0;
        while(t==0)
        {
            t = r.nextInt(16);
        }
        ans = BigInteger.valueOf(t);
        for(int i=2;i<=k;i++)
        {
            int n2 = r.nextInt(16);
            ans = (ans.multiply(BigInteger.valueOf(16))).add(BigInteger.valueOf(n2));
        }
        return ans;
    }


}
