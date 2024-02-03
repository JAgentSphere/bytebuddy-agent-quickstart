# Agent

当前模块用于加载 core 核心模块，以及将 spy 模块注入到 BootstrapClassLoader 中，启动/卸载 Agent。core 模块

使用自定义类加载器加载，既保证了与应用代码隔离，也获得了卸载的能力。借鉴自  [jvm-sandbox](https://github.com/alibaba/jvm-sandbox)。

1. 整个 JVM 声明周期中，只有一个 Agent 实例，即每次都使用一个 Instrumentation 对象进行 transform 以及 remove transform,
   不会使用新的 instrumentation 对象注册新的 Agent
2. 多次注入会先卸载之前加载的 core，然后注册新的 core 到 JVM 中
3. 支持 premain 以及 agentmain 方式注入

## 使用方式

### premain

在启动参数中添加 `-javaagent:/path/to/agent.jar` 即可

```shell
java -javaagent:/path/to/agent.jar -jar /path/to/app.jar
```

### agentmain

使用 jattach 进行动态注入

```shell
## 动态注入
jattach <pid> load instrument false "/path/to/agent.jar=install"

## 动态卸载
jattach <pid> load instrument false "/path/to/agent.jar=uninstall"
```

## 感谢

- [jvm-sandbox](https://github.com/alibaba/jvm-sandbox)
- [arthas](https://github.com/alibaba/arthas)
- [jattach](https://github.com/jattach/jattach)

