package android.malakhov.appmarket.Data.Users;

import android.content.res.Resources;
import android.malakhov.appmarket.ICallback;

import com.google.firebase.database.DatabaseReference;

interface IUserRepository {

    DatabaseReference getCurrUserRef();
    void getCurrentUser(ICallback callback);
    void getAdminsList(ICallback callback);
    void deleteAdmin(String id);
    void giveAdmin(String id);
    void exit();
    void addCardToDB(String number, String date, String CVC);
    void getUserRole(Resources resources,ICallback callback);
    void getUserPokupki(ICallback callback);
    void getCardNumber(ICallback callback);
    void getCardCVC(ICallback callback);
    void getCardDate(ICallback callback);
    void deleteCard();
    void registerUser(String password, User user, ICallback callback);
    void addUserToDB(User user);
    void signIn(String email, String password, ICallback callback);
}
