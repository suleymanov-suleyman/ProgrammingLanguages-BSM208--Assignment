import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class Musteri {
    private final int musteriNumarasi;
    private final int islemSuresi;

    public Musteri(int musteriNumarasi, int islemSuresi) {
        this.musteriNumarasi = musteriNumarasi;
        this.islemSuresi = islemSuresi;
    }

    public int getMusteriNumarasi() {
        return musteriNumarasi;
    }

    public int getIslemSuresi() {
        return islemSuresi;
    }
}
class Gise implements Runnable {
    private final int giseNumarasi;
    private final Queue<Musteri> musteriKuyrugu;

    public Gise(int giseNumarasi, Queue<Musteri> musteriKuyrugu) {
        this.giseNumarasi = giseNumarasi;
        this.musteriKuyrugu = musteriKuyrugu;
    }

    @Override
    public void run() {
        System.out.println("Gişe #" + giseNumarasi + " çalışmaya başladı.");


        while (true) {
            Musteri siradakiMusteri = null;
            synchronized (musteriKuyrugu) {
                if (!musteriKuyrugu.isEmpty()) {
                    siradakiMusteri = musteriKuyrugu.poll();
                }
            }
            if (siradakiMusteri != null) {
                System.out.println("--> Gişe #" + giseNumarasi + ", Müşteri #" + siradakiMusteri.getMusteriNumarasi() + "'nin işlemini başlattı. (Süre: " + siradakiMusteri.getIslemSuresi() + "ms)");
                try {
                    Thread.sleep(siradakiMusteri.getIslemSuresi());
                } catch (InterruptedException e) {
                    System.err.println("Gişe #" + giseNumarasi + " çalışması kesintiye uğradı.");
                    Thread.currentThread().interrupt();
                }
                System.out.println("<-- Gişe #" + giseNumarasi + ", Müşteri #" + siradakiMusteri.getMusteriNumarasi() + "'nin işlemini TAMAMLADI.");
            } else {
                System.out.println("--- Gişe #" + giseNumarasi + " için müşteri kalmadı, durduruluyor. ---");
                break;
            }
        }
    }
}

public class BankaSistemi {

    public static void main(String[] args) {
        final int GISE_SAYISI = 3;
        final int MUSTERI_SAYISI = 5;
        final int MIN_ISLEM_SURESI = 1000;
        final int MAX_ISLEM_SURESI = 5000;

        Queue<Musteri> musteriKuyrugu = new LinkedList<>();

        System.out.println("===== Müşteriler Bankaya Geliyor ve Sıraya Giriyor =====");
        for (int i = 1; i <= MUSTERI_SAYISI; i++) {
            int islemSuresi = ve join.current().nextInt(MIN_ISLEM_SURESI, MAX_ISLEM_SURESI + 1);
            Musteri yeniMusteri = new Musteri(i, islemSuresi);
            musteriKuyrugu.add(yeniMusteri);
            System.out.println("Müşteri #" + i + " sıraya girdi.");
        }
        System.out.println("========================================================\n");

        System.out.println("===== Gişeler Çalışmaya Başlıyor  (" + GISE_SAYISI + " Gişe Aktif) =====");
        List<Thread> giseThreadleri = new ArrayList<>();
        for (int i = 1; i <= GISE_SAYISI; i++) {
            Gise gise = new Gise(i, musteriKuyrugu);
            Thread giseThread = new Thread(gise);
            giseThreadleri.add(giseThread);
            giseThread.start();
        }
        for (Thread thread : giseThreadleri) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n===== Tüm Müşteri İşlemleri Tamamlandı. Banka Kapanıyor. =====");
    }
}
