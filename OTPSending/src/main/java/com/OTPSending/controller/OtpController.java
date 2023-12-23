package com.OTPSending.controller;

import com.OTPSending.Service.TwilioSmsService;
import com.OTPSending.request.OtpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class OtpController {

    private final TwilioSmsService twilioSmsService;
    private final Map<String, OtpData> otpStorage = new HashMap<>();

    @Autowired
    public OtpController(TwilioSmsService twilioSmsService) {
        this.twilioSmsService = twilioSmsService;
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody OtpRequest otpRequest) {
        String generatedOtp = generateOtp();
        twilioSmsService.sendOtp(otpRequest.getPhoneNumber(), generatedOtp);

        // Store the generated OTP along with timestamp in memory for verification
        otpStorage.put(otpRequest.getPhoneNumber(), new OtpData(generatedOtp, LocalDateTime.now()));

        return "Otp sent successfully on your registered mobile number";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String phoneNumber, @RequestParam String enteredOtp) {
        // Retrieve the stored OTP data for the given phone number
        OtpData otpData = otpStorage.get(phoneNumber);

        // Check if OTP data is available and within the valid time window
        if (otpData != null && isOtpValid(otpData)) {
            // Verify the entered OTP
            boolean isOtpValid = otpData.getOtp().equals(enteredOtp);

            if (isOtpValid) {
                // Return a success message or any data you want
                return "Otp verification successful";
            }
        }

        // Return an error message or any data you want
        return "Invalid OTP or OTP has expired. Verification failed.";
    }

    private String generateOtp() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private boolean isOtpValid(OtpData otpData) {
        // Check if the OTP is still valid (e.g., within 10 minutes)
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime otpTime = otpData.getTimestamp();
        return currentTime.minusMinutes(10).isBefore(otpTime);
    }

    // Inner class to store OTP data along with timestamp
    private static class OtpData {
        private final String otp;
        private final LocalDateTime timestamp;

        public OtpData(String otp, LocalDateTime timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}
