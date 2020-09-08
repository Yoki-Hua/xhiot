package com.xhwl.licensePlate.huawei.items;

import java.util.ArrayList;
import java.util.List;


public class VatInvoiceItem {
	private String type;
	private String serial_number;
	private String attribution;
	private String code;
	private String check_code;
	private String machine_number;
	private String print_number;
	private String number;
	private String issue_date;
	private String encryption_block;
	private String buyer_name;
	private String buyer_id;
	private String buyer_address;
	private String buyer_bank;
	private String seller_name;
	private String seller_id;
	private String seller_address;
	private String seller_bank;
	private String subtotal_amount;
	private String subtotal_tax;
	private String total;
	private String total_in_words;
	private String remarks;
	private String receiver;
	private String reviewer;
	private String issuer;

	private List<String> supervision_seal;
	private List<String> seller_seal;
	private List<VatInvoiceListItem> item_list;

	public VatInvoiceItem() {
		this.supervision_seal = new ArrayList<>();
		this.seller_seal = new ArrayList<>();
		this.item_list = new ArrayList<>();
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
	public String getSerial_number() {
		return serial_number;
	}
	public String getAttribution() {
		return attribution;
	}
	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCheck_code(String check_code) {
		this.check_code = check_code;
	}
	public String getCheck_code() {
		return check_code;
	}
	public void setMachine_number(String machine_number) {
		this.machine_number = machine_number;
	}
	public String getMachine_number() {
		return machine_number;
	}
	public void setPrint_number(String print_number) {
		this.print_number = print_number;
	}
	public String getPrint_number() {
		return print_number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getNumber() {
		return number;
	}
	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public String getEncryption_block() {
		return encryption_block;
	}
	public void setEncryption_block(String encryption_block) {
		this.encryption_block = encryption_block;
	}
	public void setBuyer_address(String buyer_address) {
		this.buyer_address = buyer_address;
	}
	public String getBuyer_address() {
		return buyer_address;
	}
	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}
	public String getBuyer_name() {
		return buyer_name;
	}
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	public String getBuyer_bank() {
		return buyer_bank;
	}
	public void setBuyer_bank(String buyer_bank) {
		this.buyer_bank = buyer_bank;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getSeller_address() {
		return seller_address;
	}
	public void setSeller_address(String seller_address) {
		this.seller_address = seller_address;
	}
	public String getSeller_bank() {
		return seller_bank;
	}
	public void setSeller_bank(String seller_bank) {
		this.seller_bank = seller_bank;
	}
	public String getSubtotal_amount() {
		return subtotal_amount;
	}
	public void setSubtotal_amount(String subtotal_amount) {
		this.subtotal_amount = subtotal_amount;
	}
	public String getSubtotal_tax() {
		return subtotal_tax;
	}
	public void setSubtotal_tax(String subtotal_tax) {
		this.subtotal_tax = subtotal_tax;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public void setTotal_in_words(String total_in_words) {
		this.total_in_words = total_in_words;
	}
	public String getTotal_in_words() {
		return total_in_words;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getIssuer() {
		return issuer;
	}

	public List<String> getSupervision_seal() {
		return supervision_seal;
	}
	public void setSupervision_seal(List<String> supervision_seal) {
		this.supervision_seal = supervision_seal;
	}
	public void setSeller_seal(List<String> seller_seal) {
		this.seller_seal = seller_seal;
	}
	public List<String> getSeller_seal() {
		return seller_seal;
	}
	public List<VatInvoiceListItem> getItem_list() {
		return item_list;
	}
	public void setItem_list(List<VatInvoiceListItem> item_list) {
		this.item_list = item_list;
	}
}
