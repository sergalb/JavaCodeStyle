# Java file code styler
Take java code file (with restrictions) and reformat it by base java code style
#### maintained
* functions
* arrays
* class without generics
* core constructions like base type, variable
* comments

 
#### пример:
вход
  
 ```$xslt
package ru.ifmo.rain.balahnin;
import java.util.ArrayList;
import java.util.List; import star.*;
public class Simple {
    int[] a = new int[1];


    ArrayInitialization[] array = {1    ,   "sdfadf"    ,
                 functionCall()};
    EmptyArray[] emptyArray = {
    };
    FieldWithoutInitialization f;


    InitializatedField b = 1 + 2;

    java.love.dot.ClassAndArrays[][][] c = new int[2][3];

    StringLiteral str = "first string" + "secondString";

    public int functionWithoutParams() {
        int a = 2;
        a = 3;
        c = a[1];
    }

    FieldBetweenMethods f;

    void functionWithParams(int a, jaba.MyClass b) {
            int a = new int[1];
            while (a != b && c + d < 10) {
            if (a +




            b-c*d/5 != "test") {
                    doSomething();
                Var array = new ar[1][2];
                            } else {
                    doElse();
            }
            }

        }
}

```

выход
```
package ru.ifmo.rain.balahnin;

import java.util.ArrayList;
import java.util.List;
import star.*;

public class Simple {
    int[] a = new int[1]; 
    ArrayInitialization[] array = {1, "sdfadf", functionCall()}; 
    EmptyArray[] emptyArray = {}; 
    FieldWithoutInitialization f; 
    InitializatedField b = 1 + 2; 
    java.love.dot.ClassAndArrays[][][] c = new int[2][3]; 
    StringLiteral str = "first string" + "secondString"; 

    public int functionWithoutParams() {
        int a = 2; 
        a = 3;
        c = a[1];
    }
    FieldBetweenMethods f; 

    void functionWithParams(int a, jaba.MyClass b) {
        int a = new int[1]; 
        while (a != b && c + d < 10) {
            if (a + b - c * d / 5 != "test") {
                doSomething();
                Var array = new ar[1][2]; 
            } else {
                doElse();
            }
        }
    }
}
```
