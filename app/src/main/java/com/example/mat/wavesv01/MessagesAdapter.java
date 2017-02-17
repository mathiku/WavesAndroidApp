package com.example.mat.wavesv01;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.*;

/**
 * Created by Mat on 16-02-2017.
 */

public class MessagesAdapter extends ArrayAdapter<Messages>{

    public MessagesAdapter (Context context, ArrayList<Messages> messages){
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Messages mMessage = getItem(position);
        TextView messageTextView = (TextView)listItemView.findViewById(R.id.messageTextView);
        TextView dateTextView = (TextView)listItemView.findViewById(R.id.daTextView);
        TextView deviceTextView = (TextView)listItemView.findViewById(R.id.deviceTextView);

        Date dateObject = new Date(mMessage.getTime());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM-DD-yy");
        String timeOfMessage = dateFormatter.format(dateObject);

        String hexMessageFromData = mMessage.getMessage();


        //messageTextView.setText(hexToAscii(hexMessageFromData));
        messageTextView.setText(hexMessageFromData);
        //messageTextView.setText(Integer.parseInt(hexMessageFromData, 16));

        dateTextView.setText(timeOfMessage);
        deviceTextView.setText(mMessage.getDevice());

        return listItemView;
    }

    private static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();
    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
}
