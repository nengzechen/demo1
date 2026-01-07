// ===========================================
// Play Framework Demo Project - build.sbt
// ===========================================

name := "play-demo"
organization := "com.example"
version := "1.0-SNAPSHOT"

// Play Framework 版本
lazy val root = (project in file(".")).enablePlugins(PlayJava)

// Scala 版本
scalaVersion := "2.13.12"

// 依赖库
// 注意：Play Framework 已内置 Jackson，无需手动添加
libraryDependencies ++= Seq(
  guice,                                    // 依赖注入
  javaJdbc,                                 // JDBC 支持
  "com.h2database" % "h2" % "2.2.224",      // H2 内存数据库
  
  // 测试依赖
  "org.assertj" % "assertj-core" % "3.24.2" % Test,
  "org.awaitility" % "awaitility" % "4.2.0" % Test
)

// Java 编译选项
javacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-parameters",
  "-Xlint:unchecked",
  "-Xlint:deprecation"
)

// 开发模式下的热重载
PlayKeys.playDefaultPort := 9000

// 资源目录
Compile / unmanagedResourceDirectories += baseDirectory.value / "conf"

