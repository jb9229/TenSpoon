package com.hoh.bowls;


import org.omg.CORBA.UNKNOWN;

/**
 * Created by jeong on 2016-03-03.
 */
public enum BowlType {
    Nature(1), Disabled(2), Children(3), Senior(4), Animal(5), Abroad(6), ETC(7);

    private int value;
    private BowlType(int value) {
        this.value = value;
    }

    public int toInt(){
        return value;
    }

    public static BowlType fromInt(int value){

        switch (value)
        {
            case 1: return Nature;
            case 2: return Disabled;
            case 3: return Children;
            case 4: return Senior;
            case 5: return Animal;
            case 6: return Abroad;
            case 7: return ETC;
            default: return null;
        }
    }
}
