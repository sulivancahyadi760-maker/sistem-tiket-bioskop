package model;

public class Studio {
    private String namaStudio;
    private int kapasitas;
    private String tipeStudio;

    // Konstruktor
    public Studio(String namaStudio, int kapasitas, String tipeStudio) {
        this.namaStudio = namaStudio;
        this.kapasitas = kapasitas;
        this.tipeStudio = tipeStudio;
    }

    // Getter Setter
    public String getNamaStudio() {
        return namaStudio;
    }

    public void setNamaStudio(String namaStudio) {
        this.namaStudio = namaStudio;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getTipeStudio() {
        return tipeStudio;
    }

    public void setTipeStudio(String tipeStudio) {
        this.tipeStudio = tipeStudio;
    }
}
