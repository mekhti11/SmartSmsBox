package com.mekhti.smartsmsbox.BroadcastReceiver;

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
                    String formattedText = String.format(context.getResources().getString(R.string.sms_message), senderPhoneNum, message);


                    // Display the SMS message in a Toast
                    Toast.makeText(context, formattedText, Toast.LENGTH_LONG).show();



                    ListSMSs inst = ListSMSs.instance();
                    inst.updateList(senderPhoneNum,message);

                    new Sqlite_utils(context).addSMSs(senderPhoneNum,message);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}