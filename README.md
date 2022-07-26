#### 1、仿真论文：Ciphertext-Policy Attribute-Based Encryption
#### 2、使用库：JPBC + lombok
#### 3、目录结构：
##### 3.1 essay：论文
##### 3.2 lib：jpbc库
##### 3.3 params：椭圆曲线参数
##### 3.4 src：源码
1. attribute：属性对象
2. engine：属性基加密引擎，完成encrypt、decrypt、keyGen工作
3. polynomial：封装的多项式对象
4. structure：访问树结构
5. test：测试类
6. text：明文和密文对象
7. util：工具类
#### 4、如何使用
根据test包下的Text.class类中getAccessTree()方法样例，构建访问树结构，然后依照test1()方法依此进行加解密