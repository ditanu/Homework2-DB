package com.ditaalexandru.tema2;

import java.util.concurrent.Semaphore;

class Sens extends Thread {
    Semaphore sem;
    String nume;
    int nr;
    boolean go;

    public Sens(Semaphore s, String n, int nr) {
        super(n);
        this.nr = nr;
        this.sem = s;
        this.nume = n;
        go = false;
    }

    public void setGo(boolean go) {
        this.go = go;
    }

    public int getNr() {
        return this.nr;
    }

    @Override
    public void run() {
        while (this.nr > 0) {
            try {
                sem.acquire();
                while (this.nr > 0 && this.go) {
                    System.out.println("s-a trecut pe " + nume);
                    nr--;
                    Thread.sleep(2000); // timp sa treaca o masina
                }
                if (this.nr == 0) {
                    System.out.println(nume + " gol");
                }
                sem.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Intersectie {
    Sens[] sensuri;
    Semaphore NS;
    Semaphore WE;

    public Intersectie() {
        sensuri = new Sens[4];
        NS = new Semaphore(2);
        WE = new Semaphore(2);
        sensuri[0] = new Sens(NS, "Nord", 17);
        sensuri[1] = new Sens(NS, "Sud", 6);
        sensuri[2] = new Sens(WE, "Est", 15);
        sensuri[3] = new Sens(WE, "Vest", 7);
    }

    public int getMasini() {
        int nrMasini = 0;
        for (Sens s : sensuri) {
            nrMasini += s.nr;
        }
        return nrMasini;
    }
}

public class Main {

    public static void main(String[] args) {
        Intersectie ins = new Intersectie();
        int rand = 1;
//		for (int i = 0; i < ins.sensuri.length; i++) {
//			ins.sensuri[i].start();
//		}
        ins.sensuri[0].start();
        ins.sensuri[1].start();
        ins.sensuri[2].start();
        ins.sensuri[3].start();
        while (ins.getMasini() > 0) {
            try {
                if (rand == 1) {
                    ins.sensuri[0].go = true;
                    ins.sensuri[1].go = true;
                    ins.sensuri[2].go = false;
                    ins.sensuri[3].go = false;
                } else {
                    ins.sensuri[0].go = false;
                    ins.sensuri[1].go = false;
                    ins.sensuri[2].go = true;
                    ins.sensuri[3].go = true;
                }
                Thread.sleep(6000); // cat dureaza un verde pe directie
                rand *= -1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < ins.sensuri.length; i++) {
            try {
                ins.sensuri[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("S a golit intersectia");
    }
}