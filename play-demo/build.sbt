// ===========================================
// Play Framework Demo Project - build.sbt
// ===========================================

name := "play-demo"
organization := "com.example"
version := "1.0-SNAPSHOT"

// Play Framework 版本
lazy val root = (project in file(".")).enablePlugins(PlayJava)

// Scala 版本（与 Play 2.8.x 兼容）
scalaVersion := "2.13.11"

// 依赖库
libraryDependencies ++= Seq(
  guice,                                                // 依赖注入
  javaJdbc,                                             // JDBC 支持
  javaJpa,                                              // JPA 支持
  "com.h2database" % "h2" % "1.4.200",                  // H2 内存数据库（兼容 Java 8）
  "mysql" % "mysql-connector-java" % "8.0.28",          // MySQL 驱动
  "org.hibernate" % "hibernate-core" % "5.6.15.Final",  // Hibernate ORM（兼容 Java 8）
  "org.hibernate.validator" % "hibernate-validator" % "6.2.5.Final", // Bean Validation
  "org.glassfish" % "javax.el" % "3.0.0",               // Expression Language
  "org.mindrot" % "jbcrypt" % "0.4",                    // 密码加密
  
  // 测试依赖
  "org.assertj" % "assertj-core" % "3.24.2" % Test,
  "org.awaitility" % "awaitility" % "4.2.0" % Test,
  "com.h2database" % "h2" % "1.4.200" % Test,
  "org.mockito" % "mockito-core" % "3.12.4" % Test,
  "org.mockito" % "mockito-inline" % "3.12.4" % Test
)

// 解决依赖版本冲突
ThisBuild / evictionErrorLevel := Level.Warn
libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
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

