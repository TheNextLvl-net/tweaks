package net.thenextlvl.tweaks.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class RingBufferStack<E> {

    private final Object[] array;
    private int index;

    public RingBufferStack(int size) {
        this.array = new Object[size];
    }

    private int modulo(int base, int mod) {
        if (base < 0) {
            base += mod;
        }
        return base % mod;
    }

    /**
     * Pushes an item onto the top of this stack.
     *
     * @param item the item to be pushed onto this stack.
     * @return the {@code item} argument.
     */
    public E push(@NotNull E item) {
        int newIndex = modulo(index++, array.length);
        array[newIndex] = item;
        return item;
    }

    /**
     * Removes the object at the top of this stack and returns that
     * object as the value of this function.
     *
     * @return The object at the top of this stack.
     */
    public synchronized @Nullable E pop() {
        if (empty()) {
            return null;
        }

        Object obj = peek();
        array[modulo(--index, array.length)] = null;

        return (E) obj;
    }

    /**
     * Looks at the object at the top of this stack without removing it
     * from the stack.
     *
     * @return the object at the top of this stack.
     */
    public synchronized @Nullable E peek() {
        if (empty())
            return null;
        var i = index - 1;
        return (E) array[modulo(i, array.length)];
    }

    /**
     * Tests if this stack is empty.
     *
     * @return {@code true} if and only if this stack contains
     * no items; {@code false} otherwise.
     */
    public boolean empty() {
        var i = index - 1;
        return array[modulo(i, array.length)] == null;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    public Object[] getArray() {
        return array;
    }
}
