package com.fit2081.fit2081assignment1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

// This class is from previous assignment, did not make any changes and not modifying it for this assignment.
public class SMSReceiver extends BroadcastReceiver {

    // used as a channel to broadcast the message
    // any application aware of this channel can listen to the broadcasts
    public static final String SMS_FILTER = "SMS_FILTER";

    // within the broadcast, we would like to send information
    // and this will be the key to identify that information, in this case the SMS message
    public static final String SMS_MSG_KEY = "SMS_MSG_KEYz";

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages= Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getDisplayMessageBody();
//            Toast.makeText(context,"Message: " + message, Toast.LENGTH_LONG).show();
            Intent msgIntent = new Intent();
            msgIntent.setAction(SMS_FILTER);
            msgIntent.putExtra(SMS_MSG_KEY, message);
            context.sendBroadcast(msgIntent);
        }
    }
}

// Alternative way
//            if (!message.startsWith("category:") && !message.startsWith("event:")) {
//                message = "Invalid";
//                Toast.makeText(context,"Message: " + message, Toast.LENGTH_LONG).show();
//
//            } else {
//            if (message.startsWith("category:")) {
//                msgIntent.putExtra(SMS_MSG_KEY_CATEGORY, message);
//            }
//            else if (message.startsWith("event:")) {
//                msgIntent.putExtra(SMS_MSG_KEY_EVENT, message);
//            }