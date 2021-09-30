package android.malakhov.appmarket.Data.Comments;

import android.malakhov.appmarket.Data.Products.Product;
import android.malakhov.appmarket.ICallback;
import android.widget.ImageView;
import java.util.List;

public interface ICommentsRepository {
    void loadCommentToDB(Comment comment, Product product, List<ImageView> list);
    void deleteComment(Comment comment);
    void getUserComments(ICallback callback);
    void getProductComments(Product product, ICallback callback);
}
