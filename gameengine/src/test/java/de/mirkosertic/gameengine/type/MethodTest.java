package de.mirkosertic.gameengine.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MethodTest {

    private Method method;

    @Before
    public void setup() {
        method = new Method("lala", String.class, new Class[] {String.class, Integer.class}) {
            @Override
            public Object invoke(Object aTarget, Object[] aArguments) {
                return null;
            }
        };
    }

    @Test
    public void testGetReturnType() throws Exception {
        assertEquals(String.class, method.getReturnType());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("lala", method.getName());
    }

    @Test
    public void testGetArgument() throws Exception {
        Class[] theArguments = method.getArgument();
        assertEquals(2, theArguments.length);
        assertEquals(String.class, theArguments[0]);
        assertEquals(Integer.class, theArguments[1]);
    }
}