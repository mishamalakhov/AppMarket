package android.malakhov.appmarket.Activities;

import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.R;
import android.malakhov.appmarket.Data.Users.User;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class AdminsListActivity extends AppCompatActivity {

    private RecyclerView recycler;
    public static List<User> list = new ArrayList<>();
    private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_admins);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        recycler.setAdapter(adapter);
        new ItemTouchHelper(adapter.itemTouchHelperCallBAck).attachToRecyclerView(recycler);
    }


    private class Holder extends RecyclerView.ViewHolder{

        private TextView name, mail, id;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            mail = itemView.findViewById(R.id.mail);
            id = itemView.findViewById(R.id.id);
        }

        public void onBind(User user){
            name.setText(user.getSecondName() + " " + user.getName()
            + " " + user.getThirdName());
            mail.setText(user.getEmail());
            id.setText("Id " + user.getId());
        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder>{

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.admin_layout, parent, false);
            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.onBind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBAck = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                UserRepository.get().deleteAdmin(list.get(viewHolder.getAdapterPosition()).getId());
                list.remove(viewHolder.getAdapterPosition());
                if (list.size() == 0) finish();
                adapter.notifyDataSetChanged();
            }
        };
    }
}
