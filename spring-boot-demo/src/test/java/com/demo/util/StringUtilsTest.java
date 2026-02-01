package com.demo.util;

/**
 * StringUtils 工具类测试
 * 使用简单的断言进行测试（无需JUnit依赖）
 * 
 * @author Demo
 * @version 1.0
 */
public class StringUtilsTest {

    private static int passCount = 0;
    private static int failCount = 0;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           StringUtils 工具类测试                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        testIsEmpty();
        testIsNotEmpty();
        testIsBlank();
        testIsNotBlank();
        testReverse();
        testCapitalize();
        testUncapitalize();
        testCountOccurrences();
        testRepeat();
        testLeftPad();
        testRightPad();
        testIsNumeric();
        testSafeSubstring();

        System.out.println();
        System.out.println("══════════════════════════════════════════════════════════");
        System.out.println("测试结果汇总:");
        System.out.println("  ✓ 通过: " + passCount);
        System.out.println("  ✗ 失败: " + failCount);
        System.out.println("  总计: " + (passCount + failCount));
        System.out.println("══════════════════════════════════════════════════════════");
        
        if (failCount > 0) {
            System.exit(1);
        }
    }

    private static void testIsEmpty() {
        System.out.println("▶ 测试 isEmpty()");
        
        assertTrue("isEmpty(null) 应返回 true", StringUtils.isEmpty(null));
        assertTrue("isEmpty(\"\") 应返回 true", StringUtils.isEmpty(""));
        assertFalse("isEmpty(\" \") 应返回 false", StringUtils.isEmpty(" "));
        assertFalse("isEmpty(\"hello\") 应返回 false", StringUtils.isEmpty("hello"));
        
        System.out.println();
    }

    private static void testIsNotEmpty() {
        System.out.println("▶ 测试 isNotEmpty()");
        
        assertFalse("isNotEmpty(null) 应返回 false", StringUtils.isNotEmpty(null));
        assertFalse("isNotEmpty(\"\") 应返回 false", StringUtils.isNotEmpty(""));
        assertTrue("isNotEmpty(\" \") 应返回 true", StringUtils.isNotEmpty(" "));
        assertTrue("isNotEmpty(\"hello\") 应返回 true", StringUtils.isNotEmpty("hello"));
        
        System.out.println();
    }

    private static void testIsBlank() {
        System.out.println("▶ 测试 isBlank()");
        
        assertTrue("isBlank(null) 应返回 true", StringUtils.isBlank(null));
        assertTrue("isBlank(\"\") 应返回 true", StringUtils.isBlank(""));
        assertTrue("isBlank(\" \") 应返回 true", StringUtils.isBlank(" "));
        assertTrue("isBlank(\"\\t\\n\") 应返回 true", StringUtils.isBlank("\t\n"));
        assertFalse("isBlank(\"hello\") 应返回 false", StringUtils.isBlank("hello"));
        assertFalse("isBlank(\" hello \") 应返回 false", StringUtils.isBlank(" hello "));
        
        System.out.println();
    }

    private static void testIsNotBlank() {
        System.out.println("▶ 测试 isNotBlank()");
        
        assertFalse("isNotBlank(null) 应返回 false", StringUtils.isNotBlank(null));
        assertTrue("isNotBlank(\"hello\") 应返回 true", StringUtils.isNotBlank("hello"));
        
        System.out.println();
    }

    private static void testReverse() {
        System.out.println("▶ 测试 reverse()");
        
        assertNull("reverse(null) 应返回 null", StringUtils.reverse(null));
        assertEquals("reverse(\"\") 应返回 \"\"", "", StringUtils.reverse(""));
        assertEquals("reverse(\"hello\") 应返回 \"olleh\"", "olleh", StringUtils.reverse("hello"));
        assertEquals("reverse(\"abc\") 应返回 \"cba\"", "cba", StringUtils.reverse("abc"));
        assertEquals("reverse(\"a\") 应返回 \"a\"", "a", StringUtils.reverse("a"));
        
        System.out.println();
    }

    private static void testCapitalize() {
        System.out.println("▶ 测试 capitalize()");
        
        assertNull("capitalize(null) 应返回 null", StringUtils.capitalize(null));
        assertEquals("capitalize(\"\") 应返回 \"\"", "", StringUtils.capitalize(""));
        assertEquals("capitalize(\"hello\") 应返回 \"Hello\"", "Hello", StringUtils.capitalize("hello"));
        assertEquals("capitalize(\"Hello\") 应返回 \"Hello\"", "Hello", StringUtils.capitalize("Hello"));
        
        System.out.println();
    }

    private static void testUncapitalize() {
        System.out.println("▶ 测试 uncapitalize()");
        
        assertNull("uncapitalize(null) 应返回 null", StringUtils.uncapitalize(null));
        assertEquals("uncapitalize(\"\") 应返回 \"\"", "", StringUtils.uncapitalize(""));
        assertEquals("uncapitalize(\"Hello\") 应返回 \"hello\"", "hello", StringUtils.uncapitalize("Hello"));
        
        System.out.println();
    }

    private static void testCountOccurrences() {
        System.out.println("▶ 测试 countOccurrences()");
        
        assertEquals("countOccurrences(null, \"a\") 应返回 0", 0, StringUtils.countOccurrences(null, "a"));
        assertEquals("countOccurrences(\"aaa\", \"a\") 应返回 3", 3, StringUtils.countOccurrences("aaa", "a"));
        assertEquals("countOccurrences(\"abcabc\", \"abc\") 应返回 2", 2, StringUtils.countOccurrences("abcabc", "abc"));
        assertEquals("countOccurrences(\"hello\", \"ll\") 应返回 1", 1, StringUtils.countOccurrences("hello", "ll"));
        assertEquals("countOccurrences(\"hello\", \"x\") 应返回 0", 0, StringUtils.countOccurrences("hello", "x"));
        
        System.out.println();
    }

    private static void testRepeat() {
        System.out.println("▶ 测试 repeat()");
        
        assertEquals("repeat(null, 3) 应返回 \"\"", "", StringUtils.repeat(null, 3));
        assertEquals("repeat(\"a\", 0) 应返回 \"\"", "", StringUtils.repeat("a", 0));
        assertEquals("repeat(\"a\", 3) 应返回 \"aaa\"", "aaa", StringUtils.repeat("a", 3));
        assertEquals("repeat(\"ab\", 2) 应返回 \"abab\"", "abab", StringUtils.repeat("ab", 2));
        
        System.out.println();
    }

    private static void testLeftPad() {
        System.out.println("▶ 测试 leftPad()");
        
        assertEquals("leftPad(\"1\", 3, '0') 应返回 \"001\"", "001", StringUtils.leftPad("1", 3, '0'));
        assertEquals("leftPad(\"123\", 3, '0') 应返回 \"123\"", "123", StringUtils.leftPad("123", 3, '0'));
        assertEquals("leftPad(\"1234\", 3, '0') 应返回 \"1234\"", "1234", StringUtils.leftPad("1234", 3, '0'));
        assertEquals("leftPad(null, 3, '0') 应返回 \"000\"", "000", StringUtils.leftPad(null, 3, '0'));
        
        System.out.println();
    }

    private static void testRightPad() {
        System.out.println("▶ 测试 rightPad()");
        
        assertEquals("rightPad(\"1\", 3, '0') 应返回 \"100\"", "100", StringUtils.rightPad("1", 3, '0'));
        assertEquals("rightPad(\"123\", 3, '0') 应返回 \"123\"", "123", StringUtils.rightPad("123", 3, '0'));
        assertEquals("rightPad(null, 3, '0') 应返回 \"000\"", "000", StringUtils.rightPad(null, 3, '0'));
        
        System.out.println();
    }

    private static void testIsNumeric() {
        System.out.println("▶ 测试 isNumeric()");
        
        assertFalse("isNumeric(null) 应返回 false", StringUtils.isNumeric(null));
        assertFalse("isNumeric(\"\") 应返回 false", StringUtils.isNumeric(""));
        assertTrue("isNumeric(\"123\") 应返回 true", StringUtils.isNumeric("123"));
        assertFalse("isNumeric(\"12.3\") 应返回 false", StringUtils.isNumeric("12.3"));
        assertFalse("isNumeric(\"12a\") 应返回 false", StringUtils.isNumeric("12a"));
        assertFalse("isNumeric(\" 123\") 应返回 false", StringUtils.isNumeric(" 123"));
        
        System.out.println();
    }

    private static void testSafeSubstring() {
        System.out.println("▶ 测试 safeSubstring()");
        
        assertNull("safeSubstring(null, 0, 1) 应返回 null", StringUtils.safeSubstring(null, 0, 1));
        assertEquals("safeSubstring(\"hello\", 0, 3) 应返回 \"hel\"", "hel", StringUtils.safeSubstring("hello", 0, 3));
        assertEquals("safeSubstring(\"hello\", -1, 3) 应返回 \"hel\"", "hel", StringUtils.safeSubstring("hello", -1, 3));
        assertEquals("safeSubstring(\"hello\", 0, 100) 应返回 \"hello\"", "hello", StringUtils.safeSubstring("hello", 0, 100));
        assertEquals("safeSubstring(\"hello\", 3, 2) 应返回 \"\"", "", StringUtils.safeSubstring("hello", 3, 2));
        
        System.out.println();
    }

    // ==================== 断言工具方法 ====================

    private static void assertTrue(String message, boolean condition) {
        if (condition) {
            System.out.println("  ✓ " + message);
            passCount++;
        } else {
            System.out.println("  ✗ " + message + " [失败: 期望 true, 实际 false]");
            failCount++;
        }
    }

    private static void assertFalse(String message, boolean condition) {
        if (!condition) {
            System.out.println("  ✓ " + message);
            passCount++;
        } else {
            System.out.println("  ✗ " + message + " [失败: 期望 false, 实际 true]");
            failCount++;
        }
    }

    private static void assertEquals(String message, String expected, String actual) {
        if (expected == null && actual == null) {
            System.out.println("  ✓ " + message);
            passCount++;
        } else if (expected != null && expected.equals(actual)) {
            System.out.println("  ✓ " + message);
            passCount++;
        } else {
            System.out.println("  ✗ " + message + " [失败: 期望 \"" + expected + "\", 实际 \"" + actual + "\"]");
            failCount++;
        }
    }

    private static void assertEquals(String message, int expected, int actual) {
        if (expected == actual) {
            System.out.println("  ✓ " + message);
            passCount++;
        } else {
            System.out.println("  ✗ " + message + " [失败: 期望 " + expected + ", 实际 " + actual + "]");
            failCount++;
        }
    }

    private static void assertNull(String message, Object actual) {
        if (actual == null) {
            System.out.println("  ✓ " + message);
            passCount++;
        } else {
            System.out.println("  ✗ " + message + " [失败: 期望 null, 实际 \"" + actual + "\"]");
            failCount++;
        }
    }
}

