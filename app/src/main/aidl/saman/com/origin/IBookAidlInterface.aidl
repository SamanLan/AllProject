// IBookAidlInterface.aidl
package saman.com.origin;

import saman.com.base.Book;
// Declare any non-default types here with import statements

interface IBookAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    Book getBook();

    void say();
}
