package com.ats.mahindrabattery.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ats.mahindrabattery.entity.LoginRequest;
import com.ats.mahindrabattery.entity.MasterUserDetailsEntity;
import com.ats.mahindrabattery.entity.OtpEntity;
import com.ats.mahindrabattery.repository.MasterUserDetailsRepository;
import com.ats.mahindrabattery.response.ResponseHandler;
import com.ats.mahindrabattery.security.JwtUtil;
import com.ats.mahindrabattery.security.MyUserDetailsService;
import com.ats.mahindrabattery.service.EmailService;

@RestController
@RequestMapping("/login")
@CrossOrigin

public class LoginController {
 int myOtp=0;
 String email;
// String emailId;
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	OtpEntity otpEntity = new OtpEntity();
	@Autowired
	MyUserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MasterUserDetailsRepository userManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Autowired
	private MasterUserDetailsRepository userDetailsRepository;

	@PostMapping("/authenticate")
public ResponseEntity<MasterUserDetailsEntity> createAuthenticationToken(
        @RequestBody LoginRequest request) throws Exception {

    String userName = request.getUserName();
    String userPassword = request.getUserPassword();

    System.out.println("request username: " + userName);

    List<MasterUserDetailsEntity> userList =
            userManager.findByuserNameAndUserIsDeleted(userName, 0);

    System.out.println("userlist: " + userList.size());

    if (userList != null && userList.size() > 0) {

        System.out.println("inside if :" + userName);

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(userName);

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        MasterUserDetailsEntity user = userList.get(0);

        user.setJwtToken(jwt);

        user.setUserPhotoImageIn64Base(
                "data:image/jpeg;base64,"
                        + Base64.getEncoder()
                                .encodeToString(user.getUserImage()));

        return new ResponseEntity<>(user, HttpStatus.OK);

    } else {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

	@PostMapping("/sendOtp/{emailId}")
	public ResponseEntity<?> sendOtp(@PathVariable String emailId, HttpSession session) {
		
		otpEntity.setEmail(emailId);
		Random random = new Random();
		int otp = random.nextInt(999999);
		
		otpEntity.setOtp(otp);
		String s = String.valueOf(otp);
		boolean sendEmail = emailService.sendEmail("OTP from user for verification", "OTP=" + otp, emailId);
 
		if (sendEmail) {
			//myOtp = otp;
			//email = emailId;
			session.setAttribute("myotp", otp);
			session.setAttribute("email", emailId);
		myOtp=otpEntity.getOtp();
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			session.setAttribute("message", "check your email id");
		
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
 
	}







	 
		@PostMapping("/verifyOtp/{otp}")
		public ResponseEntity<?> verifyOtp(@PathVariable int otp, HttpSession session) {
			// myOtp=otp;
			 // myOtp = (int) session.getAttribute("myotp");
			
			// String email = (String) session.getAttribute("email");
			String email2 = otpEntity.getEmail();
		
			if (myOtp == otp) {
				
				MasterUserDetailsEntity findByEmailId = userDetailsRepository.findByEmailId(email2);
				if (findByEmailId == null) {
					session.setAttribute("message", "email id does not exists");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

				String s = String.valueOf(otp);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
			
				session.setAttribute("message", "wrong otp");
				 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		@PostMapping("/changePassword")
		public ResponseEntity<?> changePassword(@RequestParam("newPassword1") String newPassword1,
				@RequestParam("newPassword2") String newPassword2, HttpSession session) {
			// String email = (String) session.getAttribute("email");
			String email = otpEntity.getEmail();
			MasterUserDetailsEntity findByEmailId = userDetailsRepository.findByEmailId(email);
			String userName = findByEmailId.getUserName();
			List<MasterUserDetailsEntity> findByuserName = userDetailsRepository.findByuserName(userName);
			if (newPassword1.equals(newPassword2)) {
				findByuserName.get(0).setUserPassword(bCryptPasswordEncoder.encode(newPassword1));
				userDetailsRepository.save(findByuserName.get(0));
				
				return new ResponseEntity<>(HttpStatus.OK);
			}
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
}
