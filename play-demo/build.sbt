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

  // Ebean ORM 支持
  "io.ebean" % "ebean" % "12.16.1",                     // Ebean ORM
  "io.ebean" % "ebean-ddl-generator" % "12.16.1",       // DDL生成器
  "io.ebean" % "querybean-generator" % "12.16.1" % "provided", // 查询bean生成器

  // Akka Actor 支持（版本需与Play Framework保持一致）
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.21",
  "com.typesafe.akka" %% "akka-actor" % "2.6.21",
  "com.typesafe.akka" %% "akka-stream" % "2.6.21",

  // Akka Cluster 支持
  "com.typesafe.akka" %% "akka-cluster-typed" % "2.6.21",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.6.21",
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % "2.6.21",

  // Akka Serialization
  "com.typesafe.akka" %% "akka-serialization-jackson" % "2.6.21",

  // Akka Management (用于集群管理)
  "com.lightbend.akka.management" %% "akka-management" % "1.1.4",
  "com.lightbend.akka.management" %% "akka-management-cluster-http" % "1.1.4",

  // Scala-Java8 兼容库（用于Future转换）
  "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2",

  // 测试依赖
  "org.assertj" % "assertj-core" % "3.24.2" % Test,
  "org.awaitility" % "awaitility" % "4.2.0" % Test,
  "com.h2database" % "h2" % "1.4.200" % Test,
  "org.mockito" % "mockito-core" % "3.12.4" % Test,
  "org.mockito" % "mockito-inline" % "3.12.4" % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.20" % Test
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

