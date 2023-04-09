# Эмулятор работы банкомата

## Поддерживемые оаперации:
- Поместить деньги, 
- Cнять деньги,
- Показать состояние банкомата. 

Также будем поддерживать мультивалютность. Купюрами будем оперировать теми, которые поместим в банкомат.
Если для снятия требуемой суммы будет недостаточно банкнот, то сообщим юзеру об этом.

## Достижения:
1. Разобралась с паттерном Command.
2. Познакомилась с локализацией.
3. Поняла, как раскладывать задачу на подзадачи.

## Требования:
1. Перваая задача:
-	Класс CashMachine должен быть создан в отдельном файле и иметь метод public static void main (String[] args).
-	Енум Operation должен быть создан в отдельном файле и иметь значения: INFO, DEPOSIT, WITHDRAW, EXIT.
-	Класс ConsoleHelper должен быть создан в отдельном файле.
2. Вторая задача:
-	Классы InterruptOperationException и NotEnoughMoneyException должны быть созданы в отдельных файлах, и быть checked исключениями.
-	Класс ConsoleHelper должен содержать приватное статическое поле BufferedReader bis, которое должно быть сразу проинициализировано.
-	Класс ConsoleHelper должен содержать публичные статические методы: 
writeMessage(String message), который должен выводить в консоль переданный параметр и
readString(), который должен считывать с консоли строку и возвращать ее.
3. Третья задача:
-	Класс CurrencyManipulator должен быть создан в отдельном файле и содержать приватные поля: 

```java
String currencyCode; 
Map<Integer, Integer> denominations;
```
-	Класс CurrencyManipulator должен содержать геттер для поля currencyCode.
-	Класс CurrencyManipulator должен содержать конструктор с одним параметром, инициализирующий поле currencyCode.
-	Класс CurrencyManipulatorFactory должен быть создан в отдельном файле и иметь приватный дефолтный конструктор.
-	Класс CurrencyManipulatorFactory должен содержать приватное статическое поле:

```java
Map<String, CurrencyManipulator> map.
```
-	Класс CurrencyManipulatorFactory должен иметь статический метод getManipulatorByCurrencyCode(String currencyCode).
4. Четвертая задача:
-	Класс ConsoleHelper должен иметь статические методы String askCurrencyCode() и String[] getValidTwoDigits(String currencyCode).
-	Класс CurrencyManipulator должен иметь метод void addAmount(int denomination, int count).
-	Метод main класса CashMachine должен считывать с консоли код валюты, потом считывать номинал и количество банкнот, а потом добавлять их в манипулятор.
5. Пятая задача:
-   Класс CurrencyManipulator должен иметь метод int getTotalAmount().
-	Метод main класса CashMachine должен вызывать метод getTotalAmount() у объекта класса CurrencyManipulator.
6. Шестая задача:
-	Энум Operation должен иметь статический метод Operation getAllowableOperationByOrdinal(Integer i).
-	Класс ConsoleHelper должен иметь метод Operation askOperation().
7. Седьмая задача:
-	Интерфейс Command должен быть создан в отдельном файле и иметь метод void execute().
-	В отдельном файле должны быть созданы классы, реализующие интерфейс Command: DepositCommand, InfoCommand,WithdrawCommand, ExitCommand.
-	В отдельном файле должен быть создан класс CommandExecutor.
-	Класс CommandExecutor должен содержать public static final void метод execute(Operation operation).
-	Класс CommandExecutor должен содержать приватный конструктор.
8. Восьмая задача:
-	В классе CurrencyManipulatorFactory должен быть публичный статический метод getAllCurrencyManipulators() с возвращаемым значением Collection типа CurrencyManipulator.
-	В классе CurrencyManipulator должен быть метод boolean hasMoney().
-	В классе InfoCommand в методе execute() для каждого манипулятора выведи: "код валюты - общая сумма денег для выбранной валюты", если денег нет в банкомате выведи фразу, "No money available.".
-	В классе DepositCommand в методе execute() запроси код валюты, потом запроси номинал и количество банкнот, а потом добавь их в манипулятор. Если номинал и количество банкнот пользователь ввел не правильно(не числа) - повторять попытку по введению номинала и количества банкнот.
-	В методе main класса CashMachine запроси операцию у пользователя. Выполни операцию в CommandExecutor. Повторять пока пользователь не выбрал операцию EXIT.
9. Девятая задача:
-	В классе ExitCommand в методе execute() спроси у пользователя действительно ли хочет ли выйти - варианты <y,n>. Если пользователь подтвердит свои намерения, то попрощаться с ним, если нет, то не прощаться с ним, а просто выйти.
-	Реализуй логику с прерыванием операций (пробрасыванием исключения InterruptOperationException).
-	Метод main не должен бросать исключения.
10. Десятая задача:
-	Класс CurrencyManipulator должен содержать метод boolean isAmountAvailable(int expectedAmount), который будет возвращать true, если денег достаточно для выдачи.
-	Класс CurrencyManipulator должен содержать метод withdrawAmount(int expectedAmount), который должен возвращать карту c ключами и значениями типа Integer.
-	Метод execute класса WithdrawCommand должен реализовывать алгоритм для команды WithdrawCommand.
11. Одиннадцатая задача:
-	Энум Operation должен содержать операцию LOGIN с ординал = 0.
-	В методе getAllowableOperationByOrdinal(Integer i) запрети пользователю выбирать операцию LOGIN.
-	Метод execute класса LoginCommand должен реализовывать алгоритм для команды LOGIN.
-	Карта allKnownCommandsMap класса CommandExecutor должна содержать новую операцию.
-	Метод main должен вызывать операцию LOGIN.
12. Двенадцатая задача:
-   LoginCommand должен содержать приватное поле ResourceBundle validCreditCards.
-	Поле validCreditCards должно быть проинициализировано из файла verifiedCards.properties.
-	Используй проверку кредитной карточки и пина через verifiedCards.properties.
13. Тринадцатая задача:
-	DepositCommand, ExitCommand, InfoCommand должны содержать приватное поле ResourceBundle res.
-	Поле res класса DepositCommand должно быть проинициализировано из файла deposit_en.properties.
-	Поле res класса ExitCommand должно быть проинициализировано из файла exit_en.properties.
-	Поле res класса InfoCommand должно быть проинициализировано из файла info_en.properties.
-	В методе execute() классов DepositCommand, ExitCommand, InfoCommand используй подходящие ресурсы.
14. Четырнадцатая задача:
-	LoginCommand, WithdrawCommand, ConsoleHelper должны содержать приватное поле ResourceBundle res.
-	Поле res класса LoginCommand должно быть проинициализировано из файла login_en.properties.
-	Поле res класса WithdrawCommand должно быть проинициализировано из файла withdraw_en.properties.
-	Поле res класса ConsoleHelper должно быть проинициализировано из файла common_en.properties.
-	В методе execute() классов LoginCommand, WithdrawCommand используй вызовы соответствующих новых ресурсов.
15. Пятнадцатая задача:
-	Класс CashMachine должен содержать public static final поле RESOURCE_PATH типа String.
-	Класс ConsoleHelper должен содержать public static void метод printExitMessage().

## Можно:
-   Исправить выводимые тексты.
-   Добавить ресурсы для нескольких локалей. Например, еще и для русской.
-   Добавить валидацию вводимых номиналов.
