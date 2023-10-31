import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

//Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
//Фамилия Имя Отчество дата рождения номер телефона пол
// Форматы данных:
//фамилия, имя, отчество - строки
//дата рождения - строка формата dd.mm.yyyy
//номер телефона - целое беззнаковое число без форматирования
//пол - символ латиницей f или m.
//Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, вернуть код ошибки,
// обработать его и показать пользователю сообщение, что он ввел меньше и больше данных, чем требуется.
//Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. Если форматы данных
// не совпадают, нужно бросить исключение, соответствующее типу проблемы. Можно использовать встроенные типы java и создать свои.
// Исключение должно быть корректно обработано, пользователю выведено сообщение с информацией, что именно неверно.
// Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться
// полученные данные, вида
//<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
// Однофамильцы должны записаться в один и тот же файл, в отдельные строки. Не забудьте закрыть соединение с файлом.
//При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные:\n фамилия: ");
        String surname = scanner.nextLine();
        System.out.println("имя: ");
        String name = scanner.nextLine();
        System.out.println("отчество: ");
        String patronymic = scanner.nextLine();
        System.out.println("дата рождения в формате дд.мм.гггг: ");
        String dateOfBirth = scanner.nextLine();
        System.out.println("номер телефона (10 цифр): ");
        String phoneNumber = scanner.nextLine();
        System.out.println("пол (f - женщина or m - мужчина): ");
        String sex = scanner.nextLine();

        List<String> dates = new ArrayList<>();
        dates.add(surname);
        dates.add(name);
        dates.add(patronymic);
        dates.add(dateOfBirth);
        dates.add(phoneNumber);
        dates.add(sex);

        String[] namesOfLine = new String[] {"фамилия", "имя", "отчество", "дата рождения", "номер телефона", "пол"};

        int codeResult = normalize(dates, namesOfLine);
        switch (codeResult) {
            case -2 -> System.out.println("В номере телефона не хватает цифр");
            case -3 -> System.out.println("В номере телефона слишком много циферок");
            case -4 -> System.out.println("Некорректно введен пол");
            case -5 -> System.out.println("Не знаю такого пола, укажи другую букву");
            default -> {
                createNewFile(dates);
            }
        }

        try {
            isNumeric(dates, namesOfLine);
        }
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

        try {
            birthDayFormat(dateOfBirth);
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            checkPhoneNumber(phoneNumber);
        }
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
     }

    /**
     * Проверка всех строк на отсутствие символом и проверка ФИО на количество символов
     * @param dates список вводимых данных с консоли
     * @param strings список наименований строк
     */
     static void checkSize(List<String> dates, String[] strings){
         int i = 0;
         for (String s : dates) {
             if (s.isEmpty()) System.out.println("Пусто в строке \"" + strings[i] + "\"");
             i++;
         }

         int j = 0;
         for (String s : dates.subList(0, 3)) {
             if (s.length() < 2) {
                 System.out.println(strings[j] + " меньше двух символов");
                 j++;
             }
         }
     }


    /**
     * Вывод кода ошибки
     * @param dates список вводимых данных с консоли
     * @param strings список наименований строк
     * @return числовое значение
     */
    static int normalize(List<String> dates, String[] strings) {
        checkSize(dates, strings);
        if (dates.get(4).length() > 0 && dates.get(4).length() < 10) {
            return -2;
        }
        if (dates.get(4).length() > 10) {
            return -3;
        }
        if (dates.get(5).length() > 1) {
            return -4;
        }
        if (!dates.get(5).equals("f") & !dates.get(5).equals("m")) {
            return -5;
        }
        return dates.size();
    }


    /**
     * Проверка, что ФИО введено не цифрами
     * @param dates список вводимых данных с консоли
     * @param str список наименований строк
     * @return true or false
     * @throws NumberFormatException Неверный формат одного из поля ФИО
     */

    static void isNumeric(List<String> dates, String[] str)  {
        int i = 0;
        for (String s : dates.subList(0, 3)) {
            try {
                if (s.matches(".*\\d+.*")){
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {
                System.out.println("Некорректно введены данные, " + str[i] + " содержит число");
            }
            i++;
        }
    }



    /**
     * Проверка номера телефона на корректность ввода, что введен не буквами
     * @param phoneNumber номер телефона, введенный с консоли
     * @return true or false
     * @throws NumberFormatException Неверный формат номера телефона
     */
    static void checkPhoneNumber(String phoneNumber) throws NumberFormatException {
        if (!phoneNumber.isEmpty()) {
            try {
                Integer.parseInt(phoneNumber);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат номера телефона");
            }
        }
    }


    /**
     * Проверка даты рождения на корректность ввода согласно формату дд.мм.гггг
     * @param dateOfBirth дата рождения, введенная с консоли
     * @throws ParseException Неверный формат даты рождения
     */
    static void birthDayFormat(String dateOfBirth) throws ParseException {
        if (!dateOfBirth.isEmpty()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("d.M.y");
                Date date = format.parse(dateOfBirth);
            } catch (ParseException e) {
                throw new ParseException("Неверный формат даты рождения", e.getErrorOffset());
            }
        }
    }


    /**
     * Создание нового файла с названием равным фамилии после проверки, что все введенные данные корректны
     * @param dates список вводимых данных с консоли
     */

    static void createNewFile(List<String> dates) {
        String fileName = dates.get(0);
        try (FileWriter fileWriter = new FileWriter(Paths.get(fileName).toFile(), true)) {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            br.readLine();
            fileWriter.append("\n");
            for (var i : dates) {
                fileWriter.write("<" + i + ">");
                fileWriter.write(" ");
            }
        } catch (IOException e){
            System.err.println("Файл не создан, в введенных данных есть ошибки");
            e.printStackTrace();
        }
    }
}








