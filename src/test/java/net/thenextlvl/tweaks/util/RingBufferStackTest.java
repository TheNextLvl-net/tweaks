package net.thenextlvl.tweaks.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RingBufferStackTest {

    @Test
    void push() {
        RingBufferStack<String> stack = new RingBufferStack<>(3);
        stack.push("Test");
        stack.push("Hello");
        stack.push("Stack");
        Assertions.assertArrayEquals(stack.getArray(), new String[]{"Test", "Hello", "Stack"});
        stack.push("That");
        stack.push("is");
        Assertions.assertArrayEquals(stack.getArray(), new String[]{"That", "is", "Stack"});
    }

    @Test
    void pop() {
        RingBufferStack<String> stack = new RingBufferStack<>(3);
        stack.push("Test");
        stack.push("Hello");
        stack.push("Stack");
        Assertions.assertEquals(stack.pop(), "Stack");
        Assertions.assertEquals(stack.pop(), "Hello");
        Assertions.assertEquals(stack.pop(), "Test");
        Assertions.assertNull(stack.pop());
    }

    @Test
    void peek() {
        RingBufferStack<String> stack = new RingBufferStack<>(3);
        stack.push("Test");
        stack.push("Hello");
        stack.push("Stack");
        Assertions.assertEquals(stack.peek(), "Stack");
        Assertions.assertEquals(stack.peek(), "Stack");
    }

    @Test
    void empty() {
        RingBufferStack<String> stack = new RingBufferStack<>(3);
        stack.push("Test");
        stack.push("Hello");
        stack.push("Stack");
        stack.push("Stack1");
        Assertions.assertFalse(stack.empty());
        stack.pop();
        stack.pop();
        stack.pop();
        Assertions.assertTrue(stack.empty());
    }
}