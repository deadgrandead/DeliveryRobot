import java.util.*;
import java.util.concurrent.*;

public class RobotPaths {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            service.execute(() -> {
                String path = generateRoute("RLRFR", 100);
                int freq = countR(path);
                synchronized (sizeToFreq) {
                    sizeToFreq.put(freq, sizeToFreq.getOrDefault(freq, 0) + 1);
                }
            });
        }
        service.shutdown();
        if (!service.awaitTermination(1, TimeUnit.MINUTES)) {
            System.out.println("Не все задачи выполнены за отведенное время");
        }
        analyzeFrequencies();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countR(String path) {
        int count = 0;
        for (char c : path.toCharArray()) {
            if (c == 'R') count++;
        }
        return count;
    }

    public static void analyzeFrequencies() {
        Map.Entry<Integer, Integer> maxEntry = null;
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        assert maxEntry != null;
        System.out.println("Самое частое количество повторений " + maxEntry.getKey() + " (встретилось " + maxEntry.getValue() + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (!entry.equals(maxEntry)) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }

}
