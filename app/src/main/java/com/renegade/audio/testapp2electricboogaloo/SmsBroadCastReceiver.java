package com.renegade.audio.testapp2electricboogaloo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.Vector;

/**
 * Created by Aaron on 3/14/2015.
 */
public class SmsBroadCastReceiver extends BroadcastReceiver {

    public static final String SMS_PDU_BUNDLE="pdus";
    public int voteCount = 0;
    public int totalPeeps = 1;
    String[] str ={""};
    Vector<String> usedNums = new Vector(Arrays.asList(str));
    boolean numUsedBefore;
    Date time = new Date();
    Long timeStart =  time.getTime();

    @Override
    public void onReceive(Context context, Intent intent) {


        Long timeNow = time.getTime();
        if (timeNow - timeStart >= 30000){
            timeStart = timeNow;
            voteCount = 0;
            usedNums.clear();

        }
        numUsedBefore = false;
        Bundle intentExtra =  intent.getExtras();
        if(intentExtra!=null)
        {
            Object[] pdu= (Object[])intentExtra.get(SMS_PDU_BUNDLE);
            String smsReceivedString="";
            for(int i=0;i<pdu.length;++i)
            {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu[i]);
                String smsBody = smsMessage.getMessageBody().toString();
                String sender_number = smsMessage.getOriginatingAddress();
                smsReceivedString +="SMS received from "+sender_number+"\n";
                smsReceivedString+=smsBody+"\n";


                if (smsBody.equals("%skip")) {
                    for(String iNum : usedNums)
                    {
                        if (iNum.equals(sender_number)){
                            numUsedBefore = true;
                            break;
                        }
                    }

                    voteCount++;
                    Toast.makeText(context, "Vote to Skip received", Toast.LENGTH_LONG).show();
                    usedNums.add(sender_number);

                    if (voteCount > (totalPeeps / 2)){
                        voteCount = 0;
                        timeStart = time.getTime();
                        usedNums.clear();

                        Intent skipIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                        synchronized (this) {
                            skipIntent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT));
                            context.sendOrderedBroadcast(skipIntent, null);
                            skipIntent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT));
                            context.sendOrderedBroadcast(skipIntent, null);

                        }
                    }
                }
            }

            Toast.makeText(context, smsReceivedString, Toast.LENGTH_LONG).show();
            MainActivity inst = MainActivity.instance;
            inst.updatelist(smsReceivedString);
        }

    }


}
