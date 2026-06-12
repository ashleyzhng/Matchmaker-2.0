/**
	File Name: Teen.java
	Name: Joyce Lin
	Class: ICS4U1
	Date: July 12, 2026
	Description: This program represents a Teen User object
*/

import java.time.LocalDate;

public class Teen extends User {
    public static final int MIN_AGE = 13;

    //contructor, acces, muts
    public Teen(String u, String p, String e, boolean ev, String n,  LocalDate b, String pc, String c, String bi, char g, char pg, Tag t){
        super(u, p, e, ev, n, b, pc, c, bi, g, pg, t);

    }
    
    public String toString(){
        return super.toString();
    }
}
/*
    add to menu (flushing method)
    public void flush(){
        for(int i = 0; i < 50; i++){
            System.out.println();
        }
    }
*/