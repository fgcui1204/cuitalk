# CuiTalk

个人博客+成长日志

## 数据库信息：

### 本地数据库：
```aidl
MySQL

username：root
pwd:123456

```

### Server

```
username: root
pwd: 19921204cfg
```


## 本地启动

```aidl
mvn spring-boot:run
```

## 部署


```aidl
# 打包：

mvn package -Pprod

# 上传
scp ./target/blog-0.0.1-SNAPSHOT.jar root@47.99.213.239:~/cuitalk/

# 登录到server上，先杀死之前的进程，然后执行：

nohup java -jar ./blog-0.0.1-SNAPSHOT.jar > cuitalk.log &


# 配置NGINX

vim /etc/nginx/nginx.conf

    server {
        listen       80;
        server_name  localhost;
	location / {
		proxy_pass http://localhost:8081/;
	}
    }


# 重启NGINX
service nginx restart

```