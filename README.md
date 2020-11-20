# ketrofit
### kotlin DSL retrofit 网络请求

无rxjava ，无flow，用基本的retrofit，协程及扩展函数，实现最方便快捷的网络请求

使用：
#### 自动绑定当前LifecycleOwner生命周期
```java

 api { Apis.test() }.debug { 
            
        }.error { 
            
        } down {

        }
```
#### 极简

```java
api (Apis.test()) down {

        }
```

#### 无指定协程scope

```java
  Apis.test2().err {

        } down {

        }
```

逻辑简单清晰，可任意自由扩展
