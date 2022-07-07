package com.bezkoder.spring.security.postgresql.util.mailAndpdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bezkoder.spring.security.postgresql.dto.userDto.UserDTO;
import com.bezkoder.spring.security.postgresql.util.email.smtp.MailSender;
import com.bezkoder.spring.security.postgresql.util.password.AESEncriptor;
import com.bezkoder.spring.security.postgresql.util.propertyfile.ContextConstants;

/**
 * @Generate pdf files for reports
 * @author Siya
 *
 */
public class GenereateResetforgotPasswordLink {

	public static void sendResetPasswordLink(UserDTO dto, String email, String token) {
		// TODO Auto-generated method stub
		List<String> bcc = new ArrayList<String>();
		List<String> cc = new ArrayList<String>();
		List<String> to = new ArrayList<String>();
		List<String> files = new ArrayList<String>();
		to.add(email);
		String logo = ContextConstants.fileSaveLocation;
		String baseUrl = "http://localhost/";
		Map<String, String> map = new HashMap<String, String>();

		map.put("reset", dto.getUserId() + "");
		String[] encrypted = null;
		try {
			encrypted = AESEncriptor.encryptObject(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String url = baseUrl + "password/resetPassword.html?token=" + token;

		String mailContent = "<!doctype html>\r\n" + " <html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n"
				+ " <head>\r\n" + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n"
				+ "  <meta name=\"viewport\" content=\"initial-scale=1.0\" />\r\n"
				+ "  <meta name=\"format-detection\" content=\"telephone=no\" />\r\n" + "  <title></title>\r\n"
				+ "  <style type=\"text/css\">\r\n" + " 	body {\r\n" + "		width: 100%;\r\n"
				+ "		margin: 0;\r\n" + "		padding: 0;\r\n" + "		-webkit-font-smoothing: antialiased;\r\n"
				+ "	}\r\n" + "	@media only screen and (max-width: 600px) {\r\n"
				+ "		table[class=\"table-row\"] {\r\n" + "			float: none !important;\r\n"
				+ "			width: 98% !important;\r\n" + "			padding-left: 20px !important;\r\n"
				+ "			padding-right: 20px !important;\r\n" + "		}\r\n"
				+ "		table[class=\"table-row-fixed\"] {\r\n" + "			float: none !important;\r\n"
				+ "			width: 98% !important;\r\n" + "		}\r\n"
				+ "		table[class=\"table-col\"], table[class=\"table-col-border\"] {\r\n"
				+ "			float: none !important;\r\n" + "			width: 100% !important;\r\n"
				+ "			padding-left: 0 !important;\r\n" + "			padding-right: 0 !important;\r\n"
				+ "			table-layout: fixed;\r\n" + "		}\r\n" + "		td[class=\"table-col-td\"] {\r\n"
				+ "			width: 100% !important;\r\n" + "		}\r\n"
				+ "		table[class=\"table-col-border\"] + table[class=\"table-col-border\"] {\r\n"
				+ "			padding-top: 12px;\r\n" + "			margin-top: 12px;\r\n"
				+ "			border-top: 1px solid #E8E8E8;\r\n" + "		}\r\n"
				+ "		table[class=\"table-col\"] + table[class=\"table-col\"] {\r\n"
				+ "			margin-top: 15px;\r\n" + "		}\r\n" + "		td[class=\"table-row-td\"] {\r\n"
				+ "			padding-left: 0 !important;\r\n" + "			padding-right: 0 !important;\r\n"
				+ "		}\r\n" + "		table[class=\"navbar-row\"] , td[class=\"navbar-row-td\"] {\r\n"
				+ "			width: 100% !important;\r\n" + "		}\r\n" + "		img {\r\n"
				+ "			max-width: 100% !important;\r\n" + "			display: inline !important;\r\n"
				+ "		}\r\n" + "		img[class=\"pull-right\"] {\r\n" + "			float: right;\r\n"
				+ "			margin-left: 11px;\r\n" + "            max-width: 125px !important;\r\n"
				+ "			padding-bottom: 0 !important;\r\n" + "		}\r\n" + "		img[class=\"pull-left\"] {\r\n"
				+ "			float: left;\r\n" + "			margin-right: 11px;\r\n"
				+ "			max-width: 125px !important;\r\n" + "			padding-bottom: 0 !important;\r\n"
				+ "		}\r\n" + "		table[class=\"table-space\"], table[class=\"header-row\"] {\r\n"
				+ "			float: none !important;\r\n" + "			width: 98% !important;\r\n" + "		}\r\n"
				+ "		td[class=\"header-row-td\"] {\r\n" + "			width: 100% !important;\r\n" + "		}\r\n"
				+ "	}\r\n" + "	@media only screen and (max-width: 480px) {\r\n"
				+ "		table[class=\"table-row\"] {\r\n" + "			padding-left: 16px !important;\r\n"
				+ "			padding-right: 16px !important;\r\n" + "		}\r\n" + "	}\r\n"
				+ "	@media only screen and (max-width: 320px) {\r\n" + "		table[class=\"table-row\"] {\r\n"
				+ "			padding-left: 12px !important;\r\n" + "			padding-right: 12px !important;\r\n"
				+ "		}\r\n" + "	}\r\n" + "	@media only screen and (max-width: 458px) {\r\n"
				+ "		td[class=\"table-td-wrap\"] {\r\n" + "			width: 100% !important;\r\n" + "		}\r\n"
				+ "	}\r\n" + "  </style>\r\n" + " </head>\r\n"
				+ " <body style=\"font-family: Arial, sans-serif; font-size:13px; color: #444444; min-height: 200px;\" bgcolor=\"#E4E6E9\" leftmargin=\"0\" topmargin=\"0\" marginheight=\"0\" marginwidth=\"0\">\r\n"
				+ " <table width=\"100%\" height=\"100%\" bgcolor=\"#E4E6E9\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
				+ " <tr><td width=\"100%\" align=\"center\" valign=\"top\" bgcolor=\"#E4E6E9\" style=\"background-color:#E4E6E9; min-height: 200px;\">\r\n"
				+ "<table><tr><td class=\"table-td-wrap\" align=\"center\" width=\"458\"><table class=\"table-space\" height=\"18\" style=\"height: 18px; font-size: 0px; line-height: 0; width: 450px; background-color: #e4e6e9;\" width=\"450\" bgcolor=\"#E4E6E9\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"18\" style=\"height: 18px; width: 450px; background-color: #e4e6e9;\" width=\"450\" bgcolor=\"#E4E6E9\" align=\"left\">&nbsp;</td></tr></tbody></table>\r\n"
				+ "<table class=\"table-space\" height=\"8\" style=\"height: 8px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"8\" style=\"height: 8px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\r\n"
				+ "\r\n"
				+ "<table class=\"table-row\" width=\"450\" bgcolor=\"#FFFFFF\" style=\"table-layout: fixed; background-color: #ffffff;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-row-td\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; padding-left: 36px; padding-right: 36px;\" valign=\"top\" align=\"left\">\r\n"
				+ "  <table class=\"table-col\" align=\"left\" width=\"378\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td class=\"table-col-td\" width=\"378\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; width: 378px;\" valign=\"top\" align=\"left\">\r\n"
				+ "    <table class=\"header-row\" width=\"378\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td class=\"header-row-td\" width=\"378\" style=\"font-family: Arial, sans-serif; font-weight: normal; line-height: 19px; color: #478fca; margin: 0px; font-size: 18px; padding-bottom: 10px; padding-top: 15px;\" valign=\"top\" align=\"left\">\r\n"
				+ "		Reset your Password!</td></tr></tbody></table>\r\n"
				+ "    <div style=\"font-family: Arial, sans-serif; line-height: 20px; color: #444444; font-size: 13px;\">\r\n"
				+ "      <b style=\"color: #777777;\"> Hi " + dto.getName()
				+ ", <br/> We have received your request to reset your password </b>\r\n" + "      <br>\r\n"
				+ "	  Please click the link below to complete the reset\r\n" + "    </div>\r\n"
				+ "  </td></tr></tbody></table>\r\n" + "</td></tr></tbody></table>\r\n" + "    \r\n"
				+ "<table class=\"table-space\" height=\"12\" style=\"height: 12px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"12\" style=\"height: 12px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\r\n"
				+ "<table class=\"table-space\" height=\"12\" style=\"height: 12px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"12\" style=\"height: 12px; width: 450px; padding-left: 16px; padding-right: 16px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"center\">&nbsp;<table bgcolor=\"#E8E8E8\" height=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td bgcolor=\"#E8E8E8\" height=\"1\" width=\"100%\" style=\"height: 1px; font-size:0;\" valign=\"top\" align=\"left\">&nbsp;</td></tr></tbody></table></td></tr></tbody></table>\r\n"
				+ "<table class=\"table-space\" height=\"16\" style=\"height: 16px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"16\" style=\"height: 16px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\r\n"
				+ "\r\n"
				+ "<table class=\"table-row\" width=\"450\" bgcolor=\"#FFFFFF\" style=\"table-layout: fixed; background-color: #ffffff;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-row-td\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; padding-left: 36px; padding-right: 36px;\" valign=\"top\" align=\"left\">\r\n"
				+ "  <table class=\"table-col\" align=\"left\" width=\"378\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td class=\"table-col-td\" width=\"378\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; width: 378px;\" valign=\"top\" align=\"left\">\r\n"
				+ "    <div style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; text-align: center;\">\r\n"
				+ "      <a href=\"" + url
				+ "\" style=\"color: #ffffff; text-decoration: none; margin: 0px; text-align: center; vertical-align: baseline; border: 4px solid #6fb3e0; padding: 4px 9px; font-size: 15px; line-height: 21px; background-color: #6fb3e0;\">&nbsp; Confirm &nbsp;</a>\r\n"
				+ "    </div>\r\n"
				+ "    <table class=\"table-space\" height=\"16\" style=\"height: 16px; font-size: 0px; line-height: 0; width: 378px; background-color: #ffffff;\" width=\"378\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"16\" style=\"height: 16px; width: 378px; background-color: #ffffff;\" width=\"378\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\r\n"
				+ "  </td></tr></tbody></table>\r\n" + "</td></tr></tbody></table>\r\n" + "\r\n"
				+ "<table class=\"table-space\" height=\"6\" style=\"height: 6px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"6\" style=\"height: 6px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\r\n"
				+ "\r\n"
				+ "<table class=\"table-row-fixed\" width=\"450\" bgcolor=\"#FFFFFF\" style=\"table-layout: fixed; background-color: #ffffff;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-row-fixed-td\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; padding-left: 1px; padding-right: 1px;\" valign=\"top\" align=\"left\">\r\n"
				+ "  <table class=\"table-col\" align=\"left\" width=\"448\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed; width: 100%;\"><tbody><tr><td class=\"table-col-td\" width=\"448\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal;\" valign=\"top\" align=\"left\">\r\n"
				+ "    <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td width=\"100%\" align=\"center\" bgcolor=\"#f5f5f5\" style=\"font-family: Arial, sans-serif; line-height: 24px; color: #bbbbbb; font-size: 13px; font-weight: normal; text-align: center; padding: 15px; border-width: 1px 0px 0px; border-style: solid; border-color: #e3e3e3; background-color: #f5f5f5;\" valign=\"top\">\r\n"
				+ "       <img src=\"" + logo + "\" style=\"width: 140px;\">\r\n" + "    </td></tr></tbody></table>\r\n"
				+ "  </td></tr></tbody></table>\r\n" + "</td></tr></tbody></table>\r\n"
				+ "<table class=\"table-space\" height=\"1\" style=\"height: 1px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"1\" style=\"height: 1px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\r\n"
				+ "<table class=\"table-space\" height=\"36\" style=\"height: 36px; font-size: 0px; line-height: 0; width: 450px; background-color: #e4e6e9;\" width=\"450\" bgcolor=\"#E4E6E9\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"36\" style=\"height: 36px; width: 450px; background-color: #e4e6e9;\" width=\"450\" bgcolor=\"#E4E6E9\" align=\"left\">&nbsp;</td></tr></tbody></table></td></tr></table>\r\n"
				+ "</td></tr>\r\n" + " </table>\r\n" + " </body>\r\n" + " </html>";

		MailSender.sendMail(ContextConstants.smtpPort, ContextConstants.smtpHostname, ContextConstants.smtpFromAddress,
				ContextConstants.smtpPassword, ContextConstants.smtpContentType, "Reset your account password ",
				mailContent, files, to, bcc, cc);
	}

}
