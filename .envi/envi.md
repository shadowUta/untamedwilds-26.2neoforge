# Untamed Wilds 开发与构建环境

> 本文件是项目开发规则、构建环境、版本信息和构建记录的唯一维护入口。环境或版本发生变化时，必须同步更新本文件。

## 1. 项目信息

| 项目 | 当前值 | 来源 |
| --- | --- | --- |
| 项目名称 | Untamed Wilds | `README.md` |
| Mod ID | `untamedwilds` | `gradle.properties` / `neoforge.mods.toml` |
| Minecraft | `26.2` | `gradle.properties` |
| Mod 版本 | `4.0.0` | `gradle.properties` / `neoforge.mods.toml` |
| 完整构建版本 | `26.2-4.0.0` | `gradle.properties` |
| Mod 加载器 | NeoForge | `build.gradle` / `neoforge.mods.toml` |
| NeoForge | `26.2.0.59` | `gradle.properties` |
| 构建插件 | ModDevGradle `2.0.144` | `build.gradle` |
| Java | Azul Zulu JDK `25.0.4` | `build.gradle` Java Toolchain |
| Gradle Wrapper | `9.2.1` | `gradle-wrapper.properties` |
| Mappings | NeoForge/NeoForm `26.2.0.59` | ModDevGradle |

> 当前项目已经切换到 NeoForge 26.2 和 ModDevGradle。Java 源码仍处于 Forge 1.19.2 到 NeoForge 26.2 的迁移阶段，未完成编译前不得标记为可发布版本。

## 2. 当前依赖版本

| 依赖 | 类型 | 版本 |
| --- | --- | --- |
| Citadel | 必需，待移植 | 官方源码：`https://github.com/AlexModGuy/Citadel`；当前未发现 26.2 构件 |
| Patchouli | 可选 | 待确认 NeoForge 26.2 版本 |
| JEI | 开发运行时 | 待确认 NeoForge 26.2 版本 |
| Crash Utilities | 可选 | 待确认 NeoForge 26.2 版本 |

## 3. 开发环境

- 操作系统：Windows。
- 必须安装 **64 位 JDK 25**。
- 必须设置 `JAVA_HOME`，并确保 `%JAVA_HOME%\bin` 已加入 `PATH`。
- 统一使用仓库内的 Gradle Wrapper，不使用系统全局 Gradle。
- Windows 构建入口：`gradlew.bat`。
- 客户端开发运行目录：`run/`（由 ModDevGradle 管理）。
- 模组测试游戏版本目录：`D:\Minecraft\YLauncher\.minecraft\versions\26.2-NeoForge_26.2.0.59`。
- 生成的数据资源目录：`src/generated/resources/`。
- 当前 Java 路径：`C:\Program Files\Zulu\zulu-25`。
- 当前环境检查结果（2026-08-17）：Gradle Wrapper `9.2.1` 和 JDK 25 已验证可用；项目源码尚未通过编译。
- Citadel 调查结果：官方 `master` 面向 Minecraft 1.19.3 Forge，`1.21` 分支面向 Minecraft 1.21.1 NeoForge；未发现 Minecraft 26.2 / NeoForge 26.2 分支或构件。
- Citadel 处理规则：不得把旧版 Citadel JAR 直接加入 26.2 依赖；当前采用兼容 API 移植方案，优先保持项目现有动画和模型调用接口。
- Citadel 迁移源码：`.envi/Citadel-1.21.zip`，解压目录为 `.envi/Citadel-1.21/Citadel-1.21/`。
- Citadel 源码版本：Citadel `2.7.1`、Minecraft `1.21.1`、NeoForge `21.1.203`、Java 21。

## 4. 开发规则

1. 修改代码前确认目标 Minecraft、NeoForge、Java 和依赖版本，不得混用其他版本 API。
2. 新代码保持现有包结构、命名风格和资源命名规范；Mod ID 始终使用 `untamedwilds`。
3. 不直接修改 Gradle 生成目录：`.gradle/`、`build/` 和 `run/` 中的临时文件。
4. 数据生成结果只写入 `src/generated/resources/`，正式资源写入 `src/main/resources/`。
5. 修改注册项、资源路径、Mixin 或访问转换器时，必须同步检查对应配置和资源文件。
6. 每次代码修改后至少完成编译检查；可运行时启动客户端进行验证，并在客户端进程结束后记录结果。
7. 未通过验证的构建不得标记为“成功”或对外发布。
8. 调整 Minecraft、Forge、Java、Gradle、依赖或 Mod 版本时，必须同时更新本文件及实际配置文件。
9. 每次正式构建后，必须在“构建记录”中追加一条记录，不覆盖历史记录。
10. 发布包及相关构建文件统一存放在 `.envi/` 下；建议按完整构建版本建立子目录，例如 `.envi/1.19.2-3.0.1/`。
11. 所有构建和运行日志统一存放在 `.envi/log/`；执行 `.envi/retain-logs.ps1` 后，日志总大小不得超过 50 MB，超限时按最早修改时间删除日志。

