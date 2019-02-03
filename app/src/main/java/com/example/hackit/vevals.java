package com.example.hackit;

public class vevals{
    private static vevals instance=null;
    private  int v1e1=0;
    private  int v1e2=0;


    public static vevals getInstance(){
        if(instance == null){
            instance = new vevals();
        }
        return instance;
    }


    public int getV1e1() {
        return v1e1;
    }

    public void setV1e1(int v1e1) {
        this.v1e1 = v1e1;
    }

    public int getV1e2() {
        return v1e2;
    }

    public void setV1e2(int v1e2) {
        this.v1e2 = v1e2;
    }
}
