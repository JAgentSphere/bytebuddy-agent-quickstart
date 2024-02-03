# ByteBuddy-Agent-QuickStart

当前项目旨在编写一个 Java Agent 所需要的完整能力，以及尝试解答每一个细节知识点为何需要这么写。

## 功能

- [ ] 支持方法的参数和返回值的修改
- [ ] 支持完整的 AOP 能力
- [ ] 支持清晰的日志打印，方便定位问题
- [ ] 支持动态注入、卸载以及更新
- [ ] 支持自我观测，性能指标采集
- [ ] 支持最小化验证插件功能（研究插件单测改如何编写）

## FQA

1. 为什么需要 Spy 类？

2. 为什么写一个简单的 Agent 需要分成三个包，一个不行吗？

3. 为什么 CoreClassLoader 的 parent 设置成 ExtClassLoader 而不是 SystemClassLoader 或者说是 AppClassLoader？

4. 为什么需要使用到 shadow 插件？

5. 如果继承至 ExtClassLoader 是否还需要使用 shadow 进行 relocating class？