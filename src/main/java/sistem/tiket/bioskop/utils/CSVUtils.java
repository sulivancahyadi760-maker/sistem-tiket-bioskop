package sistem.tiket.bioskop.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class CSVUtils {

    // function utils yang digunain untuk read csv digunakan package repository
    public static <T> List<T> read(String path, Function<String[], T> mapper) {
        List<T> result = new ArrayList<>();
        File file = new File(path);

        if (!file.exists())
            return result;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty())
                    continue;

                String data[] = line.split(",");
                T object = mapper.apply(data);
                if (object != null)
                    result.add(object);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File tidak ditemukan: " + e.getMessage());
        }
        return result;
    }

    public static <T> void write(String path, List<T> dataList, Function<T, String> formatter) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (T item : dataList) {
                pw.println(formatter.apply(item));
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan data ke CSV: " + e.getMessage());
        }
    }
}
