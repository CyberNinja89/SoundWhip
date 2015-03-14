package com.renegade.audio.testapp2electricboogaloo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;


/**
* Created by Aaron on 3/14/2015.
*/
public class SmsBroadCastReceiver extends BroadcastReceiver {

    public static final String SMS_PDU_BUNDLE="pdus";
    public static int voteCount = 0;
    public int totalPeeps = 5;
    String[] str ={""};
    Vector<String> usedNums = new Vector(Arrays.asList(str));
    boolean numUsedBefore;
    Date time = new Date();
    Long timeStart =  time.getTime();

    public void addVote(){
        voteCount++;
    }
    public void reset(){
        voteCount = 0;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Debug","Started from the bottom \n");

        Long timeNow = time.getTime();
        if (timeNow - timeStart >= 30000){
            String lol6 = Long.toString(timeNow-timeStart);
            Log.d("DEBUGGING:", "The timer is "+lol6);
            timeStart = timeNow;
           // voteCount = 0;
            reset();
            usedNums.clear();

        }
        numUsedBefore = false;
        Bundle intentExtra =  intent.getExtras();
        if(intentExtra!=null)
        {

            Object[] pdu= (Object[])intentExtra.get(SMS_PDU_BUNDLE);


            Log.d("DEBUGGING:", "the pdu length is "+Integer.toString(pdu.length));
           // for(int i=0;i<Sms.length;++i)
            //{
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu[0]);
                String smsBody = smsMessage.getMessageBody().toString();
                String sender_number = smsMessage.getOriginatingAddress();


                String lol4 = Integer.toString(voteCount);
                Log.d("DEBUGGING:", "The voteCount before receiving skipping "+lol4);
                if (smsBody.equals("%skip")) {
                    //for(String iNum : usedNums)
                    //{
                        Log.d("DEBUGGING:", sender_number);
                        if (!usedNums.contains(sender_number)){
                            usedNums.add(sender_number);
                            //voteCount++;
                            addVote();
                            //numUsedBefore = true;
                            Log.d("DEBUGGING:", "The senderNumber stored");
                            Toast.makeText(context, "Vote to Skip received", Toast.LENGTH_LONG).show();
                    //        Toast.makeText(context, "here", Toast.LENGTH_LONG);

                           // break;
                        }
                        else if (usedNums.contains(sender_number)){
                            Log.d("DEBUGGING:", "Repeated vote was skipped");
                        }
//
//                    String lol = Boolean.toString(numUsedBefore);
//                    Log.d("DEBUGGING:", "The usedNumBefore is "+lol);
//
//                    if (numUsedBefore == false) {
//                        voteCount++;
//                        String lol2 = Integer.toString(voteCount);
//                        Log.d("DEBUGGING:", "The voteCount is "+lol2);
//                        //Toast.makeText(context, lol2, Toast.LENGTH_LONG);
//                    }
//                    else {
//                        Log.d("DEBUGGING:", "Skipping this vote");
//                        numUsedBefore = false;
//                    }
                    //
                    //usedNums.add(sender_number);

                    if (voteCount > (totalPeeps / 2)){
                        //voteCount = 0;
                        reset();
                        Log.d("DEBUGGING:", "The reseting to 0");
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
            //}
            Log.d("Debug","Now We are here. \n");
        }

    }

}
