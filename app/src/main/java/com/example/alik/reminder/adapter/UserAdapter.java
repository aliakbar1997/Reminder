package com.example.alik.reminder.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.alik.reminder.R;
import com.example.alik.reminder.model.table_object.UserModel;
import com.example.alik.reminder.ui.MainActivity;
import com.example.alik.reminder.ui.ProfileInfo;
import com.example.alik.reminder.utility.AlarmReceiver;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.ALARM_SERVICE;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<UserModel> mData = Collections.emptyList();
    private UserEventHandler userEventHandler;
//    private AppPreferenceTools mAppPreferenceTools;

    public UserAdapter(Context context, UserEventHandler userEventHandler) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.userEventHandler = userEventHandler;
//        mAppPreferenceTools = new AppPreferenceTools(this.mContext);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView userImageProfile;
        private TextView userName;
        private ImageButton deleteUser;
        private ImageButton menu;

        public UserViewHolder(View itemView) {
            super(itemView);
            userImageProfile = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.name);
            deleteUser = itemView.findViewById(R.id.delete_btn);
            menu = itemView.findViewById(R.id.menu_btn);

            menu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.menu_btn) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mContext, v);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.user_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.edit_user:
                                    if (userEventHandler != null) {
                                        userEventHandler.editUser(mData.get(getAdapterPosition()).getId());
                                    }
                                    break;

                                //create alarm for each user
                                case R.id.set_alarm:

                                    if (userEventHandler != null) {
                                        userEventHandler.setJob(mData.get(getAdapterPosition()).getId(),
                                                mData.get(getAdapterPosition()).getFirstName(), mData.get(getAdapterPosition()).getLastName());
                                    }
                                    break;

                            }
                            return false;
                        }
                    });

                    popup.show();
                }
            }

        }

    }

    public void updateAdapterData(List<UserModel> data) {
        this.mData = data;
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.user_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {
        UserModel userModel =  mData.get(position);

        holder.userName.setText(userModel.getFirstName() + " " + userModel.getLastName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileInfo.class);
                intent.putExtra("user_id", mData.get(position).getId());
                mContext.startActivity(intent);
            }
        });

        //delete user
        holder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEventHandler != null) {
                    if (userEventHandler.deleteUser(mData.get(position).getId()) > 0) {
                        mData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeRemoved(position, mData.size());
                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {

    }

    public interface UserEventHandler{
        int deleteUser(int userId);
        void editUser(int userId);
        void setJob(int userId, String firstName, String lastName);
    }
}
