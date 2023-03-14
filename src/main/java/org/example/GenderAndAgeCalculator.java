package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class GenderAndAgeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите фамилию, имя, отчество, разделённые пробелом: ");
        String[] nameParts = scanner.nextLine().split(" ");
        if (nameParts.length < 3) {
            throw new IllegalArgumentException(
                    "Вы должны ввести все три части вашего полного имени. Было введено: %d".formatted(nameParts.length));
        }

        System.out.print("Введите дату рождения в формате дд.мм.гггг: ");
        String birthDateStr = scanner.nextLine();

        String gender = getGenderByFullName(nameParts[2]);

        int age = getAgeByBirthDateString(birthDateStr);

        String initials = nameParts[0] + " " + nameParts[1].charAt(0) + "." + nameParts[2].charAt(0) + ".";
        String ageSuffix;
        if (age % 10 == 1 && age % 100 != 11) {
            ageSuffix = "год";
        } else if (age % 10 >= 2 && age % 10 <= 4 && (age % 100 < 10 || age % 100 >= 20)) {
            ageSuffix = "года";
        } else {
            ageSuffix = "лет";
        }

        System.out.printf("%s  %s  %d %s%n", initials, gender, age, ageSuffix);
    }

    private static int getAgeByBirthDateString(String birthDateStr) {
        try {
            var birthDate = new SimpleDateFormat("dd.MM.yyyy").parse(birthDateStr);
            var calendar = Calendar.getInstance();
            calendar.setTime(birthDate);
            int birthYear = calendar.get(Calendar.YEAR);
            int birthMonth = calendar.get(Calendar.MONTH) + 1;
            int birthDay = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.setTime(new Date());
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            int age = currentYear - birthYear;
            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Формат даты рождения введён неверно, подробности:" + e.getLocalizedMessage());
        }
    }

    public static String getGenderByFullName(String middleName) {
        if (middleName.length() < 3) {
            throw new IllegalArgumentException("Фамилия длины меньше трёх символов? Что?");
        }
        String lastThreeLetters = middleName.substring(middleName.length() - 3);
        return switch (lastThreeLetters) {
            case "вич", "ьич", "тич", "глы", "тлы" -> "М";
            case "вна", "чна", "шна", "ызы", "рны", "гин" -> "Ж";
            default -> "не удалось определить пол";
        };
    }
}
