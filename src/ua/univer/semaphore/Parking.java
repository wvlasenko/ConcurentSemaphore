package ua.univer.semaphore;

import java.io.PrintStream;
import java.util.concurrent.Semaphore;

public class Parking {
    private static final boolean[] ParkingPlaces = new boolean[5];
    private static final Semaphore semaphore = new Semaphore(5, true);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 7; i++) {
            new Thread(new Car(i)).start();
            Thread.sleep(400);

        }

    }

    public static class Car implements Runnable {
        private int carNumber;

        public Car(int carNumber) {
            this.carNumber = carNumber;
        }

        @Override
        public void run() {
            System.out.printf("Автомобиль №%d подъехал к парковке.\n", carNumber);
            try {
                semaphore.acquire();
                int parkingNumber = -1;
                synchronized (ParkingPlaces) {
                    for (int i = 0; i < 5; i++)
                        if (!ParkingPlaces[i]) {
                            ParkingPlaces[i] = true;
                            parkingNumber = i;
                            System.out.printf("Автомобиль  №%d припарковался на месте %d.\n", carNumber, i);
                            break;

                        }
                }
                Thread.sleep(5000);
                synchronized (ParkingPlaces) {
                    ParkingPlaces[parkingNumber] = false;
                }
                semaphore.release();
                System.out.printf(" Автомобиль №%d покинул парковку.\n ", carNumber);

            } catch (InterruptedException e) {

            }
        }
    }
}
