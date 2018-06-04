package com.mekhti.smartsmsbox.BroadcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.mekhti.smartsmsbox.Entity.Contact;
import com.mekhti.smartsmsbox.Entity.SmsTypes;
import com.mekhti.smartsmsbox.ListSMSs;
import com.mekhti.smartsmsbox.MainActivity;
import com.mekhti.smartsmsbox.R;
import com.mekhti.smartsmsbox.utils.SmsUtils;
import com.mekhti.smartsmsbox.utils.Sqlite_utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * A broadcast receiver who listens for incoming SMS
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";
    private static final String CHANNEL_ID ="3000" ;
    final SmsManager mManager = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    // This will create an SmsMessage object from the received pdu
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    // Get sms info
                    String senderPhoneNum = sms.getDisplayOriginatingAddress();
                    String message = sms.getDisplayMessageBody();


                    // Display the SMS message in a Toast
                    //Toast.makeText(context, senderPhoneNum+"\n"+message, Toast.LENGTH_LONG).show();

                    Sqlite_utils db = new Sqlite_utils(context);
                    db.open();
                    db.addSMSs(senderPhoneNum,message);
                    String category = db.getSmsType(senderPhoneNum,message);
//                    ListSMSs ins =new  ListSMSs();
//                    ins.onStart();
//                    ins.updateList(senderPhoneNum,message);

                    createDataNotification(context,senderPhoneNum,message,category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // notificcation calışıyor ELLEME
    public void createDataNotification(Context c,String sender,String mesaj,String category){


        String title = sender;
        String message = mesaj;
        long[] pattern = {500,500,500,500};//Titreşim ayarı
        Intent intent = new Intent(c, ListSMSs.class);
        intent.putExtra("category",category);
        intent.putExtra("sender",sender);
        intent.putExtra("message",mesaj);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ListSMSs.instance(),CHANNEL_ID)
                .setSmallIcon(R.drawable.chat_1)
                .setContentTitle(title) // Bildirim başlığı
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager notificationManager =
                (NotificationManager) ListSMSs.instance().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }


}