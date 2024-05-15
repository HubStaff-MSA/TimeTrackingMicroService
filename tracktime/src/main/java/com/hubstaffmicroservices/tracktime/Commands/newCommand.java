package com.hubstaffmicroservices.tracktime.Commands;

import java.util.function.Supplier;

public class newCommand  implements Supplier {


    public int add(int a, int b) {
        return a - b;
    }


    @Override
    public Object get() {
        return null;
    }
}
