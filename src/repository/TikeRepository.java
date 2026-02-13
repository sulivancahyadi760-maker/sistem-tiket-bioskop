package repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Tiket;
import src.model.Movie;

public class TikeRepository {
    private List<Tiket> tikets = new ArrayList<>();
    
    public void addTiket(){
        Tiket t1 = new Tiket(1, null, null, "A-10", 45000);
        Tiket t2 = new Tiket(2, null, null, "B-12", 45000);
        Tiket t3 = new Tiket(3, null, null, "D-7", 45000);
        Tiket t4 = new Tiket(4, null, null, "F-18", 60000);
        Tiket t5 = new Tiket(5, null, null, "G-9", 60000);
        Tiket t6 = new Tiket(6, null, null, "B-6", 60000);
        Tiket t7 = new Tiket(7, null, null, "E-11", 60000);
        Tiket t8 = new Tiket(8, null, null, "D-16", 45000);
        Tiket t9 = new Tiket(9, null, null, "A-5", 45000);
        Tiket t10 = new Tiket(10, null, null, "C-3", 60000);
        tikets.addAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    public Tiket findTiket(String datatiket) {
        for (Tiket t : tikets ) {
            if (t.getJadwalFilm(). equals(datatiket)) {
                return t;
            }
        }
        return null;
    }

    public List<Tiket> getAllMovies(){
        return tikets;

    }
}
