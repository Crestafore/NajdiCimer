package mk.mlabs.najdicimer.helper;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.model.Message;

/**
 * Created by Darko on 5/12/2016.
 */
public class MessagesAdapter extends BaseAdapter {
    private List<Message> messages;
    private Activity ctx;
    private LayoutInflater inflater;

    public MessagesAdapter(Activity ctx, List<Message> messages){
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
        if (messages != null)
            this.messages = messages;
        else this.messages = new ArrayList<Message>();
    }

    public void add(Message item){
        messages.add(item);
        notifyDataSetChanged();
    }

    public void remove(int position){
        messages.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder;

        if (convertView == null){
            holder = new Holder();
            convertView = holder.layout = (RelativeLayout) inflater.inflate(R.layout.view_messages_item, null);
            holder.image = (ImageView) holder.layout.findViewById(R.id.vmi_image);
            holder.name = (TextView) holder.layout.findViewById(R.id.vmi_user_name);
            holder.timestamp = (TextView) holder.layout.findViewById(R.id.vmi_timestamp);

            convertView.setTag(holder);
        }

        holder = (Holder) convertView.getTag();

        Message message = (Message) getItem(position);
        //holder.image = message.getUserFrom().getImageURL();
        Picasso.with(ctx).load(Constants.SERVER_IP_ADDRESS + message.getUserTo().getImageURL()).into(holder.image);
        holder.name.setText(message.getUserTo().getUsername());
        holder.timestamp.setText(message.getSentOn());

        return convertView;
    }

    static class Holder{
        RelativeLayout layout;
        ImageView image;
        TextView name;
        TextView timestamp;
    }
}
