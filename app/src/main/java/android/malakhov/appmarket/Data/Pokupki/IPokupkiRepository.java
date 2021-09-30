package android.malakhov.appmarket.Data.Pokupki;

import android.malakhov.appmarket.ICallback;

public interface IPokupkiRepository {

    void addPokupka(Pokupka pokupka);
    void getPokupki(ICallback callback);
    void deletePokupka(Pokupka pokupka);
}
