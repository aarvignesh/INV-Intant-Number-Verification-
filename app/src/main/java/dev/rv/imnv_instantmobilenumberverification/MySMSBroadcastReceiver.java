package dev.rv.imnv_instantmobilenumberverification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
public class MySMSBroadcastReceiver extends BroadcastReceiver {

    private OTPReceiveListener otpReceiver ;

    public void setListners(OTPReceiveListener otpReceiveListener){
        Log.d("OTPP","list set");
        this.otpReceiver=otpReceiveListener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("OTPP","rece");
        Bundle extrass = intent.getExtras();
        Status statuss = (Status) extrass.get(SmsRetriever.EXTRA_STATUS);

        String messagee = (String) extrass.get(SmsRetriever.EXTRA_SMS_MESSAGE);
        messagee = messagee.replace("<#> Your ExampleApp code is: ", "");
        messagee=messagee.replace("yJRwwXUWvud","");
        OtpActivity.setotp(messagee);
//        otpReceiver.onOTPReceived(messagee);

        Log.d("OTPP",messagee);
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    if (otpReceiver != null) {
                        Log.d("OTPP",message);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        message = message.replace("<#> Your ExampleApp code is: ", "");
                        otpReceiver.onOTPReceived(message);
                    }
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    break;
                case CommonStatusCodes.TIMEOUT:
                    otpReceiver.onOTPTimeOut();
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }

    interface OTPReceiveListener {

         void onOTPReceived( String otp);

         void onOTPTimeOut();
    }
}