## 5. 版本规则

- `mc_version`：Minecraft 目标版本，当前为 `26.2`。
- `untamedwilds_version`：Mod 自身版本，采用语义化版本 `主版本.次版本.修订号`。
- 完整构建版本：`<mc_version>-<untamedwilds_version>`。
- 当前 Jar 基础名称：`untamedwilds`。
- 当前预期产物名称：`untamedwilds-26.2-4.0.0.jar`。
- 版本号以 `gradle.properties` 为主要来源；`mods.toml` 中的版本必须与其保持一致。

## 6. 标准构建流程

1. 确认 JDK 25、`JAVA_HOME` 和网络环境可用。
2. 检查 `gradle.properties`、`mods.toml` 与本文件中的版本一致。
3. 执行干净构建：Windows 使用 `gradlew.bat clean build`。
4. 从 `build/libs/` 获取完成构建的发布 Jar。
5. 将发布 Jar 复制到 `.envi/<完整构建版本>/`。
6. 使用 `gradlew.bat runClient --no-configuration-cache --console=plain` 启动开发客户端完成基本运行验证。
7. 如需使用外部测试实例，使用上方记录的 YLauncher 26.2 NeoForge 版本目录。
8. 在下方追加构建记录，包括时间、版本、提交、结果、产物和备注。

## 7. 构建记录

### 2026-08-17

- 目标版本：`26.2-4.0.0`。
- 环境：Azul Zulu JDK `25.0.4`、Gradle Wrapper `9.2.1`、NeoForge `26.2.0.59`。
- 结果：失败，尚未生成可发布 Jar。
- 验证：`compileJava` 和 `runClient` 均在编译阶段失败。
- 主要阻塞：Citadel 动画/模型 API 缺失，以及 `Vector3d`、`BlockPathTypes` 等旧 Minecraft API 尚未完成迁移。

### Citadel 官方源码调查

- 官方仓库：`https://github.com/AlexModGuy/Citadel`。
- 可参考的 API 包路径仍为：`com.github.alexthe666.citadel.animation` 和 `com.github.alexthe666.citadel.client.model`。
- 这些 API 在旧分支存在，但其构建目标和 Minecraft/Forge API 与 26.2 不兼容，不能作为当前项目的二进制依赖。
- 独立构建验证：其 Gradle 8.10 构建脚本不能在当前 JDK 25 下执行，报告 `Unsupported class file major version 69`。
- 整体源码接入验证：直接把全部 178 个 Java 文件加入主项目会引入大量额外的 1.21→26.2 API 错误，因此不采用整库直接接入；后续仅移植 Untamed Wilds 实际使用的动画和模型类及其最小依赖。
- 联合构建方案：已在 `build.gradle` 中将用户提供源码的动画/模型最小兼容层复制到 `build/generated/sources/citadelCompat/java`，并加入主模组 `sourceSets.main`，最终计划输出单一主模组 Jar，不额外依赖 Citadel 前置 Jar。
- 当前最小兼容层包含 `Animation`、`IAnimatedEntity`、`AnimationHandler`、`AdvancedEntityModel`、`AdvancedModelBox`、`BasicEntityModel`、`BasicModelPart`、`ModelAnimator` 及其必要容器类；Citadel 的 GUI、Mixin、方块、寻路、完整网络和其他无关模块未接入。
- 2026-08-18 联合构建验证：`compileJava` 已能进入主模组与兼容层共同编译；当前失败主要来自 Untamed Wilds 尚未完成的 NeoForge 26.2 API 迁移（旧 Forge 包、渲染 API、实体 API 等），不是独立 Citadel 源码构建失败。

### 2026-08-18 继续迁移记录

- 已确认 Minecraft 26.2 使用新的实体渲染架构：`EntityRenderer<T, S>`、`MobRenderer<T, S, M>`、`SubmitNodeCollector` 和 Render State；旧版 `render(..., MultiBufferSource, ...)` 入口不能直接保留。
- 已确认渲染类型位于 `net.minecraft.client.renderer.rendertype`，资源标识使用 `net.minecraft.resources.Identifier`。
- 已迁移 `MobType` 相关代码为 `EntityTypeTags.UNDEAD` 判断，并更新 `TropicalFish`、`WaterAnimal`、`Spider`、`AbstractHorse`、`BlockSource` 等包路径。
- 已移除部分旧 Forge `IForgeShearable` 接口声明，更新方向属性为 26.2 的 `EnumProperty<Direction>`。
- 当前结果：`compileJava` 仍失败，主要剩余阻塞为大批旧版实体渲染器、投射物模型以及 Citadel/26.2 Render State 适配；尚未生成可发布 Jar。

