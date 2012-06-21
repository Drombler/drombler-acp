/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util.context;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

/**
 *
 * @author puce
 */
public class ContextTests {

    private ContextTests() {
    }

    public static void assertEqualsMyCustomFoo(Context context, MyCustomFoo myCustomFoo) {
        assertEquals(myCustomFoo, context.find(Foo.class));
        assertEquals(myCustomFoo, context.find(MyCustomFoo.class));
        assertEquals(myCustomFoo, context.find(AbstractFoo.class));
        assertEquals(myCustomFoo, context.find(Object.class));

        assertNull(context.find(Date.class));
    }

   public static void assertEqualsMyCustomFooList(Context context, List<MyCustomFoo> myCustomFooList) {
        assertEquals(myCustomFooList, context.findAll(Foo.class));
        assertEquals(myCustomFooList, context.findAll(MyCustomFoo.class));
        assertEquals(myCustomFooList, context.findAll(AbstractFoo.class));
        assertEquals(myCustomFooList, context.findAll(Object.class));

        assertEquals(Collections.emptyList(), context.findAll(Date.class));
    }
}
