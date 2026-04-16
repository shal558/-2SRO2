import java.util.*;

public class MegaBankSystem {

    // Перечисление услуг для строгого порядка
    enum Service {
        CASH("Касса", "К"),
        LOAN("Кредитование", "КР"),
        CARD("Выпуск карт", "КРТ"),
        VIP("VIP-обслуживание", "VIP");

        final String name;
        final String prefix;

        Service(String name, String prefix) {
            this.name = name;
            this.prefix = prefix;
        }
    }

    static class Ticket {
        String number;
        Service service;

        Ticket(Service service, int id) {
            this.service = service;
            this.number = service.prefix + "-" + id;
        }
    }

    static Queue<Ticket> commonQueue = new LinkedList<>();
    static Random random = new Random();

    // Индекс текущего окна для поочередного вызова
    static int lastServedWindow = -1;
    static final int TOTAL_WINDOWS = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> takeTicket(Service.CASH);
                case "2" -> takeTicket(Service.LOAN);
                case "3" -> takeTicket(Service.CARD);
                case "4" -> takeTicket(Service.VIP);
                case "5" -> callNextCustomer();
                case "6" -> showFullStats();
                case "0" -> System.exit(0);
                default -> System.out.println("❌ Ошибка выбора.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- 📱 ТЕРМИНАЛ ОБСЛУЖИВАНИЯ ---");
        System.out.println("1. [К] Касса          2. [КР] Кредит");
        System.out.println("3. [КРТ] Карты        4. [VIP] Премиум");
        System.out.println("5. 📢 ВЫЗВАТЬ СЛЕДУЮЩЕГО (Пульт сотрудника)");
        System.out.println("6. 📊 Общая статистика");
        System.out.print("Выберите пункт: ");
    }

    private static void takeTicket(Service s) {
        int id = random.nextInt(899) + 100;
        Ticket t = new Ticket(s, id);
        commonQueue.add(t);
        System.out.println("\n✅ ТАЛОН ВЫДАН: " + t.number);
        System.out.println("Услуга: " + s.name);
    }

    private static void callNextCustomer() {
        if (commonQueue.isEmpty()) {
            System.out.println("\n📭 Очередь пуста. Сотрудники могут отдохнуть.");
            return;
        }

        // Логика выбора окна по очереди (round-robin)
        lastServedWindow = (lastServedWindow + 1) % TOTAL_WINDOWS;
        int currentWindow = lastServedWindow + 1;

        // Рандомная проверка: свободно ли окно именно сейчас?
        // Шанс 30%, что окно занято "внеплановым" делом
        if (random.nextInt(100) < 30) {
            System.out.println("\n⚠️ Окно №" + currentWindow + " занято обработкой документов. Пропускаем ход.");
            return;
        }

        Ticket t = commonQueue.poll();
        System.out.println("\n🔔 ДЗЫНЬ!");
        System.out.println(">>> Талон " + t.number + " приглашается к ОКНУ №" + currentWindow);
        System.out.println(">>> Цель визита: " + t.service.name);
        System.out.println("✅ Клиент обслужен. Окно №" + currentWindow + " освободилось.");
    }

    private static void showFullStats() {
        System.out.println("\n======= 📊 ОТЧЕТ СИСТЕМЫ =======");
        System.out.println("Всего людей в очереди: " + commonQueue.size());

        // Считаем по категориям
        Map<Service, Integer> counts = new EnumMap<>(Service.class);
        for (Ticket t : commonQueue) {
            counts.put(t.service, counts.getOrDefault(t.service, 0) + 1);
        }

        counts.forEach((service, count) ->
                System.out.println(" - " + service.name + " (" + service.prefix + "): " + count + " чел."));

        System.out.println("Последнее активное окно: " + (lastServedWindow + 1));
        System.out.println("================================");
    }
}
