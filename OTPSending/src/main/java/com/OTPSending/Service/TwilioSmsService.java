package com.OTPSending.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public void sendOtp(String to, String otp) {
        Twilio.init(accountSid, authToken);
        String formattedPhoneNumber = "+91" + to;
        String message = "Your OTP is: " + otp;

        Message twilioMessage = Message.creator(
                new com.twilio.type.PhoneNumber(formattedPhoneNumber),
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                message
        ).create();

        System.out.println("Twilio Message SID: " + twilioMessage.getSid());
    }
}
