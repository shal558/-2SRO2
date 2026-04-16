                                                                        СРО №2: Симуляция очереди в банке

Цель работы: Освоить структуру данных «Очередь» и принцип FIFO (First-In, First-Out) на примере программной модели банковского терминала.

1. Проектирование системы
Для реализации задачи был выбран объектно-ориентированный подход на языке Java. Программа включает следующие ключевые компоненты:

Интерфейс Queue и реализация LinkedList: Выбраны для обеспечения корректной работы принципа FIFO, где добавление происходит в конец, а извлечение — из начала структуры.

Перечисление Service: Позволяет строго классифицировать виды банковских услуг (Касса, Кредитование, Карты, VIP).

Класс Ticket: Служит контейнером для данных клиента, объединяя тип услуги и уникальный идентификационный номер талона.

Стохастическая модель: В код добавлена вероятность временной занятости окна (30%) для имитации реалистичных условий работы персонала.

2. Реализация программы
Ниже представлен полный исходный код системы:

Java

import java.util.*;

public class MegaBankSystem {

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
    static int lastServedWindow = -1;
    static final int TOTAL_WINDOWS = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- ТЕРМИНАЛ ОБСЛУЖИВАНИЯ ---");
            System.out.println("1. Касса | 2. Кредит | 3. Карты | 4. VIP");
            System.out.println("5. Вызвать следующего | 6. Статистика | 0. Выход");
            System.out.print("Выбор: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> takeTicket(Service.CASH);
                case "2" -> takeTicket(Service.LOAN);
                case "3" -> takeTicket(Service.CARD);
                case "4" -> takeTicket(Service.VIP);
                case "5" -> callNextCustomer();
                case "6" -> showFullStats();
                case "0" -> System.exit(0);
                default -> System.out.println("Ошибка выбора.");
            }
        }
    }

    private static void takeTicket(Service s) {
        int id = random.nextInt(899) + 100;
        Ticket t = new Ticket(s, id);
        commonQueue.add(t);
        System.out.println("Талон выдан: " + t.number);
    }

    private static void callNextCustomer() {
        if (commonQueue.isEmpty()) {
            System.out.println("Очередь пуста.");
            return;
        }

        lastServedWindow = (lastServedWindow + 1) % TOTAL_WINDOWS;
        int currentWindow = lastServedWindow + 1;

        if (random.nextInt(100) < 30) {
            System.out.println("Окно №" + currentWindow + " временно занято.");
            return;
        }

        Ticket t = commonQueue.poll();
        System.out.println("Клиент " + t.number + " приглашен к окну №" + currentWindow);
    }

    private static void showFullStats() {
        System.out.println("\n--- ОТЧЕТ СИСТЕМЫ ---");
        System.out.println("Всего в очереди: " + commonQueue.size());
        Map<Service, Integer> counts = new EnumMap<>(Service.class);
        for (Ticket t : commonQueue) {
            counts.put(t.service, counts.getOrDefault(t.service, 0) + 1);
        }
        counts.forEach((s, c) -> System.out.println(s.name + ": " + c));
    }
}
3. Описание логики работы
Система функционирует в интерактивном режиме. При добавлении клиента используется метод add(), помещающий объект в конец связного списка. При вызове сотрудником следующего клиента используется метод poll(), гарантирующий извлечение самого раннего из добавленных элементов. Таким образом, соблюдается строгая последовательность обслуживания в соответствии с требованиями задачи.

4. Контрольные вопросы
1. Чем очередь отличается от стека?
Очередь работает по принципу FIFO (первый вошел — первый вышел), где элементы добавляются в конец, а извлекаются из начала. Стек работает по принципу LIFO (последний вошел — первый вышел), где и добавление, и удаление происходят с одного конца («вершины»).

2. Что означает FIFO?
FIFO (First-In, First-Out) — это дисциплина обслуживания, при которой объекты обрабатываются строго в порядке их поступления в систему.

3. Где используются очереди в реальной жизни?
Очереди применяются в системном администрировании (очереди на печать, планировщики задач ОС), в сетевых протоколах (передача пакетов), в разработке программного обеспечения (обработчики событий) и в любых физических системах массового обслуживания (банки, магазины, колл-центры).

<img width="548" height="164" alt="Screenshot 2026-04-16 134003" src="https://github.com/user-attachments/assets/c21a4c8c-c59f-47cf-9d28-e7398f07fa41" />
<img width="468" height="224" alt="Screenshot 2026-04-16 134012" src="https://github.com/user-attachments/assets/45873f80-06b8-440f-8328-d5e367b63e1a" />
<img width="357" height="228" alt="Screenshot 2026-04-16 134018" src="https://github.com/user-attachments/assets/de256d96-0030-495a-9548-f195cf07234b" />
<img width="490" height="209" alt="Screenshot 2026-04-16 134028" src="https://github.com/user-attachments/assets/14b4de4c-4fd1-4331-b7b3-a18260ece34b" />
<img width="429" height="234" alt="Screenshot 2026-04-16 134037" src="https://github.com/user-attachments/assets/f45be9d7-4266-4866-9e8f-f378edbe6e7c" />
<img width="540" height="543" alt="Screenshot 2026-04-16 134049" src="https://github.com/user-attachments/assets/20cb699e-b1bf-45e6-a30b-769433a87ad9" />
<img width="540" height="626" alt="Screenshot 2026-04-16 134103" src="https://github.com/user-attachments/assets/12a343a8-d1b5-42e3-a360-4e8a48aa75a1" />

