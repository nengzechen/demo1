# StringUtils 工具类

一个轻量级的 Java 字符串处理工具类，提供常用的字符串操作方法。

## 项目结构

```
demo1/
├── README.md                                    # 项目文档
├── src/
│   ├── main/java/com/demo/util/
│   │   └── StringUtils.java                     # 工具类
│   └── test/java/com/demo/util/
│       └── StringUtilsTest.java                 # 测试类
```

## 功能列表

| 方法 | 描述 |
|------|------|
| `isEmpty(String)` | 判断字符串是否为空或null |
| `isNotEmpty(String)` | 判断字符串是否不为空且不为null |
| `isBlank(String)` | 判断字符串是否为空白（包含空格、制表符等） |
| `isNotBlank(String)` | 判断字符串是否不为空白 |
| `reverse(String)` | 反转字符串 |
| `capitalize(String)` | 将字符串首字母大写 |
| `uncapitalize(String)` | 将字符串首字母小写 |
| `countOccurrences(String, String)` | 统计子字符串出现次数 |
| `repeat(String, int)` | 重复字符串指定次数 |
| `leftPad(String, int, char)` | 左填充字符串到指定长度 |
| `rightPad(String, int, char)` | 右填充字符串到指定长度 |
| `isNumeric(String)` | 判断字符串是否只包含数字 |
| `safeSubstring(String, int, int)` | 安全地截取字符串 |

## 使用示例

```java
import com.demo.util.StringUtils;

public class Example {
    public static void main(String[] args) {
        // 判空
        StringUtils.isEmpty(null);      // true
        StringUtils.isEmpty("");        // true
        StringUtils.isEmpty(" ");       // false
        
        // 空白判断
        StringUtils.isBlank("  ");      // true
        StringUtils.isBlank("\t\n");    // true
        
        // 字符串反转
        StringUtils.reverse("hello");   // "olleh"
        
        // 首字母大写
        StringUtils.capitalize("hello"); // "Hello"
        
        // 统计出现次数
        StringUtils.countOccurrences("abcabc", "abc"); // 2
        
        // 重复字符串
        StringUtils.repeat("ab", 3);    // "ababab"
        
        // 左填充
        StringUtils.leftPad("1", 5, '0'); // "00001"
        
        // 判断是否是数字
        StringUtils.isNumeric("12345"); // true
        
        // 安全截取
        StringUtils.safeSubstring("hello", 0, 100); // "hello"
    }
}
```

## 编译和运行

### 编译

```bash
# 进入项目根目录
cd demo1

# 创建输出目录
mkdir -p target/classes

# 编译工具类
javac -d target/classes src/main/java/com/demo/util/StringUtils.java

# 编译测试类
javac -d target/classes -cp target/classes src/test/java/com/demo/util/StringUtilsTest.java
```

### 运行测试

```bash
java -cp target/classes com.demo.util.StringUtilsTest
```

## 测试输出示例

```
╔══════════════════════════════════════════════════════════╗
║           StringUtils 工具类测试                          ║
╚══════════════════════════════════════════════════════════╝

▶ 测试 isEmpty()
  ✓ isEmpty(null) 应返回 true
  ✓ isEmpty("") 应返回 true
  ...

══════════════════════════════════════════════════════════
测试结果汇总:
  ✓ 通过: 42
  ✗ 失败: 0
  总计: 42
══════════════════════════════════════════════════════════
```

## 特性

- ✅ 零依赖 - 不需要任何第三方库
- ✅ 空安全 - 所有方法都能正确处理 null 输入
- ✅ 完整测试 - 包含详细的测试用例
- ✅ 文档完善 - 每个方法都有 Javadoc 注释

## 许可证

MIT License

