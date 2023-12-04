import math
import random
import sys
sys.setrecursionlimit(2000)


#求ab的最大公约数
def getGcd(a,b):
    if(a%b):
        return getGcd(b,a%b)
    return b

#求欧拉函数 欧拉函数值=（p-1）*（q-1）
def getEuler(prime1,prime2):
    return (prime1-1)*(prime2-1)

#生成一个很大的随机数 考虑rsa的加密性，在密钥二进制有1024的情况下便可以保证全，因此生成的pq一般在十进制的180位左右
def buildRandom(a): 
    b=random.randint(pow(10,a-1),pow(10,a))
    return b

#求私钥d,扩展欧几里得求逆元
def exGcd(a,b):
    if b == 0:
        return 1, 0, a
    else:
        x,y,q=exGcd(b,a%b)
        x,y=y,(x-(a//b)*y)
        return x,y,q

def modReverse(a,p):
    x, y, q = exGcd(a,p)
    if q != 1:
        raise Exception("No solution.")  #指定位置手抛一个异常
    else:
        return (x + p) % p #防止负数

#快速幂求解a的q次幂对n的模
def binaryExponentiation(a,q,n):
    ans=1
    base=a
    m=n
    b=q
    while(b>0):
        if(b&1):
            ans=ans*base
            ans=ans%m
        base=base*base
        base=base%m
        b>>=1
    return ans

#Robin素数判断一个数n是不是素数
def getPrime(n,a):
    t=1
    k=0
    if(getGcd(n,a)!=1):
        return 0
    else :
        q=n-1
        while(q%2==0):
           q=q>>1
           k+=1
        t=binaryExponentiation(a,q,n)
        t=t%n
        if(t==1):
            return 1
        else :
            for i in range(0,k):
                if(t==n-1):
                    return 1
                t=t*t%n
            return 0

#10次判断一个数是不是素数,确保安全性
def isPrime(n):
    cnt=10
    while(cnt>0):
        a=random.randint(2,n-2)
        if(getPrime(n,a)==0):
            return 0
        cnt=cnt-1
    return 1  

#得到素数p和q 
def getPQ(a):
    while(1):
        number=buildRandom(a)
        if(isPrime(number)==1):
            return number

#求密文
def getCiphertext(m,e,n):
    a=m
    q=e
    return binaryExponentiation(a,q,n)

#求明文
def getPlaintext(c,d,n):
    a=c
    q=d
    return binaryExponentiation(a,q,n)

def count(num:str, from_:int, to:int):
	"""
	:params num: 待转换的数字
	:params from_: 原进制
	:params to: 目标进制
	return 转换结果
	"""
	s = []
	o_num = sum([int(i) * from_ ** n for n, i in enumerate(num[::-1])])
	print("{}进制:{} --> 10进制:{}".format(from_, num, o_num), end=" --> ")
	def inner(num):
		a, b = divmod(num, to)
		if a == 0:
			s.insert(0, b)
			return s
		s.insert(0, b)
		return inner(a)
	result = "".join([str(i) for i in inner(o_num)])
	print("{}进制: {}".format(to, result))
	# return result
	



#主函数
if __name__ == "__main__":
    a=random.uniform(170,180)  #随机数十进制pq的位数
    p=getPQ(int(a))
    q=getPQ(int(a))
    n=p*q       #密钥n
    t=getEuler(p,q)     #pq的欧拉函数
    while(1):               #求解e
        e=random.randint(1,t)
        if(getGcd(e,t)==1):
            break
    d=modReverse(e,t)       #求解d
    print(a,"\n",e,"\n",d)
    plaintext=1548785135105485413516841  #要加密的十进制明文
    ciphertext=getCiphertext(plaintext,e,n)     #加密后的密文

    plainTruetext=getPlaintext(ciphertext,d,n)      #要解密的十进制密文
    
    print("1")
    print(plainTruetext)
    
   
